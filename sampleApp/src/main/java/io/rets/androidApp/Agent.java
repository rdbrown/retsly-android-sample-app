package io.rets.androidApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by matthewsa on 4/24/15.
 */
public class Agent {

    private JSONObject json;
    private JSONArray media;

    public Agent(JSONObject o) {
        this.init(o);
    }

    public Agent(String json){
        try {
            this.init(new JSONObject(json));

        } catch (JSONException e){
        }
    }

    public void init(JSONObject o){
        this.json = o;
        try {
            this.media = this.json.getJSONArray("media");

        } catch (JSONException e){
        }

    }
    public String getFullName(){
        return getSomething("fullName");
    }
    public String getOfficeName(){
        return getSomething("officeName");
    }
    public String getEmail(){
        return getSomething("email");
    }
    public String getPhone(){
        String phone = getSomething("cellPhone");
        return phone.replace(".","-").substring(phone.indexOf(' '));
    }
    public String getImageUrl(){
        try {
            return this.media.length() > 0 ? this.media.getJSONObject(0).getString("url") : "";
        }
        catch (JSONException e){
            return "";
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
    @Override
    public String toString(){
        return json.toString();
    }
}
