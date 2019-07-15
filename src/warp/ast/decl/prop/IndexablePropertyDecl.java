package warp.ast.decl.prop;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseType;
import warp.types.Type;

final public class IndexablePropertyDecl extends AbsPropertyDecl {
    public Type keyType;

    @Override
    public String toString() {
        var r = isReadonly ? "readonly " : "";
        return String.format("%s[%s:%s]:%s", r, name, keyType, type);
    }

    /**
     * '[' name ':' (number|string) ']' ':' Type
     */
    @Override
    public IndexablePropertyDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        if(tokens.isValue("readonly")) {
            this.isReadonly = true;
            tokens.next();
        }

        tokens.skip(Token.Kind.LSQBR);

        this.name = tokens.value(); tokens.next();

        tokens.skip(Token.Kind.COLON);

        this.keyType = ParseType.parse(state);

        tokens.skip(Token.Kind.RSQBR);

        tokens.skip(Token.Kind.COLON);

        this.type = ParseType.parse(state);

        return this;
    }
}
