package com.android.lampnot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by Luis on 7/20/2017.
 */

public class SendNot extends AsyncTask<Integer, Void, Void> {

    private Context mContext;

    public SendNot(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Integer... params) {

        SharedPreferences sp = mContext.getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        String token = sp.getString("Token", "NONE");
        String device = sp.getString("Device", "NONE");
        String variable = sp.getString("Variable", "NONE");

        try {
            String data = params[0].toString();
            URL ubiEndPoint =
                    new URL("http://things.ubidots.com/api/v1.6/devices/" + device +
                            "/?token=" + token);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(ubiEndPoint.toString());
            JSONObject obj = new JSONObject();
            obj.put(variable,data);
            StringEntity se = new StringEntity( obj.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            client.execute(post);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("ERROR IN Class SendNot", e.toString());
        }

        return null;
    }
}
