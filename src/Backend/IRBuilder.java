package Backend;

import AST.ASTNode;
import AST.ASTVisitor;
import AST.Expr.*;
import AST.Program.ClassDef;
import AST.Program.FuncDef;
import AST.Program.RootNode;
import AST.Program.TypeNode;
import AST.Stmt.*;
import Backend.Pass.GlobalLocator;
import IR.Instruction.*;
import IR.Operand.*;
import IR.Program.IRBlock;
import IR.Program.IRFunction;
import IR.Program.IRRoot;
import IR.Type.*;
import Util.Entity.ClassEntity;
import Util.Entity.FuncEntity;
import Util.Error.InternalError;
import Util.Position;
import Util.Scope.GlobalScope;

import java.util.*;

public class IRBuilder implements ASTVisitor {
    public GlobalScope globalScope = null;
    public ClassEntity currentClass = null;
    public IRFunction currentFunc = null;
    public IRBlock currentBlock = null;
    public IRRoot IRRoot = null;
    public int symbolCounter = 0;
    public HashMap<ClassDef, Integer> symbols = new HashMap<>();
    public boolean isPara = false;
    public ArrayList<ReturnInst> returnInsts = new ArrayList<>();

    public class LogicalInstPhi {
        ArrayList<IRBlock> blocks = new ArrayList<>();
        ArrayList<BaseOperand> values = new ArrayList<>();
    }

    public HashMap<IRBlock, LogicalInstPhi> logicalInstPhis = new HashMap<>();

    public boolean setOperand = false;
    public HashSet<IRBlock> entryBlocks = new LinkedHashSet<>();
    public HashSet<IRBlock> returnedBlocks = new LinkedHashSet<>();

    public IRBuilder(GlobalScope globalScope, IRRoot IRRoot) {
        this.globalScope = globalScope;
        this.IRRoot = IRRoot;

        setMethod("print");
        setMethod("println");
        setMethod("printInt");
        setMethod("printlnInt");
        setMethod("getString");
        setMethod("getInt");
        setMethod("toString");

        IntIRType intIRType = new IntIRType(32);
        PointerIRType stringIRType = new PointerIRType(new IntIRType(8), false);
        IntIRType charIRType = new IntIRType(8);
        ClassEntity stringType = globalScope.classes.get("string");

        IRFunction function = new IRFunction("string_length", intIRType);
        function.paras.add(new ParaOperand(stringIRType, "str"));
        IRRoot.builtinFuncs.put("string_length", function);
        stringType.scope.getFunc("length", null, false).function = function;

        function = new IRFunction("string_substring", stringIRType);
        function.paras.add(new ParaOperand(stringIRType, "str"));
        function.paras.add(new ParaOperand(intIRType, "left"));
        function.paras.add(new ParaOperand(intIRType, "right"));
        IRRoot.builtinFuncs.put("string_substring", function);
        stringType.scope.getFunc("substring", null, false).function = function;

        function = new IRFunction("string_parseInt", intIRType);
        function.paras.add(new ParaOperand(stringIRType, "str"));
        IRRoot.builtinFuncs.put("string_parseInt", function);
        stringType.scope.getFunc("parseInt", null, false).function = function;

        function = new IRFunction("string_ord", charIRType);
        function.paras.add(new ParaOperand(stringIRType, "str"));
        function.paras.add(new ParaOperand(intIRType, "ord"));
        IRRoot.builtinFuncs.put("string_ord", function);
        stringType.scope.getFunc("ord", null, false).function = function;
    }

    @Override
    public void visit(RootNode it) {
        for (ASTNode def : it.defs) {
            if (def instanceof FuncDef) {
                IRFunction function = new IRFunction(((FuncDef) def).name);
                IRRoot.funcs.put(function.name, function);
            } else if (def instanceof ClassDef) {
                for (FuncDef funcDef : ((ClassDef) def).funcDefs) {
                    IRFunction function = new IRFunction((((ClassDef) def).name) + "_" + funcDef.name);
                    funcDef.funcEntity.function = function;
                    IRRoot.funcs.put(function.name, function);
                }
                for (int i = 0; i < ((ClassDef) def).constructors.size(); ++i) {
                    FuncDef constructor = ((ClassDef) def).constructors.get(i);
                    IRFunction function = new IRFunction(((ClassDef) def).name + "_" + constructor.name + i);
                    constructor.funcEntity.function = function;
                    IRRoot.funcs.put(function.name, function);
                }
            }
        }
        setOperand = true;
        for (ASTNode def : it.defs)
            if (def instanceof ClassDef || def instanceof VarDefSubStmt)
                def.accept(this);
        setOperand = false;
        for (ASTNode def : it.defs)
            if (def instanceof ClassDef || def instanceof FuncDef)
                def.accept(this);

        IRFunction initFunc = IRRoot.getInit();
        initFunc.exitBlock.addTerminalInst(new ReturnInst(initFunc.exitBlock, null));
        currentFunc = initFunc;
        setEntry();
        new GlobalLocator(IRRoot).run();
    }

