package com.example.hussainfarooq.digitalquran.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author saifkhichi96
 * @version 1.0
 *          created on 29/04/2018 12:22 AM
 */

public class Quran {
    private static Quran ourInstance;

    public static Quran getInstance(Activity activity) {
        if (ourInstance == null) {
            ourInstance = new Quran(activity);
        }
        return ourInstance;
    }

    private SurahMeta[] metadata;
    private Topic[] topics;

    private Quran(Activity activity) {
        try {
            metadata = loadMetadata(activity);
            topics = loadTopics(activity);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private SurahMeta[] loadMetadata(Activity activity) throws IOException {
        String json = loadJsonString(activity, "quran/surah.json");
        Gson gson = new Gson();
        return gson.fromJson(json, SurahMeta[].class);
    }

    private Topic[] loadTopics(Activity activity) throws IOException {
        SharedPreferences prefs = activity.getSharedPreferences("DigitalQuran", Context.MODE_PRIVATE);
        String json = prefs.getString("topics", null);
        if (json == null) {
            json = loadJsonString(activity, "quran/topics.json");
            prefs.edit().putString("topics", json).apply();
        }
        Gson gson = new Gson();
        return gson.fromJson(json, Topic[].class);
    }

    public SurahMeta[] getMetadata() {
        return metadata;
    }

    public Topic[] getTopics() {
        return topics;
    }

    public Topic[] addTopic(Activity activity, String name) {
        SharedPreferences prefs = activity.getSharedPreferences("DigitalQuran", Context.MODE_PRIVATE);
        String json = prefs.getString("topics", null);
        if (json != null) {
            json = json.substring(0, json.length() - 1);
            json += ",{ \"name\":\"" + name + "\", \"aya\":[]}]";
            prefs.edit().putString("topics", json).apply();
        }
        prefs.edit().putString("topics", json).apply();
        try {
            topics = loadTopics(activity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return topics;
    }

    public Surah getSurah(Activity activity, int surahIndex) {
        try {
            String json = loadJsonString(activity, "quran/surah/surah_" + String.valueOf(surahIndex) + ".json");
            Gson gson = new Gson();
            return gson.fromJson(json, Surah.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String loadJsonString(Activity activity, String filename) throws IOException {
        InputStream is = activity.getAssets().open(filename);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        return new String(buffer, "UTF-8");
    }

}
