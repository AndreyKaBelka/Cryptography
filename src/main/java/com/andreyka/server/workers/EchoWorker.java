package com.andreyka.server.workers;

import com.andreyka.server.NettyChatApp;
import com.andreyka.server.events.ServerDataEvent;

import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingDeque;

public class EchoWorker implements Runnable {
    private final LinkedBlockingDeque<ServerDataEvent> queue = new LinkedBlockingDeque<>();

    public void processData(NettyChatApp server, SocketChannel socket, byte[] data, int count) throws InterruptedException {
        byte[] dataCopy = new byte[count];
        System.arraycopy(data, 0, dataCopy, 0, count);
        queue.put(new ServerDataEvent(server, socket, dataCopy));
    }

    public void run() {
        ServerDataEvent dataEvent;

        while (true) {
            // Wait for data to become available
            try {
                dataEvent = queue.take();
                dataEvent.server.send(dataEvent.socket, dataEvent.data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
