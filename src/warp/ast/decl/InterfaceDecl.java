package warp.ast.decl;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.decl.func.InterfaceMethodDecl;
import warp.ast.decl.prop.IndexablePropertyDecl;
import warp.ast.decl.prop.InterfacePropertyDecl;
import warp.lex.Token;
import warp.types.Type;

/**
 * https://www.typescriptlang.org/docs/handbook/interfaces.html
 *
 * Children:
 *      InterfacePropertyDecl (0 or more)
 *
 */
public class InterfaceDecl extends Declaration {
    public String name;

    /* For indexable type interface */
    public Type indexType;    /* number | string */


    @Override public String toString() {
        return String.format("interface %s", name);
    }

    /**
     * PROP         ::=     identifier ['?'] [ ':' Type ]
     * METHOD       ::=     identifier ['?'] '(' [params] ')' '=>' Type
     * INDEXED_TYPE ::= '[' identifier ':' (number|string) ']' ':' Type
     *
     * BODY         ::= { (PROP | METHOD | INDEXED_TYPE) [','|';'] }
     *
     *
     * 'interface' identifier '{' BODY '}'
     */
    @Override public InterfaceDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("interface");

        this.name = tokens.value(); tokens.next();

        tokens.skip(Token.Kind.LCURLY);

        while(tokens.kind() != Token.Kind.RCURLY) {

            var i = 0;

            if(tokens.isKeyword("readonly")) i++;

            if(tokens.peek(i).kind == Token.Kind.LSQBR) {
                /* indexable property */

                new IndexablePropertyDecl().parse(state, this);

            } else if(tokens.peek(i+1).kind == Token.Kind.LBR ||
                      tokens.peek(i+2).kind == Token.Kind.LBR)
            {
                /* method */
                new InterfaceMethodDecl().parse(state, this);
            } else {
                /* property */
                new InterfacePropertyDecl().parse(state, this);
            }

            tokens.skipIf(Token.Kind.COMMA);
            tokens.skipIf(Token.Kind.SEMICOLON);
        }

        tokens.skip(Token.Kind.RCURLY);

        return this;
    }
}
