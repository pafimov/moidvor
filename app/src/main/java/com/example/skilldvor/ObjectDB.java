package com.example.skilldvor;

import android.view.View;

import java.io.Serializable;

public class ObjectDB implements Serializable {
    public String type, info;
    public float x, y, rotation;
    public ObjectDB(){

    }
    public ObjectDB(String type, float x, float y, float rotation) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }
}
