package warp;
/**
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/Operator_Precedence
 */
public enum Operator {
    PARENS(20),             // ( )

    DOT(19),                // .prop / .func()
    NEW_WITH_ARGS(19),      // new Thing(1)
    CALL(19),               // func()

    NEW_NOARGS(18),         // new Thing        /*rt-to-lt*/

    POSTFIX_INCDEC(17),     // ++/--

    BOOL_NOT(16),           // !                /*rt-to-lt*/
    BIT_NOT(16),            // ~                /*rt-to-lt*/
    POS(16),                // +a               /*rt-to-lt*/
    NEG(16),                // -a               /*rt-to-lt*/
    PREFIX_INCDEC(16),      // ++/--            /*rt-to-lt*/
    TYPEOF(16),             // typeof           /*rt-to-lt*/
    DELETE(16),             // delete           /*rt-to-lt*/
    AWAIT(16),              // await            /*rt-to-lt*/

    EXP(15),                // **               /*rt-to-lt*/

    MUL(14),                // *
    DIV(14),                // /
    MOD(14),                // %

    ADD(13),                // +
    SUB(13),                // -

    SHL(12),                // <<
    SHR(12),                // >>
    USHR(12),               // >>>

    LT(11),                 // <
    LTE(11),                // <=
    GT(11),                 // >
    GTE(11),                // >=
    IN(11),                 // in
    INSTANCEOF(11),         // instanceof

    EQ(10),                 // ==
    NOT_EQ(10),             // !=
    STRICT_EQ(10),          // ===
    STRICT_NOT_EQ(10),      // !==

    BIT_AND(9),             // &
    BIT_XOR(8),             // ^
    BIT_OR(7),              // |
    BOOL_AND(6),            // &&
    BOOL_OR(5),             // ||

    TERNARY(4),             // ? :              /*rt-to-lt*/

    ASSIGN(3),              // =/+= etc...      /*rt-to-lt*/

    YIELD(2),               // yield/yield*     /*rt-to-lt*/

    COMMA(1),               // expr , expr
    ;

    Operator(int precedence) {
        this.precedence = precedence;
    }

    public int precedence;
}
