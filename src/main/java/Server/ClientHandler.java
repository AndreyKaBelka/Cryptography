package Server;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;

    ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String userName = null;
        System.out.println("Remote user connection established: " + socket.getRemoteSocketAddress());
        try (Connection connection = new Connection(socket)) {
            userName = Server.onConnection(connection);
            boolean isValidPublicKeyUser = Server.getPublicKeyConnectedUser(connection);
            Server.sendToAllUsers(new Message(MessageType.USER_CONNECTED, userName));
            while (socket.isConnected() && isValidPublicKeyUser) {
                Message message = connection.getMessage();
                if (message.getMsgType() == MessageType.TEXT) {
                    message.decrypt(Server.commonKeys.get(userName), Server.publicKeysUsers.get(userName));
                    Server.sendToAllUsers(new Message(MessageType.TEXT, userName.concat(": ").concat(message.getText())));
                }
            }
        } catch (IOException | ClassNotFoundException | CloneNotSupportedException e) {
            e.printStackTrace();
        } finally {
            if (userName != null) {
                try {
                    Server.onDisconnection(userName);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Connection with " + socket.getRemoteSocketAddress() + " is closed...");
    }
}