    @Override
    public void visit(ClassDef it) {
        symbolCounter = 0;
        currentClass = (ClassEntity) globalScope.getClass(it.name, it.pos);
        if (setOperand) {
            it.varDefs.forEach(var -> var.accept(this));
            symbols.put(it, symbolCounter);
        } else {
            symbolCounter = symbols.get(it);
            it.funcDefs.forEach(func -> func.accept(this));
            if (it.hasConstructor)
                it.constructors.forEach(constructor -> constructor.accept(this));
        }
    }

    @Override
    public void visit(FuncDef it) {
        FuncEntity funcEntity = it.funcEntity;
        currentFunc = funcEntity.function;
        currentBlock = currentFunc.entryBlock;
        symbolCounter = 0;
        returnInsts.clear();
        currentFunc.returnType = IRRoot.getIRType(funcEntity.type, false);
        if (it.isMethod) currentFunc.setClassPtr(new ParaOperand(IRRoot.getIRType(currentClass, false), "this"));
        isPara = true;
        it.paras.forEach(para -> para.accept(this));
        isPara = false;
        if (currentFunc.name.equals("main"))
            currentBlock.addInst(new FunCallInst(currentBlock, null, IRRoot.getInit(), new ArrayList<>()));
        it.block.accept(this);
        if (!currentBlock.terminated) {
            ReturnInst inst;
            if (currentFunc.name.equals("main"))
                inst = new ReturnInst(currentBlock, new IntOperand(0, 32));
            else if (currentFunc.returnType instanceof VoidIRType)
                inst = new ReturnInst(currentBlock, null);
            else if (currentFunc.returnType instanceof IntIRType)
                inst = new ReturnInst(currentBlock, new IntOperand(0, currentFunc.returnType.size()));
            else if (currentFunc.returnType instanceof BoolIRType)
                inst = new ReturnInst(currentBlock, new BoolOperand(false));
            else if (currentFunc.returnType instanceof PointerIRType)
                inst = new ReturnInst(currentBlock, new NullOperand());
            else throw new InternalError("wrong return", it.pos);
            currentBlock.addTerminalInst(inst);
            returnInsts.add(inst);
        }

        setEntry();

        if (returnInsts.size() <= 1) currentFunc.exitBlock = returnInsts.get(0).block;
        else {
            IRBlock rootReturnBlock = new IRBlock("root_return_block");
            if (returnInsts.get(0).value == null) {
                for (ReturnInst inst : returnInsts) {
                    inst.block.removeTerminalInst();
                    inst.block.addTerminalInst(new JumpInst(inst.block, rootReturnBlock));
                }
                rootReturnBlock.addTerminalInst(new ReturnInst(rootReturnBlock, null));
            } else {
                ArrayList<IRBlock> blocks = new ArrayList<>();
                ArrayList<BaseOperand> values = new ArrayList<>();
                for (ReturnInst inst : returnInsts) {
                    inst.block.removeTerminalInst();
                    blocks.add(inst.block);
                    values.add(inst.value);
                    inst.block.addTerminalInst(new JumpInst(inst.block, rootReturnBlock));
                }
                Register returnValue = new Register(returnInsts.get(0).value.type, "root_return_value");
                rootReturnBlock.addPhi(new PhiInst(rootReturnBlock, returnValue, blocks, values));
            }
            currentFunc.exitBlock = rootReturnBlock;
            currentFunc.blocks.add(rootReturnBlock);
        }

        for (Register var : currentFunc.vars) {
            if (((PointerIRType) var.type).basicType instanceof PointerIRType)
                currentFunc.entryBlock.addHeadInst(new StoreInst(currentFunc.entryBlock, var, new NullOperand()));
            else
                currentFunc.entryBlock.addHeadInst(new StoreInst(currentFunc.entryBlock, var, new IntOperand(65536, ((PointerIRType) var.type).basicType.size())));
        }
        returnInsts.clear();
        currentBlock = null;
        currentFunc = null;
    }

    @Override
    public void visit(TypeNode it) {
        /* Empty */
    }

    @Override
    public void visit(AssignExpr it) {
        it.leftSrc.accept(this);
        assign(it.leftSrc.operand, it.rightSrc);
        it.operand = it.rightSrc.operand;
    }

