package warp.ast.decl.prop;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseType;

final public class InterfacePropertyDecl extends AbsPropertyDecl {

    @Override
    public String toString() {
        var r = isReadonly ? "readonly " : "";
        var opt = type.isOptional ? "?" : "";
        return String.format("%s%s%s:%s", r, name, opt, type);
    }
    /**
     * ['readonly'] name ['?'] [ ':' Type ] [','|';']
     */
    @Override
    public InterfacePropertyDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);

        var tokens = state.tokens;

        if(tokens.isValue("readonly")) {
            this.isReadonly = true;
            tokens.next();
        }

        this.name = tokens.value(); tokens.next();

        var isOptional = tokens.kind() == Token.Kind.QUESTION;
        if(isOptional) tokens.next();

        /* Optional type */
        if(tokens.isKind(Token.Kind.COLON)) {
            tokens.next();

            this.type = ParseType.parse(state);
        }
        this.type.isOptional = isOptional;

        return this;
    }
}
