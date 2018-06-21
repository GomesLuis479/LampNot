package com.android.lampnot;

import android.graphics.drawable.Drawable;

/**
 * Created by Luis on 7/22/2017.
 */

public class AppInfoModel {
    private String mAppName;
    private String mAppPackage;
    private Drawable mIcon;
    private String mColor;

    public String getAppName() {
        return mAppName;
    }

    public String getAppPackage() {
        return mAppPackage;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public String getColor() {
        return mColor;
    }

    public void setAppName(String appName) {
        mAppName = appName;
    }

    public void setAppPackage(String appPackage) {
        mAppPackage = appPackage;
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
    }

    public void setColor(String color) {
        mColor = color;
    }
}
