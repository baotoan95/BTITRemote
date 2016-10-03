/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.impls;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author BaoToan
 */
public class SoundReceiver extends Thread {

    private Socket socket;
    private SourceDataLine sourceDataLine;
    private AudioFormat format;
    private boolean isListening = true;

    public SoundReceiver(Socket socket) {
        try {
            this.socket = socket;
            format = getaudioformat();
            DataLine.Info info_out = new DataLine.Info(SourceDataLine.class, format);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(info_out);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(SoundReceiver.class.getName()).log(Level.SEVERE, null, ex);
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
            BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
            sourceDataLine.open(format);
            sourceDataLine.start();
            byte[] data = new byte[512];
            while (isListening && BTITRemote.CONNECTED) {
                int i = input.read(data, 0, data.length);
                if(i > 0) {
                    sourceDataLine.write(data, 0, data.length);
                }
            }
        } catch (IOException | LineUnavailableException ex) {
//            ex.printStackTrace();
        }
        sourceDataLine.flush();
        sourceDataLine.drain();
        sourceDataLine.close();
        
    }

    public void setIsListening(boolean isListening) {
        this.isListening = isListening;
    }

    public boolean isIsListening() {
        return isListening;
    }

    public Socket getSocket() {
        return socket;
    }
    
}
