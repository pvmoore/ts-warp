package warp.event;

final public class EventError extends Error {
    public EventError(String msg) {
        super(msg);
    }
}
