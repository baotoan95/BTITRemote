/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import com.btit.models.Message;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BaoToan
 */
public class MessageSender extends Thread {

    private Socket socket;
    private String name;
    private String message;

    public MessageSender(Socket socket, String name, String message) {
        this.socket = socket;
        this.name = name;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            while(true) {
                output.writeObject(new Message(name, message));
                output.reset();
            }
        } catch (IOException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
