package warp.ast.decl;

import warp.Access;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.misc.ErrorNotice;
import warp.parse.ParseError;
import warp.parse.ParseExpression;
import warp.parse.ParseType;
import warp.types.Type;

/**
 * https://www.typescriptlang.org/docs/handbook/variable-declarations.html
 */
final public class VariableDecl extends Declaration {
    public String name;
    public Type type = Type.UNKNOWN;
    public Access access;                           /* For class members only */
    public boolean isConst;                         /* For non-class members only */
    public boolean isReadonly;                      /* For class members only */

    boolean isClassProperty() {
        return parent instanceof ClassDecl;
    }
    boolean isInitialised() {
        return firstChild() != null;
    }
    @Override public String toString() {
        var c = isConst ? "const " : "";
        var a = access.toString(); if(a.length()>0) a += " ";
        var r = isReadonly ? "readonly " : "";
        return String.format("[VariableDecl %s%s%s:%s%s]", a, r, name, c, type);
    }

    /**
     *  NAME              ::= identifier
     *  REST              ::= '...'
     *  OBJ_DESTRUCTURE   ::= '{' { NAME|REST [',' [NAME|REST] ] } '}'
     *  ARRAY_DESTRUCTURE ::= '[' { NAME|REST [',' [NAME|REST] ] } ']'
     *  TUPLE_DESTRUCTURE ::= '[' { NAME|REST [',' [NAME|REST] ] } ']'
     *
     *  IDS      ::= NAME | OBJ_DESTRUCTURE | ARRAY_DESTRUCTURE | TUPLE_DESTRUCTURE)
     *
     *  VARIABLE ::= (let | const)       IDS [ ':' Type ] [ '=' Expression ] [ ';' ]
     *  PROPERTY ::= [Access] [readonly] IDS [ ':' Type ] [ '=' Expression ] [ ';' ]
     *
     *  DECL     ::= (VARIABLE | PROPERTY)
     */
    @Override
    public VariableDecl parse(ModuleState state, ASTNode parent) {
        log.trace("parse "+state.tokens.get());
        parent.add(this);

        var tokens = state.tokens;

        this.access = Access.parse(state);

        if(tokens.isValue("readonly")) {
            this.isReadonly = true;
            tokens.next();
        }

        if(tokens.isValue("const")) {
            isConst = true;
            tokens.next();
        } else if(tokens.isValue("let")) {
            tokens.next();
        }

        if(tokens.kind()== Token.Kind.LSQBR) {
            throw new ParseError("Array/Tuple destructuring not implemented");
        }
        if(tokens.kind()== Token.Kind.LCURLY) {
            throw new ParseError("Object destructuring not implemented");
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
        } else if(isConst) {
            state.errors.add(new ErrorNotice("const declaration must be initialised", tokens.get()));
        }
        return this;
    }
    public void resolve() {

    }
    public void check() {

    }
}