    @Override
    public void visit(BinaryExpr it) {
        switch (it.op) {
            case "+":
                it.src1.accept(this);
                it.src2.accept(this);
                if (it.src1.type.isInt()) {
                    BaseOperand operand1 = resolvePointer(currentBlock, it.src1.operand);
                    BaseOperand operand2 = resolvePointer(currentBlock, it.src2.operand);
                    it.operand = new Register(new IntIRType(32), "binary_" + it.op);
                    currentBlock.addInst(new BinaryInst(currentBlock, (Register) it.operand, it.op, operand1, operand2));
                } else {
                    BaseOperand operand1 = resolveStrPointer(currentBlock, it.src1.operand);
                    BaseOperand operand2 = resolveStrPointer(currentBlock, it.src2.operand);
                    ArrayList<BaseOperand> paras = new ArrayList<>();
                    paras.add(operand1);
                    paras.add(operand2);
                    it.operand = new Register(new PointerIRType(new IntIRType(8), false), "binary_string_plus");
                    IRFunction stringFunc = IRRoot.builtinFuncs.get("g_stringAdd");
                    ;
                    currentBlock.addInst(new FunCallInst(currentBlock, (Register) it.operand, stringFunc, paras));
                }
                break;
            case "<":
            case ">":
            case "<=":
            case ">=":
                it.src1.accept(this);
                it.src2.accept(this);
                if (it.src1.type.isInt()) {
                    BaseOperand operand1 = resolvePointer(currentBlock, it.src1.operand);
                    BaseOperand operand2 = resolvePointer(currentBlock, it.src2.operand);
                    it.operand = new Register(new BoolIRType(), "cmp_" + it.op);
                    currentBlock.addInst(new CmpInst(currentBlock, (Register) it.operand, it.op, operand1, operand2));
                } else {
                    BaseOperand operand1 = resolveStrPointer(currentBlock, it.src1.operand);
                    BaseOperand operand2 = resolveStrPointer(currentBlock, it.src2.operand);
                    ArrayList<BaseOperand> paras = new ArrayList<>();
                    paras.add(operand1);
                    paras.add(operand2);
                    it.operand = new Register(new PointerIRType(new IntIRType(8), false), "cmp_string_" + it.op);
                    IRFunction stringFunc;
                    if (it.op.equals("<")) stringFunc = IRRoot.builtinFuncs.get("g_stringLess");
                    else if (it.op.equals(">")) stringFunc = IRRoot.builtinFuncs.get("g_stringGreater");
                    else if (it.op.equals("<=")) stringFunc = IRRoot.builtinFuncs.get("g_stringLessThan");
                    else stringFunc = IRRoot.builtinFuncs.get("g_stringGreaterThan");
                    currentBlock.addInst(new FunCallInst(currentBlock, (Register) it.operand, stringFunc, paras));
                }
                branchAdd(it);
                break;
            case "==":
            case "!=":
                it.src1.accept(this);
                it.src2.accept(this);
                it.operand = new Register(new BoolIRType(), it.op);
                if (it.src1.type.isInt()) {
                    BaseOperand operand1 = toIntOperand(resolvePointer(currentBlock, it.src1.operand));
                    BaseOperand operand2 = toIntOperand(resolvePointer(currentBlock, it.src2.operand));
                    currentBlock.addInst(new CmpInst(currentBlock, (Register) it.operand, it.op, operand1, operand2));
                } else {
                    BaseOperand operand1 = resolveStrPointer(currentBlock, it.src1.operand);
                    BaseOperand operand2 = resolveStrPointer(currentBlock, it.src2.operand);
                    ArrayList<BaseOperand> paras = new ArrayList<>();
                    paras.add(operand1);
                    paras.add(operand2);
                    IRFunction stringFunc;
                    if (it.op.equals("==")) stringFunc = IRRoot.builtinFuncs.get("g_stringEqual");
                    else stringFunc = IRRoot.builtinFuncs.get("g_stringNotEqual");
                    currentBlock.addInst(new FunCallInst(currentBlock, (Register) it.operand, stringFunc, paras));
                }
                branchAdd(it);
                break;
            case "&&":
            case "||":
                if (it.thenBlock == null) {
                    IRBlock condBlock = new IRBlock(it.op + "_cond_block");
                    IRBlock targetBlock = new IRBlock(it.op + "_target_block");
                    it.operand = new Register(new BoolIRType(), it.op);
                    LogicalInstPhi logicalInstPhi = new LogicalInstPhi();
                    logicalInstPhis.put(targetBlock, logicalInstPhi);

                    if (it.op.equals("&&")) {
                        it.src1.thenBlock = condBlock;
                        it.src1.elseBlock = targetBlock;
                    } else {
                        it.src1.thenBlock = targetBlock;
                        it.src1.elseBlock = condBlock;
                        logicalInstPhi.blocks.add(currentBlock);
                        logicalInstPhi.values.add(new BoolOperand(true));
                    }
                    it.src1.accept(this);

                    currentBlock = condBlock;
                    it.src2.accept(this);
                    BaseOperand operand = resolvePointer(currentBlock, it.src2.operand);
                    currentBlock.addTerminalInst(new JumpInst(currentBlock, targetBlock));
                    logicalInstPhi.blocks.add(currentBlock);
                    logicalInstPhi.values.add(operand);
                    currentBlock = targetBlock;
                    targetBlock.addPhi(new PhiInst(currentBlock, (Register) it.operand, logicalInstPhi.blocks, logicalInstPhi.values));
                } else {
                    IRBlock condBlock = new IRBlock(it.op + "_block");
                    it.src1.thenBlock = condBlock;
                    it.src1.elseBlock = it.elseBlock;
                    it.src1.accept(this);
                    currentBlock = condBlock;
                    it.src2.thenBlock = it.thenBlock;
                    it.src2.elseBlock = it.elseBlock;
                    it.src2.accept(this);
                }
                break;
            default:
                it.src1.accept(this);
                it.src2.accept(this);
                BaseOperand operand1 = resolvePointer(currentBlock, it.src1.operand);
                BaseOperand operand2 = resolvePointer(currentBlock, it.src2.operand);
                it.operand = new Register(new IntIRType(32), "binary_" + it.op);
                currentBlock.addInst(new BinaryInst(currentBlock, (Register) it.operand, it.op, operand1, operand2));
                break;
        }
    }

