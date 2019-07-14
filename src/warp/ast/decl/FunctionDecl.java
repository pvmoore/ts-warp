package warp.ast.decl;

import warp.Access;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.BlockStmt;
import warp.ast.decl.var.ParameterDecl;
import warp.lex.Token;
import warp.parse.ParseType;
import warp.types.FunctionType;
import warp.types.Type;

import java.util.stream.Collectors;

final public class FunctionDecl extends Declaration {
    public String name;
    public Access access = Access.NOT_SPECIFIED;   /* For class members only */

    private Type returnType = new Type(Type.Kind.UNKNOWN);

    public FunctionType getType() {
        // todo - optimise this later
        return new FunctionType(children.stream()
                                        .filter((e)->e instanceof ParameterDecl)
                                        .map((e)->(ParameterDecl)e)
                                        .collect(Collectors.toList()),
                                returnType);
    }

    public boolean isClassMethod() {
        return parent instanceof ClassDecl;
    }
    @Override public String toString() {
        var type = getType();
        if(isClassMethod()) {
            var a = access.toString(); if(a.length()>0) a+=" ";
            return String.format("%s%s%s", a, name, type);
        }
        return String.format("function %s%s", name, type);
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

        while(tokens.kind() != Token.Kind.RBR) {

            new ParameterDecl().parse(state, this);

            tokens.expect(Token.Kind.COMMA, Token.Kind.RBR);
            tokens.skipIf(Token.Kind.COMMA);
        }

        tokens.skip(Token.Kind.RBR);

        /* optional return type */
        if(tokens.kind() == Token.Kind.COLON) {
            tokens.next();

            this.returnType = ParseType.parse(state);
        }

        /* BlockStmt */
        new BlockStmt().parse(state, this);

        return this;
    }


}
