package Controllers;

import Client.Client;
import Client.ClientData;
import Client.ClientException;
import Server.Server;
import com.google.common.base.Stopwatch;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


public class MainController {
    @FXML
    public VBox connectPane;

    @FXML
    public TextField userNameField;

    @FXML
    public TextField ipAddressField;

    @FXML
    public Button OKButton;

    @FXML
    private TextArea chatMessage;

    @FXML
    private Button createChatRoomButton;

    @FXML
    private Button connectToChatRoomButton;

    @FXML
    private VBox actionButtons;

    @FXML
    private Label labelIP;

    @FXML
    private GridPane chatPane;

    @FXML
    private TextArea usersRoom;

    @FXML
    private TextField inputMessage;

    @FXML
    private Button sendButton;

    private Client client = null;
    private static Queue<String> newMessages = new LinkedList<>();
    private static ArrayList<String> users = new ArrayList<>();

    private void showError(String text) throws ExecutionException, InterruptedException {
        final Runnable showDialog = () -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(text);
            alert.showAndWait();
        };
        if (Platform.isFxApplicationThread()) {
            showDialog.run();
        } else {
            FutureTask<Void> showDialogTask = new FutureTask<>(showDialog, null);
            Platform.runLater(showDialogTask);
            showDialogTask.get();
        }
    }

    public static void addNewMessage(String message) {
        newMessages.add(message);
    }

    private void addToTheChatMessage() {
        Thread addNewMsg = new Thread(() -> {
            while (true) {
                if (newMessages.size() > 0) {
                    chatMessage.setText(chatMessage.getText() + "\n" + newMessages.poll());
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        addNewMsg.setDaemon(true);
        addNewMsg.start();
    }

    private void setVisible(String message) {
        final Runnable task = () -> {
            actionButtons.setVisible(false);
            connectPane.setVisible(false);
            chatPane.setVisible(true);
            labelIP.setText(message);
        };
        FutureTask<Void> futureTask = new FutureTask<>(task, null);
        Platform.runLater(futureTask);
        try {
            futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void addNewUser(String newUser) {
        users.add(newUser);
    }

    public static void removeUser(String removableUser) {
        users.remove(removableUser);
    }

    public static void setUsers(ArrayList<String> arrayList) {
        users = arrayList;
    }

    private void changeTheUserRoom() {
        Thread changes = new Thread(() -> {
            int prevSize = users.size();
            while (true) {
                if (prevSize != users.size()) {
                    usersRoom.setText("");
                    for (String user : users) {
                        usersRoom.setText(usersRoom.getText() + "\n" + user);
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        changes.setDaemon(true);
        changes.start();
    }

    @FXML
    void initialize() {
        actionButtons.setVisible(true);
        chatPane.setVisible(false);
        connectPane.setVisible(false);

        createChatRoomButton.setOnMouseClicked(mouseEvent -> {
            Server server = new Server();
            AtomicReference<String> message = new AtomicReference<>();

            Thread serverThread = new Thread(() -> {
                try {
                    server.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            });

            serverThread.setDaemon(true);
            serverThread.start();

            Stopwatch sw = Stopwatch.createUnstarted();
            sw.start();

            Thread temp_thread2 = new Thread();
            final Object objectToLockDown = temp_thread2;
            AtomicBoolean isError = new AtomicBoolean(false);

            Thread temp_thread = new Thread(() -> {
                while (sw.elapsed(TimeUnit.SECONDS) < 10) {
                    if (server.isStarted()) {
                        message.set("IP:" + server.getServerIP());
                        synchronized (objectToLockDown) {
                            objectToLockDown.notify();
                        }
                        Thread.currentThread().interrupt();
                        return;
                    }
                    try {
                        if (!Thread.currentThread().isInterrupted()) {
                            Thread.sleep(500);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    isError.set(true);
                    showError("Can`t run the server!");
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            });

            temp_thread2 = new Thread(() -> {
                temp_thread.setDaemon(true);
                temp_thread.start();
                synchronized (objectToLockDown) {
                    try {
                        objectToLockDown.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!isError.get()) {
                    createTheClient(server.getServerIP(), "Admin");
                }
            });

            temp_thread2.setDaemon(true);
            temp_thread2.start();

        });

        connectToChatRoomButton.setOnMouseClicked(mouseEvent -> {
            actionButtons.setVisible(false);
            connectPane.setVisible(true);
        });

        OKButton.setOnMouseClicked(mouseEvent -> createTheClient(ipAddressField.getText(), userNameField.getText()));

        sendButton.setOnMouseClicked(mouseEvent -> {
            if (!inputMessage.getText().isEmpty() || !inputMessage.getText().isBlank()) {
                client.sendMessage(inputMessage.getText().trim());
            }
        });
    }

    public void createTheClient(String IP, String name) {
        if (name.isEmpty() || name.isBlank()) {
            try {
                showError("Input the valid name!");
                return;
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            if (name.contains(":")) {
                try {
                    showError("You can`t use the ':' in your username!");
                    return;
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            ClientData.setUSERNAME(name.trim());
            client = new Client(IP.trim());
            Client finalClient = client;
            Thread clientThread = new Thread(() -> {
                try {
                    finalClient.start();
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            });
            clientThread.setDaemon(true);
            clientThread.start();
        } catch (ClientException e) {
            try {
                showError(e.getClientErrorCode().getErrorString());
            } catch (ExecutionException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        Client finalClient1 = client;
        new Thread(() -> {
            while (true) {
                if (finalClient1 != null) {
                    if (finalClient1.isConnected()) {
                        setVisible(IP);
                        return;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        addToTheChatMessage();
        changeTheUserRoom();
    }
}
