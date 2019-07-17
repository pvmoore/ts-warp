package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.lex.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * ImportStmt
 */
final public class ImportStmt extends Statement {

    public static class Import {
        public String name;
        public String asName;
        public boolean isDefault;

        public String toString() {
            return String.format("%s%s%s", name, isDefault?"**":"", name.equals(asName) ? "" : " as "+asName);
        }

        Import(String name, String asName, boolean isDefault) {
            this.name = name; this.asName = asName; this.isDefault = isDefault;
        }
    }

    public List<Import> imports = new ArrayList<>();
    public String from;

    @Override
    public String toString() {
        return String.format("import %s from %s", imports, from);
    }

    /**
     * PATH         ::= ( " path " | ' path ' )
     *
     * IMPORT_NAME  ::= NAME ['as' ALIAS]
     * IMPORT_LIST  ::= IMPORT_NAME { ',' IMPORT_NAME }
     *
     * STAR         ::= [ DEFAULT ',' ] '*' 'as' name 'from' PATH
     * NON_STAR     ::= [ DEFAULT ',' ] [ '{' IMPORT_LIST '}' ] 'from' PATH
     * SIDE_EFFECT  ::= PATH
     *
     * IMPORT       ::= 'import' (STAR | NON_STAR | SIDE_EFFECT)
     */
    @Override
    public Statement parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("import");

        /* import "mod" */
        if(tokens.kind()== Token.Kind.STRING) {
            this.from = tokens.value();
            tokens.next();
            return this;
        }

        /* DEFAULT */
        if(tokens.kind() == Token.Kind.IDENTIFIER) {
            imports.add(new Import(tokens.value(), tokens.value(), true));
            tokens.next();

            tokens.skipIf(Token.Kind.COMMA);
        }

        /* * as ALIAS */
        if(tokens.kind() == Token.Kind.ASTERISK) {
            tokens.next();

            tokens.skip("as");

            imports.add(new Import("*", tokens.value(), false));
            tokens.next();

        }

        /* { IMPORT_LIST } */
        if(tokens.kind() == Token.Kind.LCURLY) {
            tokens.next();

            while(tokens.kind() != Token.Kind.RCURLY) {

                var name = tokens.value(); tokens.next();
                var alias = name;

                if(tokens.isKeyword("as")) {
                    tokens.next();

                    alias = tokens.value(); tokens.next();
                }

                imports.add(new Import(name, alias, name.equals("default")));

                tokens.expect(Token.Kind.COMMA, Token.Kind.RCURLY);
                tokens.skipIf(Token.Kind.COMMA);
            }

            tokens.skipIf(Token.Kind.RCURLY);
        }

        /* from PATH */
        tokens.skip("from");

        this.from = tokens.value(); tokens.next();

        return this;
    }
}
