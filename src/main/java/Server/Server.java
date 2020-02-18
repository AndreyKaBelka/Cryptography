package Server;

import ECC.ECPoint;
import KeyPair.KEYPair;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = 3443;
    private static Map<String, Connection> users = new ConcurrentHashMap<>();
    static Map<String, BigInteger> commonKeys = new ConcurrentHashMap<>();
    static Map<String, ECPoint> publicKeysUsers = new ConcurrentHashMap<>();
    private static BigInteger PRIVATE_KEY;
    private static ECPoint PUBLIC_KEY;
    private static String IP = null;
    public boolean isStarted = false;

    static {
        try {
            KEYPair keyPair = KEYPair.generateKeyPair();
            PRIVATE_KEY = keyPair.getPrivate_key();
            PUBLIC_KEY = keyPair.getPublic_key();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public String getServerIP(){
        return IP;
    }

    static boolean getPublicKeyConnectedUser(Connection connection) throws IOException, ClassNotFoundException, CloneNotSupportedException {
        while (true) {
            connection.sendMessage(new Message(MessageType.PUBLIC_KEY_USER));
            Message message = connection.getMessage();
            if (message.getMsgType() == MessageType.PUBLIC_KEY_USER) {
                ECPoint publicKeyConnectedUser = message.getPublic_key();
                String userName = message.getUsername();
                if (!commonKeys.containsKey(userName)) {
                    publicKeysUsers.put(userName, publicKeyConnectedUser);
                    BigInteger commonKeyConnectedUser = ECC.ECC.getCommonKey(publicKeyConnectedUser, PRIVATE_KEY);
                    commonKeys.put(userName, commonKeyConnectedUser);
                    connection.sendMessage(new Message(MessageType.PUBLIC_KEY_SERVER, PUBLIC_KEY));
                    return true;
                }
            }
        }
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            IP = InetAddress.getLocalHost().getHostAddress();
            System.out.println("IP:" + IP);
            isStarted = true;
            Commands commands = new Commands();
            commands.setDaemon(true);
            commands.start();
            while (isStarted) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            IP = "Error...";
            throw new IOException("Error...");
        }
    }

    static void onDisconnection(String userName) throws CloneNotSupportedException {
        users.remove(userName);
        sendToAllUsers(new Message(MessageType.USER_DISCONNECTED, userName));
    }

    static String onConnection(Connection connection) throws IOException, ClassNotFoundException {
        while (true) {
            connection.sendMessage(new Message(MessageType.NAME_REQUEST));
            Message message = connection.getMessage();
            if (message.getMsgType() == MessageType.USER_NAME) {
                String user = message.getText();
                if (user != null && !user.equals("") && !users.containsKey(user)) {
                    users.put(user, connection);
                    return user;
                } else {
                    connection.sendMessage(new Message(MessageType.NAME_NOT_ACCEPTED));
                }
            }
        }
    }

    static void sendToAllUsers(Message message) throws CloneNotSupportedException {
        for (Map.Entry<String, Connection> entry : users.entrySet()) {
            Message copyMessage = (Message) message.clone();
            try {
                copyMessage.encrypt(commonKeys.get(entry.getKey()), PRIVATE_KEY);
                entry.getValue().sendMessage(copyMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class Commands extends Thread {

        static String getLine(String text) {
            Scanner in = new Scanner(System.in);
            System.out.println(text + " ");
            return in.nextLine();
        }

        @Override
        public void run() {
            while (true) {
                String command = getLine("");
                switch (command) {
                    case "GET_COMMON_KEYS": {
                        System.out.println(commonKeys);
                        break;
                    }
                    case "GET_PUBLIC_KEYS": {
                        System.out.println(publicKeysUsers);
                        break;
                    }
                    case "GET_SERVER_PRIVATE_KEY": {
                        System.out.println(PRIVATE_KEY);
                        break;
                    }
                    case "GET_SERVER_PUBLIC_KEY": {
                        System.out.println(PUBLIC_KEY);
                        break;
                    }
                    case "GET_PORT_SERVER": {
                        System.out.println(PORT);
                        break;
                    }
                }

                if (command.contains("ENCRYPT_MESSAGE")) {
                    String commonKey = getLine("Введите общий ключ для шифрования");
                    String privateKey = getLine("Введите приватный ключ для формирования подписи");
                    String text = getLine("Введите сообщение для шифровки:");

                    Message message = new Message(MessageType.TEXT, text);
                    message.encrypt(new BigInteger(commonKey), new BigInteger(privateKey));

                    System.out.println(message);
                }
            }
        }
    }
}
