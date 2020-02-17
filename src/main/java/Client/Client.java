package Client;

import ECC.ECPoint;
import Server.Connection;
import Server.Message;
import Server.MessageType;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private volatile boolean isConnected = false; // From different threads
    private Connection connection;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    private static ECPoint getPublicKey() {
        return ClientData.getPublicKey();
    }

    private String getUserName() {
        return ClientData.getUSERNAME();
    }

    private void sendMessage(String text) {
        try {
            Message message = new Message(MessageType.TEXT, text);
            message.encrypt(ClientData.getCommonKey(), ClientData.getPrivateKey());
            connection.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            isConnected = false;
        }
    }

    private void start() {
        SocketThread socketThread = new SocketThread();
        socketThread.setDaemon(true);
        socketThread.start();
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (isConnected) {
            System.out.println("You are connected...");
            String msg;
            Scanner in = new Scanner(System.in);
            while (isConnected) {
                msg = in.nextLine();
                sendMessage(msg);
            }
        } else {
            System.out.println("Error...");
        }

    }

    public class SocketThread extends Thread {
        void onConnection() throws IOException, ClassNotFoundException, CloneNotSupportedException {
            Message message;
            while (true) {
                message = connection.getMessage();
                if (message.getMsgType() == MessageType.NAME_REQUEST) {
                    connection.sendMessage(new Message(MessageType.USER_NAME, getUserName()));
                } else if (message.getMsgType() == MessageType.NAME_NOT_ACCEPTED) {
                    System.out.println("The name is already taken, please change it and try again...");
                    throw new IOException("Change the name");
                } else if (message.getMsgType() == MessageType.PUBLIC_KEY_USER) {
                    ECPoint myPublicKey = getPublicKey();
                    connection.sendMessage(new Message(MessageType.PUBLIC_KEY_USER, getUserName(), myPublicKey));
                } else if (message.getMsgType() == MessageType.PUBLIC_KEY_SERVER) {
                    ClientData.setPublicKeyServer(message.getPublic_key());
                    ClientData.setCommonKey(message.getPublic_key());
                    notifyConnectionStatusChanged(true);
                    break;
                } else {
                    throw new IOException("Unexpected MessageType");
                }
            }

        }

        private void notifyConnectionStatusChanged(boolean clientConnected) {
            isConnected = clientConnected;
            synchronized (Client.this) {
                Client.this.notify();
            }
        }

        void processIncomingMessage(Message message) {
            message.decrypt(ClientData.getCommonKey(), ClientData.getPublicKeyServer());
            System.out.println(message.getText());
        }

        void informAboutAddingNewUser(Message userName) {
            userName.decrypt(ClientData.getCommonKey(), ClientData.getPublicKeyServer());
            System.out.println("User " + userName.getText() + " is connected to the chat.");
        }

        void informAboutDeletingNewUser(Message userName) {
            userName.decrypt(ClientData.getCommonKey(), ClientData.getPublicKeyServer());
            System.out.println("User " + userName.getText() + " left the chat.");
        }

        @Override
        public void run() {
            Scanner in = new Scanner(System.in);
            String address = in.nextLine();
            int port = 3443;
            try {
                Socket socket = new Socket(address, port);
                connection = new Connection(socket);
                onConnection();
                Message message;
                while (isConnected) {
                    message = connection.getMessage();
                    if (message.getMsgType() == MessageType.TEXT) {
                        processIncomingMessage(message);
                    } else if (message.getMsgType() == MessageType.USER_CONNECTED) {
                        informAboutAddingNewUser(message);
                    } else if (message.getMsgType() == MessageType.USER_DISCONNECTED) {
                        informAboutDeletingNewUser(message);
                    } else {
                        throw new IOException("Unexpected MessageType");
                    }

                }
            } catch (IOException | ClassNotFoundException | CloneNotSupportedException e) {
                e.printStackTrace();
                notifyConnectionStatusChanged(false);
            }
        }
    }

}
