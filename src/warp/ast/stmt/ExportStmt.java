package warp.ast.stmt;

import warp.ModuleState;
import warp.ast.ASTNode;
import warp.ast.decl.Declaration;
import warp.lex.Token;
import warp.parse.ParseStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * ExportStmt
 *      Statement   (optional)
 */
final public class ExportStmt extends Statement {
    public enum Kind {
        UNKNOWN,
        STAR,       // * from "mod"
        DECLARE,    // [declare] Statement
        DEFAULT,    // default Statement
        LIST        // list of Exports
    }

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

    public Kind kind = Kind.UNKNOWN;
    public List<Export> exports = new ArrayList<>();
    public String from;


    @Override public String toString() {
        if(kind==Kind.STAR) return "export * from "+from;

        var f = from!=null ? " from "+from : "";
        var e = exports.size()==0 ? "" : exports.toString();
        var d = kind==Kind.DEFAULT ? "default " : "";
        return String.format("export %s%s%s", d, e, f);
    }

    /**
     * export * from PATH
     * export 'default' Statement
     * export 'declare' Declaration
     *
     * export '{' NAME as ALIAS { ',' NAME as ALIAS } '}' [ 'from' PATH ]
     *
     * export Declaration
     */
    @Override
    public Statement parse(ModuleState state, ASTNode parent) {
        parent.add(this);
        var tokens = state.tokens;

        tokens.skip("export");

        /* 'export' '*' 'from' PATH */
        if(tokens.kind() == Token.Kind.ASTERISK) {
            tokens.next();
            tokens.skip("from");
            this.kind = Kind.STAR;
            this.from = tokens.value(); tokens.next();
            return this;
        }

        /* 'export' 'declare' Declaration */
        if(tokens.isKeyword("declare")) {
            tokens.next();
            this.kind = Kind.DECLARE;

            var stmt = ParseStatement.parseSingle(state, this);
            ((Declaration)stmt).isAmbient = true;
            return stmt;
        }

        /* 'export' 'default' Statement */
        if(tokens.isKeyword("default")) {
            tokens.next();
            this.kind = Kind.DEFAULT;

            /* Statement or Expression allowed here */
            return ParseStatement.parseSingle(state, this);
        }

        /* 'export' Declaration */
        if(tokens.kind() != Token.Kind.LCURLY) {
            this.kind = Kind.DECLARE;

            return ParseStatement.parseSingle(state, this);
        }

        /* 'export' '{' LIST '}' */
        if(tokens.kind() == Token.Kind.LCURLY) {
            tokens.next();
            this.kind = Kind.LIST;

            while(tokens.kind() != Token.Kind.RCURLY) {

                var name  = tokens.value(); tokens.next();
                var alias = name;

                if(tokens.isKeyword("as")) {
                    tokens.next();

                    alias = tokens.value(); tokens.next();
                }

                this.exports.add(new Export(name, alias, alias.equals("default")));

                tokens.expect(Token.Kind.COMMA, Token.Kind.RCURLY);
                tokens.skipIf(Token.Kind.COMMA);
            }

            tokens.skip(Token.Kind.RCURLY);

            /* Optional from PATH */
            if(tokens.isKeyword("from")) {
                tokens.next();
                this.from = tokens.value(); tokens.next();
            }
        }

        return this;
    }
}
