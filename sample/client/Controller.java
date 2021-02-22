package sample.client;

import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.Connect;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public TextArea textArea;
    public TextField textField;
    String msg;

    public final int portOutput = 8189;
    public final int portInput = 8190;
    Connect connect = new Connect(portOutput, portInput);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> {
            while (true) {
                msg = connect.input();
                textArea.appendText("server: " + msg + "\n");
                System.out.println("server: " + msg);
            }
        }).start();
    }

    public void sendMsg() {
        connect.sendMsg(textField.getText());
        textField.clear();
    }
}
