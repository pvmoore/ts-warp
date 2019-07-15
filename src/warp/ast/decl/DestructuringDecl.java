package warp.ast.decl;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseExpression;
import warp.parse.ParseType;
import warp.types.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Destructured array:
 *  [first, second]      = [1, 2];
 *  [first, ...rest]     = [1, 2, 3, 4];        // ... must be last
 *  [, second, , fourth] = [1, 2, 3, 4];
 *  // with explicit type
 *
 *  [a,b] : number[]     = [1,2];
 *
 *****************************************************************************
 * Destructured tuple:
 *  [a, b, c]  = t;
 *  [a, ...bc] = t; // ... must be last
 *  [, b]      = t;
 *
 *  // with explicit type
 *  [a,b,c]:[number,number,number] = t;
 *
 *****************************************************************************
 * Destructured object:
 *  {a,b}                   = {a: "foo", b: 12, c: "bar"};
 *  {a, ...b}               = {a: "foo", b: 12, c: "bar"};  // ... must be last
 *  {a:newname, b:newname}  = {a: "foo", b: 12, c: "bar"};
 *
 *  // with explicit type
 *  { a, b } : { a: string, b: number } = {a: "foo", b: 12, c: "bar"};
 */
final public class DestructuringDecl {
    public enum Kind {
        ARRAY,
        TUPLE,
        OBJECT,
        ARRAY_OR_TUPLE  /* could be either ARRAY or TUPLE, we don't know yet */
    }

    public Kind kind;
    public Type type;
    public boolean hasRestArg;     /* true if '...' is used on last name */

    /* The variable names */
    public List<String> names = new ArrayList<>();

    /* For objects only  */
    public List<String> properties = new ArrayList<>();

    @Override
    public String toString() {
        String ob = "[", cb = "]";
        if(kind==Kind.OBJECT) {
            ob = "{"; cb = "}";
        }
        var n = String.join(",", names);
        if(hasRestArg) {
            var i = n.lastIndexOf(',');
            if(i!=-1) {
                n = n.substring(0,i+1) + "..." + n.substring(i+1);
            } else {
                n = "..." + n;
            }
        }
        var p = properties.isEmpty() ? "" : " props: "+String.join(",", properties);
        return String.format("%s%s%s:%s%s", ob, n, cb, type, p);
    }

    public DestructuringDecl parse(ModuleState state, ASTNode parent) {

        var tokens = state.tokens;

        switch(tokens.kind()) {
            case LSQBR:
                this.kind = Kind.ARRAY_OR_TUPLE;
                break;
            case LCURLY:
                this.kind = Kind.OBJECT;
                break;
        }
        tokens.next();

        if(this.kind==Kind.ARRAY_OR_TUPLE) {
            /* Collect names. Null names represent empty commas:
               eg. [,a,]      -> names      = [null,"a",null],
                                 properties = []
                                 hasRestArg = false

                   [a,, ...r] -> names      = ["a", null,"r"],
                                 properties = [],
                                 hasRestArg = true
            */

            while(tokens.kind() != Token.Kind.RSQBR) {

                var k = tokens.kind();

                if(k == Token.Kind.COMMA) {
                    names.add(null);
                } else if(k == Token.Kind.DOT3) {
                    this.hasRestArg = true;
                    tokens.next();

                    names.add(tokens.value());
                    tokens.next();
                } else {
                    names.add(tokens.value());
                    tokens.next();
                }

                tokens.expect(Token.Kind.COMMA, Token.Kind.RSQBR);
                tokens.skipIf(Token.Kind.COMMA);
            }
            tokens.skip(Token.Kind.RSQBR);

        } else {
            /* Collect names and properties
               {a, b:newB, c, ...r } -> names      = ["a", "newB", "c", "r"   ],
                                        properties = ["a", "b",    "c", "null"],
                                        hasRestArg = true
            */
            while(tokens.kind() != Token.Kind.RCURLY) {

                var k = tokens.kind();

                if(k == Token.Kind.DOT3) {
                    this.hasRestArg = true;
                    tokens.next();

                    names.add(tokens.value());
                    properties.add(null);
                    tokens.next();
                } else {
                    var prop = tokens.value();
                    properties.add(prop);
                    tokens.next();

                    if(tokens.kind() == Token.Kind.COLON) {
                        tokens.next();
                        names.add(tokens.value());
                        tokens.next();
                    } else {
                        names.add(prop);
                    }
                }

                tokens.expect(Token.Kind.COMMA, Token.Kind.RCURLY);
                tokens.skipIf(Token.Kind.COMMA);
            }
            tokens.skip(Token.Kind.RCURLY);
        }


        /* Optional type */
        if(tokens.isKind(Token.Kind.COLON)) {
            tokens.next();

            this.type = ParseType.parse(state);
        }

        /* Optional expression */
        if(tokens.isKind(Token.Kind.EQUALS)) {
            tokens.next();

            ParseExpression.parse(state, parent);
        }

        return this;
    }
}
