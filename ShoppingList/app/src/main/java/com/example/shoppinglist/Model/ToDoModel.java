package com.example.shoppinglist.Model;

// holds info for list objects

public class ToDoModel {
    private int id, parentID, status;
    private String task; // entry text
    private String type; // for distinguishing between shop items and tasks

    //getters n setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getParentID() {
        return parentID;
    }
    public void setParentID(int parentID) {
        this.parentID = parentID;
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
    public void setType(String type) { this.type = type; }
}
