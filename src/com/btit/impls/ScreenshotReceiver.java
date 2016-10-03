/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import com.btit.gui.DesktopPane;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

/**
 *
 * @author BaoToan
 */
public class ScreenshotReceiver extends Thread {

    private ObjectInputStream objectInputStream;
    private DesktopPane desktopPanel;
    private boolean isActive;

    public ScreenshotReceiver(ObjectInputStream objectInputStream, JDesktopPane desktopPanel) {
        this.objectInputStream = objectInputStream;
        this.desktopPanel = (DesktopPane) desktopPanel;
        isActive = true;
    }

    @Override
    public void run() {
        try {
            while (isActive && BTITRemote.CONNECTED) {
                try {
                    ImageIcon imageIcon = (ImageIcon) objectInputStream.readObject();
                    desktopPanel.updateScreen(imageIcon);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ScreenshotReceiver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SocketException ex) {
            JOptionPane.showMessageDialog(desktopPanel, "Server is closed!!!", "Connection fail", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(desktopPanel, "You don't have permission to access this IP", "Timeout", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
