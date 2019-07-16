package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * ENTRY1  ::= key ':' Expression
 * ENTRY2  ::= Identifier
 * ENTRIES ::= { (ENTRY1 | ENTRY2) [',' (ENTRY1 | ENTRY2) ] }
 *
 * OBJECT_EXPR ::= '{' ENTRIES '}'
 *
 * where _key_ is always a string.
 * ENTRY2 is converted to ENTRY1 --> Identifier.identifier : Identifier
 *
 * Children:
 *      One child per entry corresponding to the value expression
 */
final public class ObjectExpr extends Expression {
    public List<String> keys = new ArrayList<>();

    @Override public String toString() {
        return String.format("[ObjectExpr keys:%s]", keys);
    }

    @Override public ObjectExpr parse(ModuleState state, ASTNode parent) {
        var tokens = state.tokens;

        parent.add(this);

        tokens.skip(Token.Kind.LCURLY);

        while(tokens.kind() != Token.Kind.RCURLY) {

            if(tokens.peek(1).kind== Token.Kind.COLON) {
                /* key : expression */

                keys.add(tokens.value());
                tokens.next();

                tokens.skip(Token.Kind.COLON);

                ParseExpression.parse(state, this);

            } else {
                /* IdentifierExpr */

                new IdentifierExpr().parse(state, this);
            }

            tokens.expect(Token.Kind.COMMA, Token.Kind.RCURLY);
            tokens.skipIf(Token.Kind.COMMA);
        }

        tokens.skip(Token.Kind.RCURLY);

        return this;
    }
}
