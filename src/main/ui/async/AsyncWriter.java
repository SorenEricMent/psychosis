package ui.async;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// File writer on a separate thread, locked per path
// Experimental, not yet used.

public class AsyncWriter {

    private final HashMap<String, Lock> fileLock;

    public AsyncWriter() {
        fileLock = new HashMap<>();
    }

    // EFFECTS: write to path with content, call posthool after finish, use progressupdate to update
    // a JProgressBar with percentage
    public void write(String path, String content, Callable<Void> postHook, ProgressUpdate progress) {
        if (!fileLock.containsKey(path)) {
            Lock newLock = new ReentrantLock();
            fileLock.put(path, newLock);
            newLock.lock();
        } else {
            if (!fileLock.get(path).tryLock()) {
                //Schedule this write for later
            }
        }
        // Now lock must have been acquired
        try (RandomAccessFile file = new RandomAccessFile(path, "w");
             FileChannel channel = file.getChannel()) {
            FileLock writeLock;
            try {
                writeLock = channel.tryLock();
            } catch (OverlappingFileLockException e) {
                // Lock acquire failed, create a backup and delay this overwrite
                //TODO
            }
            // Lock acquired
            fileLock.get(path).unlock();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
