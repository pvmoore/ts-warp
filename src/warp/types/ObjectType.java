package warp.types;

import warp.ModuleState;
import warp.lex.Token;
import warp.parse.ParseType;

import java.util.ArrayList;
import java.util.List;

final public class ObjectType extends Type {
    public List<String> properties = new ArrayList<>();
    public List<Type> subtypes = new ArrayList<>();

    public ObjectType() {
        super(Kind.OBJECT);
    }

    @Override public String toString() {
        assert(properties.size()==subtypes.size());

        var buf = new StringBuilder("{");
        for(var i=0; i<properties.size(); i++) {
            if(i>0) buf.append(", ");
            buf.append(properties.get(i))
               .append(":")
               .append(subtypes.get(i).toString());

        }
        return buf.append("}").toString();
    }

    /**
     * '{' { property [ ':' Type ] [ (, | ; | ) ] } '}'
     */
    public ObjectType parse(ModuleState state) {
        var tokens = state.tokens;

        tokens.skip(Token.Kind.LCURLY);

        while(tokens.kind() != Token.Kind.RCURLY) {

            /*
            prop [ (, | ; | ) ]
            prop : type
            */

            properties.add(tokens.value());
            tokens.next();

            if(tokens.kind() == Token.Kind.COLON) {
                tokens.next();

                subtypes.add(ParseType.parse(state));
            } else {
                subtypes.add(new Type(Kind.ANY));
            }

            /* comma, semicolon or nothing all seem to be acceptable between properties */
            tokens.skipIf(Token.Kind.COMMA);
            tokens.skipIf(Token.Kind.SEMICOLON);
        }

        tokens.skip(Token.Kind.RCURLY);

        return this;
    }
}
