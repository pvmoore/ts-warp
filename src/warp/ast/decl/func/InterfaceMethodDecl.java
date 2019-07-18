package warp.ast.decl.func;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseParameter;
import warp.parse.ParseType;

final public class InterfaceMethodDecl extends AbsMethodDecl {

    @Override
    public String toString() {
        var type = getType();
        return String.format("%s%s", name!=null?name:"", type);
    }
    /**
     *
     * UNNAMED_METHOD ::=                  '(' params ')' [ ':' Type ]
     * NAMED_METHOD   ::= identifier ['?'] '(' params ')' [ ':' Type ]
     *
     * DECL           ::= (UNNAMED_METHOD | NAMED_METHOD)
     */
    @Override
    public InterfaceMethodDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        if(tokens.kind() == Token.Kind.LBR) {
            /* This is an unnamed method */

            this.name = null;

        } else {

            this.name = tokens.value();
            tokens.next();

            this.isOptional = tokens.kind() == Token.Kind.QUESTION;
            if(isOptional) tokens.next();
        }

        tokens.skip(Token.Kind.LBR);

        while(tokens.kind() != Token.Kind.RBR) {

            ParseParameter.parse(state, this);

            tokens.expect(Token.Kind.COMMA, Token.Kind.RBR);
            tokens.skipIf(Token.Kind.COMMA);
        }

        tokens.skip(Token.Kind.RBR);

        /* optional return subtype */
        if(tokens.kind() == Token.Kind.COLON) {
            tokens.next();

            this.returnType = ParseType.parse(state);
        }

        return this;
    }
}
