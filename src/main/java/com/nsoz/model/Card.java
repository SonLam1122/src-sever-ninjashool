/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Source được chia sẻ miễn phí tại: nsotien.com
 */
@Builder
@AllArgsConstructor
@Getter
public class Card {

    private int id;
    @Builder.Default
    private int quantity = 1;
    @Builder.Default
    private long expire = -1;
    private double rate;
}
