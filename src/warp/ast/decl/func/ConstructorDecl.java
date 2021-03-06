package warp.ast.decl.func;

import warp.Access;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.stmt.BlockStmt;
import warp.ast.decl.Declaration;
import warp.ast.decl.param.ParameterDecl;
import warp.lex.Token;
import warp.parse.ParseParameter;
import warp.types.FunctionType;
import warp.types.ObjectType;

import java.util.stream.Collectors;

final public class ConstructorDecl extends Declaration {
    public Access access;

    public FunctionType getType() {
        // todo - optimise this later
        return new FunctionType(children.stream()
                                        .filter((e)->e instanceof ParameterDecl)
                                        .map((e)->(ParameterDecl)e)
                                        .collect(Collectors.toList()),
                                // fixme - should be the subtype of the class
                                new ObjectType());
    }

    @Override public String toString() {
        var type = getType();
        var a = access.toString(); if(a.length()>0) a+=" ";
        return String.format("%sconstructor%s", a, type);
    }
    /**
     * [Access] constructor '(' { Parameter [',' Parameter] } ')' BlockStmt
     */
    @Override public ConstructorDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        this.access = Access.parse(state);

        tokens.skip("constructor");

        tokens.skip(Token.Kind.LBR);

        while(tokens.kind() != Token.Kind.RBR) {

            ParseParameter.parse(state, this);

            tokens.expect(Token.Kind.COMMA, Token.Kind.RBR);
            tokens.skipIf(Token.Kind.COMMA);
        }

        tokens.skip(Token.Kind.RBR);

        /* BlockStmt */
        new BlockStmt().parse(state, this);

        return this;
    }
}
