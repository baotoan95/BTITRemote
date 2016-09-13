/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import com.btit.gui.ChatWindow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BaoToan
 */
public class MessageSender implements ActionListener, KeyListener {

    private Socket socket;
    private ChatWindow chatWindow;
    private PrintWriter printWriter;

    public MessageSender(Socket socket, ChatWindow chatWindow) {
        try {
            this.socket = socket;
            this.chatWindow = chatWindow;
            this.chatWindow.getTaTypedMess().addKeyListener(this);
            this.chatWindow.getBtnSend().addActionListener(this);

            printWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        printWriter.println(this.chatWindow.getTaTypedMess().getText());
        printWriter.flush();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 13) {
            printWriter.println(this.chatWindow.getTaTypedMess().getText());
            printWriter.flush();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
