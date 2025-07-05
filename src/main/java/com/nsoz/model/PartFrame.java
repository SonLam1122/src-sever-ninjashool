package com.nsoz.model;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@Builder
@AllArgsConstructor
public class PartFrame {

    public short idSmallImg;
    public short dx;
    public short dy;
    public byte flip;
    public byte onTop;
}
