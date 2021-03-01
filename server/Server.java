package ru.geekbrains.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    private Vector<ClientHandler> clients;
    private AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    public Server() {
        clients = new Vector<>();
        authService = new DBAuthService();
        try (ServerSocket server = new ServerSocket(8189);) {
            authService.start();
            System.out.println("Server started. Waiting for clients...");
            while (true) {
                Socket socket = server.accept();
                new ClientHandler(this, socket);
                System.out.println("Client connected");
            }
        } catch (AuthServiceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            authService.stop();
        }
    }

    public void broadcastMsg(String msg) {
        for (ClientHandler o : clients) {
            String[] msgData = msg.split("\\s");
            if (msgData[1].equals("/w")) {
                if (o.getNick().equals(msgData[2])) {
                    String msg2 = msgData[0] + " ";
                    for (int i = 3; i < msgData.length; i++) {
                        msg2 = msg2 + " " + msgData[i];
                    }
                    o.sendMsg(msg2);
                }
            } else {
                o.sendMsg(msg);
            }
        System.out.println("broadcastMsg " + o.getNick());
    }
}

    public boolean isNickBusy(String nick) {
        for (ClientHandler o : clients) {
            if (o.getNick().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }
}
