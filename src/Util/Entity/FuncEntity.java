package Util.Entity;

import Util.Position;
import Util.Scope.BaseScope;
import Util.Scope.FuncScope;
import Util.Type.BaseType;

public class FuncEntity extends BaseEntity {
    public FuncScope scope = null;

    public FuncEntity(BaseType type, String name) {
        super(type, name);
    }

    public FuncEntity(BaseType type, String name, FuncScope scope) {
        super(type, name);
        this.scope = scope;
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
