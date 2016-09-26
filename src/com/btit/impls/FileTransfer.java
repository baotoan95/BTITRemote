/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BaoToan
 */
public class FileTransfer extends Thread {

    private String name;
    private Socket socket;
    private File file;

    public FileTransfer(String name, Socket socket, File file) {
        this.name = name;
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            byte[] data = new byte[512];
            int length;
            while ((length = bufferedInputStream.read(data)) > 0) {
                bufferedOutputStream.write(data, 0, length);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileTransfer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(null != bufferedInputStream) bufferedInputStream.close();
                if(null != bufferedOutputStream) bufferedOutputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(FileTransfer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
