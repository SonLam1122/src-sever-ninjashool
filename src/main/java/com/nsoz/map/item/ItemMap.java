/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.map.item;

import com.nsoz.item.Item;
import com.nsoz.util.TimeUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Administrator
 */
@Getter
@Setter
public class ItemMap {

    public Lock lock = new ReentrantLock();
    protected short id;
    protected Item item;
    protected int ownerID;
    protected short x;
    protected short y;
    protected boolean pickedUp;
    protected int requireItemID = -1;
    protected long createdAt;

    public ItemMap(short id) {
        this.id = id;
        this.createdAt = System.currentTimeMillis();
    }

    public boolean isExpired() {
        return TimeUtils.canDoWithTime(createdAt, 30000);
    }

    public boolean isCanPickup() {
        return TimeUtils.canDoWithTime(createdAt, 20000);
    }

    public int getItemID() {
        return item.id;
    }

}
