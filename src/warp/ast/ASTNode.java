package warp.ast;

import java.util.ArrayList;
import java.util.List;

public class ASTNode {
    protected List<ASTNode> children = new ArrayList<>();

    public void add(ASTNode node) {
        children.add(node);
    }

    @Override public String toString() {
        return "[Node]";
    }
}
