package haskell.complex.ast;

/**
 * Represents a variable, i.e. a name that starts with a lower case.
 */
public class ASTVariable implements ASTExpression, ASTPattern {
    private String name;

    public ASTVariable(String name) {
        assert(name != null);
        assert(!name.trim().equals(""));
        assert(Character.isLowerCase(name.charAt(0)));
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ASTVariable that = (ASTVariable) o;

        return getName().equals(that.getName());

    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
