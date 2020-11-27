package com.example.flickrbrowserapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetFlickrJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetFlickrJsonData";
    private List<Photo> mPhotoList = null;

    private String mBaseUrl;
    private String mLanguage;
    private boolean mMatchAll;

    private final OnDataAvailable mCallback;
    private boolean runInSameThread=false;

    interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    public GetFlickrJsonData(OnDataAvailable callback, String baseUrl, String language, boolean matchAll) {
        mCallback = callback;
        mBaseUrl = baseUrl;
        mLanguage = language;
        mMatchAll = matchAll;

    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute: starts");
        if (mCallback != null) {
            mCallback.onDataAvailable(mPhotoList, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    protected List<Photo> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: starts");
        Log.d(TAG, "doInBackground: string[0] is "+strings[0]);
        String destinationUri = createUri(strings[0], mLanguage, mMatchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);

        Log.d(TAG, "doInBackground: ends");
        return mPhotoList;
    }

    void executeOnSameThread(String searchCriteria) {
        Log.d(TAG, "executeOnSameThread: starts here");
        runInSameThread=true;

        String destinationUri = createUri(searchCriteria, mLanguage, mMatchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);

        Log.d(TAG, "executeOnSameThread: end");
    }

    private String createUri(String searchCriteria, String lang, boolean match) {
        Log.d(TAG, "createUri: starts");

        return Uri.parse(mBaseUrl).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("lang", lang)
                .appendQueryParameter("tagmode", match ? "ALL" : "ANY")
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete starts " + status);
        if (status == DownloadStatus.OK) {
            mPhotoList = new ArrayList<>();
            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");
                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String author_id = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String mediaUrl = jsonMedia.getString("m");
                    String link = mediaUrl.replaceFirst("_m.", "_b.");

                    Photo photoObject = new Photo(title, author, author_id, link, tags, mediaUrl);
                    mPhotoList.add(photoObject);
                    Log.d(TAG, "onDownloadComplete: " + photoObject.toString());
                }
            } catch (JSONException j) {
                j.printStackTrace();
                Log.e(TAG, "onDownloadComplete: " + j.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }
        if (runInSameThread && mCallback != null) {
            mCallback.onDataAvailable(mPhotoList, status);
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }
}