    @Override
    public void visit(BoolLiteralExpr it) {
        it.operand = new BoolOperand(it.value);
        branchAdd(it);
    }

    @Override
    public void visit(ExprList it) {
        /* Empty */
    }

    @Override
    public void visit(FuncCallExpr it) {
        it.name.accept(this);
        FuncEntity funcEntity = (FuncEntity) it.name.type;
        if (funcEntity.name.equals("size") && !funcEntity.isMethod) {
            it.operand = new Register(new IntIRType(32), "array_size");
            BaseOperand operand = resolvePointer(currentBlock, it.name.operand);
            Register address = new Register(new PointerIRType(new IntIRType(32), false), "address_reg");
            BaseOperand bitCastPtr = new Register(new PointerIRType(new IntIRType(32), false), "bitcast_ptr");
            if (((PointerIRType) operand.type).basicType == new IntIRType(32) && ((PointerIRType) operand.type).basicType.size() == 32)
                bitCastPtr = operand;
            else currentBlock.addInst(new BitCastInst(currentBlock, (Register) bitCastPtr, operand));
            currentBlock.addInst(new GetElePtrInst(currentBlock, address, new IntIRType(32), bitCastPtr, new IntOperand(-1, 32), null));
            currentBlock.addInst(new LoadInst(currentBlock, (Register) it.operand, address));
        } else {
            if (it.type.isVoid()) it.operand = null;
            else it.operand = new Register(IRRoot.getIRType(funcEntity.type, false), "funcCall_retReg");
            ArrayList<BaseOperand> paras = new ArrayList<>();
            if (funcEntity.isMethod)
                paras.add(resolvePointer(currentBlock, it.name.operand));
            for (BaseExpr para : it.paras) {
                para.accept(this);
                paras.add(resolvePointer(currentBlock, para.operand));
            }
            currentBlock.addInst(new FunCallInst(currentBlock, (Register) it.operand, funcEntity.function, paras));
            if (!IRRoot.builtinFuncs.containsKey(funcEntity.function.name))
                currentFunc.callFuncs.add(funcEntity.function);
        }
        if (it.operand != null) branchAdd(it);
    }

    @Override
    public void visit(FuncExpr it) {
        if (((FuncEntity) it.type).isMethod)
            it.operand = currentFunc.classPtr;
    }

    @Override
    public void visit(IndexExpr it) {
        it.identifier.accept(this);
        it.index.accept(this);
        BaseOperand pointer = resolvePointer(currentBlock, it.identifier.operand);
        BaseOperand index = resolvePointer(currentBlock, it.index.operand);
        it.operand = new Register(new PointerIRType(IRRoot.getIRType(it.type, true), true), "index_ptr");
        GetElePtrInst inst = new GetElePtrInst(currentBlock, (Register) it.operand, ((PointerIRType) pointer.type).basicType, pointer, index, null);
        currentBlock.addInst(inst);
        branchAdd(it);
    }

    @Override
    public void visit(IntLiteralExpr it) {
        it.operand = new IntOperand(it.value, 32);
    }

    @Override
    public void visit(LambdaExpr it) {
        /* Empty */
    }

    @Override
    public void visit(MemberExpr it) {
        it.expr.accept(this);
        BaseOperand classPtr = resolvePointer(currentBlock, it.expr.operand);
        it.operand = new Register(it.varEntity.operand.type, "this." + it.identifier);
        BaseIRType irType = ((PointerIRType) classPtr.type).basicType;
        BaseOperand arrayOffset = new IntOperand(0, 32);
        IntOperand eleOffset = it.varEntity.index;
        GetElePtrInst inst = new GetElePtrInst(currentBlock, (Register) it.operand, irType, classPtr, arrayOffset, eleOffset);
        currentBlock.addInst(inst);
        branchAdd(it);
    }

    @Override
    public void visit(NewExpr it) {
        if (it.type.isArray()) {
            it.operand = new Register(IRRoot.getIRType(it.type, true), "new_operand");
            arrayMalloc(it, 0, (Register) it.operand);
        } else {
            Register mallocReg = new Register(new PointerIRType(new IntIRType(8), false), "mallocReg");
            it.operand = new Register(IRRoot.getIRType(it.type, false), "new_class");
            BaseOperand mallocSize = new IntOperand(((ClassEntity) it.type).allocSize / 8, 32);
            currentBlock.addInst(new MallocInst(currentBlock, mallocReg, mallocSize));
            currentBlock.addInst(new BitCastInst(currentBlock, (Register) it.operand, mallocReg));
            if (((ClassEntity) it.type).scope.constructor != null) {
                ArrayList<BaseOperand> paras = new ArrayList<>();
                paras.add(it.operand);
                currentBlock.addInst(new FunCallInst(currentBlock, null, ((ClassEntity) it.type).scope.constructor.function, paras));
            }
        }
    }

