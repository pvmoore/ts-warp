package warp.ast.decl;

import warp.Access;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.Statement;
import warp.lex.Token;
import warp.misc.ErrorNotice;
import warp.parse.ParseExpression;
import warp.parse.ParseType;
import warp.types.Type;

final public class VariableDecl extends Statement {
    public String name;
    public Type type;
    public Access access = Access.PUBLIC;   /* For class members only */
    public boolean isConst;
    public boolean isReadonly;              /* For class members only */

    boolean isClassMember() {
        return parent instanceof ClassDecl;
    }
    boolean isInitialised() {
        return firstChild() != null;
    }

    /**
     *  (let | const) IDENTIFIER
     *      [ : TYPE ]
     *      [ = EXPRESSION ]
     *      [ ; ]
     */
    public void parse(ModuleState state, ASTNode parent) {
        parent.add(this);

        var tokens = state.tokens;

        if(tokens.isValue("let")) {
            this.isConst = false;
        } else {
            this.isConst = true;
        }
        tokens.next();

        this.name = tokens.get().value;
        tokens.next();

        /* Optional type */
        if(tokens.isKind(Token.Kind.COLON)) {
            tokens.next();

            this.type = ParseType.parse(state, parent);
        }

        /* Optional expression */
        if(tokens.isKind(Token.Kind.EQUALS)) {
            tokens.next();

            ParseExpression.parse(state, this);
        } else if(isConst) {
            state.errors.add(new ErrorNotice("const declaration must be initialised", tokens.get()));
        }
    }
    public void resolve() {

    }
    public void check() {

    }

    @Override public String toString() {
        var c = isConst ? "const " : "";
        return String.format("[VariableDecl name:%s type:%s%s]", name, c, type);
    }
}
