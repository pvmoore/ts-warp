package warp.misc;

import java.io.File;

final public class Util {

    public static void todo() {
        throw new RuntimeException("Todo");
    }
    public static String getModuleName(File filename) {

        var name= filename.getName();

        return name.substring(0,name.length()-3);
    }

    public static <T> boolean in(T t, T... array) {
        for(var value : array) {
            if(value.equals(t)) return true;
        }
        return false;
    }
}


