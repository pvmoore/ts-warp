package warp.util;

import java.lang.annotation.*;

/**
 * Placed on a method, indicates that the method can be called from
 * different threads meaning that class state needs to be made thread-safe.
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface Async {

}