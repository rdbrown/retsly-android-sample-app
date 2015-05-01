package io.rets.androidApp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Date;
import java.util.TimerTask;

/**
 * Created by matthewsa on 4/27/15.
 */
public class RetslyTask extends TimerTask{

    Context context;
    Date lastTime;
    public RetslyTask(Context context){
        this.context = context;
        lastTime = new Date();
    }

    @Override
    public void run(){

        /*List<NameValuePair> q = new ArrayList<NameValuePair>();
        q.add(new BasicNameValuePair("listDate[gt]", lastTime.toString()));
        new RetslyAndroidClient().setQuery(q).queryListings(new RetslyListCallback<Listing>() {
            @Override
            public void getDataList(List<Listing> data) {
                if(data.size() > 0) {
                    sendNotification(data.size() + " new Listings available");
                    lastTime = new Date();
                }
            }
        });*/


    }

    public void sendNotification(String content){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.abc_btn_check_material)
                        .setContentTitle("Retsly Notification")
                        .setContentText(content);

        Intent resultIntent = new Intent(context, ListingsActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = new Double(Math.random() * Integer.MAX_VALUE).intValue();
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
