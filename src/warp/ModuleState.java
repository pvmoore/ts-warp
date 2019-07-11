package warp;

import warp.ast.ModuleFile;
import warp.lex.Tokens;
import warp.misc.ErrorNotice;
import warp.misc.MemoryBarrier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

final public class ModuleState {
    final private ProjectState project;

    public File file;
    public String source;
    public Tokens tokens;
    public ModuleFile module;

    public List<ErrorNotice> errors = new ArrayList<>();

    public ModuleState(ProjectState project) {
        this.project = project;
    }

    public boolean isDeclarationFile() {
        return file.getName().toLowerCase().endsWith(".d.ts");
    }
    public boolean isComplete() {
        if(hasErrors()) return true;

        // todo - check that module children are fully resolved

        return module!=null;
    }
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    public void addError(String msg) {
        errors.add(new ErrorNotice(msg, tokens.get()));
    }
    public MemoryBarrier getBarrier() {
        return project.getBarrier();
    }
}
