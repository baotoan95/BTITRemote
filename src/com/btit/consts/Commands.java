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
public enum Commands {
    MOUSE_MOVE(-1),
    MOUSE_DRAG(-2),
    MOUSE_PRESS(-3),
    MOUSE_RELEASE(-4),
    PRESS_KEY(-5),
    RELEASE_KEY(-6);

    private int code;

    private Commands(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
