package com.softaai.realtimelocation.startupui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.softaai.realtimelocation.R;
import com.softaai.realtimelocation.common.Util;
import com.softaai.realtimelocation.dashboard.MainActivity;

import java.util.Arrays;

public class StartupActivity extends AppCompatActivity {

    // request code untuk login
    private static final int RC_LOGIN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Util.checkGooglePlayServicesAvailability(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOGIN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                goToMainActivity();
            } else {
                if (response == null) {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No Network Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void onSignInClicked(View view) {
        Intent i = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.GoogleBuilder().build()
                ))
                .setLogo(R.drawable.logo_globe)
                .build();
        startActivityForResult(i, RC_LOGIN);
    }

    private void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
