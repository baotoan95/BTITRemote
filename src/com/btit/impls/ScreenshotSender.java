/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author BaoToan
 */
public class ScreenshotSender extends Thread {

    private Socket socket;
    private Robot robot;

    public ScreenshotSender(Socket socket, Robot robot) {
        this.socket = socket;
        this.robot = robot;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream sender = new ObjectOutputStream(socket.getOutputStream());
            while (true) {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Dimension screenSize = toolkit.getScreenSize();
                Rectangle screenRect = new Rectangle(screenSize);
                BufferedImage bufferedImage = robot.createScreenCapture(screenRect);
                sender.writeObject(new ImageIcon(bufferedImage));
                sender.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(ScreenshotSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
