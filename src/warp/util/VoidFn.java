package warp.util;

/**
 * A function that takes no parameters and returns void.
 *
 * VoidFn fn = ()-> {};
 */
@FunctionalInterface
public interface VoidFn {
    void apply();
}

