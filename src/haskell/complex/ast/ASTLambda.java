package haskell.complex.ast;

import haskell.complex.reduction.ComplexHaskellTransformation;
import haskell.complex.reduction.TooComplexException;

import java.util.*;

/**
 * Represents an anonymous function.
 */
public class ASTLambda implements ASTExpression {
    private List<ASTPattern> pats;
    private ASTExpression exp;

    public ASTLambda(List<ASTPattern> pats, ASTExpression exp) {
        assert(exp != null);
        assert(pats != null);
        assert(pats.size() >= 1);
        this.pats = pats;
        this.exp = exp;
    }

    public ASTLambda(ASTPattern pat, ASTExpression exp) {
        assert(pat != null);
        assert(exp != null);
        this.pats = Collections.singletonList(pat);
        this.exp = exp;
    }

    public List<ASTPattern> getPats() {
        return pats;
    }

    public void setPats(List<ASTPattern> pats) {
        assert(pats != null);
        assert(pats.size() >= 1);
        this.pats = pats;
    }

    public ASTExpression getExp() {
        return exp;
    }

    public void setExp(ASTExpression exp) {
        assert(exp != null);
        this.exp = exp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ASTLambda astLambda = (ASTLambda) o;

        if (!getPats().equals(astLambda.getPats())) return false;
        return getExp().equals(astLambda.getExp());

    }

    @Override
    public int hashCode() {
        int result = getPats().hashCode();
        result = 31 * result + getExp().hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(\\");

        for (ASTPattern pat : pats) {
            builder.append(pat);
            builder.append(" ");
        }
        builder.append("-> ");
        builder.append(exp);
        builder.append(")");

        return builder.toString();
    }

    @Override
    public Set<ASTVariable> getAllVariables() {
        Set<ASTVariable> vars = new HashSet<>();
        for (ASTPattern pat : pats) {
            vars.addAll(pat.getAllVariables());
        }
        vars.addAll(exp.getAllVariables());
        return vars;
    }

    @Override
    public Set<ASTVariable> getFreeVars() {
        Set<ASTVariable> vars = new HashSet<>();
        vars.addAll(exp.getFreeVars());
        for (ASTPattern pat : pats) {
            vars.removeAll(pat.getAllVariables());
        }
        return vars;
    }

    @Override
    public haskell.simple.ast.ASTExpression castToSimple() throws TooComplexException {
        if (pats.size() == 1) {
            ASTPattern pat = pats.get(0);

            if (pat instanceof ASTVariable) {
                ASTVariable var = (ASTVariable) pat;

                return new haskell.simple.ast.ASTFunction((haskell.simple.ast.ASTVariable) var.castToSimple(), exp.castToSimple());
            }
            else {
                throw new TooComplexException(this, "Lambdas must map a variable.");
            }
        }
        else {
            throw new TooComplexException(this, "Lambdas must only map one variable.");
        }
    }

    @Override
    public boolean accept(ComplexHaskellTransformation tr) {
        return tr.visit(this);
    }
}
