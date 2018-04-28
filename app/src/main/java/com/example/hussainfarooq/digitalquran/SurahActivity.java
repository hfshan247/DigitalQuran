package com.example.hussainfarooq.digitalquran;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hussainfarooq.digitalquran.model.Quran;
import com.example.hussainfarooq.digitalquran.model.Surah;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Map;

public class SurahActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah);

        // Load Surah
        Intent mIntent = getIntent();
        int surahIndex = mIntent.getIntExtra("surahIndex", -1);
        Quran mQuran = Quran.getInstance(this);
        Surah mSurah = mQuran.getSurah(this, surahIndex);
        if (surahIndex == -1 || mSurah == null) {
            finish();
            return;
        }

        // Set Activity Title
        setTitle(mSurah.getName());

        // Display Verses
        ListView mAyaList = findViewById(R.id.aya_list);
        final ArrayList<String> list = new ArrayList<String>();
        LinkedTreeMap<String, String> verses = (LinkedTreeMap<String, String>) mSurah.getVerse();
        for (Map.Entry<String, String> entry : verses.entrySet()) {
            list.add(entry.getValue());
        }
        final ArrayAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);
        mAyaList.setAdapter(adapter);
        mAyaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                // TODO: Handle clicks on ayat
            }
        });
    }
}
