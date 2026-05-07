package service;

import model.Module;
import util.ModuleFileUtil;
import java.io.IOException;
import java.util.*;

public class ModuleService {

    // CREATE
    public void addModule(Module m) throws IOException {
        List<Module> list = ModuleFileUtil.readModules();
        list.add(m);
        ModuleFileUtil.writeModules(list);
    }

    // READ
    public List<Module> getModules() throws IOException {
        return ModuleFileUtil.readModules();
    }

    // UPDATE
    public void updateModule(String id, String name, int capacity)
            throws IOException {

        List<Module> list = ModuleFileUtil.readModules();

        for (Module m : list) {
            if (m.getId().equals(id)) {
                m.setName(name);
                m.setCapacity(capacity);
            }
        }

        ModuleFileUtil.writeModules(list);
    }

    // DELETE
    public void deleteModule(String id) throws IOException {

        List<Module> list = ModuleFileUtil.readModules();
        list.removeIf(m -> m.getId().equals(id));
        ModuleFileUtil.writeModules(list);
    }

    // CAPACITY CHECK
    public int availableSeats(Module m) {
        return m.getCapacity() - m.getEnrolled();
    }
}