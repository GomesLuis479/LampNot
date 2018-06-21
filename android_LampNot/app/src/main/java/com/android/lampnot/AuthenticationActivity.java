package com.android.lampnot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AuthenticationActivity extends AppCompatActivity {

    private static final String EXTRA_TOKEN = "token";
    private static final String EXTRA_DEVICE = "device";
    private static final String EXTRA_VARIABLE = "variable";

    private EditText mToken;
    private EditText mDevice;
    private EditText mVariable;
    private Button mSaveButton;

    public static Intent newInstance(Context context, String token, String device, String variable) {
        Intent i = new Intent(context, AuthenticationActivity.class);
        i.putExtra(EXTRA_TOKEN, token);
        i.putExtra(EXTRA_DEVICE, device);
        i.putExtra(EXTRA_VARIABLE, variable);

        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mDevice = (EditText) findViewById(R.id.EditText_device);
        mToken = (EditText) findViewById(R.id.EditText_token);
        mVariable = (EditText) findViewById(R.id.EditText_variable);
        mSaveButton = (Button) findViewById(R.id.button_save_authentication);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDevice.getText() != null &&
                        mToken.getText() != null &&
                        mVariable.getText() != null ) {

                    String token = mToken.getText().toString();
                    String device = mDevice.getText().toString();
                    String variable = mVariable.getText().toString();

                    SharedPreferences sp = getSharedPreferences(MainActivity.PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString("Token", token);
                    editor.putString("Device", device );
                    editor.putString("Variable", variable);
                    editor.apply();

                    finish();
                }
            }
        });

        initialiseViews();
    }

    private void initialiseViews() {
        String token = (String) getIntent().getSerializableExtra(EXTRA_TOKEN);
        String device = (String) getIntent().getSerializableExtra(EXTRA_DEVICE);
        String variable = (String) getIntent().getSerializableExtra(EXTRA_VARIABLE);

        if ( !token.equals("NONE") && !device.equals("NONE") &&
        !variable.equals("NONE")) {
            mDevice.setText(device);
            mVariable.setText(variable);
            mToken.setText(token);
        }
    }
}
