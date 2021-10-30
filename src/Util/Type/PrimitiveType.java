package Util.Type;

public class PrimitiveType extends BaseType {
    public PrimitiveType(String typeName) {
        super(typeName);
    }

    @Override
    public boolean equals(BaseType other) {
        return (typeName.equals(other.typeName) && other.dim == 0) || (isNull() && (other.isArray() || other.isClass()));
    }
}
