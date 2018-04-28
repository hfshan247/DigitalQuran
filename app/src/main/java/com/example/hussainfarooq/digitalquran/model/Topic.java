package com.example.hussainfarooq.digitalquran.model;

/**
 * @author saifkhichi96
 * @version 1.0
 *          created on 29/04/2018 1:07 AM
 */

public class Topic {

    private String name;
    private Object[] aya;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object[] getAya() {
        return aya;
    }

    public void setAya(Object[] aya) {
        this.aya = aya;
    }
}
