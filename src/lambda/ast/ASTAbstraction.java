package lambda.ast;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a lambda abstraction (function).
 */
public class ASTAbstraction extends ASTTerm {
    private ASTVariable input;
    private ASTTerm output;

    public ASTAbstraction(ASTVariable input, ASTTerm output) {
        assert(input != null);
        assert(output != null);

        this.input = input;
        this.output = output;
    }

    public ASTVariable getInput() {
        return input;
    }

    public void setInput(ASTVariable input) {
        assert(input != null);
        this.input = input;
    }

    public ASTTerm getOutput() {
        return output;
    }

    public void setOutput(ASTTerm output) {
        assert(output != null);
        this.output = output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ASTAbstraction that = (ASTAbstraction) o;

        if (!getInput().equals(that.getInput())) return false;
        return getOutput().equals(that.getOutput());

    }

    @Override
    public int hashCode() {
        int result = getInput().hashCode();
        result = 31 * result + getOutput().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(λ" + input.toString() + "." + output.toString() + ")";
    }

    @Override
    public Optional<ASTTerm> applyBetaReduction() {
        return Optional.empty();
    }

    @Override
    public Set<ASTVariable> getFreeVars() {
        // free variables of an abstraction are the free variables of the output without the bounded variable
        HashSet<ASTVariable> freeVars = new HashSet<>();
        freeVars.addAll(output.getFreeVars());
        freeVars.remove(input);
        return freeVars;
    }

    @Override
    public ASTTerm substitute(ASTVariable var, ASTTerm expr) {
        // if our input variable should be replaced, we do nothing (because it's already bounded by this term)
        if (var.equals(input)) {
            return this;
        }
        else {
            // if our input variable is not a free variable of the expression we can simply replace it by expr
            Set<ASTVariable> freeVars = expr.getFreeVars();
            if (!freeVars.contains(input)) {
                ASTTerm substitutedExpr = output.substitute(var, expr);
                return new ASTAbstraction(input, substitutedExpr);
            }
            else {
                // our input variable is a free variable of the expression, so we need to rename it

                // the new variable name must not be within the following set:
                freeVars.addAll(output.getFreeVars());

                // we simply add an index to the input variable
                int index = 0;
                ASTVariable renamedVar = new ASTVariable(input.getName() + index);
                // we need to make sure the index is not also a free variable
                while (freeVars.contains(renamedVar)) {
                    index++;
                    renamedVar.setName(input.getName() + index);
                }

                // now replace the old input variable name by the new one
                ASTTerm renamedOutput = output.substitute(input, renamedVar);
                // and also apply the substitution
                return new ASTAbstraction(renamedVar, renamedOutput.substitute(var, expr));
            }
        }
    }
}
