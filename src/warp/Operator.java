package warp;

import warp.lex.Token;
import warp.parse.ParseError;

/**
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/Operator_Precedence
 */
public enum Operator {
    HIGHEST(100,"HIGHEST"),

    PARENS(20,"()"),             // ( )

    DOT(19,"."),                // .prop / .func()
    NEW_WITH_ARGS(19,"new args"),// new Thing(1)
    CALL(19,"call"),           // func()

    NEW_NOARGS(18,"new noargs"),// new Thing        /*rt-to-lt*/

    POSTFIX_INCDEC(17,"post ++/--"),// ++/--

    BOOL_NOT(16,"!"),          // !                /*rt-to-lt*/
    BIT_NOT(16,"~"),           // ~                /*rt-to-lt*/
    POS(16,"+ pos"),           // +a               /*rt-to-lt*/
    NEG(16,"- neg"),           // -a               /*rt-to-lt*/

    PREFIX_INCDEC(16,"pre ++/--"),// ++/--         /*rt-to-lt*/
    TYPEOF(16,"typeof"),       // typeof           /*rt-to-lt*/
    DELETE(16,"delete"),       // delete           /*rt-to-lt*/
    AWAIT(16,"await"),         // await            /*rt-to-lt*/

    EXP(15,"**"),              // **               /*rt-to-lt*/

    MUL(14,"*"),               // *
    DIV(14,"/"),               // /
    MOD(14,"%"),               // %

    ADD(13,"+"),               // +
    SUB(13,"-"),               // -

    SHL(12,"<<"),              // <<
    SHR(12,">>"),              // >>
    USHR(12,">>>"),            // >>>

    LT(11,"<"),                // <
    LTE(11,"<="),              // <=
    GT(11,">"),                // >
    GTE(11,">="),              // >=
    IN(11,"in"),               // in
    INSTANCEOF(11,"instanceof"),// instanceof

    EQ(10,"=="),               // ==
    NOT_EQ(10,"!="),           // !=
    STRICT_EQ(10,"==="),       // ===
    STRICT_NOT_EQ(10,"!=="),   // !==

    BIT_AND(9,"&"),            // &
    BIT_XOR(8,"^"),            // ^
    BIT_OR(7,"|"),             // |

    BOOL_AND(6,"&&"),          // &&
    BOOL_OR(5,"||"),           // ||

    TERNARY(4,"?:"),           // ? :              /*rt-to-lt*/

    ASSIGN(3,"="),             // =/+= etc...      /*rt-to-lt*/
    ADD_ASSIGN(3, "+="),
    SUB_ASSIGN(3, "-="),
    MUL_ASSIGN(3, "*="),
    DIV_ASSIGN(3, "/="),
    MOD_ASSIGN(3, "%="),
    AND_ASSIGN(3, "&="),
    OR_ASSIGN(3, "|="),
    XOR_ASSIGN(3, "^="),
    SHL_ASSIGN(3, "<<="),
    SHR_ASSIGN(3, ">>="),
    USHR_ASSIGN(3, ">>>="),

    YIELD(2,"YIELD"),          // yield/yield*     /*rt-to-lt*/

    COMMA(1,","),              // expr , expr

    LOWEST(0,"LOWEST"),
    ;

    Operator(int precedence, String label) {
        this.precedence = precedence;
        this.label = label;
    }

    @Override public String toString() {
        return label;
    }

    public int precedence;
    private String label;

    public static Operator parseBinary(ModuleState state) {
        var tokens = state.tokens;
        var k = tokens.kind();
        Operator op;

        switch(k) {
            case PLUS: op = Operator.ADD; break;
            case MINUS: op = Operator.SUB; break;
            case ASTERISK: op = Operator.MUL; break;
            case FWD_SLASH: op = Operator.DIV; break;
            case PERCENT: op = Operator.MOD; break;
            case AMPERSAND: op = Operator.BIT_AND; break;
            case PIPE: op = Operator.BIT_OR; break;
            case HAT: op = Operator.BIT_XOR; break;
            case LANGLE:
                if(tokens.peek(1).kind == Token.Kind.LANGLE) {
                    tokens.next();
                    op = Operator.SHL;
                    break;
                }
                op = Operator.LT;
                break;
            case RANGLE:
                if(tokens.peek(1).kind == Token.Kind.RANGLE) {
                    if(tokens.peek(2).kind == Token.Kind.RANGLE) {
                        tokens.next();
                        tokens.next();
                        op = Operator.USHR;
                        break;
                    }
                    tokens.next();
                    op = Operator.SHR;
                    break;
                }
                op = Operator.GT;
                break;

            case LANGLE_EQ: op = Operator.LTE; break;
            case RANGLE_EQ: op = Operator.GTE; break;
            case AMPERSAND2: op = Operator.BOOL_AND; break;
            case PIPE2: op = Operator.BOOL_OR; break;

            case EQ2: op = Operator.EQ; break;
            case EQ3: op = Operator.STRICT_EQ; break;
            case EXCLAMATION_EQ: op = Operator.NOT_EQ; break;
            case EXCLAMATION_EQ2: op = Operator.STRICT_NOT_EQ; break;

            case EQ: op = Operator.ASSIGN; break;
            case PLUS_EQ: op = Operator.ADD_ASSIGN; break;
            case MINUS_EQ: op = Operator.SUB_ASSIGN; break;
            case ASTERISK_EQ: op = Operator.MUL_ASSIGN; break;
            case FWD_SLASH_EQ: op = Operator.DIV_ASSIGN; break;
            case PERCENT_EQ: op = Operator.MOD_ASSIGN; break;
            case AMPERSAND_EQ: op = Operator.AND_ASSIGN; break;
            case PIPE_EQ: op = Operator.OR_ASSIGN; break;
            case HAT_EQ: op = Operator.XOR_ASSIGN; break;

            case LANGE2_EQ: op = Operator.SHL_ASSIGN; break;
            case RANGLE2_EQ: op = Operator.SHR_ASSIGN; break;
            case RANGLE3_EQ: op = Operator.USHR_ASSIGN; break;

            default:
                throw new ParseError("Unsupported binary operator: "+k);
        }

        tokens.next();
        return op;
    }
    public static Operator parseUnary(ModuleState state) {
        var tokens = state.tokens;
        var k = tokens.kind();
        Operator op;

        switch(k) {
            case EXCLAMATION: op = Operator.BOOL_NOT; break;
            case TILDE: op = Operator.BIT_NOT; break;
            case PLUS: op = Operator.POS; break;
            case MINUS: op = Operator.NEG; break;

            default:
                throw new ParseError("Unsupported unary operator: "+k);
        }

        tokens.next();
        return op;
    }
}
