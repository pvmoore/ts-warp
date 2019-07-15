package warp.ast.decl.var;

import warp.Access;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseExpression;
import warp.parse.ParseType;

final public class ClassPropertyDecl extends AbsPropertyDecl {
    public Access access = Access.NOT_SPECIFIED;

    @Override
    public String toString() {
        var a = access.toString(); if(a.length()>0) a += " ";
        var r = isReadonly ? "readonly " : "";
        var opt = type.isOptional ? "?" : "";
        return String.format("%s%s%s%s:%s", a, r, name, opt, type);
    }

    /**
     * [Access] ['readonly'] name ['?'] [ ':' Type ] [ '=' Expression ] [','|';']
     */
    @Override
    public ClassPropertyDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);

        var tokens = state.tokens;

        this.access = Access.parse(state);

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

        /* Optional expression */
        if(tokens.isKind(Token.Kind.EQUALS)) {
            tokens.next();

            ParseExpression.parse(state, this);
        }
        return this;
    }
}
