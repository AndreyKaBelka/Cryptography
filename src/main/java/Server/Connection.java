package Server;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection implements Closeable {
    private final ObjectOutputStream output;
    private Socket socket;
    private final ObjectInputStream input;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
    }

    public void sendMessage(Message message) throws IOException {
        synchronized (output) {
            output.writeObject(message);
        }
    }

    public Message getMessage() throws IOException, ClassNotFoundException {
        synchronized (input) {
            return (Message) input.readObject();
        }
    }

    @Override
    public void close() throws IOException {
        output.close();
        input.close();
        socket.close();
    }
}
