package com.android.lampnot;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Luis on 7/22/2017.
 */

public class AppInfoLab {
    private SharedPreferences sp;
    private List<AppInfoModel> mList;

    public AppInfoLab(Context context){
        sp = context.getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        mList = new ArrayList<>();
        setList(context);
    }

    private void setList(Context context) {

        AppInfoModel app_data;
        String app_name;
        String package_name;
        Drawable icon;
        String color;
        ApplicationInfo app;

        Map<String, ?> data = sp.getAll();
        PackageManager pm = context.getPackageManager();

        for(String pack : data.keySet()){
            try {
                app_data = new AppInfoModel();
                app = pm.getApplicationInfo(pack, 0);

                package_name = pack;
                color = data.get(pack).toString();
                app_name = pm.getApplicationLabel(app).toString();
                icon = pm.getApplicationIcon(pack);

                if(color.equals("Red") || color.equals("Green") || color.equals("Blue") ) {
                    app_data.setAppName(app_name);
                    app_data.setAppPackage(package_name);
                    app_data.setColor(color);
                    app_data.setIcon(icon);

                    mList.add(app_data);
                }
            }
            catch (Exception e) {
                Log.d("setList", e.toString());
            }
        }
    }

    public List<AppInfoModel> getList() {
        return mList;
    }
}
