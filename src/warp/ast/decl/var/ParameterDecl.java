package warp.ast.decl.var;

import warp.Access;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseError;
import warp.parse.ParseExpression;
import warp.parse.ParseType;
import warp.types.Type;

final public class ParameterDecl extends AbsVariableDecl {
    public Access access = Access.NOT_SPECIFIED;
    public boolean hasInitialiser;

    @Override public String toString() {
        var a = access.toString(); if(a.length()>0) a += " ";
        return String.format("%s%s:%s", a, name, type);
    }

    /**
     * [Access] name [?] [':' Type] [ '=' Expression ]
     *
     * Access is only allowed on constructor parameters.
     * Initialiser is only allowed on class method parameters.
     */
    @Override public ParameterDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        this.access = Access.parse(state);

        if(tokens.kind()== Token.Kind.LSQBR) {
            throw new ParseError("todo - Handle destructuring params");
        }
        if(tokens.kind()== Token.Kind.LCURLY) {
            throw new ParseError("todo - Handle destructuring params");
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

        this.type.isOptional = optional;

        if(tokens.kind() == Token.Kind.EQUALS) {
            tokens.next();

            this.hasInitialiser = true;

            ParseExpression.parse(state, this);
        }

        return this;
    }
}
