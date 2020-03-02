package org.vv.logwatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;

public class FileWatcher extends Thread {
    private Path filePath;

    public FileWatcher(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            java.io.FileReader reader = new java.io.FileReader(this.filePath.toFile());
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while(true){
                synchronized(this){
                    line = bufferedReader.readLine();
                    while (line == null) {
                        this.wait();
                        line = bufferedReader.readLine();
                    }
                    System.out.println(line);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}