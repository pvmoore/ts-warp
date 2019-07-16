package warp.ast.decl.func;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseParameter;
import warp.parse.ParseType;

final public class InterfaceMethodDecl extends AbsMethodDecl {

    @Override public String toString() {
        var type = getType();
        return String.format("%s%s", name, type);
    }
    /**
     * identifier ['?'] '(' params ')' [ ':' Type ]
     */
    @Override
    public InterfaceMethodDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        this.name = tokens.value(); tokens.next();

        this.isOptional = tokens.kind() == Token.Kind.QUESTION;
        if(isOptional) tokens.next();

        tokens.skip(Token.Kind.LBR);

        while(tokens.kind() != Token.Kind.RBR) {

            ParseParameter.parse(state, this);

            tokens.expect(Token.Kind.COMMA, Token.Kind.RBR);
            tokens.skipIf(Token.Kind.COMMA);
        }

        tokens.skip(Token.Kind.RBR);

        /* optional return type */
        if(tokens.kind() == Token.Kind.COLON) {
            tokens.next();

            this.returnType = ParseType.parse(state);
        }

        return this;
    }
}
