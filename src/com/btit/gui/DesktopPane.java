/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

/**
 *
 * @author BaoToan
 */
public class DesktopPane extends JDesktopPane {
    private ImageIcon imageIcon;

    public DesktopPane() {
        setFocusable(true);
    }
    
    public void updateScreen(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        if(imageIcon != null) {
            g2d.drawImage(imageIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }
    
}
