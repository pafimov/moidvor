package com.example.skilldvor;

import android.view.View;

public class ObjDvor {
    public String type;
    public float x, y, rotation;
    private View v;

    public ObjDvor(String type, View v, float percentX, float percentY, float dropLeft, float dropTop) {
        this.type = type;
        this.v = v;
        this.x = (float)(v.getX() - dropLeft)/ percentX;
        this.y = (float)(v.getY() - dropTop)/ percentY;
        this.rotation = 0;
    }
    public void updateCoords(float percentX, float percentY, float dropLeft, float dropTop){
        this.x = (float)(v.getX() - dropLeft)/percentX;
        this.y = (float)( v.getY() - dropTop)/percentY;
    }
    public View getView(){
        return v;
    }
}
