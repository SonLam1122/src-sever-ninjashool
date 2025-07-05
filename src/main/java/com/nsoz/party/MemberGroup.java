/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.party;

import com.nsoz.map.world.World;
import com.nsoz.map.zones.NymozCave;
import com.nsoz.model.Char;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Admin
 */
public class MemberGroup {

    private final List<World> worlds = new ArrayList<>();
    public int charId;
    public byte classId;
    public String name;
    @Getter
    @Setter
    private NymozCave nymozCave;
    private Char p;

    public void setWorld(World world) {
        synchronized (worlds) {
            for (World w : worlds) {
                if (w.getType() == world.getType()) {
                    remove(w);
                    break;
                }
            }
            add(world);
        }
    }

    public void add(World world) {
        synchronized (worlds) {
            worlds.add(world);
        }
    }

    public void remove(World world) {
        synchronized (worlds) {
            worlds.remove(world);
        }
    }

    public World find(byte type) {
        synchronized (worlds) {
            for (World world : worlds) {
                if (world.getType() == type) {
                    return world;
                }
            }
        }
        return null;
    }

    public Char getChar() {
        return this.p;
    }

    public void setChar(Char p) {
        this.p = p;
    }
}
