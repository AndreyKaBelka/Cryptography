package com.andreyka.server.events;

import com.andreyka.server.NettyChatApp;

import java.nio.channels.SocketChannel;

public class ServerDataEvent {
    public NettyChatApp server;
    public SocketChannel socket;
    public byte[] data;

    public ServerDataEvent(NettyChatApp server, SocketChannel socket, byte[] data) {
        this.server = server;
        this.socket = socket;
        this.data = data;
    }
}