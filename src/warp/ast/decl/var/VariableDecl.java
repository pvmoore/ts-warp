package warp.ast.decl.var;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseExpression;
import warp.parse.ParseType;
import warp.types.Type;

/**
 * Local/global variable declaration.
 */
public class VariableDecl extends AbsVariableDecl {
    public String name;
    public Type type = new Type(Type.Kind.UNKNOWN);

    @Override public String toString() {
        var c = isConst ? "const" : "let";
        return String.format("%s %s:%s", c, name, type);
    }

    /**
     * DECL ::= (let | const) identifier [ ':' Type ] [ '=' Expression ]
     */
    @Override
    public VariableDecl parse(ModuleState state, ASTNode parent) {
        log.trace("parse "+state.tokens.get());
        parent.add(this);
        var tokens = state.tokens;

        /* Optional if part of a MultiVariableDecl */
        if(tokens.isValue("const")) {
            this.isConst = true;
            tokens.next();
        } else if(tokens.isValue("let")) {
            tokens.next();
        }

        this.name = tokens.get().value;
        tokens.next();

        /* Optional subtype */
        if(tokens.isKind(Token.Kind.COLON)) {
            tokens.next();

            this.type = ParseType.parse(state);
        }

        /* Optional expression */
        if(tokens.isKind(Token.Kind.EQ)) {
            tokens.next();

            log.trace("eq "+tokens.get());

            ParseExpression.parse(state, this);
        }

        return this;
    }
    public void resolve() {

    }
    public void check() {

    }
}
