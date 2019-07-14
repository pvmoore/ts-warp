package warp.ast.decl.var;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.misc.ErrorNotice;
import warp.parse.ParseExpression;
import warp.parse.ParseType;

/**
 * Local/global variable declaration.
 */
public class VariableDecl extends AbsVariableDecl {
    public boolean isConst;

    public boolean isClassProperty() {
        return false;
    }

    @Override public String toString() {
        var c = isConst ? "const" : "let";
        return String.format("%s %s:%s", c, name, type);
    }

    /**
     * DECL ::= (let | const) name [ ':' Type ] [ '=' Expression ] [ ';' ]
     */
    @Override
    public VariableDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);

        var tokens = state.tokens;

        if(tokens.isValue("const")) {
            this.isConst = true;
            tokens.next();
        } else if(tokens.isValue("let")) {
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
        } else if(this.isConst) {
            state.errors.add(new ErrorNotice("const declaration must be initialised", tokens.get()));
        }
        return this;
    }
    public void resolve() {

    }
    public void check() {

    }
}
