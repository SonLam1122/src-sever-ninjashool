/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.npc;

/**
 * Source được chia sẻ miễn phí tại: nsotien.com
 */
public class NpcTemplate {

    public int npcTemplateId;
    public String name;
    public int headId;
    public int bodyId;
    public int legId;
    public String[][] menu;

    @Override
    public String toString() {
        return String.format("ID: %d name: %s head: %d body: %d leg: %d", this.npcTemplateId, this.name, this.headId, this.bodyId, this.legId);
    }
}
