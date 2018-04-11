package com.highgate.highgate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.highgate.highgate.myUtils.User;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tryToRequestMarshmallowAccessNetWorkStatePermission();

        start();
    }

    private void start(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                startApp();
            }

        }, 2000);
    }



    private void startApp(){

        SharedPreferences sharedPref = getSharedPreferences(SHARE_FILE, Context.MODE_PRIVATE);
        myUser = new User();
        myUser.setId(sharedPref.getInt(SHARE_ID, -1));
        myUser.setFirstName(sharedPref.getString(SHARE_FIRSTNAME, ""));
        myUser.setLastName(sharedPref.getString(SHARE_LASTNAME, ""));
        myUser.setEmailAddress(sharedPref.getString(SHARE_EMAIL, ""));
        myUser.setCompanyName(sharedPref.getString(SHARE_COMPANY, ""));
        myUser.setAddress(sharedPref.getString(SHARE_ADDRESS, ""));
        myUser.setSuburb(sharedPref.getString(SHARE_SUBURB, ""));
        myUser.setPostCode(sharedPref.getString(SHARE_POSTCODE, ""));
        myUser.setState(sharedPref.getString(SHARE_STATE, ""));
        myUser.setFreightMethod(sharedPref.getString(SHARE_FREIGHT, ""));
        myUser.setDebtor_id(sharedPref.getInt(SHARE_DEBTORID, -1));
        myUser.setGroup_id(sharedPref.getInt(SHARE_GROUPID, -1));

        if (myUser.getId() == -1) {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
