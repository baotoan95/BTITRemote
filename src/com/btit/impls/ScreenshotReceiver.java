/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import com.btit.gui.DesktopPanel;
import com.btit.init.MainGUI;
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
            while(true) {
                ImageIcon imageIcon = (ImageIcon) receiver.readObject();
                desktopPanel.updateBackground(imageIcon);
            }
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(mainGUI, "You don't have permission to access this IP", "Timeout", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
}
