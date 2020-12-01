package dev.lumme.reactivedemo.frontend;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class FileTest {

    private static final int NUM_READS = 20;

    private static final int BUFFER_SIZE = 4096;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(1);

    private static final String FILE_NAME = "/testfile.foo";

    @Test
    void readFileSync() {
        IntStream.range(0, NUM_READS)
                .forEach(this::readFile);
    }

    @Test
    void readFileAsync() {
        AtomicInteger countDown = new AtomicInteger(NUM_READS);
        IntStream.range(0, NUM_READS)
                .forEach(i -> executorService.execute(() -> {
                    readFile(i);
                    countDown.decrementAndGet();
                }));
        while (countDown.get() > 0);
    }

    @Test
    void readFileNonBlocking() {
        AtomicInteger countDown = new AtomicInteger(NUM_READS);
        IntStream.range(0, NUM_READS)
                .forEach(i -> new FileCompletionHandler(FILE_NAME, () -> {
                    System.out.printf("Sync read %d completed%n", i);
                    countDown.decrementAndGet();
                }));
        while (countDown.get() > 0);
    }

    private void readFile(int index) {
        try (InputStream in = getClass().getResourceAsStream(FILE_NAME)) {
            byte[] data = new byte[BUFFER_SIZE];
            while (in.read(data, 0, data.length) != -1) {}
            System.out.printf("Sync read %d completed%n", index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class FileCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

        private Runnable onCompleted;
        private AsynchronousFileChannel fileChannel;

        private long position;

        private FileCompletionHandler(String fileName, Runnable onCompleted) {
            try {
                this.onCompleted = onCompleted;
                AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                        Paths.get(getClass().getResource(fileName).toURI()),
                        Collections.singleton(StandardOpenOption.READ),
                        executorService);
                ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
                fileChannel.read(byteBuffer, 0, byteBuffer, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void completed(Integer bytesRead, ByteBuffer buffer) {
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
