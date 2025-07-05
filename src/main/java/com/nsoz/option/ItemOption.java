/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.option;

import com.nsoz.item.ItemManager;
import com.nsoz.item.ItemOptionTemplate;
import com.nsoz.util.NinjaUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemOption implements Cloneable {

    public byte active;
    public int param;
    public ItemOptionTemplate optionTemplate;
    public ItemOption(int optionTemplateId, int param) {
        this.param = param;
        this.optionTemplate = ItemManager.getInstance().getItemOptionTemplate(optionTemplateId);
    }

    public String getOptionString() {
        return NinjaUtils.replace(this.optionTemplate.name, "#", this.param + "");
    }

    @Override
    public ItemOption clone() {
        try {
            return (ItemOption) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ItemOption.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
