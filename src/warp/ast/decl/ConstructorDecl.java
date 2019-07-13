package warp.ast.decl;

import warp.Access;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.BlockStmt;
import warp.lex.Token;
import warp.parse.ParseType;
import warp.types.FunctionType;


final public class ConstructorDecl extends Declaration {
    public Access access;
    public FunctionType type;

    @Override public String toString() {
        var a = access.toString(); if(a.length()>0) a+=" ";
        return String.format("%sconstructor%s", a, type);
    }
    /**
     * CONSTRUCTOR ::= [Access] constructor '(' { Parameter [',' Parameter] } ')' BlockStmt
     */
    @Override public ConstructorDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        this.access = Access.parse(state);

        tokens.skip("constructor");

        tokens.skip(Token.Kind.LBR);

        this.type = new FunctionType();

        while(tokens.kind() != Token.Kind.RBR) {

            /* Get param and add any initialisers to AST */
            var p = ParseType.parseParam(state, this);

            type.parameters.add(p);

            tokens.expect(Token.Kind.COMMA, Token.Kind.RBR);
            tokens.skipIf(Token.Kind.COMMA);
        }

        tokens.skip(Token.Kind.RBR);

        /* BlockStmt */
        new BlockStmt().parse(state, this);

        return this;
    }
}
