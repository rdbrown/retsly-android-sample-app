package io.rets.androidApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import java.util.List;

import io.rets.androidApp.sdk.RetslyAndroidClient;
import io.rets.sdk.async.RetslyCallback;
import io.rets.sdk.resources.Agent;


public class AgentActivity extends ActionBarActivity {

    public static String AGENT_ID_EXTRA  = "AGENT_ID_EXTRA";
    MapView mapView;
    GoogleMap map;
    Agent agent;
    ImageView agentPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);
        String agentID = getIntent().getExtras().getString(AGENT_ID_EXTRA);

        agentPhoto = (ImageView) findViewById(R.id.agent_photo);

        try {


            new RetslyAndroidClient().agents().findByIdAsync(agentID, new RetslyCallback<Agent>() {
                @Override
                public void getData(Agent data) {
                    Log.v("Agent", data.toString());
                    agent = data;
                    ((TextView) findViewById(R.id.agent_name)).setText(agent.getFullName());
                    ((TextView) findViewById(R.id.office_name)).setText(agent.getOfficeName());
                    ((TextView) findViewById(R.id.agent_email)).setText(agent.getEmail());
                    ((Button) findViewById(R.id.call_agent_button)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            makeCall();
                        }
                    });
                    new ImageLoadAsyncTask(new BitmapLoaded() {
                        @Override
                        public void onBitmapLoad(List<Bitmap> result) {
                            if (result.size() > 0) agentPhoto.setImageBitmap(result.get(0));
                        }
                    }).execute(agent.getMediaUrl(0));
                }
            });
        }
        catch(Exception e){

        }
    }



    protected void makeCall() {
        Log.i("Make call", "");

        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + agent.getCellPhone()));

        try {
            startActivity(phoneIntent);
            Log.v("phone","Finished making a call...");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Call failed, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

}
