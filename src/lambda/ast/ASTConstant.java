package lambda.ast;

import lambda.reduction.delta.DeltaRule;

import java.util.*;

/**
 * Represents a constant.
 */
public class ASTConstant extends ASTTerm {
    private Object value;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ASTConstant(Object value) {
        assert(value != null);
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ASTConstant that = (ASTConstant) o;

        return getValue().equals(that.getValue());

    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }

    @Override
    protected Optional<ASTTerm> applyBetaReduction() {
        return Optional.empty();
    }

    @Override
    protected Optional<ASTTerm> applyDeltaReduction(DeltaRule delta) {
        // try to apply the delta rule only to the constant
        return delta.getRHS(this, new ArrayList<>());
    }

    @Override
    public Optional<ASTTerm> applyWHNOReduction(List<DeltaRule> deltaRules) {
        return Optional.empty();
    }

    @Override
    public List<ASTTerm> getLMOMArguments() {
        return new ArrayList<>();
    }

    @Override
    public ASTTerm getLMOMTerm() {
        return this;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Set<ASTVariable> getFreeVars() {
        // a constant is never free
        return new HashSet<>();
    }

    @Override
    public ASTTerm substitute(ASTVariable var, ASTTerm expr) {
        // constants cannot be substituted because they're not free
        return this;
    }
}