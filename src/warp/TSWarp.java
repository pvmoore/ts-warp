package warp;

import warp.parse.AsyncParser;
import warp.parse.ParseError;

import java.io.File;
import java.util.ArrayList;

final public class TSWarp {
    private TSConfig config;

    public TSWarp(TSConfig config) {
        this.config = config;
    }
    public void run() {
        System.out.println("TSWarp version "+Version.MAJOR+"."+Version.MINOR+"."+Version.PATCH);

        var parsers = new ArrayList<AsyncParser>();

        try {
            for(File file : config.getFiles()) {
                var parser = new AsyncParser(config, file);
                parser.start();
                parsers.add(parser);
            }
        }catch(ParseError e) {
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }finally{

        }
        System.out.println("finished");
    }
}
