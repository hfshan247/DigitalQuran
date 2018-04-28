package com.example.hussainfarooq.digitalquran.model;

/**
 * @author saifkhichi96
 * @version 1.0
 *          created on 29/04/2018 12:21 AM
 */

public class Surah {

    private String index;
    private String name;
    private Object verse;
    private int count;
    private Object juz;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getVerse() {
        return verse;
    }

    public void setVerse(Object verse) {
        this.verse = verse;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Object getJuz() {
        return juz;
    }

    public void setJuz(Object juz) {
        this.juz = juz;
    }
}
