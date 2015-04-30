package io.rets.androidApp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewsa on 4/24/15.
 */
public class Retsly
{

    public String url = "https://10.0.2.2/api/v1/";

    private RetslyListCallback<Listing> listingCallback;
    private RetslyCallback<Agent> agentCallback;

    private List<NameValuePair> query;

    public String vendorID = "test_sf";

    public Retsly(){
    }

    public Retsly setQuery(List<NameValuePair> q){
        query = q;
        return this;
    }
    //getListing()
    /*new Retsly.getListings(). ->retslyQuery<Listing>
        where()
        limit()
        offset()
        .exec()*/


    public void queryListings(RetslyListCallback<Listing> callback){
        this.listingCallback = callback;
        new ListingHttpAsyncTask().execute(url + "listing/" + vendorID + ".json");
    }

    public void queryAgent(String agentId, RetslyCallback<Agent> callback){
        this.agentCallback = callback;
        new AgentHttpObj().execute(url + "agent/" + vendorID + "/" + agentId + ".json");
    }

    public void queryMe(final RetslyCallback<RetslyUser> callback){
        final RetslyCallback<RetslyUser> userMeCallback = callback;

        class UserMeHttpObj extends HttpAsyncObjTask {
            @Override
            protected void onPostExecute(JSONObject result) {
                try {
                    if(userMeCallback!=null) userMeCallback.getData(new RetslyUser(result));
                }
                catch (Exception e){

                }
            }
        };
        new UserMeHttpObj().execute(url + "user/me");
    }

    public void queryVendor(final RetslyCallback<RetslyVendor> callback){
        final RetslyCallback<RetslyVendor> userMeCallback = callback;

        class UserMeHttpObj extends HttpAsyncObjTask {
            @Override
            protected void onPostExecute(JSONObject result) {
                try {
                    if(userMeCallback!=null) userMeCallback.getData(new RetslyVendor(result));
                }
                catch (Exception e){

                }
            }
        };
        new UserMeHttpObj().execute(url + "vendor/" + vendorID);
    }



    protected void agentReceived(JSONObject jagent){
        try {
            if(agentCallback!=null) agentCallback.getData(new Agent(jagent));
        }
        catch (Exception e){}
    }

    protected void listingsReceived(JSONArray jlistings){
        try {
            List<Listing> listings = new ArrayList<Listing>();
            for (int i = 0; i < jlistings.length(); i++) {
                listings.add(new Listing(jlistings.getJSONObject(i)));
            }
            if(listingCallback!=null) listingCallback.getDataList(listings);
        }
        catch (Exception e){}
    }

    private class ListingHttpAsyncTask extends HttpAsyncArrayTask {
        @Override
        protected void onPostExecute(JSONArray result) {
            listingsReceived(result);
            //etResponse.setText(result);
        }
    }

    private class AgentHttpObj extends HttpAsyncObjTask {
        @Override
        protected void onPostExecute(JSONObject result) {
            agentReceived(result);
            //etResponse.setText(result);
        }
    }


    private class HttpAsyncArrayTask extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... urls) {

            InputStream inputStream = null;
            String result = "";
            try {
                String url = urls[0];
                // create HttpClient
                HttpClient httpclient = HackHttpClient.getNewHttpClient();

                // make GET request to the given URL
                url = url + "?access_token="+OauthLoginActivity.OAUTH_CODE;
                if(query != null) url = url + "&" + URLEncodedUtils.format(query, "utf-8");
                Log.v("req", url);
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

                // receive response as inputStream
                String res = EntityUtils.toString(httpResponse.getEntity());
                //Convert the string result to a JSON Object
                JSONObject resultJson = new JSONObject(res);
                JSONArray jlistings = resultJson.getJSONArray("bundle");
                return jlistings;

            } catch (Exception e) {
                Log.d("Retsly Request Error", e.getLocalizedMessage());
            }

            return new JSONArray();
        }

    }

    private class HttpAsyncObjTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... urls) {

            InputStream inputStream = null;
            String result = "";
            try {

                String url = urls[0];
                // create HttpClient
                HttpClient httpclient = HackHttpClient.getNewHttpClient();

                // make GET request to the given URL
                url = url + "?access_token="+OauthLoginActivity.OAUTH_CODE;
                if(query != null) url = url + "&" + URLEncodedUtils.format(query, "utf-8");
                Log.v("req", url);
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

                // receive response as inputStream
                String res = EntityUtils.toString(httpResponse.getEntity());
                //Convert the string result to a JSON Object
                JSONObject resultJson = new JSONObject(res);
                JSONObject jlistings = resultJson.getJSONObject("bundle");
                return jlistings;

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            return null;
        }

    }



}
