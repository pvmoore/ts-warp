package warp.types;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.lex.Token;
import warp.parse.Parameter;
import warp.parse.ParseType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

final public class FunctionType extends Type {
    final private static Logger log = Logger.getLogger(FunctionType.class);

    final public List<Parameter> parameters = new ArrayList<>();
    public Type returnType = new Type(Kind.UNKNOWN);

    public FunctionType() {
        super(Kind.FUNCTION);
    }
    /**
     * PARAM ::= name [?] ':' Type
     * '(' { PARAM [ ',' PARAM ] } ')' '=>' Type
     */
    public FunctionType parse(ModuleState state) {
        log.trace("parse "+state.tokens.get());

        var tokens = state.tokens;

        tokens.skip(Token.Kind.LBR);

        while(tokens.kind()!=Token.Kind.RBR) {
            parameters.add(ParseType.parseParam(state));

            tokens.expect(Token.Kind.COMMA, Token.Kind.RBR);
            tokens.skipIf(Token.Kind.COMMA);
        }

        tokens.skip(Token.Kind.RBR);
        tokens.skip(Token.Kind.RARROW);

        this.returnType = ParseType.parse(state);

        return this;
    }

    @Override public String toString() {

        var ps = parameters.stream()
                           .map(Parameter::toString)
                           .collect(Collectors.joining("," ));

        return String.format("(%s)=>%s", ps, returnType);
    }
}
