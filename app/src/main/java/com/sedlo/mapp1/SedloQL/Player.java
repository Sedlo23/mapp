package com.sedlo.mapp1.SedloQL;

public class Player
{
    String name;
    String ID;
    public boolean aBoolean=false;

    public Player(String name, String ID) {
        this.name = name;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public String getID() {
        return ID;
    }

    public Player setID(String ID) {
        this.ID = ID;
        return this;
    }
}
