package Frontend;

import AST.*;
import AST.Expr.*;
import AST.Program.*;
import AST.Stmt.*;
import Parser.MxBaseVisitor;
import Parser.MxParser;
import Util.Error.InternalError;
import Util.Error.SyntaxError;
import Util.Position;
import Util.Type.ArrayType;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;

public class ASTBuilder extends MxBaseVisitor<ASTNode> {
    @Override
    public ASTNode visitProgram(MxParser.ProgramContext ctx) {
        RootNode root = new RootNode(new Position(ctx));
        for (ParserRuleContext obj : ctx.subProgram()) {
            ASTNode tmp = visit(obj);
            if (tmp instanceof VarDefStmt)
                root.defs.addAll(((VarDefStmt) tmp).vars);
            else root.defs.add(tmp);
        }
        return root;
    }

    @Override
    public ASTNode visitSubProgram(MxParser.SubProgramContext ctx) {
        if (ctx.classDef() != null) return visit(ctx.classDef());
        else if (ctx.funcDef() != null) return visit(ctx.funcDef());
        else if (ctx.varDef() != null) return visit(ctx.varDef());
        else return null;
    }

    @Override
    public ASTNode visitVarDef(MxParser.VarDefContext ctx) {
        VarDefStmt varDef = new VarDefStmt(new Position(ctx));
        TypeNode type = (TypeNode) visit(ctx.varType());
        for (ParserRuleContext obj : ctx.subVar()) {
            VarDefSubStmt tmp = (VarDefSubStmt) visit(obj);
            tmp.type = type;
            varDef.vars.add(tmp);
        }
        return varDef;
    }

    @Override
    public ASTNode visitFuncDef(MxParser.FuncDefContext ctx) {
        String name = ctx.Identifier().getText();
        TypeNode type = null;
        boolean isConstructor = false;
        if (ctx.returnType() == null) isConstructor = true;
        else type = (TypeNode) visit(ctx.returnType());
        ArrayList<VarDefSubStmt> paras = new ArrayList<>();
        if (ctx.parameterList() != null) {
            for (int i = 0; i < ctx.parameterList().varType().size(); ++i) {
                TypeNode varType = (TypeNode) visit(ctx.parameterList().varType(i));
                String identifier = ctx.parameterList().Identifier(i).getText();
                VarDefSubStmt tmp = new VarDefSubStmt(varType, identifier, null, new Position(ctx.parameterList().varType(i)));
                paras.add(tmp);
            }
        }
        BlockStmt block = (BlockStmt) visit(ctx.suite());
        return new FuncDef(new Position(ctx), name, type, block, paras, isConstructor);
    }

    @Override
    public ASTNode visitClassDef(MxParser.ClassDefContext ctx) {
        String name = ctx.Identifier().getText();
        boolean hasConstructor = false;
        ArrayList<VarDefSubStmt> vars = new ArrayList<>();
        ArrayList<FuncDef> funcs = new ArrayList<>();
        ArrayList<FuncDef> constructors = new ArrayList<>();
        for (ParserRuleContext obj : ctx.varDef()) {
            VarDefStmt tmp = (VarDefStmt) visit(obj);
            vars.addAll(tmp.vars);
        }
        for (ParserRuleContext obj : ctx.funcDef()) {
            FuncDef tmp = (FuncDef) visit(obj);
            if (tmp.isConstructor) {
                hasConstructor = true;
                constructors.add(tmp);
            } else funcs.add(tmp);
        }
        return new ClassDef(new Position(ctx), name, hasConstructor, vars, funcs, constructors);
    }

    @Override
    public ASTNode visitSuite(MxParser.SuiteContext ctx) {
        BlockStmt block = new BlockStmt(new Position(ctx));
        for (ParserRuleContext obj : ctx.statement()) {
            BaseStmt tmp = (BaseStmt) visit(obj);
            if (tmp instanceof VarDefStmt)
                block.stmts.addAll(((VarDefStmt) tmp).vars);
            else block.stmts.add(tmp);
        }
        return block;
    }

