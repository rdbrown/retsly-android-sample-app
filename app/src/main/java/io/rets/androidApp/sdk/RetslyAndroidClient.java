package io.rets.androidApp.sdk;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import io.rets.androidApp.OauthLoginActivity;
import io.rets.sdk.RetslyClient;
import io.rets.sdk.async.Async;
import io.rets.sdk.async.AsyncInvoke;
import io.rets.sdk.async.AsyncListInvoke;
import io.rets.sdk.async.RetslyCallback;
import io.rets.sdk.async.RetslyListCallback;

/**
 * Created by matthewsa on 4/24/15.
 */
public class RetslyAndroidClient extends RetslyClient
{

    public RetslyAndroidClient(){
        super(OauthLoginActivity.OAUTH_TOKEN);
        RetslyClient.RESTLY_URL = "https://rets.io/api/v1/";
        this.setVendor("test_sf");
        this.setAsync(new Async() {
            @Override
            public void excute(final AsyncInvoke asyncInvoke, final RetslyCallback cb) {
                new AsyncTask<Void,Void, Object>() {
                    @Override
                    protected Object doInBackground(Void... s) {
                        try {
                            return asyncInvoke.run();
                        }
                        catch(Exception e){
                            Log.v("e",e.toString());
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Object result) {
                        cb.getData(result);
                    }
                }.execute();
            }

            @Override
            public void excuteList(final AsyncListInvoke asyncInvoke, final RetslyListCallback cb) {
                new AsyncTask<Void,Void, List>() {
                    @Override
                    protected List doInBackground(Void... s) {
                        try {
                            return asyncInvoke.runList();
                        }
                        catch(Exception e){
                            Log.v("e",e.toString());
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(List result) {
                        cb.getDataList(result);
                    }
                }.execute();
            }
        });
    }

}
