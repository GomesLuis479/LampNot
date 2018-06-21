package com.android.lampnot;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luis on 7/20/2017.
 */

public class AppLab {

    private PackageManager mPackageManager;
    private List<ApplicationInfo> mPackages;

    AppLab(Context context) {
        mPackageManager = context.getPackageManager();
        List<ApplicationInfo> allApps = mPackageManager
                .getInstalledApplications(PackageManager.GET_META_DATA);

        mPackages = new ArrayList<>();
        List<String> packages = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> intentList = context.getPackageManager().queryIntentActivities(intent, 0);

        for(ResolveInfo i : intentList) {
            try {
                String pack = i.activityInfo.packageName;
                //if(!isSystemPackage(app)) {
                if(!packages.contains(pack)) {
                    ApplicationInfo a = context.getPackageManager().getApplicationInfo(pack, 0);
                    mPackages.add(a);
                    packages.add(pack);
                }
                //}
            }catch (Exception e) {
                Log.d("AppLAb", e.toString());
            }
        }
    }

    public List<ApplicationInfo> getPackages() {
        return mPackages;
    }

    private boolean isSystemPackage(ApplicationInfo applicationInfo) {
        return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}
