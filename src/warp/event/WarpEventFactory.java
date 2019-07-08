package warp.event;

import warp.State;

final public class WarpEventFactory {

    public enum Kind {
        ERROR,
        LEX_FILE,
        PARSE_FILE,
        RESOLVE_FILE,
        EMIT_FILE,
        ;
    }

    public Event<Throwable> error(Throwable error) {
        return new Event<>(Kind.ERROR.ordinal(), error);
    }
    public Event<State> lexFile(State state) {
        return new Event<>(Kind.LEX_FILE.ordinal(), state);
    }
    public Event<State> parseFile(State state) {
        return new Event<>(Kind.PARSE_FILE.ordinal(), state);
    }
    public Event<State> resolveFile(State state) {
        return new Event<>(Kind.RESOLVE_FILE.ordinal(), state);
    }
    public Event<State> emitFile(State state) {
        return new Event<>(Kind.EMIT_FILE.ordinal(), state);
    }
}
