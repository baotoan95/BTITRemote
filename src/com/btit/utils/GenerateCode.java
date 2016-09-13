/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.utils;

import java.util.Random;
import sun.applet.Main;

/**
 *
 * @author BaoToan
 */
public class GenerateCode {
    public String generate(int length) {
        String rs = "";
        Random random = new Random();
        int ascii = random.nextInt(122);
        while(rs.length() < length) {
            if(ascii >= 48 && ascii <= 57 || ascii >= 65 && ascii <= 90 || ascii >= 97 && ascii <= 122) {
                rs += (char) ascii;
            }
            ascii = random.nextInt(122);
        }
        return rs;
    }
    
    public static void main(String[] args) {
        System.out.println(new GenerateCode().generate(20));
    }
}
