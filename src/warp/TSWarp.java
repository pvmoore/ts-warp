package warp;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import warp.event.EventLoop;
import warp.event.WarpEventFactory;
import warp.event.listeners.ErrorListener;
import warp.event.listeners.LexTrigger;
import warp.event.listeners.ParseTrigger;
import warp.event.listeners.ResolveTrigger;

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


        events.register(new LexTrigger(events, eventFactory), WarpEventFactory.Kind.LEX_FILE.ordinal());
        events.register(new ParseTrigger(events, eventFactory), WarpEventFactory.Kind.PARSE_FILE.ordinal());
        events.register(new ResolveTrigger(events), WarpEventFactory.Kind.RESOLVE_FILE.ordinal());

        events.register(new ErrorListener(events), WarpEventFactory.Kind.ERROR.ordinal());
        //        events.register(new Log(events),
//                        WarpEventFactory.Kind.LEX_FILE.ordinal(),
//                        WarpEventFactory.Kind.PARSE_FILE.ordinal());



        log.debug(events.toString());

        /* Begin file processing */
        for(File file : config.getFiles()) {
            var state = new ModuleState();
            state.config = config;
            state.file = file;
            events.fire(eventFactory.lexFile(state));
        }

        log.info("Exiting");
    }
}
