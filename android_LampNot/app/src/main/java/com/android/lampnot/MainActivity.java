package com.android.lampnot;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mStartButton;
    private Button mStopButton;
    private EditText mEditText;
    private TextView mTextView;
    private Context mContext;
    private SharedPreferences sharedpreferences;
    private MainAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Button mAuthButton;

    private String apiKey = "dbc88022cf820c98afc4271a52ebac22912474a3";
    private String varId = "58d4f0027625421a8552ce0d";
    private  String token = "NqRj9lLaMJzWfT5otF9SK6gYRm3ECn";
    public static final String PREFERENCES = "application3_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        sharedpreferences  = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        if( !( sharedpreferences.contains("Device") &&
                sharedpreferences.contains("Variable") &&
                sharedpreferences.contains("Token") )
                ) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Token", "NONE");
            editor.putString("Device", "NONE" );
            editor.putString("Variable", "NONE");
            editor.apply();
        }

        mStartButton = (Button)findViewById(R.id.button_start);
        mStopButton = (Button)findViewById(R.id.button_stop);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        mAuthButton = (Button) findViewById(R.id.button_authentication);
        updateUI();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = DetailsActivity.newInstance(getApplicationContext(), "NONE", "NONE", "NONE");
                startActivity(i);
            }
        });

        mAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = sharedpreferences.getString("Token", "NONE");
                String device = sharedpreferences.getString("Device", "NONE");
                String variable = sharedpreferences.getString("Variable", "NONE");

                Intent i = AuthenticationActivity.newInstance(getApplicationContext(), token, device, variable);
                startActivity(i);
            }
        });

    }

    private void updateUI() {
        List<AppInfoModel> apps = new AppInfoLab(getApplicationContext()).getList();
        if(mAdapter == null) {
            mAdapter = new MainAdapter(apps);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setChangedApps(apps);
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //setStartButton(mContext);
        updateUI();
    }

    private boolean checkAccess(Context context) {
        ComponentName cn = new ComponentName(context, ReadNotService.class);
        String flat = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        boolean enabled = flat != null && flat.contains(cn.flattenToString());
        return enabled;
    }

    private void setStartButton(Context context) {
        if(checkAccess(context)) {
            mStartButton.setEnabled(false);
            mStartButton.setVisibility(View.INVISIBLE);
        }
        else {
            mStartButton.setEnabled(true);
            mStartButton.setVisibility(View.VISIBLE);
        }
    }



    private class MainViewHolder extends RecyclerView.ViewHolder{
        public TextView app_name;
        public ImageView app_icon;
        public ImageButton app_color;
        public Button button_edit;

        public MainViewHolder(View view) {
            super(view);
            app_name = (TextView) view.findViewById(R.id.TextView_app_name_main);
            app_icon = (ImageView) view.findViewById(R.id.ImageView_icon_main);
            app_color = (ImageButton) view.findViewById(R.id.ImageButton_color_main);
            button_edit = (Button) view.findViewById(R.id.ImageButton_edit_main);
        }
    }

    public class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {

        private List<AppInfoModel> appList;

        public MainAdapter(List<AppInfoModel> appList) {
            this.appList = appList;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.apps_display_main, parent, false);
            return new MainViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            final AppInfoModel appInfo = appList.get(position);
            String color = appInfo.getColor();

            switch(color) {
                case "Red" : holder.app_color.setBackgroundColor(getColor(R.color.colorRed));  break;
                case "Green" : holder.app_color.setBackgroundColor(getColor(R.color.colorGreen));  break;
                case "Blue" : holder.app_color.setBackgroundColor(getColor(R.color.colorBlue));  break;
                default: break;
            }

            holder.app_name.setText(appInfo.getAppName());
            holder.app_name.setTag(appInfo.getAppPackage());
            holder.app_icon.setImageDrawable(appInfo.getIcon());

            holder.button_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = DetailsActivity.newInstance(getApplicationContext(),
                            appInfo.getAppPackage(),
                            appInfo.getAppName(),
                            appInfo.getColor());
                    startActivity(i);
                }
            });
        }

        public void setChangedApps(List<AppInfoModel> appList) {
            this.appList = appList;
        }

        @Override
        public int getItemCount() {
            return appList.size();
        }
    }

}

