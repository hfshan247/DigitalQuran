package com.example.hussainfarooq.digitalquran;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by suhasbachewar on 10/5/16.
 */
public class OCRAsyncTask extends AsyncTask {

    //Javascript engine
//    private static ScriptEngine engine = new ScriptEngineManager()
//            .getEngineByName("JavaScript");
    private ProgressDialog getmProgressDialog;

    private static final String TAG = OCRAsyncTask.class.getName();

    String url = "https://api.ocr.space/parse/image"; // OCR API Endpoints
    String URLRequest;
    private String mApiKey;
    private boolean isOverlayRequired = false;
    private String mImageUrl;
    private Bitmap mBitmap;
    private String mLanguage;
    private Activity mActivity;
    private ProgressDialog mProgressDialog;
    private IOCRCallBack mIOCRCallBack;

    public OCRAsyncTask(Activity activity, String apiKey, boolean isOverlayRequired, String imageUrl, String language, IOCRCallBack iOCRCallBack) {
        this.mActivity = activity;
        this.mApiKey = apiKey;
        this.isOverlayRequired = isOverlayRequired;
        this.mImageUrl = imageUrl;
        this.mLanguage = language;
        this.mIOCRCallBack = iOCRCallBack;
    }
    public OCRAsyncTask(Activity activity, String apiKey, boolean isOverlayRequired, Bitmap bitmap, String language, IOCRCallBack iOCRCallBack) {
        this.mActivity = activity;
        this.mApiKey = apiKey;
        this.isOverlayRequired = isOverlayRequired;
        this.mBitmap = bitmap;
        this.mLanguage = language;
        this.mIOCRCallBack = iOCRCallBack;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setTitle("Extracting Text From Image....");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Object[] params) {

        try {
            return sendPost(mApiKey, isOverlayRequired, mBitmap, mLanguage);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String sendPost(String apiKey, boolean isOverlayRequired, String imageUrl, String language) throws Exception {

        URL obj = new URL(url); // OCR API Endpoints
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");


        JSONObject postDataParams = new JSONObject();

        postDataParams.put("apikey", apiKey);//TODO Add your Registered API key
        postDataParams.put("isOverlayRequired", isOverlayRequired);
        postDataParams.put("url", imageUrl);
        postDataParams.put("language", language);


        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(getPostDataString(postDataParams));
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //return result

        return String.valueOf(response);
    }
    private String sendPost(String apiKey, boolean isOverlayRequired, Bitmap bitmap, String language) throws Exception {

        URL obj = new URL(url); // OCR API Endpoints
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");


        JSONObject postDataParams = new JSONObject();

        postDataParams.put("apikey", apiKey);//TODO Add your Registered API key
        postDataParams.put("isOverlayRequired", isOverlayRequired);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            //bitmap.recycle();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);


        // base64Image Format : data:image/<content_type>;base64,<base64_image_content>
        // Example            : data:image/png;base64,XXXXXXXXXXDFSGSDFGSDFGREGCSFGDFSDFGDFGSF==

        String preparedSting = "data";
        preparedSting+=":";
        preparedSting += "image";
        preparedSting += "/";
        preparedSting += "png";
        preparedSting += ";";
        preparedSting +="base64";
        preparedSting +=",";
        preparedSting+=encodedImage;
        URLRequest = preparedSting;

        //postDataParams.put("base64image", preparedSting);


        String input = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAD4AAAAWCAYAAACYPi8fAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAB3RJTUUH4AofExM4z9cKbwAAAAd0RVh0QXV0aG9yAKmuzEgAAAAMdEVYdERlc2NyaXB0aW9uABMJISMAAAAKdEVYdENvcHlyaWdodACsD8w6AAAADnRFWHRDcmVhdGlvbiB0aW1lADX3DwkAAAAJdEVYdFNvZnR3YXJlAF1w/zoAAAALdEVYdERpc2NsYWltZXIAt8C0jwAAAAh0RVh0V2FybmluZwDAG+aHAAAAB3RFWHRTb3VyY2UA9f+D6wAAAAh0RVh0Q29tbWVudAD2zJa/AAAABnRFWHRUaXRsZQCo7tInAAAC5klEQVRYhe2YPZKbQBCFv3X5GIyuQIom2jkAochMTqbCmYkVilJk8g1HmTkA2aDQXEHDPXAAQiCBtK6yasu7+6oUaP66X/frnpGemqZp+ID48tYOvBU+iX80fBI/IU8USimSfH5TlYUoFZJVj3Std+ghtqYzLgQ2nTFWZWy1/bdevAFmpC4QwlKaa+a51ggpH+zW4zFD3MFbCmxpGFPPORiJ503tyUlUWybtJ2FYLVUWosKMPAsHayZUlSfj+fraUjU6Y2yrnRvbhoosVKhB/c42t4VcImzJKOn5ASM9/GtPCFWKkTFFUVAUBbE0pJcOWE1ar7o1OwJh0dvsHNw8QaUGGbdnFLslpTZXpNdaEHd22nMMadie48olAsNhFHVDaUF6Z8/nu7orWV7IPT+Y0eZ+XGusCNhtznP+JkZi2I9SKon7NS7RSkIf3Ipsb0DGnJdEfA/EkAGmtIggGATfRS4F2Jpj7zeYAfPKlFgkQ9dvXGcujuAs9ypjb8abT87UFhAO7mjcx5OMy0U4LGbtHakvsgLgOkPiLtFLwUvk0stXKdajZtsFwhw6tbXB4kKpN+9x35N9JCtTYqdk3nOap/QqVDWTd8XCYZTzvr7XaCuJi4LdSBXgRivkSe4TMod7Dxjf6w5oozYl8xNsfZyeuFLCDNwxwR7HYUBytLaIYNfV92YmEa3azCGflDncfbktcASY/ZbSTskcTiWBrSdugL9RQmfrcNGPazv8gkWwlMNQdlK+gN8yR9fXMoe7xE+Nw96UuR8ECKtZD66LPEkxSFbRq/JN3+xMen41Xj6WXAeBRetB48q2tEss9TDynVqNuZY5wNe77sglQlvEDZnjRrwUDolKUSptx0TAroheJ/Pe2Q0FCSpVtMcIgkBie/I+m11NuL6wEwvWqaE+wtmgjydTzGRDBpp3i9/Nz2/PzfOPX5Oz7/fXWWUorSAIppV6V+r/H3ISlWIAGRfMtZinpvn8z+1D4Q9Qn1zF/oPQJwAAAABJRU5ErkJggg==";
        Log.d(TAG+" Prepared String : ",preparedSting);
        preparedSting = preparedSting.replace("\n", "").replace("\r", "");
        //Log.d(TAG+" Hard Coded : ", input);
        postDataParams.put("base64image", preparedSting);
        postDataParams.put("language", language);

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());

        String request = getPostDataString(postDataParams);
        //Log.d(TAG+" OCR Request : ", getPostDataString(postDataParams));

        //request.replace("%3A", ":");
        //Log.d(TAG+" OCR Request : ", getPostDataString(postDataParams));
        //wr.writeBytes(request);
        wr.writeBytes(request);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //return result


        return String.valueOf(response);
    }
    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        String response = (String) result;
        mIOCRCallBack.getOCRCallBackResult(response);
        //Log.d(TAG+" OCR Reply", response.toString());
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        String fromJson =  result.toString();
        //Log.d(TAG+" From JSON", fromJson);
        return fromJson;
    }

    //Replace Javascript
    public static String formatString(String str){
        str.replace("%20", " ");
        str.replace("%3A", ":");
        str.replace("%2F", "/");
        str.replace("%3B", ";");
        str.replace("%40", "@");
        str.replace("%3C", "<");
        str.replace("%3E", ">");
        str.replace("%3D", "=");
        str.replace("%26", "&");
        str.replace("%25", "%");
        str.replace("%24", "$");
        str.replace("%23", "#");
        str.replace("%2B", "+");
        str.replace("%2C", ",");
        str.replace("%3F", "?");
        return str;
    }
}


