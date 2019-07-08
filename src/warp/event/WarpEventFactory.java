package warp.event;

import warp.State;

final public class WarpEventFactory {
    public enum Kind {
        ERROR,
        PROCESS_FILE,
        LEX_COMPLETED,
        PARSE_COMPLETED,
        EMIT_COMPLETED,
        ;
    }

    public Event<Error> error(Error error) {
        return new Event<>(Kind.ERROR.ordinal(), error);
    }
    public Event<State> processFile(State state) {
        return new Event<>(Kind.PROCESS_FILE.ordinal(), state);
    }
    public Event<State> lexCompleted(State state) {
        return new Event<>(Kind.LEX_COMPLETED.ordinal(), state);
    }
    public Event<State> parseCompleted(State state) {
        return new Event<>(Kind.PARSE_COMPLETED.ordinal(), state);
    }
    public Event<State> emitCompleted(State state) {
        return new Event<>(Kind.EMIT_COMPLETED.ordinal(), state);
    }
}
