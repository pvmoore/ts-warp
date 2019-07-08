package warp.util;

/**
 * A function that takes one parameter and returns void.
 *
 * P2VoidFn<Integer>fn = (Integer a)-> {};
 */
@FunctionalInterface
public interface VoidFn1<A> {
    void apply(A a);
}
