/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.skill;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class SkillTemplate {

    public final List<Skill> skills = new ArrayList<>();
    public int id;
    public String name;
    public byte maxPoint;
    public byte type;
    public short iconId;
    public String description;

    public void addSkill(Skill skill) {
        skills.add(skill);
    }
}
