package dev.lumme.reactivedemo.frontend.socket;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

import static dev.lumme.reactivedemo.common.socket.Server.ADDRESS;

@Component
public class SocketClient {

    public SocketClient() {
    }

    public void readMessages() {
        try {
            new Reader();
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class Reader implements CompletionHandler<Integer, ByteBuffer> {

        private final AsynchronousSocketChannel client;

        private Reader() throws IOException, ExecutionException, InterruptedException {
            this.client = AsynchronousSocketChannel.open();
            client.connect(ADDRESS).get();
            ByteBuffer buffer = ByteBuffer.allocate(8);
            client.read(buffer, buffer, this);

        }

        @Override
        public void completed(Integer bytesRead, ByteBuffer buffer) {
            System.out.println("Client read " + bytesRead + " bytes!");
            if (bytesRead > 0) {
                client.read(buffer, buffer, this);
            }
        }

        @Override
        public void failed(Throwable exc, ByteBuffer buffer) {
            System.out.println("Client waap :(");
        }

    }
}
