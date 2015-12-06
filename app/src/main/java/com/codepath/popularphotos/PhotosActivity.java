package com.codepath.popularphotos;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    private static final String URL = "https://api.instagram.com/v1/media/popular?client_id=e05c462ebd86446ea48a5af73769b602";
    private final String TAG = PhotosActivity.class.getSimpleName();
    private ArrayList<InstagramPhoto> photoArray;
    private InstagramPhotoAdapter photoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photoArray = new ArrayList<>();

        ListView listView = (ListView)findViewById(R.id.listView);
        photoAdapter = new InstagramPhotoAdapter(this,photoArray);
        listView.setAdapter(photoAdapter);
        fetchPopularPhotos();



    }
    public void fetchPopularPhotos(){

       // Start HTTP async client to get the pictures from instagram

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(URL, null,new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                /*type: data=>[x]=>type (image or video)
                  url: data =>[x]=>images=>standard_resolution=>url
                  caption: data=>[x]=>caption=>text */

                JSONArray photoJSON = null;

                try {
                    photoJSON = response.getJSONArray("data");
                    for(int i=0 ;i < photoJSON.length();i++) {
                        InstagramPhoto photo = new InstagramPhoto();

                        JSONObject objectJSON = photoJSON.getJSONObject(i);
                        photo.username = objectJSON.getJSONObject("user").getString("username");
                        photo.contentType=objectJSON.getString("type");

                        if (objectJSON.getJSONObject("caption") == null) {
                            photo.caption = "";
                        } else {
                            photo.caption = objectJSON.getJSONObject("caption").getString("text");
                        }

                        photo.imageurl= objectJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.userProfileUrl= objectJSON.getJSONObject("user").getString("profile_picture");
                        photo.imagewidth = objectJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("width");
                        photo.imageheight = objectJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likeCount = objectJSON.getJSONObject("likes").getInt("count");
                        photoArray.add(photo);
                        Log.i(TAG,"username="+photo.username+" "+"content type = "+photo.contentType+" caption="+photo.caption+" url="+photo.imageurl+"user profile url = "+photo.userProfileUrl+"like count ="+photo.likeCount);

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
                photoAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
