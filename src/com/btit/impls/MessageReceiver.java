/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import com.btit.gui.ChatWindow;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
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
            Scanner scanner = new Scanner(socket.getInputStream());
            while (true) {
                if (scanner.hasNext()) {
                    String message = scanner.nextLine();
                        chatWindow.setVisible(true);
                    chatWindow.setMessages(message + "\n");
                }
                scanner.reset();
            }
        } catch (IOException ex) {
            Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
