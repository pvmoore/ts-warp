package warp.types;

import warp.ModuleState;
import warp.lex.Token;
import warp.parse.ParseType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

final public class TupleType extends Type {
    public List<Type> subtypes = new ArrayList<>();

    public TupleType() {
        super(Kind.TUPLE);
    }

    @Override public String toString() {
        var st = subtypes.stream().map(Type::toString).collect(Collectors.joining(","));
        return "["+st+"]";
    }
    /**
     * '[' { Type [ ',' Type ] } ']'
     */
    public TupleType parse(ModuleState state) {
        var tokens = state.tokens;

        tokens.skip(Token.Kind.LSQBR);

        while(tokens.kind() != Token.Kind.RSQBR) {
            subtypes.add(ParseType.parse(state));

            tokens.expect(Token.Kind.RSQBR, Token.Kind.COMMA);
            tokens.skipIf(Token.Kind.COMMA);
        }

        tokens.skip(Token.Kind.RSQBR);

        return this;
    }
}