    @Override
    public void visit(NullLiteralExpr it) {
        it.operand = new NullOperand();
    }

    @Override
    public void visit(PrefixExpr it) {
        BaseIRType irType;
        if (it.op.equals("!")) irType = new BoolIRType();
        else irType = new IntIRType(32);
        it.operand = new Register(irType, "prefix_" + it.op);

        if (it.op.equals("!") && it.thenBlock != null) {
            it.src.thenBlock = it.elseBlock;
            it.src.elseBlock = it.thenBlock;
            it.src.accept(this);
            return;
        }

        it.src.accept(this);
        BaseOperand srcOperand = resolvePointer(currentBlock, it.src.operand);
        switch (it.op) {
            case "!":
                currentBlock.addInst(new BinaryInst(currentBlock, (Register) it.operand, "^", srcOperand, new BoolOperand(true)));
                break;
            case "~":
                currentBlock.addInst(new BinaryInst(currentBlock, (Register) it.operand, "^", srcOperand, new IntOperand(-1, 32)));
                break;
            case "+":
                it.operand = it.src.operand;
                break;
            case "-":
                currentBlock.addInst(new BinaryInst(currentBlock, (Register) it.operand, "-", new IntOperand(0, 32), srcOperand));
                break;
            case "++":
                currentBlock.addInst(new BinaryInst(currentBlock, (Register) it.operand, "+", srcOperand, new IntOperand(1, 32)));
                if (it.src.assignable) currentBlock.addInst(new StoreInst(currentBlock, it.src.operand, it.operand));
                break;
            case "--":
                currentBlock.addInst(new BinaryInst(currentBlock, (Register) it.operand, "-", srcOperand, new IntOperand(1, 32)));
                if (it.src.assignable) currentBlock.addInst(new StoreInst(currentBlock, it.src.operand, it.operand));
                break;
            default:
                break;
        }
        branchAdd(it);
    }

    @Override
    public void visit(StringLiteralExpr it) {
        String value = getString(it.value.substring(1, it.value.length() - 1));
        StringOperand strOperand = IRRoot.usualStrings.getOrDefault(value, null);
        if (strOperand == null) {
            String name = currentFunc.name + "." + symbolCounter;
            symbolCounter++;
            IRRoot.addConstString(name, value);
            strOperand = IRRoot.constStrings.get(name);
            it.operand = new Register(new PointerIRType(new IntIRType(8), false), "resolved_" + name);
        } else it.operand = new Register(new PointerIRType(new IntIRType(8), false), "resolved_" + strOperand.name);
        BaseIRType irType = new ArrayIRType(new IntIRType(8), value.length());
        GetElePtrInst inst = new GetElePtrInst(currentBlock, (Register) it.operand, irType, strOperand, new IntOperand(0, 32), new IntOperand(0, 32));
        currentBlock.addInst(inst);
    }

    @Override
    public void visit(SuffixExpr it) {
        it.src.accept(this);
        it.operand = resolvePointer(currentBlock, it.src.operand);
        BinaryInst inst;
        Register leftOp = new Register(new IntIRType(32), "suffix_leftOp");
        if (it.op.equals("++"))
            inst = new BinaryInst(currentBlock, (Register) it.operand, "+", leftOp, new IntOperand(1, 32));
        else inst = new BinaryInst(currentBlock, (Register) it.operand, "-", leftOp, new IntOperand(1, 32));
        currentBlock.addInst(inst);
        currentBlock.addInst(new StoreInst(currentBlock, it.src.operand, leftOp));
    }

    @Override
    public void visit(ThisExpr it) {
        it.operand = currentFunc.classPtr;
    }

    @Override
    public void visit(VarExpr it) {
        if (it.varEntity.isMember) it.operand = it.varEntity.operand;
        else {
            it.operand = new Register(it.varEntity.operand.type, "this." + it.identifier + "_addr");
            BaseIRType irType = ((PointerIRType) currentFunc.classPtr.type).basicType;
            BaseOperand pointer = currentFunc.classPtr;
            BaseOperand arrayOffset = new IntOperand(0, 32);
            IntOperand eleOffset = it.varEntity.index;
            GetElePtrInst inst = new GetElePtrInst(currentBlock, (Register) it.operand, irType, pointer, arrayOffset, eleOffset);
            currentBlock.addInst(inst);
        }
        branchAdd(it);
    }

    @Override
    public void visit(BlockStmt it) {
        for (BaseStmt stmt : it.stmts) {
            stmt.accept(this);
            if (currentBlock.terminated) break;
        }
    }

    @Override
    public void visit(BreakStmt it) {
        IRBlock targetBlock;
        if (it.target instanceof ForStmt) targetBlock = ((ForStmt) it.target).targetBlock;
        else if (it.target instanceof WhileStmt) targetBlock = ((WhileStmt) it.target).targetBlock;
        else throw new InternalError("unexpected break", it.pos);
        currentBlock.addTerminalInst(new JumpInst(currentBlock, targetBlock));
    }

