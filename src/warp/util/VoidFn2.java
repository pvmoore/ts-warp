package warp.util;

/**
 * A function that takes two parameters and returns void.
 *
 * P2VoidFn<Integer, Float>fn = (Integer a, Float b)-> {};
 */
@FunctionalInterface
public interface VoidFn2<A,B> {
    void apply(A a, B b);
}
