package Util.Entity;

import Util.Position;
import Util.Scope.ClassScope;
import Util.Type.BaseType;

public class ClassEntity extends BaseEntity {
    public ClassScope scope = null;
    public int allocSize = 0;

    public ClassEntity(String name) {
        super(new BaseType(name), name);
    }

    public ClassEntity(BaseType type, String name, ClassScope scope) {
        super(type, name);
        this.scope = scope;
    }

    public void defineFunc(String name, FuncEntity func, Position pos) {
        scope.defineFunc(name, func, pos);
    }

    public int addAllocSize(BaseType type) {
        allocSize += type.size();
        return allocSize;
    }

    @Override
    public boolean isArray() {
        return this.type.dim > 0;
    }
}
