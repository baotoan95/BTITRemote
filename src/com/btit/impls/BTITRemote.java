/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import com.btit.consts.RMMode;
import com.btit.gui.ChatWindow;
import com.btit.init.MainGUI;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BaoToan
 */
public class BTITRemote extends Thread {

    private MainGUI mainGUI;
    public static ChatWindow chatWindow;

    private ServerSocket serverSocket = null;
    private Socket socket;
    private Robot robot;
    private BlockingQueue<Runnable> threadQueue;
    private ThreadPoolExecutor socketPool;

    private ArrayList<Socket> socketConnected;

    private int mode;
    private boolean established = false;

    private ScreenshotReceiver screenshotReceiver;
    private ScreenshotSender screenshotSender;
    private CommandsReceiver commandsReceiver;
    private MessageSender messageSender;
    private MessageReceiver messageReceiver;

    public BTITRemote(Socket socket, MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.socket = socket;
        chatWindow = new ChatWindow(this);
        System.out.println(chatWindow);
        mainGUI.getDesktopPanel().add(chatWindow);
    }

    public BTITRemote(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        chatWindow = new ChatWindow(this);
        mainGUI.getDesktopPanel().add(chatWindow);
    }

    public boolean createClient(String name, String ip, int port) {
        try {
            socket = new Socket(ip, port);
            socket.setSoTimeout(5000);
            this.setSocket(socket);
            this.setMode(RMMode.CLIENT_MODE);
            this.setName(name);
            this.setEstablished(true);
            mainGUI.established(isEstablished());

            // Get screen dimensions
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            new CommandsSender(mainGUI.getDesktopPanel(), socket, new Rectangle(screenSize));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createServer(String name, int port) {
        try {
            robot = new Robot();
            this.setServerSocket(new ServerSocket(port));
            this.setMode(RMMode.SERVER_MODE);
            this.setEstablished(true);
            this.setName(name);
            mainGUI.established(isEstablished());
            return true;
        } catch (IOException | AWTException ex) {
            Logger.getLogger(BTITRemote.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean createRoom(String name, int port) {
        try {
            robot = new Robot();
            socketConnected = new ArrayList<>();
            this.setServerSocket(new ServerSocket(port));
            this.setName(name);
            this.setMode(RMMode.ROOM_MODE);
            this.setEstablished(true);
            threadQueue = new ArrayBlockingQueue<>(100);
            socketPool = new ThreadPoolExecutor(5, 500, 15, TimeUnit.MINUTES, threadQueue);
            mainGUI.established(isEstablished());
            return true;
        } catch (IOException | AWTException ex) {
            Logger.getLogger(BTITRemote.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void sendMessage(String message) {
        messageSender = new MessageSender(socket, getName(), message);
        messageSender.start();
    }

    @Override
    public void run() {
        if (mode == RMMode.SERVER_MODE.getCode()) {
            try {
                socket = serverSocket.accept();
                screenshotSender = new ScreenshotSender(socket, robot);
                screenshotSender.start();
                commandsReceiver = new CommandsReceiver(socket, robot);
                commandsReceiver.start();
//                messageReceiver = new MessageReceiver(chatWindow, socket);
//                messageReceiver.start();
            } catch (IOException ex) {
                System.out.println("Server is closed...");
            }
        } else if (mode == RMMode.CLIENT_MODE.getCode()) {
            screenshotReceiver = new ScreenshotReceiver(socket, mainGUI);
            screenshotReceiver.start();
        } else if (mode == RMMode.ROOM_MODE.getCode()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (established) {
                        try {
                            Socket newConnect = serverSocket.accept();
                            socketPool.execute(new BTITRemote(newConnect, mainGUI));
                            socketConnected.add(newConnect);
                            screenshotSender = new ScreenshotSender(newConnect, robot);
                            screenshotSender.start();
//                            messageReceiver = new MessageReceiver(chatWindow, socket);
//                            messageReceiver.start();
                        } catch (IOException ex) {
                            Logger.getLogger(BTITRemote.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }).start();
        }
    }

    public boolean isEstablished() {
        return established;
    }

    public void setEstablished(boolean established) {
        this.established = established;
    }

    public static ChatWindow getChatWindow() {
        return chatWindow;
    }

    public void setMode(RMMode mode) {
        this.mode = mode.getCode();
    }

    public int getMode() {
        return mode;
    }

    /**
     * @return the server
     */
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * @param serverSocket
     */
    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @param socket the socket to set
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * @return the threadQueue
     */
    public BlockingQueue<Runnable> getThreadQueue() {
        return threadQueue;
    }

    /**
     * @param threadQueue the threadQueue to set
     */
    public void setThreadQueue(BlockingQueue<Runnable> threadQueue) {
        this.threadQueue = threadQueue;
    }

    /**
     * @return the socketPool
     */
    public ThreadPoolExecutor getSocketPool() {
        return socketPool;
    }

    /**
     * @param socketPool the socketPool to set
     */
    public void setSocketPool(ThreadPoolExecutor socketPool) {
        this.socketPool = socketPool;
    }

}
