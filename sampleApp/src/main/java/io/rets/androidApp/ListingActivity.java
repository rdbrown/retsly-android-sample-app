package io.rets.androidApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class ListingActivity extends ActionBarActivity {

    public static String LISTING_JSON_EXTRA  = "LISTING_JSON_EXTRA";
    private Listing listing;
    MapView mapView;
    GoogleMap map;
    LinearLayout listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        String json = getIntent().getExtras().getString(LISTING_JSON_EXTRA);
        this.listing = new Listing(json);

        ((TextView)findViewById(R.id.list_address)).setText(this.listing.getAddress());
        ((TextView)findViewById(R.id.list_price)).setText(this.listing.getPrice());
        ((TextView)findViewById(R.id.year_built)).setText(this.listing.getYearBuilt());

        ((TextView)findViewById(R.id.beds)).setText(this.listing.getBeds());
        ((TextView)findViewById(R.id.baths)).setText(this.listing.getBaths());


        MapsInitializer.initialize(this);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        Log.v("latln",listing.getLatLng().toString());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(listing.getLatLng())
                .zoom(12)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        Marker marker = map.addMarker(new MarkerOptions()
                .position(listing.getLatLng())
                .title(listing.getAddress())
                .snippet("Home"));

        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this);

        listView = (LinearLayout)findViewById(R.id.photo_list);

        new ImageLoadAsyncTask(new BitmapLoaded(){
            @Override
            public void onBitmapLoad(List<Bitmap> result){

                for (Bitmap b : result) {
                    ImageView imageView = new ImageView(listView.getContext());
                    imageView.setImageBitmap(b);
                    listView.addView(imageView);
                }
            }

        }).execute(this.listing.getImageUrlsArray());

        Button agentButton = (Button) findViewById(R.id.agent_button);
        agentButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Intent intent = new Intent(v.getContext(), AgentActivity.class);
                    intent.putExtra(AgentActivity.AGENT_ID_EXTRA, listing.getAgentID());
                    v.getContext().startActivity(intent);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