    @Override
    public void visit(ContinueStmt it) {
        IRBlock targetBlock;
        if (it.target instanceof ForStmt) targetBlock = ((ForStmt) it.target).incrBlock;
        else if (it.target instanceof WhileStmt) targetBlock = ((WhileStmt) it.target).condBlock;
        else throw new InternalError("unexpected continue", it.pos);
        currentBlock.addTerminalInst(new JumpInst(currentBlock, targetBlock));
    }

    @Override
    public void visit(EmptyStmt it) {
        /* Empty */
    }

    @Override
    public void visit(ForStmt it) {
        IRBlock bodyBlock = new IRBlock("for_body");
        IRBlock condBlock = it.condition == null ? bodyBlock : new IRBlock("for_cond");
        IRBlock incrBlock = new IRBlock("for_incr");
        IRBlock targetBlock = new IRBlock("for_target");
        it.targetBlock = targetBlock;
        it.incrBlock = incrBlock;

        if (it.init != null) it.init.accept(this);
        if (it.condition == null)
            currentBlock.addTerminalInst(new JumpInst(currentBlock, bodyBlock));
        else {
            currentBlock.addTerminalInst(new JumpInst(currentBlock, condBlock));
            currentBlock = condBlock;
            it.condition.thenBlock = bodyBlock;
            it.condition.elseBlock = targetBlock;
            it.condition.accept(this);
        }

        currentBlock = bodyBlock;
        it.stmts.accept(this);
        if (!currentBlock.terminated)
            currentBlock.addTerminalInst(new JumpInst(currentBlock, incrBlock));
        currentBlock = incrBlock;
        if (it.incr != null) it.incr.accept(this);
        if (!currentBlock.terminated)
            currentBlock.addTerminalInst(new JumpInst(currentBlock, condBlock));
        currentBlock = targetBlock;
    }

    @Override
    public void visit(IfStmt it) {
        IRBlock trueBlock = new IRBlock("if_then");
        IRBlock targetBlock = new IRBlock("if_target");
        IRBlock falseBlock = it.falseStmt == null ? targetBlock : new IRBlock("if_else");
        it.condition.thenBlock = trueBlock;
        it.condition.elseBlock = falseBlock;
        it.condition.accept(this);

        currentBlock = trueBlock;
        it.trueStmt.accept(this);
        if (!currentBlock.terminated)
            currentBlock.addTerminalInst(new JumpInst(currentBlock, targetBlock));

        if (it.falseStmt != null) {
            currentBlock = falseBlock;
            it.falseStmt.accept(this);
            if (!currentBlock.terminated)
                currentBlock.addTerminalInst(new JumpInst(currentBlock, targetBlock));
        }
        if (!trueBlock.retTerminated() || !falseBlock.retTerminated())
            currentBlock = targetBlock;
    }

    @Override
    public void visit(PureExprStmt it) {
        it.expr.accept(this);
    }

    @Override
    public void visit(ReturnStmt it) {
        ReturnInst returnInst;
        if (it.expr == null) returnInst = new ReturnInst(currentBlock, null);
        else {
            it.expr.accept(this);
            if (it.expr.operand.type.dim() > currentFunc.returnType.dim() + 1)
                throw new InternalError("wrong dimension", it.pos);
            else if (it.expr.operand.type.dim() == currentFunc.returnType.dim() + 1)
                returnInst = new ReturnInst(currentBlock, resolvePointer(currentBlock, it.expr.operand));
            else returnInst = new ReturnInst(currentBlock, it.expr.operand);
        }
        currentBlock.addTerminalInst(returnInst);
        returnInsts.add(returnInst);
    }

    @Override
    public void visit(VarDefStmt it) {
        /* Empty */
    }

    @Override
    public void visit(VarDefSubStmt it) {
        BaseIRType irType = IRRoot.getIRType(it.varEntity.type, true);
        if (it.varEntity.isGlobal) {
            GlobalVarOperand operand = new GlobalVarOperand(new PointerIRType(irType, true), it.name);
            it.varEntity.operand = operand;
            IRRoot.gVars.add(operand);
            if (it.init != null) {
                currentFunc = IRRoot.getInit();
                currentBlock = IRRoot.getInit().exitBlock;
                assign(it.varEntity.operand, it.init);
                currentFunc.exitBlock = currentBlock;
                currentFunc = null;
                currentBlock = null;
            }
        } else if (isPara) {
            ParaOperand para = new ParaOperand(irType, it.name + "_para");
            currentFunc.paras.add(para);
            Register addrReg = new Register(new PointerIRType(irType, true), it.name + "_addr");
            it.varEntity.operand = addrReg;
            currentFunc.vars.add(addrReg);
            currentBlock.addInst(new StoreInst(currentBlock, addrReg, para));
        } else if (currentFunc == null) {
            if (irType instanceof ClassIRType)
                irType = new PointerIRType(irType, false);
            it.varEntity.operand = new Register(new PointerIRType(irType, true), it.name + "_addr");
        } else {
            Register addrReg = new Register(new PointerIRType(irType, true), it.name + "_addr");
            it.varEntity.operand = addrReg;
            if (it.init != null) assign(addrReg, it.init);
            currentFunc.vars.add(addrReg);
        }
    }

