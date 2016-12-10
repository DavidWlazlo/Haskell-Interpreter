package haskell.complex.ast;

import haskell.complex.reduction.SimpleReducer;

import java.util.Collections;
import java.util.Set;

/**
 * Represents a complex haskell boolean.
 */
public class ASTBoolean implements ASTExpression, ASTPattern {
    private boolean value;

    public ASTBoolean(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ASTBoolean that = (ASTBoolean) o;

        return getValue() == that.getValue();

    }

    @Override
    public int hashCode() {
        return (getValue() ? 1 : 0);
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    @Override
    public Set<ASTVariable> getAllVariables() {
        return Collections.emptySet();
    }

    @Override
    public boolean nestMultipleLambdas() {
        return false;
    }

    @Override
    public boolean lambdaPatternToCase() {
        return false;
    }

    @Override
    public boolean caseToMatch() {
        return false;
    }

    @Override
    public boolean nestMultipleLets() {
        return false;
    }

    @Override
    public boolean tuplePatLetToSingleVar() {
        return false;
    }

    @Override
    public haskell.simple.ast.ASTExpression castToSimple() throws SimpleReducer.TooComplexException {
        return new haskell.simple.ast.ASTConstant(value);
    }

    @Override
    public Set<ASTVariable> getFreeVars() {
        return Collections.emptySet();
    }
}
