package warp.parse;

import warp.TSConfig;

import java.io.File;

final public class Parser implements Runnable {
    private TSConfig config;
    private File file;

    public Parser(TSConfig config, File file) {
        this.config = config;
        this.file = file;
    }
    @Override
    public void run() {
        System.out.println("["+file+"] run");

        if(!file.exists()) throw new ParseError("File '"+file+"' does not exist");

        var tokens = Lexer.lex(file);

        //throw new ParseError("oops");

        System.out.println("["+file+"] finished");
    }
}
