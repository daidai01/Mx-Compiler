package Util.Entity;

import Util.Position;
import Util.Scope.BaseScope;
import Util.Scope.FuncScope;
import Util.Type.BaseType;

public class FuncEntity extends BaseEntity {
    public FuncScope scope = null;

    public FuncEntity(BaseType type, String name, FuncScope scope) {
        super(type, name);
        this.scope = scope;
    }

    public void addPara(VarEntity para, Position pos) {
        scope.addPara(para, pos);
    }
}
