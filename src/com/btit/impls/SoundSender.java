/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import com.btit.gui.MainGUI;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author BaoToan
 */
public class SoundSender extends Thread {

    private Socket socket;
    private TargetDataLine targetDataLine;
    private AudioFormat format;
    private boolean isRecording = true;
    private MainGUI mainGUI;

    public SoundSender(Socket socket, MainGUI mainGui) {
        try {
            this.socket = socket;
            format = getaudioformat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            this.mainGUI = mainGui;
        } catch (LineUnavailableException ex) {
            Logger.getLogger(SoundSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static AudioFormat getaudioformat() {
        float sampleRate = 8000.0F;
        int sampleSizeInbits = 16;
        int channel = 2;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInbits, channel, signed, bigEndian);
    }

    @Override
    public void run() {
        try {
            BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream());
            byte[] buffer = new byte[512];
            targetDataLine.open();
            targetDataLine.start();
            while (isRecording && BTITRemote.CONNECTED) {
                targetDataLine.read(buffer, 0, buffer.length);
                output.write(buffer);
                output.flush();
            }
        } catch (LineUnavailableException | IOException ex) {
            System.out.println("");
            mainGUI.getBTITRemote().disConnect();
        }
        targetDataLine.drain();
        targetDataLine.close();
        
    }

    public void setIsRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }

    public boolean isIsRecording() {
        return isRecording;
    }

    public Socket getSocket() {
        return socket;
    }

}
