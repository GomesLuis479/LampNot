package com.android.lampnot;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AppChooser extends AppCompatActivity {

    public static final String EXTRA_APP_PACKAGE = "app_package";
    public static final String EXTRA_APP_NAME = "app_name";
    public static final String EXTRA_OLD_APP_PACKAGE = "app_package_old";

    private RecyclerView mRecyclerView;
    private AppAdapter mAppAdapter;
    private String oldPackage;

    public static Intent newInstance(Context context, String oldAppPackage) {
        Intent i = new Intent(context, AppChooser.class);
        i.putExtra(EXTRA_OLD_APP_PACKAGE, oldAppPackage);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_chooser);

        oldPackage = getIntent().getSerializableExtra(EXTRA_OLD_APP_PACKAGE).toString();

        mAppAdapter = new AppAdapter(new AppLab(getApplicationContext()).getPackages());

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAppAdapter);
    }



    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.TextView_app_name);
            icon = (ImageView) view.findViewById(R.id.ImageView_icon);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent();
            TextView temp = (TextView) v.findViewById(R.id.TextView_app_name);
            i.putExtra(EXTRA_APP_PACKAGE, temp.getTag().toString());
            i.putExtra(EXTRA_APP_NAME, temp.getText());
            i.putExtra(EXTRA_OLD_APP_PACKAGE, oldPackage);
            setResult(RESULT_OK, i);
            finish();
        }

    }

    public class AppAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<ApplicationInfo> appList;
        private PackageManager mPackageManager;
        public AppAdapter(List<ApplicationInfo> appList) {
            this.appList = appList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.app_name_display, parent, false);
            mPackageManager = getPackageManager();
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ApplicationInfo appInfo = appList.get(position);
            if(appInfo.packageName != null) {
                try {
                    String appName = (String) mPackageManager.getApplicationLabel(appInfo);
                    String packageName = appInfo.packageName;
                    Drawable ic = mPackageManager.getApplicationIcon(packageName);

                    holder.icon.setImageDrawable(ic);
                    holder.title.setText(appName);
                    holder.title.setTag(packageName);
                }
                catch (Exception e) {
                    Log.d("ONBindView", e.toString());
                }

            }
            else
                holder.title.setText("not defined");
        }

        @Override
        public int getItemCount() {
            return appList.size();
        }
    }
}
