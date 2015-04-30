package io.rets.androidApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by matthewsa on 4/27/15.
 */
public class RetslyUser {


    private JSONObject json;
    private JSONArray media;

    public RetslyUser(JSONObject o) {
        this.init(o);
    }

    public RetslyUser(String json){
        try {
            this.init(new JSONObject(json));

        } catch (JSONException e){
        }
    }

    public void init(JSONObject o){
        this.json = o;
    }

    public String getEmail(){
        return getSomething("email");
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
