/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.map.zones;

import com.nsoz.map.Map;
import com.nsoz.map.world.CandyBattlefield;
import com.nsoz.map.world.World;
import com.nsoz.model.Char;
import com.nsoz.util.NinjaUtils;
import lombok.Setter;

/**
 * Source được chia sẻ miễn phí tại: nsotien.com
 */
public class WaitingRoom extends Zone {

    @Setter
    private World world;
    private boolean waitToClose;

    public WaitingRoom(Map map, World world) {
        super(0, map.tilemap, map);
        setWorld(world);
    }

    public void join(Char p) {
        Zone preZone = p.zone;
        p.addMemberForWorld(preZone, this);
        super.join(p);

    }

    public void out(Char p) {
        super.out(p);
        if (getNumberChar() == 0 && !waitToClose) {
            waitToClose = true;
            NinjaUtils.setTimeout(() -> {
                waitToClose = false;
                if (getNumberChar() == 0) {
                    world.close();
                }
            }, 300000);
        }
    }

    @Override
    public void update() {
        if (!world.isClosed()) {
            if (getNumberChar() == 20) {
                CandyBattlefield candyBattlefield = (CandyBattlefield) world;
                candyBattlefield.removeZone(this);
                candyBattlefield.open();
                close();
                return;
            }
        }
        super.update();
    }

}
