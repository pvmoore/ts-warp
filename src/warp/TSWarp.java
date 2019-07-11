package warp;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import warp.actions.*;
import warp.event.EventLoop;
import warp.event.WarpEventFactory;

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


        events.register(new LexAction(events, eventFactory), WarpEventFactory.Kind.LEX_FILE.ordinal());
        events.register(new ParseAction(events, eventFactory), WarpEventFactory.Kind.PARSE_FILE.ordinal());
        events.register(new ResolveAction(events, eventFactory), WarpEventFactory.Kind.RESOLVE_FILE.ordinal());
        events.register(new EmitAction(events, eventFactory), WarpEventFactory.Kind.EMIT_FILE.ordinal());

        events.register(new ErrorAction(events), WarpEventFactory.Kind.ERROR.ordinal());

        //        events.register(new LogAction(events),
//                        WarpEventFactory.Kind.LEX_FILE.ordinal(),
//                        WarpEventFactory.Kind.PARSE_FILE.ordinal());



        log.debug(events.toString());

        var project = new ProjectState(config);

        /* Begin file processing */
        for(File file : config.getFiles()) {
            var state = new ModuleState(project);
            state.file = file;
            events.fire(eventFactory.lexFile(state));
        }

        log.info("Exiting");
    }
}
