/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.map;

import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * @author PC
 */
@Builder
@AllArgsConstructor
public class ItemTree {

    public int idTree;
    public int xTree, yTree;
    public ItemTree(int x, int y) {
        this.xTree = x;
        this.yTree = y;
    }
}
