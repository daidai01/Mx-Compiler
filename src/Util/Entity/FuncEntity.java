package Util.Entity;

import IR.Program.IRFunction;
import Util.Position;
import Util.Scope.FuncScope;
import Util.Type.BaseType;

public class FuncEntity extends BaseEntity {
    public FuncScope scope = null;
    public IRFunction function = null;
    public boolean isMethod = false;

    public FuncEntity(BaseType type, String name, boolean isMethod) {
        super(type, name);
        this.isMethod = isMethod;
    }

    public FuncEntity(BaseType type, String name, FuncScope scope) {
        super(type, name);
        this.scope = scope;
    }

    public FuncEntity(BaseType type, String name, FuncScope scope, boolean isMethod) {
        super(type, name);
        this.scope = scope;
        this.isMethod = isMethod;
    }

    public void check() {
        if (type != null) {
            this.typeName = type.typeName;
            this.dim = type.dim;
        }
    }

    public void addPara(VarEntity para, Position pos) {
        scope.addPara(para, pos);
    }
}
