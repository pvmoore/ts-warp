package warp.ast.decl;

import warp.Access;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.decl.func.ClassMethodDecl;
import warp.ast.decl.func.ConstructorDecl;
import warp.ast.decl.prop.ClassPropertyDecl;
import warp.ast.decl.prop.IndexablePropertyDecl;
import warp.lex.Token;

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

            var i = 0;

            if(Access.isAccessKeyword(tokens.value())) i++;
            if(tokens.isKeyword("readonly")) i++;

            if(tokens.peek(i).kind == Token.Kind.LSQBR) {
                /* indexable property */
                new IndexablePropertyDecl().parse(state, this);

            } else if(tokens.peek(i).value.equals("constructor") &&
                      tokens.peek(i+1).kind== Token.Kind.LBR)
            {
                /* constructor */
                new ConstructorDecl().parse(state, this);

            } else if(tokens.peek(i+1).kind == Token.Kind.LBR ||
                      tokens.peek(i+2).kind == Token.Kind.LBR)
            {
                /* method */
                new ClassMethodDecl().parse(state, this);

            } else {
                /* property */
                new ClassPropertyDecl().parse(state, this);
            }

            tokens.skipIf(Token.Kind.COMMA);
            tokens.skipIf(Token.Kind.SEMICOLON);
        }

        tokens.skip(Token.Kind.RCURLY);

        return this;
    }
}
