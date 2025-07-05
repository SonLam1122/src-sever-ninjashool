/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.model;

import com.nsoz.item.Item;
import com.nsoz.skill.Skill;

import java.util.ArrayList;
import java.util.List;

/**
 * Source được chia sẻ miễn phí tại: nsotien.com
 */
public class Bijuu {

    private List<Skill> skills;
    private short[] potential;
    private Item[] equips;
    private int ppoint, spoint;

    public Bijuu() {
        this.skills = new ArrayList<>();
        this.potential = new short[5];
    }

}
