/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.map.zones;

import com.nsoz.map.Map;
import com.nsoz.map.Waypoint;
import com.nsoz.map.world.World;
import com.nsoz.model.Char;
import org.jetbrains.annotations.NotNull;

/**
 * Source được chia sẻ miễn phí tại: nsotien.com
 */
public class WarCandy extends ZWorld {

    public WarCandy(Map map, World world) {
        super(0, map.tilemap, map);
        setWorld(world);
    }

    @Override
    public void requestChangeMap(@NotNull Char p) {
        Waypoint wp = tilemap.findWaypoint(p.x, p.y);
        if (wp == null) {
            return;
        }
        Zone z = world.find(wp.next);
    }

}
