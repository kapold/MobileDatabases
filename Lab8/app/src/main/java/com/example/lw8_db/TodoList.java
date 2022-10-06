package com.example.lw8_db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TodoList implements Serializable {
    private List tasks = new ArrayList();
    public TodoList(){}

    public List getTasks() {
        return tasks;
    }
    public void setTasks(List tasks) {
        this.tasks = tasks;
    }
}
