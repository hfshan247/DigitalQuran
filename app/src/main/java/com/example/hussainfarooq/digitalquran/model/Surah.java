package com.example.hussainfarooq.digitalquran.model;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private transient ArrayList<Ayat> verses = new ArrayList<>();

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

    public ArrayList<Ayat> getVerses() {
        if (verses.size() == 0) {
            LinkedTreeMap<String, String> verses = (LinkedTreeMap<String, String>) verse;
            for (Map.Entry<String, String> entry : verses.entrySet()) {
                try {
                    Ayat ayat = new Ayat();
                    ayat.setSurah(getName());
                    ayat.setSurahIndex(getIndex());
                    ayat.setIndex(Integer.parseInt(entry.getKey().split("_")[1]));
                    ayat.setText(entry.getValue());
                    this.verses.add(ayat);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return verses;
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

    public List<Ayat> search(String keyword) {
        List<Ayat> searchResult = new ArrayList<>();
        for (Ayat ayat : getVerses()) {
            if (ayat.getIndex() == 0 && Integer.parseInt(getIndex()) != 1) {
                continue;
            }

            String ayatText = ayat.getText();

            // Remove a'raab
            ayatText = ayatText.replace("ُ", "");
            ayatText = ayatText.replace("ِ", "");
            ayatText = ayatText.replace("َ", "");
            ayatText = ayatText.replace("ْ", "");
            ayatText = ayatText.replace("ّ", "");
            ayatText = ayatText.replace("ٰ", "");

            keyword = keyword.replace("ُ", "");
            keyword = keyword.replace("ِ", "");
            keyword = keyword.replace("َ", "");
            keyword = keyword.replace("ْ", "");
            keyword = keyword.replace("ّ", "");
            keyword = keyword.replace("ٰ", "");

            if (ayatText.contains(keyword)) {
                searchResult.add(ayat);
            }
        }
        return searchResult;
    }

}
