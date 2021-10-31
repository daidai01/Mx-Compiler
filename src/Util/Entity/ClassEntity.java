package Util.Entity;

import Util.Position;
import Util.Scope.ClassScope;
import Util.Type.BaseType;

public class ClassEntity extends BaseEntity {
    public ClassScope scope = null;

    public ClassEntity(String name) {
        //TODO how to define type of a class
        super(new BaseType(name), name);
    }

    public ClassEntity(BaseType type, String name, ClassScope scope) {
        super(type, name);
        this.scope = scope;
    }

    public void defineFunc(String name, FuncEntity func, Position pos) {
        scope.defineFunc(name, func, pos);
    }

    @Override
    public boolean isArray() {
        return this.type.dim > 0;
    }
}
