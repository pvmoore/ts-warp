package warp.types;

import org.apache.log4j.Logger;
import warp.ModuleState;
import warp.ast.decl.param.ParameterDecl;
import warp.lex.Token;
import warp.parse.ParseType;

import java.util.ArrayList;
import java.util.List;

final public class FunctionType extends Type {
    final private static Logger log = Logger.getLogger(FunctionType.class);

    final public List<String> paramNames = new ArrayList<>();
    final public List<Type> paramTypes = new ArrayList<>();
    final List<Boolean> paramIsOptional = new ArrayList<>();
    public Type returnType = new Type(Kind.UNKNOWN);

    public FunctionType() {
        super(Kind.FUNCTION);
    }
    public FunctionType(List<ParameterDecl> params, Type retType) {
        this();

        for(var p : params) {
            paramNames.add(p.name);
            paramTypes.add(p.type);
            paramIsOptional.add(p.isOptional);
        }
        this.returnType = retType;
    }

    @Override public String toString() {
        assert(paramNames.size() == paramTypes.size());

        var buf = new StringBuilder();
        for(var i=0; i<paramNames.size(); i++) {
            if(i>0) buf.append(", ");

            buf.append(paramNames.get(i))
               .append(paramIsOptional.get(i) ? "?" : "")
               .append(":")
               .append(paramTypes.get(i));
        }

        return String.format("(%s)=>%s", buf.toString(), returnType);
    }

    /**
     * PARAM ::= identifier [?] ':' Type
     * '(' { PARAM [ ',' PARAM ] } ')' '=>' Type
     */
    public FunctionType parse(ModuleState state) {
        log.trace("parse "+state.tokens.get());

        var tokens = state.tokens;

        tokens.skip(Token.Kind.LBR);

        while(tokens.kind()!=Token.Kind.RBR) {

            /* identifier [ '?' ] ':' Type */

            paramNames.add(tokens.value());
            tokens.next();

            var isOptional = tokens.kind() == Token.Kind.QUESTION;
            if(isOptional) {
                tokens.next();
            }

            tokens.skip(Token.Kind.COLON);

            var type = ParseType.parse(state);
            paramIsOptional.add(isOptional);

            paramTypes.add(type);

            tokens.expect(Token.Kind.COMMA, Token.Kind.RBR);
            tokens.skipIf(Token.Kind.COMMA);
        }

        tokens.skip(Token.Kind.RBR);
        tokens.skip(Token.Kind.RARROW);

        this.returnType = ParseType.parse(state);

        return this;
    }
}
