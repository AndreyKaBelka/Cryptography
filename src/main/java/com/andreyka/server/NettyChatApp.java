package com.andreyka.server;

import com.andreyka.server.requests.ChangeRequest;
import com.andreyka.server.workers.EchoWorker;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class NettyChatApp implements Runnable {

    // The host:port combination to listen on
    private final InetAddress hostAddress;
    private final int port;

    // The selector we'll be monitoring
    private final Selector selector;

    // The buffer into which we'll read data when it's available
    private final ByteBuffer readBuffer = ByteBuffer.allocate(8192);

    private final EchoWorker worker;

    private final BlockingQueue<ChangeRequest> pendingChanges = new LinkedBlockingDeque<>();

    // Maps a SocketChannel to a list of ByteBuffer instances
    private final Map<SocketChannel, List<ByteBuffer>> pendingData = new HashMap<>();

    public NettyChatApp(InetAddress hostAddress, int port, EchoWorker worker) throws IOException {
        this.hostAddress = hostAddress;
        this.port = port;
        this.selector = this.initSelector();
        this.worker = worker;
    }

    public static void main(String[] args) {
        try {
            EchoWorker worker = new EchoWorker();
            new Thread(worker).start();
            new Thread(new NettyChatApp(null, 9090, worker)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(SocketChannel socket, byte[] data) throws InterruptedException {
        // Indicate we want the interest ops set changed
        this.pendingChanges.put(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

        // And queue the data we want written
        synchronized (this.pendingData) {
            List<ByteBuffer> queue = this.pendingData.computeIfAbsent(socket, k -> new ArrayList<>());
            queue.add(ByteBuffer.wrap(data));
        }

        // Finally, wake up our selecting thread so it can make the required changes
        this.selector.wakeup();
    }

    public void run() {
        while (true) {
            try {
                // Process any pending changes
                for (ChangeRequest change : this.pendingChanges) {
                    if (change.type == ChangeRequest.CHANGEOPS) {
                        SelectionKey key = change.socket.keyFor(this.selector);
                        key.interestOps(change.ops);
                    }
                }
                this.pendingChanges.clear();

                // Wait for an event one of the registered channels
                this.selector.select();

                // Iterate over the set of keys for which events are available
                Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    // Check what event is available and deal with it
                    if (key.isAcceptable()) {
                        this.accept(key);
                    } else if (key.isReadable()) {
                        this.read(key);
                    } else if (key.isWritable()) {
                        this.write(key);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        // For an accept to be pending the channel must be a server socket channel.
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        // Accept the connection and make it non-blocking
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        // Register the new SocketChannel with our Selector, indicating
        // we'd like to be notified when there's data waiting to be read
        socketChannel.register(this.selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException, InterruptedException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        // Clear out our read buffer so it's ready for new data
        this.readBuffer.clear();

        // Attempt to read off the channel
        int numRead;
        try {
            numRead = socketChannel.read(this.readBuffer);
        } catch (IOException e) {
            // The remote forcibly closed the connection, cancel
            // the selection key and close the channel.
            key.cancel();
            socketChannel.close();
            return;
        }

        if (numRead == -1) {
            // Remote entity shut the socket down cleanly. Do the
            // same from our end and cancel the channel.
            key.channel().close();
            key.cancel();
            return;
        }

        System.out.printf("Received message: %s", new String(readBuffer.array(), StandardCharsets.UTF_8));

        // Hand the data off to our worker thread
        this.worker.processData(this, socketChannel, this.readBuffer.array(), numRead);
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        synchronized (this.pendingData) {
            List<ByteBuffer> queue = this.pendingData.get(socketChannel);

            // Write until there's not more data ...
            while (!queue.isEmpty()) {
                ByteBuffer buf = queue.get(0);
                socketChannel.write(buf);
                if (buf.remaining() > 0) {
                    // ... or the socket's buffer fills up
                    break;
                }
                queue.remove(0);
            }

            if (queue.isEmpty()) {
                // We wrote away all data, so we're no longer interested
                // in writing on this socket. Switch back to waiting for
                // data.
                key.interestOps(SelectionKey.OP_READ);
            }
        }
    }

    private Selector initSelector() throws IOException {
        // Create a new selector
        Selector socketSelector = SelectorProvider.provider().openSelector();

        // Create a new non-blocking server socket channel
        // The channel on which we'll accept connections
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // Bind the server socket to the specified address and port
        InetSocketAddress isa = new InetSocketAddress(this.hostAddress, this.port);
        serverChannel.socket().bind(isa);

        // Register the server socket channel, indicating an interest in
        // accepting new connections
        serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

        return socketSelector;
    }
}
