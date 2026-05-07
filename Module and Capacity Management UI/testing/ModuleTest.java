package testing;

import model.Module;
import service.ModuleService;

public class ModuleTest {

    public static void main(String[] args) {

        try {
            ModuleService service = new ModuleService();

            // CREATE
            service.addModule(new Module("M100", "AI", 50, 0));

            // READ
            System.out.println(service.getModules());

            // UPDATE
            service.updateModule("M100", "AI Updated", 60);

            // DELETE
            service.deleteModule("M100");

            System.out.println("Test Completed");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}