package warp.event;

public class Event<T> {

    public interface Listener<T> {
        void trigger(Event<T> event);
    }

    public Event(int key, String name, T payload) {
        this.key = key;
        this.name = name;
        this.payload = payload;
    }

    final public int key;
    final String name;
    final public T payload;

    @Override public String toString() {
        return name;
    }
}
