package warp.ast.stmt;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import warp.ModuleState;
import warp.ast.ASTNode;
import warp.misc.ErrorNotice;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/**
 * https://www.typescriptlang.org/docs/handbook/triple-slash-directives.html
 *
 * <reference path="..." />
 * <reference lib="lib" />
 * <reference types="types" />
 * <reference no-default-lib="true|false" />
 *
 * Only valid at tope of file. Can only be preceeded by comments.
 */
final public class TSDirective extends Statement {
    public enum Kind {
        UNKNOWN,
        PATH,
        LIB,
        TYPES,
        NO_DEFAULT_LIB
    }
    public Kind kind = Kind.UNKNOWN;

    @Override public Statement parse(ModuleState state, ASTNode parent) {
        var value = state.tokens.value();

        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(value)));

            // <reference path="blah" />

            var ele = doc.getDocumentElement();
            var attribs = ele.getAttributes();

            log.trace("value="+value);
            log.trace("xml="+attribs);

            var path = attribs.getNamedItem("path");
            var lib = attribs.getNamedItem("lib");
            var types = attribs.getNamedItem("types");
            var ndl = attribs.getNamedItem("no-default-lib");


            if(path!=null) {
                log.trace("path="+path.getNodeValue());
            } else if(lib!=null) {
                log.trace("lib="+lib.getNodeValue());
            } else if(types!=null) {
                log.trace("types="+types.getNodeValue());
            } else if(ndl!=null) {
                log.trace("ndl="+ndl.getNodeValue());
            } else throw new Exception("Expected at least one of these attribs");

            parent.add(this);

        }catch(Exception e) {
            state.errors.add(new ErrorNotice("Invalid reference directive", state.tokens.get()));
        }

        state.tokens.next();

        return this;
    }
}
