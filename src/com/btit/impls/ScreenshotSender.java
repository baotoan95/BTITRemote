/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import java.awt.Rectangle;
import java.awt.Robot;
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
    private Rectangle screenRect;
    private boolean isActive;

    public ScreenshotSender(Socket socket, Robot robot, Rectangle rectangle) {
        this.socket = socket;
        this.robot = robot;
        this.screenRect = rectangle;
        isActive = true;
    }

    @Override
    public void run() {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(screenRect);
            while (isActive) {
                // Capture screen
                BufferedImage image = robot.createScreenCapture(screenRect);
                ImageIcon imageIcon = new ImageIcon(image);
                objectOutputStream.writeObject(imageIcon);
                objectOutputStream.reset(); // Clear ObjectOutputStream cache
            }
        } catch (IOException ex) {
            try {
                socket.close();
            } catch (IOException ex1) {
                Logger.getLogger(ScreenshotSender.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

}
