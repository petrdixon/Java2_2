package sample.server;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.Connect;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public final int portOutput = 8190;
    public final int portInput = 8189;
    public TextArea textArea;
    public TextField textField;
    String msg;
    Connect connect = new Connect(portOutput, portInput);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {
            while (true) {
                msg = connect.input();
                textArea.appendText("client: " + msg + "\n");
                System.out.println("client: " + msg);
            }
        }).start();
    }

    public void sendMsg() {
        connect.sendMsg(textField.getText());
        textField.clear();
    }
}
