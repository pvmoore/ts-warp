package warp.ast.decl;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Note that when all members in an enum have non-calculated values,
 * they are also regarded as types eg.
 * enum A {
 *     One, Two=1
 * }
 * let a:A.One;
 *
 * Also, A is regarded as a union of its members eg.
 * A === A.One | A.Two
 *
 * Enums are objects eg.
 * enum A { One, Two } is also { 0:"One", 1:"Two" }
 *
 * Reverse mapping:
 * enum A { One, Two }; let a = A[A.One]; a === "One"
 * * Does not apply for enums with string members or for const enums.
 *
 * A const enum is not generated at runtime.
 */
final public class EnumDecl extends Declaration {
    public String name;
    public boolean isConst;
    public List<String> names = new ArrayList<>();
    public List<Boolean> isInitialised = new ArrayList<>();

    @Override
    public String toString() {
        return String.format("enum %s", name);
    }

    @Override
    public EnumDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        if(tokens.isKeyword("const")) {
            tokens.next();

            this.isConst = true;
        }

        tokens.skip("enum");

        this.name = tokens.value(); tokens.next();

        tokens.skip(Token.Kind.LCURLY);

        while(tokens.kind() != Token.Kind.RCURLY) {

            /* name [ '=' Expression ] */

            names.add(tokens.value()); tokens.next();

            if(tokens.kind() == Token.Kind.EQUALS) {
                tokens.next();
                isInitialised.add(true);

                ParseExpression.parse(state, this);

            } else {
                isInitialised.add(false);
            }

            tokens.expect(Token.Kind.COMMA, Token.Kind.RCURLY);
            tokens.skipIf(Token.Kind.COMMA);
        }

        tokens.skip(Token.Kind.RCURLY);

        return this;
    }
}