    @Override
    public void visit(WhileStmt it) {
        IRBlock condBlock = new IRBlock("while_cond");
        IRBlock bodyBlock = new IRBlock("while_body");
        IRBlock targetBlock = new IRBlock("while_target");
        it.targetBlock = targetBlock;
        if (it.condition == null) {
            currentBlock.addTerminalInst(new JumpInst(currentBlock, bodyBlock));
            it.condBlock = bodyBlock;
            condBlock = bodyBlock;
        } else {
            currentBlock.addTerminalInst(new JumpInst(currentBlock, condBlock));
            currentBlock = condBlock;
            it.condition.thenBlock = bodyBlock;
            it.condition.elseBlock = targetBlock;
            it.condition.accept(this);
            it.condBlock = condBlock;
        }
        currentBlock = bodyBlock;
        it.stmts.accept(this);
        if (!currentBlock.terminated)
            currentBlock.addTerminalInst(new JumpInst(currentBlock, condBlock));
        currentBlock = targetBlock;
    }

    private String getString(String str) {
        String string = str.replace("\\n", "\n");
        string = string.replace("\\t", "\t");
        string = string.replace("\\\"", "\"");
        string = string.replace("\\\\", "\\");
        string += "\0";
        return string;
    }

    private void setMethod(String name) {
        FuncEntity funcEntity = globalScope.getFunc(name, new Position(0, 0), false);
        funcEntity.function = IRRoot.funcs.get("g_" + name);
    }

    private void assign(BaseOperand operand, BaseExpr expr) {
        expr.accept(this);
        BaseOperand value = resolvePointer(currentBlock, expr.operand);
        if (value.type instanceof BoolIRType) {
            if (value instanceof BoolOperand) {
                BaseOperand value1 = new IntOperand(((BoolOperand) value).getIntValue(), 8);
                currentBlock.addInst(new StoreInst(currentBlock, operand, value1));
            } else {
                BaseOperand value1 = new Register(new IntIRType(8), "assignto");
                currentBlock.addInst(new ZextInst(currentBlock, (Register) value1, value));
                currentBlock.addInst(new StoreInst(currentBlock, operand, value1));
            }
        } else currentBlock.addInst(new StoreInst(currentBlock, operand, value));
    }

    private BaseOperand resolvePointer(IRBlock block, BaseOperand operand) {
        if (!operand.type.resolvable()) return operand;
        Register register = new Register(((PointerIRType) operand.type).basicType, "resolved_" + operand.getName());
        currentBlock.addInst(new LoadInst(currentBlock, register, operand));
        if (!(register.type instanceof IntIRType) || register.type.size() != 8) return register;
        Register zextReg = new Register(new BoolIRType(), "zext_" + register.name);
        currentBlock.addInst(new ZextInst(currentBlock, zextReg, register));
        return zextReg;
    }

    private BaseOperand resolveStrPointer(IRBlock block, BaseOperand operand) {
        if (!(operand.type instanceof PointerIRType)) throw new InternalError("not a string", new Position(0, 0));
        if (!(((PointerIRType) operand.type).basicType instanceof PointerIRType)) return operand;
        Register register = new Register(((PointerIRType) operand.type).basicType, "resolved_" + operand.getName());
        currentBlock.addInst(new LoadInst(currentBlock, register, operand));
        return register;
    }

    private void branchAdd(BaseExpr expr) {
        if (expr.thenBlock == null) return;
        BaseOperand operand = resolvePointer(currentBlock, expr.operand);
        currentBlock.addTerminalInst(new BranchInst(currentBlock, operand, expr.thenBlock, expr.elseBlock));
        if (logicalInstPhis.containsKey(expr.thenBlock)) {
            LogicalInstPhi phis = logicalInstPhis.get(expr.thenBlock);
            phis.blocks.add(currentBlock);
            phis.values.add(new BoolOperand(true));
        }
        if (logicalInstPhis.containsKey(expr.elseBlock)) {
            LogicalInstPhi phis = logicalInstPhis.get(expr.elseBlock);
            phis.blocks.add(currentBlock);
            phis.values.add(new BoolOperand(false));
        }
    }

