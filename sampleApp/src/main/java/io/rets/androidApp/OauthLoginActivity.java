package io.rets.androidApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OauthLoginActivity extends Activity {

/*CONSTANT FOR THE AUTHORIZATION PROCESS*/

    /****FILL THIS WITH YOUR INFORMATION*********/
    private static final String API_KEY = "rhUL0Ou5VPPwDUvf9z0n"; //clientID
    private static final String REDIRECT_URI = "http://testapproval.com";
    /*********************************************/

//These are constants used for build the urls
    private static final String AUTHORIZATION_URL = "https://10.0.2.2/oauth/authorize";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String RESPONSE_TYPE_VALUE ="code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    /*---------------------------------------*/
    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";

    private WebView webView;
    private ProgressDialog pd;

    public static String OAUTH_CODE = "";
    public static String OAUTH_TOKEN = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the webView from the layout
        webView = (WebView) findViewById(R.id.main_activity_web_view);

        //Request focus for the webview
        webView.requestFocus(View.FOCUS_DOWN);

        final Activity thisAct = this;
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {

                if(authorizationUrl.startsWith(REDIRECT_URI)){
                    Uri uri = Uri.parse(authorizationUrl);
                    String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                    Log.v("success token:", authorizationToken);
                    OauthLoginActivity.OAUTH_CODE = authorizationToken;

                    new HttpAsyncTask(thisAct).execute();
                    return true;
                }
                return false;
            }
        });

        //Get the authorization Url
        String authUrl = getAuthorizationUrl();
        //Load the authorization URL into the webView
        webView.loadUrl(authUrl);

    }

    /**
     * Method that generates the url for get the authorization token from the Service
     * @return Url
     */
    private static String getAuthorizationUrl(){
        return AUTHORIZATION_URL
                +QUESTION_MARK+RESPONSE_TYPE_PARAM+EQUALS+RESPONSE_TYPE_VALUE
                +AMPERSAND+CLIENT_ID_PARAM+EQUALS+API_KEY
                +AMPERSAND+REDIRECT_URI_PARAM+EQUALS+REDIRECT_URI;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        private Activity thisAct;
        public HttpAsyncTask(Activity thisAct){
            this.thisAct = thisAct;
        }
        @Override
        protected String doInBackground(String... urls) {


            HttpClient httpClient = HackHttpClient.getNewHttpClient();
            HttpPost httppost = new HttpPost("https://10.0.2.2/oauth/token");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair("code", OauthLoginActivity.OAUTH_CODE));
            nameValuePairs.add(new BasicNameValuePair("client_id", API_KEY));
            nameValuePairs.add(new BasicNameValuePair("client_secret", "jL9AVhfqDQc7alDTOOPacbXokZDjTeLzzKJuxGM7"));
            nameValuePairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
            nameValuePairs.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));

            JSONObject resultJson = null;
            try{
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpClient.execute(httppost);
                Log.v("response ?","res");

                if(response!=null){
                    //If status is OK 200
                    if(response.getStatusLine().getStatusCode()==200){
                        String result = EntityUtils.toString(response.getEntity());
                        //Convert the string result to a JSON Object
                        resultJson = new JSONObject(result);
                        //Extract data from JSON Response

                        OauthLoginActivity.OAUTH_TOKEN = resultJson.has("access_token") ? resultJson.getString("access_token") : null;
                        this.thisAct.finish();

                        Intent intent = new Intent(this.thisAct, StartActivity.class);
                        startActivity(intent);

                    }
                }
            }catch(IOException e){
                Log.e("Authorize","Error Http response "+e.getLocalizedMessage());
            }
            catch (JSONException e) {
                Log.e("Authorize","Error Parsing Http response "+e.getLocalizedMessage());
            }
            return resultJson.toString();

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.v("YAY",result);
            //etResponse.setText(result);
        }
    }


}