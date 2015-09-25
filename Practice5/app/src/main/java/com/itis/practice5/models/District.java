package com.itis.practice5.models;

public class District {
    
    public enum Size {
        LARGE, MIDDLE, SMALL
    }

    private String mName;
    private Size mSize;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Size getSize() {
        return mSize;
    }

    public void setSize(Size size) {
        mSize = size;
    }
}
