package warp.ast.expr;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.BlockStmt;
import warp.ast.Expression;
import warp.ast.decl.param.ParameterDecl;
import warp.lex.Token;
import warp.parse.ParseExpression;
import warp.parse.ParseType;
import warp.types.FunctionType;
import warp.types.Type;

import java.util.stream.Collectors;

/**
 * FunctionExpr ::= (CLASSIC_FUNC | ARROW_FUNC)
 * CLASSIC_FUNC ::= 'function' '(' { Parameters } ')' [ ':' Type ] BlockStmt
 * ARROW_FUNC   ::= '(' Parameters ')' '=>'  [ ':' Type ] (Expression | BlockStmt)
 *
 * Children:
 *      ParameterDecl   (1 per parameter)
 *      (Expression | BlockStmt)
 */
final public class FunctionExpr extends Expression {
    public boolean isArrowFunction;
    private Type returnType = new Type(Type.Kind.UNKNOWN);

    public FunctionType getType() {
        // todo - optimise this later
        return new FunctionType(children.stream()
                                        .filter((e)->e instanceof ParameterDecl)
                                        .map((e)->(ParameterDecl)e)
                                        .collect(Collectors.toList()),
                                returnType);
    }
    @Override public String toString() {
        var type = getType();
        return "FunctionExpr "+type;
    }

    /**
     * let v1 = function(a:number):void      {};
     * let v2 = function(a:number)           {};
     * let v3 = function(a)                  {}
     *
     * let v4 =         (a:number):void   => {};
     * let v5 =         (a:number)        => {};
     * let v6 =         (a:number):number => 3;
     * let v7 =         (a:number)        => 3;
     * let v8 =         (a)               => 3;
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

        tokens.skip(Token.Kind.LBR);

        while(tokens.kind() != Token.Kind.RBR) {

            new ParameterDecl().parse(state, this);

            tokens.expect(Token.Kind.COMMA, Token.Kind.RBR);
            tokens.skipIf(Token.Kind.COMMA);
        }

        tokens.skip(Token.Kind.RBR);

        if(tokens.kind() == Token.Kind.COLON) {
            tokens.next();

            this.returnType = ParseType.parse(state);
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
}
