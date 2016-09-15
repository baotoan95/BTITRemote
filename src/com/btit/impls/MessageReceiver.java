/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import com.btit.gui.ChatWindow;
import com.btit.models.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BaoToan
 */
public class MessageReceiver extends Thread {

    private ChatWindow chatWindow;
    private Socket socket;

    public MessageReceiver(ChatWindow chatWindow, Socket socket) {
        this.socket = socket;
        this.chatWindow = chatWindow;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            while(true) {
                try {
                    Object object = input.readObject();
                    if(object instanceof Message) {
                        Message message = (Message)object;
                        System.out.println(message.getName() + ": " + message.getMessage());
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
