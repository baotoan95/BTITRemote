/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BaoToan
 */
public class MessageSender {

    private String name;
    private Socket socket;
    private List<Socket> sockets;

    // Constructor for client
    public MessageSender(Socket socket, String name) {
        this.name = name;
        this.socket = socket;
    }

    public MessageSender(String name) {
        this.name = name;
        this.sockets = new ArrayList<>();
    }

    public void addClient(Socket socket) {
        sockets.add(socket);
    }
    
    public void setExceptSocket(Socket socket) {
        this.socket = socket;
    }

    public void send(String message) {
        if (null != sockets) {
            for (Socket otherSocket : sockets) {
                if (otherSocket != this.socket) {
                    sendMessage(otherSocket, message);
                }
            }
        } else {
            sendMessage(this.socket, message);
        }

    }

    private void sendMessage(Socket socket, String message) {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println(this.name + ": " + message);
            printWriter.flush();
        } catch (IOException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
