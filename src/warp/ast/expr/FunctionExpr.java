package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.BlockStmt;
import warp.ast.Expression;
import warp.lex.Token;
import warp.parse.ParseExpression;
import warp.parse.ParseType;
import warp.types.FunctionType;

/**
 * FunctionExpr ::= (CLASSIC_FUNC | ARROW_FUNC)
 * CLASSIC_FUNC ::= 'function' '(' { Parameters } ')' [ ':' Type ] BlockStmt
 * ARROW_FUNC   ::= '(' Parameters ')' '=>'  [ ':' Type ] (Expression | BlockStmt)
 *
 * Children:
 *      { Parameter initialisers }
 *      (Expression | BlockStmt)
 */
final public class FunctionExpr extends Expression {
    public boolean isArrowFunction;
    public FunctionType type;

    /**
     * let v1 = function(a:number):void      {};
     * let v2 = function(a:number)           {};
     *
     * let v3 =         (a:number):void   => {};
     * let v4 =         (a:number)        => {};
     * let v5 =         (a:number):number => 3;
     * let v6 =         (a:number)        => 3;
     */
    @Override public FunctionExpr parse(ModuleState state, ASTNode parent) {
        log.trace("parse "+state.tokens.get());

        parent.add(this);

        var tokens = state.tokens;

        if(tokens.isValue("function")) {
            tokens.next();
        } else {
            this.isArrowFunction = true;
        }

        this.type = new FunctionType();

        tokens.skip(Token.Kind.LBR);

        while(tokens.kind() != Token.Kind.RBR) {

            /* Get param - allow initialisers */
            type.parameters.add(ParseType.parseParam(state, this));

            tokens.expect(Token.Kind.COMMA, Token.Kind.RBR);
            tokens.skipIf(Token.Kind.COMMA);
        }

        tokens.skip(Token.Kind.RBR);

        if(tokens.kind() == Token.Kind.COLON) {
            tokens.next();

            type.returnType = ParseType.parse(state);
        }

        if(isArrowFunction) {
            tokens.skip(Token.Kind.RARROW);

            if(tokens.kind() == Token.Kind.LCURLY) {
                new BlockStmt().parse(state, this);
            } else {
                /* Must be a single expression */
                ParseExpression.parse(state, this);
            }
        } else {
            new BlockStmt().parse(state, this);
        }

        return this;
    }

    @Override public String toString() {
        return "FunctionExpr "+type;
    }
}
