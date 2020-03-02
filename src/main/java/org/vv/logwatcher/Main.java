package org.vv.logwatcher;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Path path = Paths.get("C:\\Users\\vasudvis\\java-workspace", "sample");
        System.out.println(path.toAbsolutePath().toString());
        FileWatcher fileWatcher = new FileWatcher(Paths.get(path.toAbsolutePath().toString(), "thing.txt"));
        fileWatcher.start();
        execute(path, fileWatcher);
    }

    private static void execute(Path path, FileWatcher fileWatcher)  throws IOException, InterruptedException {
        WatchService watcher = FileSystems.getDefault().newWatchService();
        path.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
        WatchKey key  = null;
        for(;;){
            System.out.println("Listening....");
            key = watcher.take();
            System.out.println("Gets key");
            for(WatchEvent<?> event: key.pollEvents()) {
                WatchEvent<Path> pathWatchEvent = (WatchEvent<Path>) event;
                Path file = pathWatchEvent.context();
                System.out.println(event.kind().name() + " - " + file.getFileName());
                synchronized (fileWatcher) {
                    fileWatcher.notifyAll();
                }
            }
            key.reset();
        }
    }
}
