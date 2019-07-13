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
    public Access access = Access.NOT_SPECIFIED;   /* For class members only */

    boolean isClassMethod() {
        return parent instanceof ClassDecl;
    }
    @Override public String toString() {
        if(isClassMethod()) {
            var a = access.toString(); if(a.length()>0) a+=" ";
            return String.format("%s%s%s]", a, name, type);
        }
        return String.format("function %s%s]", name, type);
    }

    /**
     * STANDARD  ::= function name '(' { Parameter [',' Parameter] } ')' [ ':' Type ] BlockStmt
     * METHOD    ::= [Access] name '(' { Parameter [',' Parameter] } ')' [ ':' Type ] BlockStmt
     *
     * FUNC_DECL ::= (STANDARD | METHOD)
     */
    @Override public FunctionDecl parse(ModuleState state, ASTNode parent) {
        var tokens = state.tokens;

        parent.add(this);

        // todo - use this for error checking
        boolean isMethod = parent instanceof ClassDecl;

        if(tokens.isKeyword("function")) {
            /* standard function */
            tokens.skip("function");
        } else {
            /* class method */
            this.access = Access.parse(state);
        }

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


}
