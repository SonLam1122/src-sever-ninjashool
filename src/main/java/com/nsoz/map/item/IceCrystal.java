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
public class IceCrystal extends ItemMap {

    public IceCrystal(short id) {
        super(id);
        Item item = ItemFactory.getInstance().newItem(ItemName.TINH_THE_BANG);
        item.setQuantity(1);
        item.isLock = true;
        setItem(item);
        this.pickedUp = true;
        this.ownerID = -1;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

}
