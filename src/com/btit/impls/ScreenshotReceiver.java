/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import com.btit.gui.DesktopPanel;
import com.btit.init.MainGUI;
import com.btit.models.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author BaoToan
 */
public class ScreenshotReceiver extends Thread {

    private Socket socket;
    private MainGUI mainGUI;
    private DesktopPanel desktopPanel;

    public ScreenshotReceiver(Socket socket, MainGUI mainGUI) {
        this.socket = socket;
        this.mainGUI = mainGUI;
        this.desktopPanel = mainGUI.getDesktopPanel();
    }

    @Override
    public void run() {
        try {
            ObjectInputStream receiver = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Object object = receiver.readObject();
                if (object instanceof ImageIcon) {
                    ImageIcon imageIcon = (ImageIcon) object;
                    desktopPanel.updateBackground(imageIcon);
                } else if (object instanceof Message) {
                    Message message = (Message) object;
                    System.out.println(message.getName() + ": " + message.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(mainGUI, "You don't have permission to access this IP", "Timeout", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
