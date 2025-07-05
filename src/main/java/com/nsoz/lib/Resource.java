/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.lib;

/**
 * Source được chia sẻ miễn phí tại: nsotien.com
 */
public class Resource {

    private long timeRemoveResource;
    private byte[] data;
    private long createdAt;

    public Resource(byte[] data, long timeRemoveResource) {
        this.data = data;
        this.timeRemoveResource = timeRemoveResource;
        this.createdAt = System.currentTimeMillis();
    }

    public boolean isExpired() {
        //return (System.currentTimeMillis() - createdAt) > timeRemoveResource;
        return false;
    }

    public byte[] getData() {
        return this.data;
    }
}
