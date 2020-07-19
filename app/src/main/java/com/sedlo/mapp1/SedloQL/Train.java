package com.sedlo.mapp1.SedloQL;

public class Train
{
    String name;
    String ID;

    public Train(String name, String ID) {
        this.name = name;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public Train setName(String name) {
        this.name = name;
        return this;
    }

    public String getID() {
        return ID;
    }

    public Train setID(String ID) {
        this.ID = ID;
        return this;
    }
}
