package com.example.hussainfarooq.digitalquran.model;

/**
 * @author saifkhichi96
 * @version 1.0
 *          created on 29/04/2018 10:29 AM
 */

public class Ayat {

    private String surahIndex;
    private String surah;
    private int index;
    private String text;

    public String getSurahIndex() {
        return surahIndex;
    }

    public void setSurahIndex(String surahIndex) {
        this.surahIndex = surahIndex;
    }

    public String getSurah() {
        return surah;
    }

    public void setSurah(String surah) {
        this.surah = surah;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
