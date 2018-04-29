package com.example.hussainfarooq.digitalquran.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author saifkhichi96
 * @version 1.0
 *          created on 29/04/2018 12:22 AM
 */

public class Quran {
    private static Quran ourInstance;
    private final HashMap<Integer, Surah> surahs = new HashMap<>();
    private SurahMeta[] metadata;
    private Topic[] topics;

    private Quran(Activity activity) {
        try {
            metadata = loadMetadata(activity);
            topics = loadTopics(activity);
            new SurahLoader(activity).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Quran getInstance(Activity activity) {
        if (ourInstance == null) {
            ourInstance = new Quran(activity);
        }
        return ourInstance;
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

    private void loadSurahs(Activity activity) {
        for (int i = 1; i <= 114; i++) {
            if (surahs.containsKey(i)) { // Skip Surah which have been already loaded
                continue;
            }

            synchronized (surahs) {
                surahs.put(i, loadSurah(activity, i));
            }
        }
    }

    private synchronized Surah loadSurah(Activity activity, int surahIndex) {
        try {
            String json = loadJsonString(activity, "quran/surah/surah_" + String.valueOf(surahIndex) + ".json");
            Gson gson = new Gson();
            return gson.fromJson(json, Surah.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Surah getSurah(Activity activity, int surahIndex) {
        if (surahs.containsKey(surahIndex)) {
            return surahs.get(surahIndex);
        } else {
            Surah surah = loadSurah(activity, surahIndex);
            synchronized (surahs) {
                if (surah != null && !surahs.containsKey(surahIndex)) {
                    surahs.put(surahIndex, surah);
                }
            }
            return surah;
        }
    }

    private String loadJsonString(Activity activity, String filename) throws IOException {
        InputStream is = activity.getAssets().open(filename);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        return new String(buffer, "UTF-8");
    }

    public List<Ayat> search(String keyword) {
        List<Ayat> searchResult = new ArrayList<>();
        synchronized (surahs) {
            for (Surah surah : surahs.values()) {
                searchResult.addAll(surah.search(keyword));
            }
        }
        return searchResult;
    }

    private class SurahLoader extends AsyncTask<Void, Void, Void> {

        private Activity activity;

        private SurahLoader(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                loadSurahs(activity);
            } catch (Exception ex) {

            } finally {
                activity = null;
            }
            return null;
        }
    }

}
