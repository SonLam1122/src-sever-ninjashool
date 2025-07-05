/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.model;

import lombok.Getter;

/**
 * @author Admin
 */
@Getter
public class ConfirmPopup {

    private int id;
    private String title;
    private Runnable runnable;

    public ConfirmPopup(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public ConfirmPopup(int id, String title, Runnable runnable) {
        this.id = id;
        this.title = title;
        this.runnable = runnable;
    }

    public void confirm() {
        if (runnable != null) {
            runnable.run();
        }
    }
}
