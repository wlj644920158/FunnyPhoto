package com.wlj.funnyphoto;

public class Function {
    private String name;
    private int resId;


    public Function(String name, int resId) {
        this.name = name;
        this.resId = resId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