    @Override
    public ASTNode visitSubVar(MxParser.SubVarContext ctx) {
        BaseExpr init = null;
        if (ctx.expression() != null)
            init = (BaseExpr) visit(ctx.expression());
        return new VarDefSubStmt(null, ctx.Identifier().getText(), init, new Position(ctx));
    }

    @Override
    public ASTNode visitParameterList(MxParser.ParameterListContext ctx) {
        VarDefStmt varDefStmt = new VarDefStmt(new Position(ctx));
        if (ctx.varType() != null) {
            for (int i = 0; i < ctx.varType().size(); ++i) {
                TypeNode type = (TypeNode) visit(ctx.varType(i));
                String identifier = ctx.Identifier(i).getText();
                varDefStmt.vars.add(new VarDefSubStmt(type, identifier, null, new Position(ctx.varType(i))));
            }
        }
        return varDefStmt;
    }

    @Override
    public ASTNode visitBlock(MxParser.BlockContext ctx) {
        return visit(ctx.suite());
    }

    @Override
    public ASTNode visitVarDefStmt(MxParser.VarDefStmtContext ctx) {
        return visit(ctx.varDef());
    }

    @Override
    public ASTNode visitIfStmt(MxParser.IfStmtContext ctx) {
        BaseExpr condition = (BaseExpr) visit(ctx.expression());
        BaseStmt trueStmt = (BaseStmt) visit(ctx.trueStmt);
        BaseStmt falseStmt = null;
        if (ctx.falseStmt != null)
            falseStmt = (BaseStmt) visit(ctx.falseStmt);
        return new IfStmt(condition, trueStmt, falseStmt, new Position(ctx));
    }

    @Override
    public ASTNode visitWhileStmt(MxParser.WhileStmtContext ctx) {
        BaseExpr condition = (BaseExpr) visit(ctx.expression());
        BaseStmt stmts = (BaseStmt) visit(ctx.statement());
        return new WhileStmt(condition, stmts, new Position(ctx));
    }

    @Override
    public ASTNode visitForStmt(MxParser.ForStmtContext ctx) {
        BaseExpr init = null;
        if (ctx.init != null)
            init = (BaseExpr) visit(ctx.init);
        BaseExpr cond = null;
        if (ctx.cond != null)
            cond = (BaseExpr) visit(ctx.cond);
        BaseExpr incr = null;
        if (ctx.incr != null)
            incr = (BaseExpr) visit(ctx.incr);
        BaseStmt stmts = (BaseStmt) visit(ctx.statement());
        return new ForStmt(init, cond, incr, stmts, new Position(ctx));
    }

    @Override
    public ASTNode visitReturnStmt(MxParser.ReturnStmtContext ctx) {
        BaseExpr expr = null;
        if (ctx.expression() != null)
            expr = (BaseExpr) visit(ctx.expression());
        return new ReturnStmt(expr, new Position(ctx));
    }

    @Override
    public ASTNode visitBreakStmt(MxParser.BreakStmtContext ctx) {
        return new BreakStmt(new Position(ctx));
    }

    @Override
    public ASTNode visitContinueStmt(MxParser.ContinueStmtContext ctx) {
        return new ContinueStmt(new Position(ctx));
    }

    @Override
    public ASTNode visitPureExprStmt(MxParser.PureExprStmtContext ctx) {
        BaseExpr expr = (BaseExpr) visit(ctx.expression());
        return new PureExprStmt(expr, new Position(ctx));
    }

    @Override
    public ASTNode visitEmptyStmt(MxParser.EmptyStmtContext ctx) {
        return new EmptyStmt(new Position(ctx));
    }

    @Override
    public ASTNode visitNewExpr(MxParser.NewExprContext ctx) {
        return visit(ctx.creator());
    }

    @Override
    public ASTNode visitIndexExpr(MxParser.IndexExprContext ctx) {
        BaseExpr identifier = (BaseExpr) visit(ctx.expression(0));
        BaseExpr index = (BaseExpr) visit(ctx.expression(1));
        return new IndexExpr(identifier, index, new Position(ctx));
    }

