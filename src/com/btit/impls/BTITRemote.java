/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import com.btit.consts.RMMode;
import com.btit.gui.ChatWindow;
import com.btit.gui.MainGUI;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BaoToan
 */
public class BTITRemote extends Thread {

    private MainGUI mainGUI;
    public static ChatWindow chatWindow;

    private String host;
    private int port;
    private RMMode mode;

    private ServerSocket serverSocket;

    private Socket mainSocket;
    private MessageSender messageSender;

    public BTITRemote(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        chatWindow = new ChatWindow(this);
        mainGUI.getDesktopPanel().add(chatWindow);
    }

    public void createServer(String name, int port, RMMode rmMode) {
        try {
            serverSocket = new ServerSocket(port);
            setMode(rmMode);
            setName(name);
            setPort(port);
            start();
        } catch (IOException ex) {
            Logger.getLogger(BTITRemote.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createClient(String name, String host, int port) {
        try {
            mainSocket = new Socket(host, port);
            setMode(RMMode.CLIENT_MODE);
            setName(name);
            setHost(host);
            setPort(port);
            start();
        } catch (IOException ex) {
            Logger.getLogger(BTITRemote.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessage(String message) {
        messageSender.send(message);
    }

    @Override
    public void run() {
        if (mode == RMMode.SERVER_MODE) {
            try {
                // Send screenshot and commands
                Socket socket = serverSocket.accept();
                // Get size screen of current device
                GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gDev = gEnv.getDefaultScreenDevice();
                // Get screen dimensions
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                Rectangle rectangle = new Rectangle(dim);

                new ScreenshotSender(socket, new Robot(gDev), rectangle).start();
                new CommandsReceiver(socket, new Robot(gDev)).start();

                // Chat task
                Socket chatSocket = serverSocket.accept();
                messageSender = new MessageSender(chatSocket, getName());
                new MessageReceiver(chatWindow, chatSocket).start();
            } catch (IOException | AWTException ex) {
                Logger.getLogger(BTITRemote.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (mode == RMMode.CLIENT_MODE) {
            ObjectInputStream objectInputStream = null;
            try {
                // Get rectangle dimension of server
                objectInputStream = new ObjectInputStream(mainSocket.getInputStream());
                Rectangle rectangle = (Rectangle) objectInputStream.readObject();
                new ScreenshotReceiver(objectInputStream, mainGUI.getDesktopPanel()).start();
                new CommandsSender(mainGUI.getDesktopPanel(), mainSocket, rectangle);

                // Chat task
                Socket chatSocket = new Socket(host, port);
                messageSender = new MessageSender(chatSocket, getName());
                new MessageReceiver(chatWindow, chatSocket).start();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(BTITRemote.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (mode == RMMode.ROOM_MODE) {
            while (true) {
                try {
                    // Send screenshot and commands
                    Socket socket = serverSocket.accept();
                    // Get size screen of current device
                    GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    GraphicsDevice gDev = gEnv.getDefaultScreenDevice();
                    // Get screen dimensions
                    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                    Rectangle rectangle = new Rectangle(dim);

                    new ScreenshotSender(socket, new Robot(gDev), rectangle).start();

                    // Chat task
                    Socket chatSocket = serverSocket.accept();
                    messageSender = new MessageSender(chatSocket, getName());
                    new MessageReceiver(chatWindow, chatSocket).start();
                } catch (IOException | AWTException ex) {
                    Logger.getLogger(BTITRemote.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    /**
     * @return the mainGUI
     */
    public MainGUI getMainGUI() {
        return mainGUI;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the mode
     */
    public RMMode getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(RMMode mode) {
        this.mode = mode;
    }

}
