package warp.ast.decl.var;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.decl.Declaration;
import warp.lex.Token;
import warp.misc.ErrorNotice;
import warp.parse.ParseExpression;
import warp.parse.ParseType;
import warp.types.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Destructured array:
 *  let [first, second]      = [1, 2];
 *  let [first, ...rest]     = [1, 2, 3, 4];        // ... must be last
 *  let [, second, , fourth] = [1, 2, 3, 4];
 *  // with explicit type
 *
 *  let [a,b] : number[]     = [1,2];
 *
 *****************************************************************************
 * Destructured tuple:
 *  let [a, b, c]  = t;
 *  let [a, ...bc] = t; // ... must be last
 *  let [, b]      = t;
 *
 *  // with explicit type
 *  let [a,b,c]:[number,number,number] = t;
 *
 *****************************************************************************
 * Destructured object:
 *  let {a,b}                   = {a: "foo", b: 12, c: "bar"};
 *  let {a, ...b}               = {a: "foo", b: 12, c: "bar"};  // ... must be last
 *  let {a:newname, b:newname}  = {a: "foo", b: 12, c: "bar"};
 *
 *  // with explicit type
 *  let { a, b } : { a: string, b: number } = {a: "foo", b: 12, c: "bar"};
 */
final public class DestructuringDecl extends Declaration {
    public enum Kind {
        ARRAY,
        TUPLE,
        OBJECT,
        ARRAY_OR_TUPLE  /* could be either ARRAY or TUPLE, we don't know yet */
    }

    public Kind kind;
    public boolean isConst;
    public boolean hasRestArg;     /* true if '...' is used on last name */
    public Type type = new Type(Type.Kind.UNKNOWN); /* object, tuple or array */

    /* The variable names */
    public List<String> names = new ArrayList<>();

    /* For objects only  */
    public List<String> properties = new ArrayList<>();

    @Override
    public String toString() {
        var c  = isConst ? "const" : "let";
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
        return String.format("%s %s%s%s:%s%s", c, ob, n, cb, type, p);
    }

    @Override
    public DestructuringDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);

        var tokens = state.tokens;

        if(tokens.isValue("const")) {
            this.isConst = true;
            tokens.next();
        } else if(tokens.isValue("let")) {
            tokens.next();
        }

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

            ParseExpression.parse(state, this);
        } else if(this.isConst) {
            state.errors.add(new ErrorNotice("const declaration must be initialised", tokens.get()));
        }

        return this;
    }
}
