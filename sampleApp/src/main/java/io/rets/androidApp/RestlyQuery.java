package io.rets.androidApp;

import android.util.Log;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewsa on 4/28/15.
 */
public abstract class RestlyQuery<T> {

    private String url;
    private String retslyURL = "https://10.0.2.2/api/v1/";
    private String vendorID = "test_sf";
    private String OAUTH_CODE = "e61c7144a5b054a67ae25bac8e0939d8";
    public static String LIMIT_ARGUMENT = "limit";
    public static String OFFSET_ARGUMENT = "offset";

    private List<NameValuePair> arguments;

    public RestlyQuery( String url){
        this.url = url;
        arguments = new ArrayList<NameValuePair>();
    }

    public RestlyQuery<T> where(NameValuePair nv){
        arguments.add(nv);
        return this;
    }
    //where(listingQuery.)

    //near

    //sortBy(ListingQuery.bedrooms)

    //where(ListingQuery.bedrooms.greaterThan(3))

    public RestlyQuery<T> limit(int limit){
        arguments.add(new BasicNameValuePair(LIMIT_ARGUMENT,Integer.toString(limit)));
        return this;
    }

    public RestlyQuery<T> offset(int offset){
        arguments.add(new BasicNameValuePair(OFFSET_ARGUMENT,Integer.toString(offset)));
        return this;
    }

    protected JSONArray executeQuery() throws IOException,JSONException, HttpException {
            // create HttpClient
        HttpClient httpclient = HackHttpClient.getNewHttpClient();
        String request = retslyURL + this.url + "/" + vendorID + ".json";
        // make GET request to the given URL
        request = request + "?access_token="+ OAUTH_CODE;
        if(arguments != null) request = request + "&" + URLEncodedUtils.format(arguments, "utf-8");
        Log.v("req", request);
        HttpResponse httpResponse = httpclient.execute(new HttpGet(request));

        // receive response as inputStream
        String res = EntityUtils.toString(httpResponse.getEntity());
        //Convert the string result to a JSON Object
        JSONObject resultJson = new JSONObject(res);
        if(resultJson.has("status") && resultJson.getBoolean("success") == true)
        {
            Log.v("resp","SUCESS BUNDLE");
            return resultJson.getJSONArray("bundle");
        }
        else{
            Log.v("resp", "FAIL BUNDLE");

            throw new HttpException("Server responded with ");
        }
    }
    public abstract List<T> exec() throws IOException,JSONException, HttpException;
}