    @Override
    public ASTNode visitPrefixExpr(MxParser.PrefixExprContext ctx) {
        String op = ctx.op.getText();
        switch (op) {
            case "!":
            case "~":
            case "++":
            case "--":
            case "+":
            case "-":
                break;
            default:
                throw new InternalError(op + ": wrong prefixExpr operator", new Position(ctx));
        }
        BaseExpr expr = (BaseExpr) visit(ctx.expression());
        return new PrefixExpr(expr, op, new Position(ctx));
    }

    @Override
    public ASTNode visitLambdaExpr(MxParser.LambdaExprContext ctx) {
        ArrayList<VarDefSubStmt> paras = new ArrayList<>();
        BlockStmt block = (BlockStmt) visit(ctx.suite());
        ArrayList<BaseExpr> exprs = new ArrayList<>();
        if (ctx.parameterList() != null) {
            for (int i = 0; i < ctx.parameterList(0).varType().size(); ++i) {
                TypeNode type = (TypeNode) visit(ctx.parameterList(0).varType(i));
                String identifier = ctx.parameterList(0).Identifier(i).getText();
                VarDefSubStmt tmp = new VarDefSubStmt(type, identifier, null, new Position(ctx.parameterList(0).varType(i)));
                paras.add(tmp);
            }
        }
        for (ParserRuleContext obj : ctx.expressionList().expression()) {
            BaseExpr tmp = (BaseExpr) visit(obj);
            exprs.add(tmp);
        }
        return new LambdaExpr(paras, block, exprs, new Position(ctx));
    }

    @Override
    public ASTNode visitMemberExpr(MxParser.MemberExprContext ctx) {
        BaseExpr expr = (BaseExpr) visit(ctx.expression());
        String identifier = ctx.Identifier().getText();
        return new MemberExpr(expr, identifier, new Position(ctx));
    }

    @Override
    public ASTNode visitSuffixExpr(MxParser.SuffixExprContext ctx) {
        String op = ctx.op.getText();
        switch (op) {
            case "++":
            case "--":
                break;
            default:
                throw new InternalError(op + ": wrong SuffixExpr operator", new Position(ctx));
        }
        BaseExpr expr = (BaseExpr) visit(ctx.expression());
        return new SuffixExpr(expr, op, new Position(ctx));
    }

    @Override
    public ASTNode visitAtomExpr(MxParser.AtomExprContext ctx) {
        return visit(ctx.primary());
    }

    @Override
    public ASTNode visitBinaryExpr(MxParser.BinaryExprContext ctx) {
        String op = ctx.op.getText();
        switch (op) {
            case "*":
            case "/":
            case "%":
            case "+":
            case "-":
            case ">>":
            case "<<":
            case ">":
            case "<":
            case ">=":
            case "<=":
            case "==":
            case "!=":
            case "&":
            case "^":
            case "|":
            case "&&":
            case "||":
                break;
            default:
                throw new InternalError(op + ": wrong BinaryExpr operator", new Position(ctx));
        }
        BaseExpr src1 = (BaseExpr) visit(ctx.expression(0));
        BaseExpr src2 = (BaseExpr) visit(ctx.expression(1));
        return new BinaryExpr(src1, src2, op, new Position(ctx));
    }

    @Override
    public ASTNode visitAssignExpr(MxParser.AssignExprContext ctx) {
        BaseExpr src1 = (BaseExpr) visit(ctx.expression(0));
        BaseExpr src2 = (BaseExpr) visit(ctx.expression(1));
        return new AssignExpr(src1, src2, new Position(ctx));
    }

    @Override
    public ASTNode visitFunctionExpr(MxParser.FunctionExprContext ctx) {
        BaseExpr expr = (BaseExpr) visit(ctx.expression());
        ArrayList<BaseExpr> paras = new ArrayList<>();
        if (ctx.expressionList() != null) {
            for (ParserRuleContext obj : ctx.expressionList().expression()) {
                BaseExpr tmp = (BaseExpr) visit(obj);
                paras.add(tmp);
            }
        }
        if (expr instanceof VarExpr)
            expr = new FuncExpr(((VarExpr) expr).identifier, expr.pos);
        else if (expr instanceof MemberExpr)
            ((MemberExpr) expr).setIsFunc();
        //TODO check this
//        else if(expr instanceof MemberExpr)
        return new FuncCallExpr(expr, paras, new Position(ctx));
    }

