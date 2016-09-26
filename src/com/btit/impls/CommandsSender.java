/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import com.btit.consts.Commands;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;

/**
 *
 * @author BaoToan
 */
public class CommandsSender implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

    private JDesktopPane desktopPanel;
    private Rectangle screeenSize;
    private PrintWriter printWriter;

    @SuppressWarnings("LeakingThisInConstructor")
    public CommandsSender(JDesktopPane desktopPanel, Socket socket, Rectangle screeenSize) {
        try {
            this.desktopPanel = desktopPanel;
            this.screeenSize = screeenSize;
            this.printWriter = new PrintWriter(socket.getOutputStream());
            this.desktopPanel.addMouseListener(this);
            this.desktopPanel.addMouseMotionListener(this);
            this.desktopPanel.addKeyListener(this);
        } catch (IOException ex) {
            Logger.getLogger(CommandsSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        printWriter.println(Commands.MOUSE_PRESS.getCode());
        int button = e.getButton();
        int xButton = 16;
        if (button == 3) {
            xButton = 4;
        }
        printWriter.println(xButton);
        printWriter.flush();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        printWriter.println(Commands.MOUSE_RELEASE.getCode());
        int button = e.getButton();
        int xButton = 16;
        if (button == 3) {
            xButton = 4;
        }
        printWriter.println(xButton);
        printWriter.flush();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        double x = (screeenSize.getWidth() / desktopPanel.getWidth()) * e.getX();
        double y = (screeenSize.getHeight() / desktopPanel.getHeight()) * e.getY();
        printWriter.println(Commands.MOUSE_MOVE.getCode());
        printWriter.println(x);
        printWriter.println(y);
        printWriter.flush();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        printWriter.println(Commands.PRESS_KEY.getCode());
        printWriter.println(e.getKeyCode());
        printWriter.flush();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        printWriter.println(Commands.RELEASE_KEY.getCode());
        printWriter.println(e.getKeyCode());
        printWriter.flush();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        printWriter.println(Commands.RELEASE_KEY.getCode());
        printWriter.println(e.getWheelRotation());
        printWriter.flush();
    }

}
