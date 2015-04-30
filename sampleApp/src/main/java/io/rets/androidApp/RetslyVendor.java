package io.rets.androidApp;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by matthewsa on 4/28/15.
 */
public class RetslyVendor {

    private JSONObject json;
    private JSONArray center;

    public RetslyVendor(JSONObject o) {
        this.init(o);
    }

    public RetslyVendor(String json){
        try {
            this.init(new JSONObject(json));

        } catch (JSONException e){
        }
    }

    public void init(JSONObject o){
        this.json = o;
        try {
            this.center = o.getJSONArray("center");

        } catch (JSONException e){
        }
    }

    public LatLng getCenter(){
        LatLng c = null;
        try {
            c = new LatLng(center.getDouble(1), center.getDouble(0));
        }
        catch (JSONException e){
        }
        return c;
    }
    public String getName(){
        return getSomething("name");
    }

    private String getSomething(String str){
        try{
            return json.getString(str);
        }
        catch (JSONException e){
            return "Error";
        }
    }
    @Override
    public String toString(){
        return json.toString();
    }



}
