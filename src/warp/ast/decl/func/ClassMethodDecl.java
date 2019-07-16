package warp.ast.decl.func;

import warp.Access;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.BlockStmt;
import warp.lex.Token;
import warp.parse.ParseParameter;
import warp.parse.ParseType;

final public class ClassMethodDecl extends AbsMethodDecl {
    public Access access = Access.NOT_SPECIFIED;

    @Override public String toString() {
        var type = getType();
        var a = access.toString(); if(a.length()>0) a+=" ";
        return String.format("%s%s%s", a, name, type);
    }

    /**
     * [Access] identifier ['?'] '(' params ')' [ ':' Type ] BlockStmt
     */
    @Override
    public ClassMethodDecl parse(ModuleState state, ASTNode parent) {
        var tokens = state.tokens;

        parent.add(this);

        this.access = Access.parse(state);

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

        /* optional return subtype */
        if(tokens.kind() == Token.Kind.COLON) {
            tokens.next();

            this.returnType = ParseType.parse(state);
        }

        /* BlockStmt */
        new BlockStmt().parse(state, this);

        return this;
    }
}
