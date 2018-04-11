package com.highgate.highgate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.highgate.highgate.myUtils.FontManager;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class ProfileActivity extends BaseActivity {

    private Button btnSave;
    private Button btnRtn;
    private TextView txtImg;
    private TextView txtTitle;

    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtEmail;
    private EditText edtCompany;
    private EditText edtAddress;
    private EditText edtSuburb;
    private EditText edtPostCode;
    private Spinner spnState;
    private TextView txtState;
    private Spinner spnFreight;
    private TextView txtFreight;

    private DrawerLayout layoutDrawer_Profile;

//    private String freights = {};

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FontManager.setFontType(findViewById(R.id.layout_Profile), gothamFont);

        setMenuEvents(this);

        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWarning()) {
                    Toast.makeText(ProfileActivity.this, "Please insert in red items!!!", Toast.LENGTH_SHORT).show();
                    makeWarning();
                }
                else {
                    cancelWarning();
                    saveProfile();
                    Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        edtFirstName = (EditText) findViewById(R.id.editFName_of_Profile);
        edtLastName = (EditText) findViewById(R.id.editLName_of_Profile);
        edtEmail = (EditText) findViewById(R.id.editEmail_of_Profile);
        edtCompany = (EditText) findViewById(R.id.editCompany_of_Profile);
        edtAddress = (EditText) findViewById(R.id.editAddress_of_Profile);
        edtSuburb = (EditText) findViewById(R.id.editSuburb_of_Profile);
        edtPostCode = (EditText) findViewById(R.id.editPostCode_of_Profile);
        spnState = (Spinner) findViewById(R.id.spnState_of_Profile);
        txtState = (TextView) findViewById(R.id.txtState_of_Profile);
        spnFreight = (Spinner) findViewById(R.id.spnFreight_of_Profile);
        txtFreight = (TextView) findViewById(R.id.txtFreight_of_Profile);

        showProfile();
        setReturn();

        spnState.setDropDownWidth(screenWidth * 33 / 100);
        txtState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spnState.performClick();
                txtState.setVisibility(View.INVISIBLE);
            }
        });

        spnFreight.setDropDownWidth(screenWidth * 80 / 100);
        txtFreight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spnFreight.performClick();
                txtFreight.setVisibility(View.INVISIBLE);
            }
        });


    }

    private void setReturn(){

        btnRtn = (Button)findViewById(R.id.btnRtn_Profile);
        btnRtn.setTypeface(iconFont);
        btnRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtImg = (TextView)findViewById(R.id.txtProfileImg);
        txtImg.setTypeface(iconFont);
        txtImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtTitle = (TextView)findViewById(R.id.txtProfileTitle);
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showProfile(){

        edtFirstName.setText(myUser.getFirstName());
        edtLastName.setText(myUser.getLastName());
        edtEmail.setText(myUser.getEmailAddress());
        edtCompany.setText(myUser.getCompanyName());
        edtAddress.setText(myUser.getAddress());
        edtSuburb.setText(myUser.getSuburb());
        edtPostCode.setText(myUser.getPostCode());

        String state = myUser.getState();
        if (state.equals("")) {
            txtState.setVisibility(View.VISIBLE);
        }
        else {
            txtState.setVisibility(View.INVISIBLE);
            spnState.setSelection(getIdState(state));
        }

        String freight = myUser.getFreightMethod();
        if (freight.equals("")) {
            txtFreight.setVisibility(View.VISIBLE);
        }
        else {
            txtFreight.setVisibility(View.INVISIBLE);
            spnFreight.setSelection(getIdFreight(freight));
        }

    }

    private int getIdState(String state){
        String[] states = getResources().getStringArray(R.array.state_arrays);
        for (int i = 0; i < states.length; i++){
            if ( state.equals(states[i]) )
                return i;
        }
        return 0;
    }

    private int getIdFreight(String freight){
        String[] freights = getResources().getStringArray(R.array.freight_arrays);
        for (int i = 0; i < freights.length; i++){
            if ( freight.equals(freights[i]) )
                return i;
        }
        return 0;
    }

    private void saveProfile(){
        myUser.setFirstName(edtFirstName.getText().toString());
        myUser.setLastName(edtLastName.getText().toString());
        myUser.setEmailAddress(edtEmail.getText().toString());
        myUser.setCompanyName(edtCompany.getText().toString());
        myUser.setAddress(edtAddress.getText().toString());
        myUser.setSuburb(edtSuburb.getText().toString());
        myUser.setPostCode(edtPostCode.getText().toString());

        if (txtState.getVisibility() == View.VISIBLE) {
            myUser.setState("");
        }
        else {
            myUser.setState(spnState.getSelectedItem().toString());
        }

        if (txtFreight.getVisibility() == View.VISIBLE) {
            myUser.setFreightMethod("");
        }
        else {
            myUser.setFreightMethod(spnFreight.getSelectedItem().toString());
        }

        SharedPreferences sharedPref = getSharedPreferences(SHARE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARE_FIRSTNAME, myUser.getFirstName());
        editor.putString(SHARE_LASTNAME, myUser.getLastName());
        editor.putString(SHARE_EMAIL, myUser.getEmailAddress());
        editor.putString(SHARE_COMPANY, myUser.getCompanyName());
        editor.putString(SHARE_ADDRESS, myUser.getAddress());
        editor.putString(SHARE_SUBURB, myUser.getSuburb());
        editor.putString(SHARE_POSTCODE, myUser.getPostCode());
        editor.putString(SHARE_STATE, myUser.getState());
        editor.putString(SHARE_FREIGHT, myUser.getFreightMethod());

        editor.commit();

        System.out.println(myUser);
    }

    private void makeWarning(){
        edtFirstName.setHintTextColor(Color.RED);
        edtLastName.setHintTextColor(Color.RED);
        edtEmail.setHintTextColor(Color.RED);
        edtCompany.setHintTextColor(Color.RED);
        edtAddress.setHintTextColor(Color.RED);
        edtSuburb.setHintTextColor(Color.RED);
        edtPostCode.setHintTextColor(Color.RED);
        txtState.setHintTextColor(Color.RED);
        txtFreight.setHintTextColor(Color.RED);
    }

    private void cancelWarning(){
        edtFirstName.setHintTextColor(getResources().getColor(R.color.colorMyDarkGray));
        edtLastName.setHintTextColor(getResources().getColor(R.color.colorMyDarkGray));
        edtEmail.setHintTextColor(getResources().getColor(R.color.colorMyDarkGray));
        edtCompany.setHintTextColor(getResources().getColor(R.color.colorMyDarkGray));
        edtAddress.setHintTextColor(getResources().getColor(R.color.colorMyDarkGray));
        edtSuburb.setHintTextColor(getResources().getColor(R.color.colorMyDarkGray));
        edtPostCode.setHintTextColor(getResources().getColor(R.color.colorMyDarkGray));
        txtState.setHintTextColor(getResources().getColor(R.color.colorMyDarkGray));
        txtFreight.setHintTextColor(getResources().getColor(R.color.colorMyDarkGray));
    }

    private boolean isWarning(){
        if (edtFirstName.getText().toString().equals("")) return true;
        if (edtLastName.getText().toString().equals("")) return true;
        if (edtEmail.getText().toString().equals("")) return true;
        if (edtCompany.getText().toString().equals("")) return true;
        if (edtAddress.getText().toString().equals("")) return true;
        if (edtSuburb.getText().toString().equals("")) return true;
        if (edtPostCode.getText().toString().equals("")) return true;
        if (txtState.getVisibility() == View.VISIBLE) return true;
        if (txtFreight.getVisibility() == View.VISIBLE) return true;
        return false;
    }
}
