package service;

import model.Module;
import java.util.HashMap;

public class ModuleManager {

    private static HashMap<String, Module> modules = new HashMap<>();

    static {
        modules.put("OOP", new Module("OOP", 2));
        modules.put("DSA", new Module("DSA", 2));
    }

    public static Module getModule(String name) {
        return modules.get(name);
    }

    public static HashMap<String, Module> getAllModules() {
        return modules;
    }
}