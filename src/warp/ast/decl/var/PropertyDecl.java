package warp.ast.decl.var;

import warp.Access;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseExpression;
import warp.parse.ParseType;

final public class PropertyDecl extends AbsVariableDecl {
    public boolean isReadonly;
    public Access access = Access.NOT_SPECIFIED;

    public boolean isClassProperty() {
        return true;
    }

    @Override
    public String toString() {
        var a = access.toString(); if(a.length()>0) a += " ";
        var r = isReadonly ? "readonly " : "";
        return String.format("%s%s%s:%s", a, r, name, type);
    }

    @Override
    public PropertyDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);

        var tokens = state.tokens;

        this.access = Access.parse(state);

        if(tokens.isValue("readonly")) {
            this.isReadonly = true;
            tokens.next();
        }

        this.name = tokens.get().value;
        tokens.next();

        /* Optional type */
        if(tokens.isKind(Token.Kind.COLON)) {
            tokens.next();

            this.type = ParseType.parse(state);
        }

        /* Optional expression */
        if(tokens.isKind(Token.Kind.EQUALS)) {
            tokens.next();

            ParseExpression.parse(state, this);
        }
        return this;
    }
}
