package IR.Type;

abstract public class BaseIRType {
    public BaseIRType(){}

    abstract public int getBytes();

    abstract public boolean equals(BaseIRType other);
}
