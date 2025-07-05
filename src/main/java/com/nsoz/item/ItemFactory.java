/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.item;

import com.nsoz.lib.RandomCollection;
import com.nsoz.option.ItemOption;
import com.nsoz.util.NinjaUtils;

/**
 * Source được chia sẻ miễn phí tại: nsotien.com
 */
public class ItemFactory {

    private static final ItemFactory instance = new ItemFactory();

    public static ItemFactory getInstance() {
        return instance;
    }

    public Item newItem(RandomCollection<Integer> rd) {
        Item item = newItem(rd.next());
        return item;
    }

    public Item newItem9X(int id) {
        return newItem9X(id, false);
    }

    public Item newItem9X(int id, boolean isMaxOption) {
        Item itm = newItem(id);
        itm.randomOptionItem9x(isMaxOption);
        return itm;
    }

    public Item newItem10X(int id) {
        return newItem10X(id, NinjaUtils.nextInt(30) == 1);
    }
    public Item newItem10X(int id, boolean isMaxOption) {
        Item itm = newItem(id);
        itm.randomOptionItem10x(isMaxOption);
        return itm;
    }


    public Item newItem(int id) {
        Item item = new Item(id);
        long now = System.currentTimeMillis();
        item.setCreatedAt(now);
        item.setUpdatedAt(now);
        item.setNew(true);
        return item;
    }

    public Item newGem(int id, boolean max) {
        Item item = newItem(id);
        if (max) {
            item.options.clear();
            if (item.id == 652) {
                item.options.add(new ItemOption(106, 0));
                item.options.add(new ItemOption(102, 500));
                item.options.add(new ItemOption(115, -1));
                item.options.add(new ItemOption(107, 0));
                item.options.add(new ItemOption(126, 5));
                item.options.add(new ItemOption(105, -1));
                item.options.add(new ItemOption(108, 0));
                item.options.add(new ItemOption(114, 5));
                item.options.add(new ItemOption(118, -1));
            } else if (item.id == 653) {
                item.options.add(new ItemOption(106, 0));
                item.options.add(new ItemOption(73, 100));
                item.options.add(new ItemOption(114, -1));
                item.options.add(new ItemOption(107, 0));
                item.options.add(new ItemOption(124, 10));
                item.options.add(new ItemOption(114, -1));
                item.options.add(new ItemOption(108, 0));
                item.options.add(new ItemOption(115, 10));
                item.options.add(new ItemOption(119, -1));
            } else if (item.id == 654) {
                item.options.add(new ItemOption(106, 0));
                item.options.add(new ItemOption(103, 200));
                item.options.add(new ItemOption(125, -1));
                item.options.add(new ItemOption(107, 0));
                item.options.add(new ItemOption(121, 5));
                item.options.add(new ItemOption(120, -1));
                item.options.add(new ItemOption(108, 0));
                item.options.add(new ItemOption(116, 10));
                item.options.add(new ItemOption(126, -1));
            } else if (item.id == 655) {
                item.options.add(new ItemOption(106, 0));
                item.options.add(new ItemOption(105, 500));
                item.options.add(new ItemOption(116, -1));
                item.options.add(new ItemOption(107, 0));
                item.options.add(new ItemOption(125, 50));
                item.options.add(new ItemOption(117, -1));
                item.options.add(new ItemOption(108, 0));
                item.options.add(new ItemOption(117, 50));
                item.options.add(new ItemOption(124, -1));
            }
        }
        return item;
    }

    public Mount newMount(int id) {
        Mount mount = new Mount(id);
        long now = System.currentTimeMillis();
        mount.setCreatedAt(now);
        mount.setUpdatedAt(now);
        mount.setNew(true);
        return mount;
    }

    public Equip newEquipment(int id) {
        Equip equipment = new Equip(id);
        long now = System.currentTimeMillis();
        equipment.setCreatedAt(now);
        equipment.setUpdatedAt(now);
        equipment.setNew(true);
        return equipment;
    }
}
