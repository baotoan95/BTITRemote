/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import java.awt.Robot;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BaoToan
 */
public class CommandsReceiver extends Thread {

    private Socket socket;
    private Robot robot;

    public CommandsReceiver(Socket socket, Robot robot) {
        this.socket = socket;
        this.robot = robot;
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(socket.getInputStream());
            while (BTITRemote.CONNECTED) {
                if (scanner.hasNext()) {
                    int command = scanner.nextInt();
                    switch (command) {
                        case -1:
                            int x = (int) scanner.nextDouble();
                            int y = (int) scanner.nextDouble();
                            robot.mouseMove(x, y);
                            break;
                        case -2:
                            robot.mousePress(scanner.nextInt());
                            break;
                        case -3:
                            robot.mouseRelease(scanner.nextInt());
                            break;
                        case -4:
                            System.out.println(scanner.nextInt());
                            robot.mouseWheel(scanner.nextInt());
                            break;
                        case -5:
                            robot.keyPress(scanner.nextInt());
                            break;
                        case -6:
                            robot.keyRelease(scanner.nextInt());
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CommandsReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
