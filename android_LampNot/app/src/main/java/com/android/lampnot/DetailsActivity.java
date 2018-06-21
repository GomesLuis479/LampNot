package com.android.lampnot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private static final int REQUEST_SELECT_APP = 1;
    private static final int REQUEST_CHANGE_APP = 2;

    private static final int START_MODE_EDIT_APP = 3;
    private static final int START_MODE_ADD_APP = 4;


    private static final String EXTRA_PACKAGE_NAME = "package_name";
    private static final String EXTRA_APP_NAME = "app_name";
    private static final String EXTRA_COLOR = "color";


    private Spinner mSpinner;
    private ArrayAdapter<CharSequence> mAdapter;
    private TextView mTextView;
    private Button mSelectButton;
    private ImageView mImageView;

    private Button mDoneButton;
    private Button mDeleteButton;


    public static Intent newInstance(Context context, String packageName, String appName, String color) {
        Intent i = new Intent(context, DetailsActivity.class);
        i.putExtra(EXTRA_PACKAGE_NAME, packageName);
        i.putExtra(EXTRA_APP_NAME, appName);
        i.putExtra(EXTRA_COLOR, color);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mSpinner = (Spinner) findViewById(R.id.Spinner_select_color);
        mTextView = (TextView) findViewById(R.id.TextView_app_details);
        mSelectButton = (Button) findViewById(R.id.Button_select_app);
        mImageView = (ImageView) findViewById(R.id.ImageView_icon_details);
        mDoneButton = (Button) findViewById(R.id.button_done);
        mDeleteButton = (Button) findViewById(R.id.button_delete);

        mAdapter = ArrayAdapter.createFromResource(this, R.array.color_list, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sp = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString((String) mTextView.getTag(),
                        (String) mSpinner.getSelectedItem());
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = AppChooser.newInstance(getApplicationContext(), mTextView.getTag().toString());
                    startActivityForResult(i, REQUEST_SELECT_APP);
            }
        });

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString((String) mTextView.getTag(),"NONE");
                editor.apply();
                finish();
            }
        });

        initialiseViews();
    }

    public void initialiseViews() {
        try {
            String app_name = getIntent().getSerializableExtra(EXTRA_APP_NAME).toString();
            String package_name = getIntent().getSerializableExtra(EXTRA_PACKAGE_NAME).toString();
            String color = getIntent().getSerializableExtra(EXTRA_COLOR).toString();
            Drawable icon = this.getPackageManager().getApplicationIcon(package_name);

            if( !app_name.equals("NONE") && !package_name.equals("NONE")
                    && !color.equals("NONE") ) {
                mTextView.setText(app_name);
                switch (color) {
                    case "Red" : mSpinner.setSelection(0);break;
                    case "Green" : mSpinner.setSelection(1);break;
                    case "Blue" : mSpinner.setSelection(2);break;
                    default:break;
                }
                mImageView.setImageDrawable(icon);
                mSelectButton.setText(R.string.change_app);
                mTextView.setTag(package_name);
            }
            else {
                mTextView.setTag("NONE");
        }
        }
        catch (Exception e) {
            Log.d("DetailsInitialise", e.toString());
            mTextView.setTag("NONE");
            mSelectButton.setText("Please select an app...");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(data != null) {
                try {
                    String app_package = (String) data.getSerializableExtra(AppChooser.EXTRA_APP_PACKAGE);
                    String app_name = (String) data.getSerializableExtra(AppChooser.EXTRA_APP_NAME);
                    Drawable icon = this.getPackageManager().getApplicationIcon(app_package);
                    String oldPackage = (String)data.getSerializableExtra(AppChooser.EXTRA_OLD_APP_PACKAGE);
                    mTextView.setText(app_name);
                    mTextView.setTag(app_package);
                    mImageView.setImageDrawable(icon);
                    mSelectButton.setText("Change App");


                    SharedPreferences sp = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(oldPackage,"NONE");
                    editor.putString(app_package, mSpinner.getSelectedItem().toString());
                    editor.apply();
                }
                catch (Exception e) {
                    Log.d("DETAIL_RESULT", e.toString());
                }
            }
        }
        else {
            Log.d("DETAILS ACTIVITY", "NO RESULT");
        }
    }
}
