package warp.ast.decl.func;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.stmt.BlockStmt;
import warp.ast.decl.Declaration;
import warp.ast.decl.param.ParameterDecl;
import warp.lex.Token;
import warp.parse.ParseParameter;
import warp.parse.ParseType;
import warp.types.FunctionType;
import warp.types.Type;

import java.util.stream.Collectors;

final public class FunctionDecl extends Declaration {
    public String name;

    private Type returnType = new Type(Type.Kind.UNKNOWN);

    public FunctionType getType() {
        // todo - optimise this later
        return new FunctionType(children.stream()
                                        .filter((e)->e instanceof ParameterDecl)
                                        .map((e)->(ParameterDecl)e)
                                        .collect(Collectors.toList()),
                                returnType);
    }

    @Override public String toString() {
        var type = getType();
        return String.format("function %s%s", name, type);
    }

    /**
     * 'function' identifier '(' parameters } ')' [ ':' Type ] BlockStmt
     */
    @Override public FunctionDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("function");

        this.name = tokens.value();
        tokens.next();

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
