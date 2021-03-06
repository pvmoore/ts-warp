package warp.ast;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class ASTNode {
    private static final ASTNode EMPTY_NODE = new ASTNode() {};

    protected static Logger log = Logger.getLogger(ASTNode.class);

    protected List<ASTNode> children = new ArrayList<>();
    private ASTNode parent = EMPTY_NODE;

    public boolean hasChildren() {
        return !children.isEmpty();
    }
    public int numChildren() {
        return children.size();
    }
    public boolean hasParent() {
        return parent != EMPTY_NODE;
    }
    public ASTNode getParent() {
        return parent;
    }
    public ASTNode firstChild() {
        return hasChildren() ? children.get(0) : null;
    }
    public ASTNode lastChild() {
        return hasChildren() ? children.get(children.size()-1) : null;
    }

    public void add(ASTNode node) {
        if(node.hasParent()) node.parent.remove(node);

        children.add(node);
        node.parent = this;
    }
    public void remove(ASTNode node) {
        children.remove(node);
        node.parent = EMPTY_NODE;
    }

    public void writeToDebug(String indent) {
        log.debug(indent + this.toString()+" ");
        for(var ch : children) {
            ch.writeToDebug(indent+"   ");
        }
    }
}
