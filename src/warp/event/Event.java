package warp.event;

public class Event<T> {

    public interface Listener<T> {
        void trigger(Event<T> event);
    }

//    public static abstract class Action<T> {
//        protected EventLoop events;
//
//        public Action(EventLoop events) {
//            this.events = events;
//        }
//
//        abstract public void run(T payload);
//    }

    public Event(int key, T payload) {
        this.key = key;
        this.payload = payload;
    }

    final public int key;
    final public T payload;
}
