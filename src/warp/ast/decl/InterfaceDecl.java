package warp.ast.decl;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseVariable;

/**
 * https://www.typescriptlang.org/docs/handbook/interfaces.html
 */
final public class InterfaceDecl extends Declaration {
    public String name;

    @Override public String toString() {
        return String.format("interface %s", name);
    }

    /**
     * BODY ::= { prop ['?'] [ ':' Type ] [','|';'] }
     *
     * 'interface' name '{' BODY '}'
     */
    @Override public InterfaceDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("interface");

        this.name = tokens.value(); tokens.next();

        tokens.skip(Token.Kind.LCURLY);

        while(tokens.kind() != Token.Kind.RCURLY) {

            /* prop [ ':' Type ] [','|';'] */

            ParseVariable.parse(state, this);

            tokens.skipIf(Token.Kind.COMMA);
            tokens.skipIf(Token.Kind.SEMICOLON);
        }

        tokens.skip(Token.Kind.RCURLY);

        return this;
    }
}
