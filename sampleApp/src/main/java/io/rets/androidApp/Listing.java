package io.rets.androidApp;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewsa on 4/23/15.
 */
public class Listing {
    private JSONObject json;
    private JSONArray media;
    private LatLng coordinates;
    public Listing(JSONObject o) {
        this.init(o);
    }

    public Listing(String json){
        try {
            this.init(new JSONObject(json));

        } catch (JSONException e){
        }
    }

    public void init(JSONObject o){
        this.json = o;
        try {
            this.media = this.json.getJSONArray("media");
            JSONArray coords = this.json.getJSONArray("coordinates");
            double lng = coords.getDouble(0);
            double lat = coords.getDouble(1);
            Log.v("coords",lng + "-" + lat);
            this.coordinates = new LatLng(lat,lng);

        } catch (JSONException e){
        }

    }

    private String getSomething(String str){
        try{
            return json.getString(str);
        }
        catch (JSONException e){
            return "Error";
        }
    }
    public String getAddress(){
        return getSomething("address");
    }
    public String getYearBuilt(){
        return getSomething("yearBuilt");
    }

    public String getBeds(){
        return getSomething("bedrooms");
    }
    public String getBaths(){
        return getSomething("halfBaths");
    }
    public String getAgentID(){
        return getSomething("agentID");
    }


    public String getPrice(){
        return getSomething("price") + "";
    }
    public String getImageUrl() {
        return getImageUrl(0);
    }
    public LatLng getLatLng(){
        return this.coordinates;
    }
    public String getImageUrl(int index) {
        try {
            index = index > this.media.length() ? this.media.length() -1 : index;
            return this.media.getJSONObject(index).getString("url");
        } catch (Exception e) {
            Log.e("ERRR","url bad img");
        }
        return null;
    }
    public List<String> getImageUrls(){
        ArrayList<String> urls = new ArrayList<String>();
        for(int i = 0; i < this.media.length(); i++){
            urls.add(this.getImageUrl(i));
        }
        return urls;
    }
    public String[] getImageUrlsArray(){
        return this.getImageUrls().toArray(new String[this.media.length()]);
    }
    public String getJSON(){
        return json.toString();
    }
}
