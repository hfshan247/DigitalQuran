package com.example.hussainfarooq.digitalquran;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hussainfarooq.digitalquran.model.Quran;
import com.example.hussainfarooq.digitalquran.model.SurahMeta;
import com.example.hussainfarooq.digitalquran.util.StableArrayAdapter;

import java.util.ArrayList;


public class Home extends AppCompatActivity {


    static final int REQUEST_IMAGE_CAPTURE = 1;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_quran:
                    findViewById(R.id.camera_button).setVisibility(View.GONE);
                    findViewById(R.id.surah_list).setVisibility(View.VISIBLE);
                    findViewById(R.id.topics_list).setVisibility(View.GONE);
                    return true;

                case R.id.navigation_topics:
                    findViewById(R.id.camera_button).setVisibility(View.GONE);
                    findViewById(R.id.surah_list).setVisibility(View.GONE);
                    findViewById(R.id.topics_list).setVisibility(View.VISIBLE);
                    return true;

                case R.id.navigation_search:
                    findViewById(R.id.camera_button).setVisibility(View.VISIBLE);
                    findViewById(R.id.surah_list).setVisibility(View.GONE);
                    findViewById(R.id.topics_list).setVisibility(View.GONE);
                    return true;

                case R.id.navigation_settings:
                    findViewById(R.id.camera_button).setVisibility(View.GONE);
                    findViewById(R.id.surah_list).setVisibility(View.GONE);
                    findViewById(R.id.topics_list).setVisibility(View.GONE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        findViewById(R.id.camera_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        // Load Quran
        Quran mQuran = Quran.getInstance(this);

        // Display Chapter Names
        ListView mSurahList = findViewById(R.id.surah_list);
        final ArrayList<String> list = new ArrayList<String>();
        for (SurahMeta meta : mQuran.getMetadata()) {
            list.add(meta.getTitle());
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        mSurahList.setAdapter(adapter);
        mSurahList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                // Open chapter on click
                Intent mSurahIntent = new Intent(Home.this, SurahActivity.class);
                mSurahIntent.putExtra("surahIndex", position + 1);
                startActivity(mSurahIntent);
            }
        });


        ListView mTopicList = findViewById(R.id.topics_list);
        String[] topics = new String[]{"Namaz", "Hajj"};

        final ArrayList<String> topicsList = new ArrayList<String>();
        for (int i = 0; i < topics.length; ++i) {
            topicsList.add(topics[i]);
        }
        final StableArrayAdapter tpicsAdapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, topicsList);
        mTopicList.setAdapter(tpicsAdapter);
        mTopicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }

        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //mImageView.setImageBitmap(imageBitmap);
        }
    }


}
