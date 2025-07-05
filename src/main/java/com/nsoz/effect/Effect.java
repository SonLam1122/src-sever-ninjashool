/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.effect;

import lombok.Getter;
import org.json.simple.JSONObject;

@Getter
public class Effect {

    public final byte EFF_ME = 0;
    public final byte EFF_FRIEND = 1;

    public int param;
    public int param2;
    public EffectTemplate template;
    private long startAt;
    private long endAt;

    public Effect(int templateId, long startAt, long endAt, int param) {
        this.template = EffectTemplateManager.getInstance().find(templateId);
        this.startAt = startAt;
        this.endAt = endAt;
        this.param = param;
    }

    public Effect(int templateId, long length, int param) {
        this.template = EffectTemplateManager.getInstance().find(templateId);
        this.startAt = System.currentTimeMillis();
        this.endAt = startAt + length;
        this.param = param;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= endAt;
    }

    public int getTimeStart() {
        return (int) ((System.currentTimeMillis() - startAt) / 1000);
    }

    public int getTimeLength() {
        return (int) (endAt - startAt);
    }

    public long getTimeRemaining() {
        return endAt - System.currentTimeMillis();
    }

    public void addTime(long time) {
        this.endAt += time;
    }

    public JSONObject toJSONObject() {
        JSONObject job = new JSONObject();
        job.put("id", this.template.id);
        job.put("start_at", startAt);
        job.put("end_at", endAt);
        job.put("param", this.param);
        return job;
    }
}
