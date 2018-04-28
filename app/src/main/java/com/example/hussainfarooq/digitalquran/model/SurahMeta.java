package com.example.hussainfarooq.digitalquran.model;

/**
 * @author saifkhichi96
 * @version 1.0
 *          created on 29/04/2018 12:19 AM
 */

public class SurahMeta {

    private String place;
    private String type;
    private int count;
    private String title;
    private String index;
    private String pages;
    private Object juz;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public Object getJuz() {
        return juz;
    }

    public void setJuz(Object juz) {
        this.juz = juz;
    }
}
