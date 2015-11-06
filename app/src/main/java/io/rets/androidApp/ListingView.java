package io.rets.androidApp;

/**
 * Created by matthewsa on 4/23/15.
 */
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.net.URL;

import io.rets.sdk.resources.Listing;


public class ListingView extends RelativeLayout {
        private TextView header;
        private TextView description;
        private ImageView thumbnail;
        private Button button;
        private Listing listing;
        private GestureDetectorCompat mDetector;
        public ListingView(Listing l, Context context) {
            super(context);
            this.listing = l;
            inflate(getContext(), R.layout.listing_view, this);
            this.description = (TextView)findViewById(R.id.description);
            this.header = (TextView)findViewById(R.id.title);
            //this.thumbnail = (ImageView)findViewById(R.id.imageView);
            this.header.setText(Double.toString(l.getPrice()));

            this.description.setText(l.getAddress());

            new HttpAsyncTask(this).execute(l.getMediaUrl(0));
            this.setOnTouchListener(new OnTouchListener() {
                private float lastDownX;

                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Log.v("touch ", event.getX() + "DOWN");

                        lastDownX = event.getX();
                    } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_OUTSIDE || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        if (lastDownX>0) {
                            float diff = event.getX() - lastDownX;
                            Log.v("touch diff", event.getX() + " _ " +  lastDownX+ "_" + Float.toString(diff));
                            if (diff > 100)
                                startListAcitivity();
                        }
                        lastDownX = (float)-1.0;
                        return false;

                    }
                    return true;
                }
            });
        }



        private void startListAcitivity(){

            Intent intent = new Intent(this.getContext(), ListingActivity.class);
            intent.putExtra(ListingActivity.LISTING_JSON_EXTRA, this.listing.getJSON());
            this.getContext().startActivity(intent);
        }

        private class HttpAsyncTask extends AsyncTask<String, Void, String> {
            private ListingView  l;

            public  HttpAsyncTask(ListingView l){
                this.l = l;
            }
            @Override
            protected String doInBackground(String... urls) {


                try{
                    URL imgUrl = new URL(urls[0]);

                    if(imgUrl!=null){
                        //l.thumbnail.setImageBitmap(
                        //        BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream())
                        //);
                    }
                }
                catch (Exception e){
                    Log.v("err", e.toString());
                }

                return "yay";
            }
        }

    }
