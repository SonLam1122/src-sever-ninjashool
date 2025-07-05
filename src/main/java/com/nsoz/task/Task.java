/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.task;

import java.util.ArrayList;

/**
 * @author PC
 */
public class Task {

    public static ArrayList<TaskTemplate> arrTaskTemplate;
    public int index;
    public short taskId;
    public short count;
    public TaskTemplate template;
    public Task(short taskId, byte index, short count) {
        this.taskId = taskId;
        this.index = index;
        this.count = count;
        this.template = getTaskTemplate(taskId);
    }

    public static TaskTemplate getTaskTemplate(int id) {
        for (TaskTemplate task : arrTaskTemplate) {
            if (task.getTaskId() == id) {
                return task;
            }
        }
        return null;
    }

    public boolean isComplete() {
        return index >= template.getSubNames().length - 1;
    }
}
