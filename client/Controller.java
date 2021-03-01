package ru.geekbrains.chat.client;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public TextArea textArea;
    public TextField textField;

    public TextField loginField;
    public TextField passField;
    public Button btnLogin;

    public HBox loginPanel;
    public HBox msgPanel;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private boolean isAuthorized;
    private String myNick;

    public void setIsAuthorized(boolean value) {
        isAuthorized = value;
        Platform.runLater(() -> {
            if (value) {
                loginPanel.setVisible(false);
                loginPanel.setManaged(false);
                msgPanel.setVisible(true);
                msgPanel.setManaged(true);
                textField.requestFocus();
            } else {
                loginPanel.setVisible(true);
                loginPanel.setManaged(true);
                msgPanel.setVisible(false);
                msgPanel.setManaged(false);
                myNick = "";
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setIsAuthorized(false);
        connect();
    }

    public void connect() {
        try {
            socket = new Socket("localhost", 8189);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            Thread inputThread = new Thread(() -> {
                try {
                    String msg = null;
                    while (true) {
                        msg = in.readUTF();
                        if (msg.equals("78s7d6fjh53987t5hkj&^KGujgd")) {
                            setIsAuthorized(true);
                            break;
                        } else {
                            textArea.appendText(msg + "\n");
                        }
                    }
                    while (true) {
                        msg = in.readUTF();
                        if (msg.startsWith("/")) {
                            if (msg.startsWith("/yournickis")) {
                                myNick = msg.split("\\s")[1];
                                System.out.println(myNick);
                            }
                        } else {
                            textArea.appendText(msg + "\n");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Проблемы при обращении к серверу");
                    setIsAuthorized(false);
                }
            });
            inputThread.setDaemon(true);
            inputThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAuth() {
        try {
            out.writeUTF("/auth " + loginField.getText() + " " + passField.getText());
            loginField.clear();
            passField.clear();
            loginField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg() {
        try {
            out.writeUTF(textField.getText());
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            showAlert("Не получается отослать сообщение, проверьте подключение");
            e.printStackTrace();
        }
    }

    public void showAlert(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Возникли проблемы");
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }
}
