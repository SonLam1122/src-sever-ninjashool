package com.nsoz.task;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskTemplate {

    private short taskId;
    private String name;
    private String detail;
    private String[] subNames;
    private short[] counts;
    private short leveRequire;
    private short[][] mobs;
    private short[] items;

}
