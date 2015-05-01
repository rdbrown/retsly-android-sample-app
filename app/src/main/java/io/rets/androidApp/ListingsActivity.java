package io.rets.androidApp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.rets.androidApp.sdk.RetslyAndroidClient;
import io.rets.sdk.async.RetslyListCallback;
import io.rets.sdk.resources.Listing;


public class ListingsActivity extends ActionBarActivity {

    RetslyAndroidClient retsly;
    Date lastTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);
        retsly = new RetslyAndroidClient();
        //lastTime = new Date();
    }

    @Override
    protected  void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        loadListings();
    }

    @Override
    protected void onPostResume(){
        super.onPostResume();
        loadListings();
    }

    public void loadListings(){
        List<NameValuePair> q = new ArrayList<NameValuePair>();
        if(lastTime != null) {
            q.add(new BasicNameValuePair("listDate[gt]", lastTime.toString()));
            lastTime = new Date();
        }
        q.add(new BasicNameValuePair("sortBy", "lastModified"));
        q.add(new BasicNameValuePair("limit", "20"));
        try{
            retsly.listings().findAllAysnc(new RetslyListCallback<Listing>() {
                @Override
                public void getDataList(List<Listing> data) {
                    addListings(data);
                }
            });
        }
        catch (Exception e){

        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
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

    public void addListings(List<Listing> listings){
        Log.v("listings",listings.toString());
        LinearLayout ll = (LinearLayout)findViewById(R.id.Linear_Layout);
        for(Listing l : listings){
            ll.addView(new ListingView(l,this));
        }

    }

}
