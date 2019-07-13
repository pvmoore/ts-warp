package warp.ast.decl;

import warp.Access;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseVariable;

/**
 * CPARAM      ::= [Access] name [?] ':' Type [ '=' Expression ]
 * CPARAMS     ::= { CPARAM [ ',' CPARAM ] }
 * CONSTRUCTOR ::= 'constructor' '(' CPARAMS ')' BlockStmt
 *
 * MPARAM      ::= name [?] ':' Type [ '=' Expression ]
 * MPARAMS     ::= { MPARAM [ ',' MPARAM ] }
 * METHOD      ::= name '(' MPARAMS ')'
 *
 * PROP        ::= name [?] ':' Type [ '=' Expression ] ';'
 *
 * BODY        ::= [ CONSTRUCTOR ] { (PROP | METHOD) }
 *
 * CLASS       ::= 'class' name '{' BODY '}'
 */
final public class ClassDecl extends Declaration {
    public String name;

    @Override public String toString() {
        return String.format("class %s", name);
    }

    @Override public ClassDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);

        var tokens = state.tokens;

        tokens.skip("class");

        this.name = tokens.value();
        tokens.next();

        tokens.skip(Token.Kind.LCURLY);

        while(tokens.kind() != Token.Kind.RCURLY) {

            parseStmt(state);

            /* Semicolon? */
            if(tokens.kind() == Token.Kind.SEMICOLON) {
                tokens.next();
            } else {
                // todo - should be rcurly here
            }
        }

        tokens.skip(Token.Kind.RCURLY);


        return this;
    }

    private Declaration parseStmt(ModuleState state) {
        var tokens = state.tokens;

        var offset = Access.isAccessKeyword(tokens.value()) ? 1 : 0;

        var value = tokens.peek(offset).value;

        /* constructor */
        if(value.equals("constructor")) {
            return new ConstructorDecl().parse(state, this);
        }

        boolean isMethod = tokens.peek(offset+1).kind == Token.Kind.LBR;

        /* method */
        if(isMethod) {
            return new FunctionDecl().parse(state, this);
        }

        /* property */
        return ParseVariable.parse(state, this);


        //throw new ParseError("Parse failed while parsing class "+name+" in file ["+state.file+"] @ "+tokens.get());
    }
}
