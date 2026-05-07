package util;

import model.Module;
import java.io.*;
import java.util.*;

public class ModuleFileUtil {

    private static final String FILE = "data/modules.txt";

    public static List<Module> readModules() throws IOException {

        List<Module> list = new ArrayList<>();
        File file = new File(FILE);

        if (!file.exists()) file.createNewFile();

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {

            String[] data = line.split(",");

            if (data.length == 4) {
                list.add(new Module(
                        data[0],
                        data[1],
                        Integer.parseInt(data[2]),
                        Integer.parseInt(data[3])
                ));
            }
        }

        br.close();
        return list;
    }

    public static void writeModules(List<Module> modules) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(FILE));

        for (Module m : modules) {
            bw.write(m.getId() + "," +
                    m.getName() + "," +
                    m.getCapacity() + "," +
                    m.getEnrolled());
            bw.newLine();
        }

        bw.close();
    }
}