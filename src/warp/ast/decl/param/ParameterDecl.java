package warp.ast.decl.param;

import warp.Access;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseExpression;
import warp.parse.ParseType;
import warp.types.Type;

final public class ParameterDecl extends AbsParameterDecl {
    public String name;
    public Type type = new Type(Type.Kind.UNKNOWN);
    public Access access = Access.NOT_SPECIFIED;
    public boolean isRest;  /* ...param */
    public boolean isOptional;

    @Override public String toString() {
        var a = access.toString(); if(a.length()>0) a += " ";
        var r = isRest ? "..." : "";
        return String.format("%s%s%s:%s", a, r, name, type);
    }

    /**
     * [Access] identifier [?] [':' Type] [ '=' Expression ]
     *
     * Access is only allowed on constructor parameters.
     * Initialiser is only allowed on class method parameters.
     */
    @Override public ParameterDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        this.access = Access.parse(state);

        if(tokens.kind()== Token.Kind.DOT3) {
            /* rest parameter */
            this.isRest = true;
            tokens.next();
        }

        this.name = tokens.value(); tokens.next();

        boolean optional = tokens.kind() == Token.Kind.QUESTION;
        if(optional) tokens.next();

        if(tokens.kind()== Token.Kind.COLON) {
            tokens.skip(Token.Kind.COLON);

            this.type = ParseType.parse(state);
        } else {
            this.type = new Type(Type.Kind.ANY);
        }

        this.isOptional = optional;

        if(tokens.kind() == Token.Kind.EQ) {
            tokens.next();

            ParseExpression.parse(state, this);
        }

        return this;
    }
}
