package com.example.hussainfarooq.digitalquran;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.hussainfarooq.digitalquran.model.Ayat;
import com.example.hussainfarooq.digitalquran.model.Quran;
import com.example.hussainfarooq.digitalquran.model.SurahMeta;
import com.example.hussainfarooq.digitalquran.model.Topic;

import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_quran:
                    setTitle("Digital Quran");
                    findViewById(R.id.camera_button).setVisibility(View.GONE);
                    findViewById(R.id.add_topic_button).setVisibility(View.GONE);
                    findViewById(R.id.surah_list).setVisibility(View.VISIBLE);
                    findViewById(R.id.topics_list).setVisibility(View.GONE);
                    findViewById(R.id.search_results).setVisibility(View.GONE);
                    findViewById(R.id.menuSearch).setVisibility(View.GONE);

                    return true;

                case R.id.navigation_topics:
                    // Set Activity Title
                    setTitle("Topics");
                    findViewById(R.id.camera_button).setVisibility(View.GONE);
                    findViewById(R.id.add_topic_button).setVisibility(View.VISIBLE);
                    findViewById(R.id.surah_list).setVisibility(View.GONE);
                    findViewById(R.id.topics_list).setVisibility(View.VISIBLE);
                    findViewById(R.id.search_results).setVisibility(View.GONE);
                    findViewById(R.id.menuSearch).setVisibility(View.GONE);

                    return true;

                case R.id.navigation_search:
                    setTitle("Search Quran");
                    findViewById(R.id.camera_button).setVisibility(View.VISIBLE);
                    findViewById(R.id.add_topic_button).setVisibility(View.GONE);
                    findViewById(R.id.surah_list).setVisibility(View.GONE);
                    findViewById(R.id.topics_list).setVisibility(View.GONE);
                    findViewById(R.id.search_results).setVisibility(View.VISIBLE);
                    findViewById(R.id.menuSearch).setVisibility(View.VISIBLE);
                    return true;

                case R.id.navigation_settings:
                    setTitle("Settings");
                    findViewById(R.id.camera_button).setVisibility(View.GONE);
                    findViewById(R.id.add_topic_button).setVisibility(View.GONE);
                    findViewById(R.id.surah_list).setVisibility(View.GONE);
                    findViewById(R.id.topics_list).setVisibility(View.GONE);
                    findViewById(R.id.search_results).setVisibility(View.GONE);
                    findViewById(R.id.menuSearch).setVisibility(View.GONE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Configure navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        // Set up camera button
        findViewById(R.id.camera_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        // Display Quran data (Surahs and Topics)
        final Quran mQuran = Quran.getInstance(this);

        ListView mSurahListView = findViewById(R.id.surah_list);
        final ArrayList<String> mSurahList = new ArrayList<String>();
        for (SurahMeta mSurahMeta : mQuran.getMetadata()) {
            mSurahList.add(mSurahMeta.getTitle());
        }

        final ArrayAdapter<String> mSurahAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mSurahList);
        mSurahListView.setAdapter(mSurahAdapter);
        mSurahListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent mSurahIntent = new Intent(Home.this, SurahActivity.class);
                mSurahIntent.putExtra("surahIndex", position + 1);
                startActivity(mSurahIntent);
            }
        });


        final ListView mTopicListView = findViewById(R.id.topics_list);
        final ArrayList<String> mTopicsList = new ArrayList<>();
        for (Topic topic : mQuran.getTopics()) {
            mTopicsList.add(topic.getName());
        }

        final ArrayAdapter<String> mTopicAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mTopicsList);
        mTopicListView.setAdapter(mTopicAdapter);
        mTopicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                // TODO: Display aya in this topic
            }

        });

        // Topics
        findViewById(R.id.add_topic_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputTopic = new EditText(Home.this);
                inputTopic.setInputType(InputType.TYPE_CLASS_TEXT);

                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this)
                        .setTitle("Add Topic")
                        .setView(inputTopic)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Home.this, "Topic Added : Cancelled by User", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                //Add Topic
                                String topic = inputTopic.getText().toString().trim();
                                if (!topic.isEmpty()) {
                                    boolean exists = false;
                                    for (Topic t : mQuran.getTopics()) {
                                        if (t.getName().equalsIgnoreCase(topic)) {
                                            exists = true;
                                            break;
                                        }
                                    }

                                    if (!exists) {
                                        mTopicAdapter.add(topic);
                                        mQuran.addTopic(Home.this, topic);
                                    } else {
                                        Toast.makeText(Home.this, "Topic \'" + topic + "\' already exists!", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(Home.this, "Name cannot be empty!", Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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

    //Search


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ListView searchResultsView = findViewById(R.id.search_results);
                List<Ayat> ayats = Quran.getInstance(Home.this).search(s);
                AyatAdapter adapter = new AyatAdapter(ayats, Home.this);
                searchResultsView.setAdapter(adapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
