package warp;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import warp.event.EventLoop;
import warp.event.WarpEventFactory;
import warp.listeners.LexFile;
import warp.listeners.ResolveFile;
import warp.listeners.ParseTokens;

import java.io.File;

final public class TSWarp {
    private Logger log = Logger.getLogger("TS-Warp");
    private TSConfig config;

    public TSWarp(TSConfig config) {
        this.config = config;

        /* Ensure all logs are flushed when program exits */
        Runtime.getRuntime().addShutdownHook(new Thread(LogManager::shutdown));
    }
    public void run() {
        log.info("TSWarp version "+Version.MAJOR+"."+Version.MINOR+"."+Version.PATCH);

        /* Initialise the event loop */
        var events       = new EventLoop();
        var eventFactory = new WarpEventFactory();


        events.register(new LexFile(events, eventFactory), WarpEventFactory.Kind.PROCESS_FILE.ordinal());
        events.register(new ParseTokens(events, eventFactory), WarpEventFactory.Kind.LEX_COMPLETED.ordinal());
        events.register(new ResolveFile(events), WarpEventFactory.Kind.PARSE_COMPLETED.ordinal());

        //events.register(new ErrorListener(events), WarpEventFactory.Kind.ERROR.ordinal());
        //        events.register(new EventLogger(events),
//                        WarpEventFactory.Kind.PROCESS_FILE.ordinal(),
//                        WarpEventFactory.Kind.LEX_COMPLETED.ordinal());



        log.debug(events.toString());

        /* Begin file processing */
        for(File file : config.getFiles()) {
            var state = new State();
            state.config = config;
            state.file = file;
            events.fire(eventFactory.processFile(state));
        }

        log.info("Exiting");
    }
}
