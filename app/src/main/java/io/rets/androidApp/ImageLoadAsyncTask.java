package io.rets.androidApp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewsa on 4/27/15.
*/
public class ImageLoadAsyncTask extends AsyncTask<String, Void, List<Bitmap>> {

    BitmapLoaded bitLoadedCb;
    public ImageLoadAsyncTask(BitmapLoaded b ){
        bitLoadedCb = b;
    }

    @Override
    protected List<Bitmap> doInBackground(String... urls) {

        List<Bitmap> bits = new ArrayList<Bitmap>();
        try{
            for(int i = 0; i < urls.length; i++){
                URL imgUrl = new URL(urls[i]);

                if(imgUrl!=null){
                    bits.add(BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream()));

                }
            }

        }
        catch (Exception e){
            Log.v("err", e.toString());
        }
        return bits;
    }
    @Override
    protected void onPostExecute(List<Bitmap> result) {
        bitLoadedCb.onBitmapLoad(result);
    }

}