    @Override
    public ASTNode visitPrimary(MxParser.PrimaryContext ctx) {
        if (ctx.expression() != null) return visit(ctx.expression());
        else if (ctx.Identifier() != null) return new VarExpr(ctx.Identifier().getText(), new Position(ctx));
        else if (ctx.literal() != null) return visit(ctx.literal());
        else return new ThisExpr(new Position(ctx));
    }

    @Override
    public ASTNode visitLiteral(MxParser.LiteralContext ctx) {
        if (ctx.True() != null)
            return new BoolLiteralExpr(true, new Position(ctx));
        else if (ctx.False() != null)
            return new BoolLiteralExpr(false, new Position(ctx));
        else if (ctx.ConstInteger() != null) {
            int value = Integer.parseInt(ctx.ConstInteger().getText());
            return new IntLiteralExpr(value, new Position(ctx));
        } else if (ctx.ConstString() != null) {
            String value = ctx.ConstString().getText();
            return new StringLiteralExpr(value, new Position(ctx));
        } else return new NullLiteralExpr(new Position(ctx));
    }

    @Override
    public ASTNode visitExpressionList(MxParser.ExpressionListContext ctx) {
        ArrayList<BaseExpr> exprs = new ArrayList<>();
        for (ParserRuleContext obj : ctx.expression()) {
            BaseExpr expr = (BaseExpr) visit(obj);
            exprs.add(expr);
        }
        return new ExprList(exprs, new Position(ctx));
    }

    @Override
    public ASTNode visitArrayCreator(MxParser.ArrayCreatorContext ctx) {
        String name = ctx.basicType().getText();
        TypeNode type = new TypeNode(new Position(ctx.basicType()), name, ctx.LeftBracket().size());
        ArrayList<BaseExpr> exprs = new ArrayList<>();
        for (ParserRuleContext obj : ctx.expression()) {
            BaseExpr tmp = (BaseExpr) visit(obj);
            exprs.add(tmp);
        }
        return new NewExpr(type, exprs, new Position(ctx));
    }

    @Override
    public ASTNode visitBasicCreator(MxParser.BasicCreatorContext ctx) {
        String name = ctx.basicType().getText();
        TypeNode type = new TypeNode(new Position(ctx.basicType()), name, 0);
        return new NewExpr(type, new ArrayList<BaseExpr>(), new Position(ctx));
    }

    @Override
    public ASTNode visitWrongCreator(MxParser.WrongCreatorContext ctx) {
        throw new SyntaxError("wrong creator", new Position(ctx));
    }

    @Override
    public ASTNode visitBasicType(MxParser.BasicTypeContext ctx) {
        if (ctx.Bool() != null) return new TypeNode(new Position(ctx), "bool", 0);
        else if (ctx.Int() != null) return new TypeNode(new Position(ctx), "int", 0);
        else if (ctx.String() != null) return new TypeNode(new Position(ctx), "string", 0);
        else return new TypeNode(new Position(ctx), ctx.Identifier().getText(), 0);
    }

    @Override
    public ASTNode visitReturnType(MxParser.ReturnTypeContext ctx) {
        if (ctx.basicType() != null) {
            TypeNode typeNode = (TypeNode) visit(ctx.basicType());
            typeNode.setDim(ctx.LeftBracket().size());
            return typeNode;
        } else return new TypeNode(new Position(ctx), "void", 0);
    }

    @Override
    public ASTNode visitVarType(MxParser.VarTypeContext ctx) {
        String type = ctx.basicType().getText();
        int dim = 0;
        if (ctx.LeftBracket() != null) dim = ctx.LeftBracket().size();
        return new TypeNode(new Position(ctx), type, dim);
    }
}
