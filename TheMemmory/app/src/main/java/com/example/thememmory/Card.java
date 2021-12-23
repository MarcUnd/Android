package com.example.thememmory;

public class Card {
    private boolean visible;
    private int value;


    public String toString(){
        return visible + " " + value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getValue() {
        return value;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
