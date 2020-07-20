package com.sedlo.mapp1.SedloQL;

public class Team  {
    String name;
    String ID;
    public boolean aBoolean=false;

    public Team(String name, String ID) {
        this.name = name;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public Team setName(String name) {
        this.name = name;
        return this;
    }

    public String getID() {
        return ID;
    }

    public Team setID(String ID) {
        this.ID = ID;
        return this;
    }

}