    private void arrayMalloc(NewExpr expr, int dim, Register register) {
        if (dim == expr.exprs.size()) return;
        expr.exprs.get(dim).accept(this);
        Register arrayPtr = dim == 0 ? register : new Register(((PointerIRType) register.type).basicType, "arrayMallocPtr");
        BaseOperand operand = resolvePointer(currentBlock, expr.exprs.get(dim).operand);
        Register pureWidthReg = new Register(new IntIRType(32), "pure_width");
        Register arrayWidthReg = new Register(new IntIRType(32), "array_width");
        Register allocReg = new Register(new PointerIRType(new IntIRType(8), false), "allocReg");
        Register bitReg = new Register(new PointerIRType(new IntIRType(32), false), "bitReg");
        Register offsetReg = new Register(new PointerIRType(new IntIRType(32), false), "offsetReg");
        IntOperand typeWidth = new IntOperand(((PointerIRType) arrayPtr.type).basicType.size() / 8, 32);
        currentBlock.addInst(new BinaryInst(currentBlock, pureWidthReg, "*", operand, typeWidth));
        currentBlock.addInst(new BinaryInst(currentBlock, arrayWidthReg, "+", pureWidthReg, new IntOperand(4, 32)));
        currentBlock.addInst(new MallocInst(currentBlock, allocReg, arrayWidthReg));
        currentBlock.addInst(new BitCastInst(currentBlock, bitReg, allocReg));
        currentBlock.addInst(new StoreInst(currentBlock, bitReg, operand));

        BaseIRType basicType = ((PointerIRType) arrayPtr.type).basicType;
        if (basicType instanceof IntIRType && basicType.size() == 32)
            currentBlock.addInst(new GetElePtrInst(currentBlock, arrayPtr, new IntIRType(32), bitReg, new IntOperand(1, 32), null));
        else {
            currentBlock.addInst(new GetElePtrInst(currentBlock, offsetReg, new IntIRType(32), bitReg, new IntOperand(1, 32), null));
            currentBlock.addInst(new BitCastInst(currentBlock, arrayPtr, offsetReg));
        }
        if (dim != 0)
            currentBlock.addInst(new StoreInst(currentBlock, register, arrayPtr));
        if (dim < expr.exprs.size() - 1) {
            IRBlock bodyBlock = new IRBlock("array_body");
            IRBlock incrBlock = new IRBlock("array_incr");
            IRBlock targetBlock = new IRBlock("array_target");
            Register pointer = new Register(new PointerIRType(new IntIRType(32), false), "pointer");
            Register castedPtr = new Register(arrayPtr.type, "castedPtr");
            Register counter = new Register(new IntIRType(32), "counter");
            Register counterTmp = new Register(new IntIRType(32), "counterTmp");
            Register isBranch = new Register(new BoolIRType(), "isBranch");
            ArrayList<IRBlock> blocks = new ArrayList<>();
            ArrayList<BaseOperand> values = new ArrayList<>();
            blocks.add(currentBlock);
            values.add(new IntOperand(0, 32));
            currentBlock.addTerminalInst(new JumpInst(currentBlock, incrBlock));
            currentBlock = incrBlock;
            currentBlock.addInst(new BinaryInst(currentBlock, counterTmp, "+", counter, new IntOperand(1, 32)));
            currentBlock.addInst(new CmpInst(currentBlock, isBranch, "<=", counter, operand));
            currentBlock.addTerminalInst(new BranchInst(currentBlock, isBranch, bodyBlock, targetBlock));
            currentBlock = bodyBlock;
            currentBlock.addInst(new GetElePtrInst(currentBlock, pointer, new IntIRType(32), bitReg, counter, null));
            currentBlock.addInst(new BitCastInst(currentBlock, castedPtr, pointer));
            arrayMalloc(expr, dim + 1, castedPtr);
            currentBlock.addTerminalInst(new JumpInst(currentBlock, incrBlock));
            blocks.add(currentBlock);
            values.add(counterTmp);
            currentBlock = incrBlock;
            currentBlock.addPhi(new PhiInst(currentBlock, counter, blocks, values));
            currentBlock = targetBlock;
        }
    }

    private BaseOperand toIntOperand(BaseOperand operand) {
        if (!(operand.type instanceof BoolIRType)) return operand;
        Register zextReg = new Register(new IntIRType(32), "zext_" + operand.toString());
        currentBlock.addInst(new ZextInst(currentBlock, zextReg, operand));
        return zextReg;
    }

    private void dfsEntry(IRBlock block) {
        entryBlocks.add(block);
        for (IRBlock successor : block.successors) {
            if (!entryBlocks.contains(successor))
                dfsEntry(successor);
        }
    }

    private boolean dfsReturn(IRBlock block) {
        returnedBlocks.add(block);
        for (IRBlock pressessor : block.pressessors) {
            if (!returnedBlocks.contains(pressessor) && dfsReturn(pressessor))
                pressessor.removeTerminalInst();
        }
        if (entryBlocks.contains(block)) return false;
        else return block.pressessors.isEmpty();
    }

    private void setEntry() {
        entryBlocks = new LinkedHashSet<>();
        returnedBlocks = new LinkedHashSet<>();
        dfsEntry(currentFunc.entryBlock);
        for (Iterator<ReturnInst> iter = returnInsts.iterator(); iter.hasNext(); ) {
            if (dfsReturn(iter.next().block)) {
                ReturnInst nxt = iter.next();
                iter.remove();
                nxt.block.removeTerminalInst();
            }
        }
        entryBlocks.removeIf(block -> !block.terminated);
        currentFunc.blocks.addAll(entryBlocks);
        entryBlocks = null;
        returnedBlocks = null;
    }
}

