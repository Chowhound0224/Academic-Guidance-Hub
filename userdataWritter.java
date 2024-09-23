package com.mycompany.oodj_assignment;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class userdataWriter {

    public static void UpdateUserDataFile(String Username, String NewPassword, String LastChangePasswordDate) {
        Path filePath = Paths.get("userdata.txt");
        try {
            List<String> lines = Files.readAllLines(filePath);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith(Username)) {
                    String[] parts = line.split(",");
                    parts[1] = NewPassword;
                    parts[6] = LastChangePasswordDate;
                    StringBuilder UpdatedLine = new StringBuilder();
                    for (String part : parts) {
                        UpdatedLine.append(part).append(",");
                    }
                    lines.set(i, UpdatedLine.toString());
                    break;
                }
            }
            
            FileWriter writer = new FileWriter(filePath.toFile());
            for (String line : lines) {
                writer.write(line + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
