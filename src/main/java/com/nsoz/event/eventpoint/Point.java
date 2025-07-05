/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.event.eventpoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Source được chia sẻ miễn phí tại: nsotien.com
 */
@Setter
@Getter
@AllArgsConstructor

public class Point {

    private String key;
    private int point;
    private int rewarded;
    private int rewarded50k;
    private int rewardedAll;

    public void addPoint(int point) {
//        System.out.println("addpint: "+point);
        this.point += point;
    }

    public void subPoint(int point) {
        this.point -= point;
    }
}
