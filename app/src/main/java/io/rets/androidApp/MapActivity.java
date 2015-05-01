package io.rets.androidApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.message.BasicNameValuePair;

import java.util.List;

import io.rets.androidApp.sdk.RetslyAndroidClient;
import io.rets.sdk.async.RetslyCallback;
import io.rets.sdk.async.RetslyListCallback;
import io.rets.sdk.resources.Listing;
import io.rets.sdk.resources.Vendor;


public class MapActivity extends ActionBarActivity {

    private MapView mapView;
    GoogleMap map;
    private RetslyAndroidClient retsly;
    private TextView vendorText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapsInitializer.initialize(this);
         vendorText = ((TextView) findViewById(R.id.vendor_text));
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) findViewById(R.id.big_map_view);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        MapsInitializer.initialize(this);
        retsly = new RetslyAndroidClient();
        loadListings();
    }

    public void loadListings(){
        final Activity self = this;
        try {
            retsly.vendors().findByIdAsync("test_sf", new RetslyCallback<Vendor>() {
                @Override
                public void getData(Vendor vendor) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(vendor.getCenterLatitude(), vendor.getCenterLongitude()))
                            .zoom(12)                   // Sets the zoom
                            .build();                   // Creates a CameraPosition from the builder


                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    //vendorText.setText(vendor.getName());

                }
            });

            retsly.listings()
            .where(new BasicNameValuePair("sortBy", "lastModified"))
            .limit(20)
            .findAllAysnc(new RetslyListCallback<Listing>() {
                @Override
                public void getDataList(List<Listing> data) {
                    for (Listing l : data) {
                        Marker marker = map.addMarker(new MarkerOptions()
                                .position(new LatLng(l.getLatitude(), l.getLongitude()))
                                .title(l.getAddress())
                                .snippet("Home"));
                    }
                    map.setOnMarkerClickListener(new MarkerClickListener(self, data));

                }
            });

        }
        catch (Exception e){

            Log.e("Error", e.getMessage());
            Log.e("Error", e.getStackTrace().toString());
        }
    }

    @Override
    protected void onPostResume(){
        super.onPostResume();
        loadListings();
    }

    private class MarkerClickListener implements GoogleMap.OnMarkerClickListener{
        List<Listing> listings;
        Activity activity ;
        public MarkerClickListener(Activity parent,List<Listing>  listings){
            this.listings = listings;
            this.activity = parent;
        }
        @Override
        public boolean onMarkerClick(Marker marker) {
            for(Listing l : listings){
                if(l.getAddress().equals(marker.getTitle())){
                    Intent intent = new Intent(this.activity, ListingActivity.class);
                    intent.putExtra(ListingActivity.LISTING_JSON_EXTRA, l.getJSON());
                    this.activity.startActivity(intent);
                }
            }
            return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);


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
