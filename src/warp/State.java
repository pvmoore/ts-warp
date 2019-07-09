package warp;

import warp.ast.ASTRoot;
import warp.lex.Tokens;

import java.io.File;

final public class State {
    public TSConfig config;
    public File file;
    public String source;
    public Tokens tokens;

    public ASTRoot root;
}
