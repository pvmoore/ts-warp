package warp;

import warp.ast.ModuleFile;
import warp.lex.Tokens;
import warp.misc.ErrorNotice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

final public class ModuleState {
    public TSConfig config;
    public File file;
    public String source;
    public Tokens tokens;

    public ModuleFile module;

    public List<ErrorNotice> errors = new ArrayList<>();

    public boolean isComplete() {
        if(hasErrors()) return true;

        // todo - check that module children are fully resolved

        return module!=null;
    }
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
