package sample;

import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connect {

    private Socket socket;
    private DataInputStream in;
    public DataOutputStream out;
    private int portOutput;
    private int portInput;
    public String msg;

    public Connect(int portOutput, int portInput) {
        this.portOutput = portOutput;
        this.portInput = portInput;
    }

    public String input() {
        try (ServerSocket server = new ServerSocket(portInput)) {
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                try {
                    Socket socket = server.accept();
                    System.out.println("Client connected");
                    in = new DataInputStream(socket.getInputStream()); // входящий поток
                    String msg;
                    msg = in.readUTF();
                    return msg;


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public void sendMsg(String msgOutput) {
        try {
            socket = new Socket("localhost", portOutput);
            out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF(msgOutput);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

}
