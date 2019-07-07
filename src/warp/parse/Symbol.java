package warp.parse;

import java.io.File;

public enum Symbol {
    NONE(null);

    public File declaredInFile;

    Symbol(File declaredIn) {
        this.declaredInFile = declaredIn;
    }
}
