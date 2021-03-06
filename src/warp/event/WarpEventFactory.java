package warp.event;

import warp.ModuleState;

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
        return new Event<>(Kind.ERROR.ordinal(), "Error", error);
    }
    public Event<ModuleState> lexFile(ModuleState state) {
        return new Event<>(Kind.LEX_FILE.ordinal(), "Lex", state);
    }
    public Event<ModuleState> parseFile(ModuleState state) {
        return new Event<>(Kind.PARSE_FILE.ordinal(), "Parse", state);
    }
    public Event<ModuleState> resolveFile(ModuleState state) {
        return new Event<>(Kind.RESOLVE_FILE.ordinal(), "Resolve", state);
    }
    public Event<ModuleState> emitFile(ModuleState state) {
        return new Event<>(Kind.EMIT_FILE.ordinal(), "Emit", state);
    }
}
