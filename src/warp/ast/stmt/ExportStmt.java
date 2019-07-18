package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * ExportStmt
 *
 */
final public class ExportStmt extends Statement {

    public static class Export {
        public String name;
        public String asName;
        public boolean isDefault;

        public String toString() {
            return String.format("%s%s%s", name, isDefault?"**":"", name.equals(asName) ? "" : " as "+asName);
        }

        Export(String name, String asName, boolean isDefault) {
            this.name = name; this.asName = asName; this.isDefault = isDefault;
        }
    }

    public List<Export> exports = new ArrayList<>();
    public String from;

    @Override public String toString() {
        var f = from!=null ? " from "+from : "";
        return String.format("export %s%s", exports, f);
    }

    /**
     * PATH     ::= ( " path " | ' path ' )
     * LIST     ::= '{' NAME [ 'as ALIAS ] { ',' NAME ['as' ALIAS] } '}' [ 'from' PATH ]
     * STAR     ::= '*' 'from' PATH
     * DEFAULT  ::= 'default' NAME
     *
     * EXPORT   ::= 'export' (DEFAULT | STAR | LIST)
     */
    @Override
    public Statement parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("export");

        /* default NAME */
        if(tokens.isKeyword("default")) {
            tokens.next();
            this.exports.add(new Export(tokens.value(), tokens.value(), true));
            tokens.next();
            return this;
        }

        /* * from PATH */
        if(tokens.kind() == Token.Kind.ASTERISK) {
            tokens.next();
            this.exports.add(new Export("*", "*", false));
        }

        /* { LIST } */
        if(tokens.kind() == Token.Kind.LCURLY) {
            tokens.next();

            while(tokens.kind() != Token.Kind.RCURLY) {

                var name  = tokens.value(); tokens.next();
                var alias = name;

                if(tokens.isKeyword("as")) {
                    tokens.next();

                    alias = tokens.value(); tokens.next();
                }

                this.exports.add(new Export(name, alias, name.equals("default")));

                tokens.expect(Token.Kind.COMMA, Token.Kind.RCURLY);
                tokens.skipIf(Token.Kind.COMMA);
            }

            tokens.skip(Token.Kind.RCURLY);
        }

        /* Optional from PATH */
        if(tokens.isKeyword("from")) {
            tokens.next();
            this.from = tokens.value(); tokens.next();
        }

        return this;
    }
}
