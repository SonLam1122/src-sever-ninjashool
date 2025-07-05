/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.skill;

import com.nsoz.convert.Converter;
import com.nsoz.server.GameData;

/**
 * @author Admin
 */
public class SkillFactory {

    private static final SkillFactory instance = new SkillFactory();

    public static SkillFactory getInstance() {
        return instance;
    }

    public Skill newSkill(int id, int point) {
        Skill skill = GameData.getInstance().getSkill(id, point);
        if (skill != null) {
            return Converter.getInstance().newSkill(skill);
        }
        return null;
    }
}
