package haskell.complex.ast;

import haskell.complex.reduction.SimpleReducer;
import haskell.simple.ast.ASTConstant;
import lambda.reduction.WHNOReducer;
import lambda.reduction.delta.ConstructorRule;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a type constructor, i.e. a name that starts with an upper case.
 */
public class ASTTypeConstr implements ASTExpression, ASTPattern {
    private String name;

    public ASTTypeConstr(String name) {
        assert(name != null);
        assert(!name.trim().equals(""));
        assert(Character.isUpperCase(name.charAt(0)));
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

    @Override
    public Set<ASTVariable> getAllVariables() {
        return new HashSet<>();
    }

    @Override
    public boolean funcDeclToPatDecl() {
        return false;
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
    public haskell.simple.ast.ASTExpression castToSimple() throws SimpleReducer.TooComplexException {
        Optional<lambda.ast.ASTConstant> constant = WHNOReducer.toConst(name);
        if (constant.isPresent()) {
            return new ASTConstant(constant.get().getValue());
        }
        else {
            return new ASTConstant(ConstructorRule.getConstructor(name));
        }
    }
}
