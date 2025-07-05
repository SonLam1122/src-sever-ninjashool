/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.map.item;

import com.nsoz.constants.ItemName;
import com.nsoz.item.Item;
import com.nsoz.item.ItemFactory;

/**
 * Source được chia sẻ miễn phí tại: nsotien.com
 */
public class Envelope extends ItemMap {

    public Envelope(short id) {
        super(id);
        Item item = ItemFactory.getInstance().newItem(ItemName.BAO_LI_XI_LON);
        item.setQuantity(1);
        setItem(item);
        item.isLock = true;
        this.ownerID = -1;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

}
