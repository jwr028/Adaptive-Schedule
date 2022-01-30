package com.example.shoppinglist.Model;

// holds info for items and tasks in list when creating list

public class ToDoModel {
    private int id, status;
    private String task; // entry text
    private String type; // for distinguishing between shop items and tasks

    //getters n setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
