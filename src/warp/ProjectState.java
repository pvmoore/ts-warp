package warp;

import warp.misc.MemoryBarrier;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

final public class ProjectState {
    final private MemoryBarrier barrier = new MemoryBarrier();
    final private Map<File, ModuleState> modules = new HashMap<>();
    final public TSConfig config;

    public ProjectState(TSConfig config) {
        this.config = config;
    }

    public boolean containsModule(File file) {
        return modules.containsKey(file);
    }
    public ModuleState getModule(File file) {
        return modules.get(file);
    }
    public MemoryBarrier getBarrier() {
        return barrier;
    }
}
