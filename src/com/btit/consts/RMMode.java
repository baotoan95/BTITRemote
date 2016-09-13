/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.btit.consts;

/**
 *
 * @author BaoToan
 */
public enum RMMode {
    CLIENT_MODE(1),
    SERVER_MODE(2),
    ROOM_MODE(3),
    ROOMMATE(4);
    
    private int code;

    RMMode(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
    
}
