package Util.Scope;

import Util.Error.SemanticError;
import Util.Position;
import Util.Entity.*;

import java.util.HashMap;

public class BaseScope {
    public HashMap<String, VarEntity> vars = new HashMap<>();
    public HashMap<String, FuncEntity> funcs = new HashMap<>();
    public BaseScope parentScope = null;
    //TODO do I need constructor?

    public BaseScope(BaseScope parentScope) {
        this.parentScope = parentScope;
    }

    public void defineVar(String name, VarEntity var, Position pos) {
        if (vars.containsKey(name))
            throw new SemanticError(name + ": redefine var ", pos);
        vars.put(name, var);
    }

    public boolean containVar(String name, boolean lookUpon) {
        if (vars.containsKey(name)) return true;
        if (parentScope != null && lookUpon) return parentScope.containVar(name, true);
        return false;
    }

    public VarEntity getVar(String name, Position pos, boolean lookUpon) {
        if (vars.containsKey(name)) return vars.get(name);
        if (parentScope != null && lookUpon) return parentScope.getVar(name, pos, true);
        throw new SemanticError(name + ": undefined variable", pos);
    }

    public void defineFunc(String name, FuncEntity func, Position pos) {
        if (funcs.containsKey(name))
            throw new SemanticError(name + ": redefine function", pos);
        funcs.put(name, func);
    }

    public boolean containFunc(String name, boolean lookUpon) {
        if (funcs.containsKey(name)) return true;
        if (parentScope != null && lookUpon) return parentScope.containFunc(name, true);
        return false;
    }

    public FuncEntity getFunc(String name, Position pos, boolean lookUpon) {
        if (funcs.containsKey(name)) {
            FuncEntity func = funcs.get(name);
            func.check();
            return func;
        } else if (parentScope != null && lookUpon) return parentScope.getFunc(name, pos, true);
        throw new SemanticError(name + ": undefined function", pos);
    }
}
