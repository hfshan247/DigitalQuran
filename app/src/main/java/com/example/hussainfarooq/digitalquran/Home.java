package com.example.hussainfarooq.digitalquran;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hussainfarooq.digitalquran.OCR.ParsedResults;
import com.example.hussainfarooq.digitalquran.OCR.Rootobject;
import com.example.hussainfarooq.digitalquran.model.Ayat;
import com.example.hussainfarooq.digitalquran.model.Quran;
import com.example.hussainfarooq.digitalquran.model.SurahMeta;
import com.example.hussainfarooq.digitalquran.model.Topic;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity implements IOCRCallBack {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    //Gallery
    private static final int PICK_IMAGE = 100;
    // OCR Variables
    private static final int MY_REQUEST_CDE = 1;
    Uri imageURI;
    //Interface Variables;
    Menu mMenu;
    private String mAPiKey = "5b677daef088957"; //TODO Add your own Registered API key
    private boolean isOverlayRequired;
    private String mImageUrl;
    private Bitmap mBitmap;
    private String mLanguage;
    private TextView mTxtResult;
    private IOCRCallBack mIOCRCallBack;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_quran:
                    setTitle("Digital Quran");
                    findViewById(R.id.camera_button).setVisibility(View.GONE);
                    findViewById(R.id.gallery_button).setVisibility(View.GONE);
                    findViewById(R.id.add_topic_button).setVisibility(View.GONE);
                    findViewById(R.id.surah_list).setVisibility(View.VISIBLE);
                    findViewById(R.id.topics_list).setVisibility(View.GONE);
                    findViewById(R.id.search_results).setVisibility(View.GONE);
                    findViewById(R.id.menuSearch).setVisibility(View.GONE);
                    findViewById(R.id.no_results).setVisibility(View.GONE);

                    return true;

                case R.id.navigation_topics:
                    // Set Activity Title
                    setTitle("Topics");
                    findViewById(R.id.camera_button).setVisibility(View.GONE);
                    findViewById(R.id.gallery_button).setVisibility(View.GONE);
                    findViewById(R.id.add_topic_button).setVisibility(View.VISIBLE);
                    findViewById(R.id.surah_list).setVisibility(View.GONE);
                    findViewById(R.id.topics_list).setVisibility(View.VISIBLE);
                    findViewById(R.id.search_results).setVisibility(View.GONE);
                    findViewById(R.id.menuSearch).setVisibility(View.GONE);
                    findViewById(R.id.no_results).setVisibility(View.GONE);

                    return true;

                case R.id.navigation_search:
                    setTitle("Search Quran");
                    findViewById(R.id.camera_button).setVisibility(View.VISIBLE);
                    findViewById(R.id.gallery_button).setVisibility(View.GONE);
                    findViewById(R.id.add_topic_button).setVisibility(View.GONE);
                    findViewById(R.id.surah_list).setVisibility(View.GONE);
                    findViewById(R.id.topics_list).setVisibility(View.GONE);
                    findViewById(R.id.search_results).setVisibility(View.VISIBLE);
                    findViewById(R.id.menuSearch).setVisibility(View.VISIBLE);
                    findViewById(R.id.no_results).setVisibility(View.GONE);
                    return true;

                case R.id.navigation_settings:
                    setTitle("Settings");
                    findViewById(R.id.camera_button).setVisibility(View.GONE);
                    findViewById(R.id.gallery_button).setVisibility(View.GONE);
                    findViewById(R.id.add_topic_button).setVisibility(View.GONE);
                    findViewById(R.id.surah_list).setVisibility(View.GONE);
                    findViewById(R.id.topics_list).setVisibility(View.GONE);
                    findViewById(R.id.search_results).setVisibility(View.GONE);
                    findViewById(R.id.menuSearch).setVisibility(View.GONE);
                    findViewById(R.id.no_results).setVisibility(View.GONE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //OCR Variables
        mIOCRCallBack = this;
        mImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSJi6BCVjr7RmUwVbrwFCcLYCC5CA1XbO7KjuGn6A2eK7Dfb5Sh"; // Image url to apply OCR API
        mLanguage = "ara"; //Language
        isOverlayRequired = true;
        //Get Permissions:
        requestPermissions();

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

        // Gallery button
        findViewById(R.id.gallery_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
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

    // Gallery Intent
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    //Camera Intent
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Camera Request
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            findViewById(R.id.no_results).setVisibility(View.GONE);

            Bundle extras = data.getExtras();
            mBitmap = (Bitmap) extras.get("data");
            if (isNetworkConnected()) {

//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                imageBitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//                imageBitmap.recycle();
//                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                OCRAsyncTask oCRAsyncTask = new OCRAsyncTask(Home.this, mAPiKey, isOverlayRequired, mBitmap, "ara", mIOCRCallBack);
                //OCRAsyncTask oCRAsyncTask = new OCRAsyncTask(Home.this, mAPiKey, isOverlayRequired, mImageUrl, "ara",mIOCRCallBack);
                oCRAsyncTask.execute();
            } else {
                Toast.makeText(Home.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            findViewById(R.id.no_results).setVisibility(View.GONE);


            findViewById(R.id.no_results).setVisibility(View.GONE);

//            imageURI = data.getData();
//            String[] filePath = { MediaStore.Images.Media.DATA };
//            Cursor cursor = getContentResolver().query(imageURI, filePath, null, null, null);
//            cursor.moveToFirst();
//            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
//
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

//
//            try{
//                imageURI = data.getData();
//                 mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURI);
//            }
//            catch (Exception ex){
//                ex.printStackTrace();
//            }

//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Uri imageUri = data.getData();
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

            } catch (IOException e) {
                e.printStackTrace();
            }


            if (isNetworkConnected()) {

//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                imageBitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//                imageBitmap.recycle();
//                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(getResources(), R.id.gallery_button, options);
                int imageHeight = options.outHeight;
                int imageWidth = options.outWidth;
                String imageType = options.outMimeType;

                Bitmap bitmap =  decodeSampledBitmapFromResource(getResources(), R.id.gallery_button, 100, 100);

                OCRAsyncTask oCRAsyncTask = new OCRAsyncTask(Home.this, mAPiKey, isOverlayRequired, bitmap, "ara", mIOCRCallBack);
                //OCRAsyncTask oCRAsyncTask = new OCRAsyncTask(Home.this, mAPiKey, isOverlayRequired, mImageUrl, "ara",mIOCRCallBack);
                oCRAsyncTask.execute();
            } else {
                Toast.makeText(Home.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Decreasing Bitmap size
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    //

    //Search


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);

        final SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                findViewById(R.id.no_results).setVisibility(View.GONE);
                ListView searchResultsView = findViewById(R.id.search_results);
                List<Ayat> ayats = Quran.getInstance(Home.this).search(s);
                AyatAdapter adapter = new AyatAdapter(ayats, Home.this);
                searchResultsView.setAdapter(adapter);
                if (ayats.size() == 0) {
                    findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //Permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CDE) {
            if (grantResults.length == 4) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    // To Do
                } else {
                    //Toast.makeText(this, "Permissions not Granted", Toast.LENGTH_SHORT).show();
                    //finish();
                }
            }
        }
    }

    // Permissions
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                ) {
            // To Do
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, MY_REQUEST_CDE);
        }
    }

    //
    @Override
    public void getOCRCallBackResult(String response) {

        //Result from Server
        String result = "";

        //Toast.makeText(this, "OCR Result :"+response, Toast.LENGTH_LONG).show();
        try {
            Gson gson = new Gson();
            Rootobject rootobject = gson.fromJson(response, Rootobject.class);
            ParsedResults[] parsedresults = rootobject.getParsedResults();

            if (rootobject.getOCRExitCode() == 1 + "") {
                for (int i = 0; i < parsedresults.length; i++) {
                    result += parsedresults[i].getParsedText();
                }
            } else {
                Toast.makeText(this, "An Error Occured Wile Processing", Toast.LENGTH_SHORT).show();
            }
            result = result.replace("\n", "").replace("\r", "");

            //mTxtResult.setText("Result: \n"+result);


            Log.d("OCR Output", result);
            // Preview Search Results:

            //Search Results:

            if (!result.trim().equals("")) {
                ListView searchResultsView = findViewById(R.id.search_results);
                List<Ayat> ayats = Quran.getInstance(Home.this).search(result);
                ;
                if (ayats.size() == 0) {

                    if(ayats.size() == 0){
                        String[] words = result.split("\\s+");
                        for (int i = 0; i < words.length; i++) {
                            // You may want to check for a non-word character before blindly
                            // performing a replacement
                            // It may also be necessary to adjust the character class
                            words[i] = words[i].replaceAll("[^\\w]", "");
                            List<Ayat> ayats_2 = Quran.getInstance(Home.this).search(words[i]);

                            for (Ayat ayat : ayats_2) {
                                if (!ayats.contains(ayat)) {
                                    ayats.add(ayat);
                                }
                                if(ayats.size()>5){
                                    break;
                                }
                            }
                        }
                    }

                    if(ayats.size() == 0)
                    {
                        findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                    }
                }
                AyatAdapter adapter = new AyatAdapter(ayats, Home.this);
                searchResultsView.setAdapter(adapter);
            } else {
                findViewById(R.id.no_results).setVisibility(View.VISIBLE);
            }
            //findViewById(R.id.ocr_input).setVisibility(View.VISIBLE);
            //TextView ocr_input = (TextView) findViewById(R.id.ocr_input);
            //ocr_input.setText(result);


            // TODO Auto-generated method stub
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", result);
            clipboard.setPrimaryClip(clip);

            if (result.trim().equals("")) {
                Toast.makeText(this, "Invalid Input image", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            Toast.makeText(Home.this, "Server Error", Toast.LENGTH_SHORT).show();
        }

    }

    //Check Internet State
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}


