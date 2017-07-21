package com.surine.family.EventBus;

/**
 * Created by surine on 2017/4/8.
 */

public class SimpleEvent {
    private int id;
    private String message;

    public SimpleEvent(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
