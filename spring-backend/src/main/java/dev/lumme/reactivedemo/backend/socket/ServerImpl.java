package dev.lumme.reactivedemo.backend.socket;

import com.github.javafaker.Faker;
import dev.lumme.reactivedemo.common.socket.Server;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class ServerImpl implements Server, CompletionHandler<AsynchronousSocketChannel, Object> {

    private final Faker faker = new Faker();

    private AsynchronousServerSocketChannel serverSocketChannel;

    @PostConstruct
    private void init() throws IOException {
        serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(ADDRESS);
        serverSocketChannel.accept(null, this);
    }

    @PreDestroy
    private void destroy() throws IOException {
        serverSocketChannel.close();
    }


    @Override
    public void completed(AsynchronousSocketChannel clientChannel, Object attachment) {
        if (serverSocketChannel.isOpen()) {
            serverSocketChannel.accept(null, this);
        }
        if (clientChannel != null && clientChannel.isOpen()) {
            ByteBuffer buffer = ByteBuffer.allocate(128);
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println(i);
                    buffer.put(faker.country().name().getBytes());
                    Future<Integer> writeFuture = clientChannel.write(buffer);
                    writeFuture.get();
                    buffer.clear();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("Woop!");
    }

    @Override
    public void failed(Throwable exc, Object attachment) {
        System.out.println("Waap :(");
    }
}
