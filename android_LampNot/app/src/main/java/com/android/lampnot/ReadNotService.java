package com.android.lampnot;

import android.content.SharedPreferences;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.Map;

/**
 * Created by Luis on 7/20/2017.
 */

public class ReadNotService extends NotificationListenerService {

    SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        String pack = sbn.getPackageName();
        Log.i("PackagePostedNot",pack);
        sp = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);


        Map<String, ?> data = sp.getAll();

        if(data.containsKey(pack)) {
            String color = (String) data.get(pack);


            switch (color) {
                case "Red":
                    new SendNot(getApplicationContext()).execute(11);
                    break;
                case "Green":
                    new SendNot(getApplicationContext()).execute(21);
                    break;
                case "Blue":
                    new SendNot(getApplicationContext()).execute(31);
                    break;
                default:
                    break;
                }

        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);

        String pack = sbn.getPackageName();
        Log.i("PackagePostedNot",pack);
        sp = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);


        Map<String, ?> data = sp.getAll();

        if(data.containsKey(pack)) {
            String color = (String) data.get(pack);


            switch (color) {
                case "Red":
                    new SendNot(getApplicationContext()).execute(10);
                    break;
                case "Green":
                    new SendNot(getApplicationContext()).execute(20);
                    break;
                case "Blue":
                    new SendNot(getApplicationContext()).execute(30);
                    break;
                default:
                    break;
            }

        }
    }
}
