package Controllers;

import Server.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;


public class MainController {

    @FXML
    private Button createChatRoomButton;

    @FXML
    private Button connectToChatRoomButton;

    @FXML
    private VBox actionButtons;

    @FXML
    private Label labelIP;

    @FXML
    private VBox chatPane;

    @FXML
    void initialize() {
        actionButtons.setVisible(true);
        chatPane.setVisible(false);

        createChatRoomButton.setOnMouseClicked(mouseEvent -> {
            actionButtons.setVisible(false);
            chatPane.setVisible(true);
            Server server = new Server();
            String message;

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

            while (true) {
                if (server.isStarted) {
                    message = "IP:" + server.getServerIP();
                    break;
                }
            }

            labelIP.setText(message);
        });

        connectToChatRoomButton.setOnMouseClicked(mouseEvent -> {

        });

    }
}
