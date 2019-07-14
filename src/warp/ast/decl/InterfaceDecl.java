package warp.ast.decl;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;
import warp.parse.ParseType;
import warp.types.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * https://www.typescriptlang.org/docs/handbook/interfaces.html
 */
final public class InterfaceDecl extends Declaration {
    public String name;
    public List<String> names = new ArrayList<>();
    public List<Type> types = new ArrayList<>();

    @Override public String toString() {
        assert(names.size() == types.size());

        var buf = new StringBuilder("interface ").append(name).append(" {");

        for(var i=0;i<names.size(); i++) {
            if(i>0) buf.append(", ");
            buf.append(names.get(i))
               .append(":")
               .append(types.get(i));
        }

        return buf.append("}").toString();
    }

    /**
     * BODY ::= { prop [ ':' Type ] [','|';'] }
     *
     * 'interface' name '{' BODY '}'
     */
    @Override public InterfaceDecl parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("interface");

        this.name = tokens.value(); tokens.next();

        tokens.skip(Token.Kind.LCURLY);

        while(tokens.kind() != Token.Kind.RCURLY) {

            /* prop [ ':' Type ] [','|';'] */

            names.add(tokens.value());
            tokens.next();

            if(tokens.kind() == Token.Kind.COLON) {
                tokens.next();

                types.add(ParseType.parse(state));
            } else {
                types.add(new Type(Type.Kind.ANY));
            }

            tokens.skipIf(Token.Kind.COMMA);
            tokens.skipIf(Token.Kind.SEMICOLON);
        }

        tokens.skip(Token.Kind.RCURLY);

        return this;
    }
}
