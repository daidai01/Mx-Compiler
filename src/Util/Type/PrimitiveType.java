package Util.Type;

public class PrimitiveType extends BaseType {
    public PrimitiveType(String typeName) {
        super(typeName);
    }

    @Override
    public boolean equals(BaseType other) {
        return (typeName.equals(other.typeName) && other.dim == 0) || (isNull() && (other.isArray() || other.isClass()));
    }

    @Override
    public boolean isBool() {
        return typeName.equals("bool");
    }

    @Override
    public boolean isInt() {
        return typeName.equals("int");
    }

    @Override
    public boolean isVoid() {
        return typeName.equals("void");
    }

    @Override
    public boolean isString() {
        return typeName.equals("string");
    }

    @Override
    public boolean isNull() {
        return typeName.equals("null");
    }
}
