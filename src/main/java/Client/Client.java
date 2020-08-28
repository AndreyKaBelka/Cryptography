package Client;

import Controllers.MainController;
import ECC.ECPoint;
import Server.Connection;
import Server.Message;
import Server.MessageType;
import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    private volatile boolean isConnected = false; // From different threads
    private Connection connection;
    private String address;

    public Client(String address) throws ClientException {
        if (checkValidIp(address.trim())) {
            this.address = address.trim();
        } else {
            throw new ClientException(ClientErrorCode.WRONG_IP_ADDRESS);
        }
    }

    public synchronized boolean isConnected() {
        return isConnected;
    }

    private boolean checkValidIp(String IP) {
        final String regex = "(\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(IP);

        return matcher.find();
    }

    private static ECPoint getPublicKey() {
        return ClientData.getPublicKey();
    }

    private String getUserName() {
        return ClientData.getUSERNAME();
    }

    public void sendMessage(String text) {
        try {
            Message message = new Message(MessageType.TEXT, text);
            message.encrypt(ClientData.getCommonKey(), ClientData.getPrivateKey());
            connection.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            isConnected = false;
        }
    }

    public void start() throws ClientException {
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
            while (isConnected) {
                Thread.onSpinWait();
            }
        } else {
            throw new ClientException(ClientErrorCode.NOT_CONNECTED);
        }
    }

    public class SocketThread extends Thread {
        void onConnection() throws IOException, ClassNotFoundException, CloneNotSupportedException, ClientException {
            Message message;
            while (true) {
                message = connection.getMessage();
                if (message.getMsgType() == MessageType.NAME_REQUEST) {
                    connection.sendMessage(new Message(MessageType.USER_NAME, getUserName()));
                } else if (message.getMsgType() == MessageType.NAME_NOT_ACCEPTED) {
                    throw new ClientException(ClientErrorCode.NAME_NOT_ACCEPTED);
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
            MainController.addNewMessage(message.getText());
        }

        void informAboutAddingNewUser(Message userName) {
            userName.decrypt(ClientData.getCommonKey(), ClientData.getPublicKeyServer());
            System.out.println("User " + userName.getText() + " is connected to the chat.");
        }

        void informAboutDeletingNewUser(Message userName) {
            userName.decrypt(ClientData.getCommonKey(), ClientData.getPublicKeyServer());
            System.out.println("User " + userName.getText() + " left the chat.");
        }

        private void setUsersList(Message usersList) {
            System.out.println(usersList.getText());
        }

        @Override
        public void run() {
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
                    } else if (message.getMsgType() == MessageType.USERS_LIST) {
                        setUsersList(message);
                    } else {
                        throw new IOException("Unexpected MessageType");
                    }
                }
            } catch (IOException | ClassNotFoundException | CloneNotSupportedException | ClientException e) {
                e.printStackTrace();
                notifyConnectionStatusChanged(false);
            }
        }


    }

}
