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
    private MessageSender messageSender;

    public MessageReceiver(ChatWindow chatWindow, Socket socket) {
        this.chatWindow = chatWindow;
        this.socket = socket;
    }

    public MessageReceiver(ChatWindow chatWindow, Socket socket, MessageSender messageSender) {
        this.chatWindow = chatWindow;
        this.socket = socket;
        this.messageSender = messageSender;
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
                    chatWindow.getDesktopPane().getDesktopManager().deiconifyFrame(chatWindow);
                    chatWindow.moveToFront();
                    if (null != messageSender) {
                        messageSender.setExceptSocket(socket);
                        messageSender.send(message);
                        messageSender.setExceptSocket(null);
                    }
                }
                scanner.reset();
            }
        } catch (IOException ex) {
            Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
