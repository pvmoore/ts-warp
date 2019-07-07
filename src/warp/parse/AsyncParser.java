package warp.parse;

import warp.TSConfig;

import java.io.File;

final public class AsyncParser extends Thread {
    private TSConfig config;
    private File file;

    public AsyncParser(TSConfig config, File file) {
        this.config = config;
        this.file = file;
    }
    public void run() {
        System.out.println("["+file+"] run");

        if(!file.exists()) throw new ParseError("File '"+file+"' does not exist");

        var tokens = Lexer.lex(file);



        System.out.println("["+file+"] finished");
    }
}
