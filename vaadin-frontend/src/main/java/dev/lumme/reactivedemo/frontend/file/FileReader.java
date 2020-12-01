package dev.lumme.reactivedemo.frontend.file;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class FileReader {

    private static long count;

    private static final int BUFFER_SIZE = 100096;

    private static final int THREAD_POOL_SIZE = 1;

    private static final ExecutorService asyncExecutorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public void readSync(File file, Runnable onCompleted) {
        long i = count++;
        //try (FileInputStream in = new FileInputStream(file)) {
        try (InputStream in = new URL("https://filesamples.com/samples/video/mp4/sample_960x540.mp4").openStream()) {
            byte[] data = new byte[BUFFER_SIZE];
            while (in.read(data, 0, data.length) != -1) {
            }
            onCompleted.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readAsync(File file, Runnable onCompleted) {
        asyncExecutorService.execute(() -> readSync(file, onCompleted));
    }

    public void readReactive(File file, Runnable onCompleted) {
        try {
            new FileCompletionHandler(file.toPath(), onCompleted);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class FileCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

        private final Runnable onCompleted;
        private final AsynchronousFileChannel fileChannel;

        private long position;
        private final long i;

        private FileCompletionHandler(Path path, Runnable onCompleted) throws IOException {
            i = count++;
            this.onCompleted = onCompleted;
            fileChannel = AsynchronousFileChannel.open(
                    path,
                    Collections.singleton(StandardOpenOption.READ),
                    asyncExecutorService);
            ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            fileChannel.read(byteBuffer, 0, byteBuffer, this);
        }

        @Override
        public void completed(Integer bytesRead, ByteBuffer buffer) {
            //System.out.printf("File %d on thread %s%n", i, Thread.currentThread().getName());
            if (bytesRead < 0) {
                onCompleted.run();
                return;
            }
            buffer.clear();

            position += bytesRead;
            fileChannel.read(buffer, position, buffer, this);
        }

        @Override
        public void failed(Throwable exc, ByteBuffer attachment) {
            throw new RuntimeException("Reading file failed", exc);
        }
    }
}
