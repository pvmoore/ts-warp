package warp.ast.decl;

import warp.Access;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.BlockStmt;
import warp.lex.Token;
import warp.parse.ParseType;
import warp.types.FunctionType;

final public class FunctionDecl extends Declaration {
    public String name;
    public FunctionType type;
    public Access access = Access.PUBLIC;   /* For class members only */

    boolean isClassMember() {
        return parent instanceof ClassDecl;
    }

    /**
     * function name '(' { Parameter [',' Parameter] } ')' [ ':' Type ] BlockStmt
     */
    @Override public FunctionDecl parse(ModuleState state, ASTNode parent) {
        var tokens = state.tokens;

        parent.add(this);

        tokens.skip("function");

        this.name = tokens.value();
        tokens.next();

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

        /* optional return type */
        if(tokens.kind() == Token.Kind.COLON) {
            tokens.next();

            type.returnType = ParseType.parse(state);
        }

        /* BlockStmt */
        new BlockStmt().parse(state, this);

        return this;
    }

    @Override public String toString() {
        return String.format("[FunctionDecl %s%s]", name, type);
    }
}
