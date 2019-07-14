package warp.ast.decl;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseType;
import warp.types.Type;

final public class TypeAliasDecl extends Declaration {
    public String name;
    public Type type;

    @Override public String toString() {
        return String.format("type %s = %s", name, type);
    }

    /**
     * 'type' name '=' Type;
     */
    @Override public TypeAliasDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("type");

        this.name = tokens.value(); tokens.next();

        tokens.skip(Token.Kind.EQUALS);

        this.type = ParseType.parse(state);

        return this;
    }
}
