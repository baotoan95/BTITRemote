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
public class DesktopPanel extends JDesktopPane {

    public DesktopPanel() {
    }

    private ImageIcon imageIcon;

    public void updateBackground(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if(null != imageIcon) {
            g2d.drawImage(imageIcon.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

}
