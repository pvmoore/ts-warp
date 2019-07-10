package warp.ast;

final public class ModuleFile extends ASTNode {
    final public String name;

    public ModuleFile(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return String.format("[ModuleFile %s]", name);
    }
}
