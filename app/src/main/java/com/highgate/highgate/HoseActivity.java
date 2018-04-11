package com.highgate.highgate;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidessence.lib.RichTextView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.highgate.highgate.adapters.Product;
import com.highgate.highgate.adapters.ProductListAdapter;
import com.highgate.highgate.myUtils.FontManager;
import com.highgate.highgate.myUtils.ImageZoomTouchListener;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class HoseActivity extends BaseActivity {

    private Button btnRtn;
    private TextView txtRtn;
    private TextView txtTitle;

    private Button btnSelectFittingType;
    private Button btnSelectHoseType;
    private Button btnSelectAngle;
    private Button btnSelectFittingSize;
    private Button btnSelectHoseSize;
    private Button btnAdditionalOptions;
    private Button btnSelectMaterial;
    private Button btnSelectAccess;

    private TextView introduction;

    private DrawerLayout layoutDrawer_Hose1;
    private DrawerLayout layoutDrawer_Hose2;
    private DrawerLayout layoutDrawer_Hose3;
    private DrawerLayout layoutDrawer_Hose4;

    private ListView listViewSelectFittingType;
    private ListView listViewSelectHoseType;
    private ListView listViewSelectAngle;
    private ListView listViewSelectFittingSize;
    private ListView listViewSelectHoseSize;
    private ListView listViewAdditionalOptions;
    private ListView listViewSelectMaterial;
    private ListView listViewSelectAccess;

    /////////////////////////////////

    private List<String> fittingTypeList = new ArrayList<>();
    private List<String> hoseTypeList = new ArrayList<>();
    private List<String> angleList = new ArrayList<>();
    private List<String> fittingSizeList = new ArrayList<>();
    private List<String> hoseSizeList = new ArrayList<>();
    private List<String> addOptionsList = new ArrayList<>();
    private List<String> materialList = new ArrayList<>();
    private List<String> accessList = new ArrayList<>();

    ///////////////////////

    private String hoseFittingType = "";
    private String hoseHoseType = "";
    private String hoseAngle = "";
    private String hoseFittingSize = "";
    private String hoseHoseSize = "";
    private String hoseAddOptions = "";
    private String hoseMaterial= "";
    private String hoseAccess = "";

    /////////////////////////

    private boolean fittingTypeMenuFlag = false;
    private boolean hoseTypeMenuFlag = false;
    private boolean angleMenuFlag = false;
    private boolean fittingSizeMenuFlag = false;
    private boolean hoseSizeMenuFlag = false;
    private boolean addOptionMenuFlag = false;
    private boolean materialMenuFlag = false;
    private boolean accessMenuFlag = false;

    private ListView listViewSelectedHose;
    private LinearLayout layoutHoseContent;

    private ProductListAdapter adapter;

    private TextView txtClearHose;
    private Button btnClearHose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hose);

        FontManager.setFontType(findViewById(R.id.layout_Hose), gothamFont);

        setMenuEvents(this);
        DrawerLayout layoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);
        layoutDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        progressBar = (ProgressBar) findViewById(R.id.progressBar_Hose);

        ///////////////////////////////////////////

        introduction = (TextView) findViewById(R.id.txtIntroduct_Hose);
//        introduction.setTypeface(gothamFont);
//        String str = introduction.getText().toString();
//        introduction.formatSpan(str.length() - 16, str.length(), RichTextView.FormatType.BOLD);

        ///////////////////////////////////////////////////////////////////////////////////////////
        listViewSelectedHose = (ListView) findViewById(R.id.listViewSelectedHose);
        layoutHoseContent = (LinearLayout) findViewById(R.id.layoutHoseContent);

        txtClearHose= (TextView) findViewById(R.id.txtClearHose);
        txtClearHose.setTypeface(iconFont);
        txtClearHose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIntroduction();
            }
        });

        btnClearHose = (Button) findViewById(R.id.btnClearHose);
        btnClearHose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIntroduction();
            }
        });

        TextView txtMark = (TextView) findViewById(R.id.txtMark_OrderReport);
        txtMark.setTypeface(iconFont);
        layoutOrderReport = (LinearLayout) findViewById(R.id.layoutOrderReport_Hose);
        txtOrderReference = (TextView) findViewById(R.id.txtOrderReference_OrderReport);
        btnCloseOrderReport = (Button) findViewById(R.id.btnClose_OrderReport);
        btnCloseOrderReport.setTypeface(iconFont);
        btnCloseOrderReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutOrderReport.setVisibility(View.GONE);
            }
        });

        layoutMain = (DrawerLayout) findViewById(R.id.layoutMain_Hose);
        layoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        layoutZoomImage = (RelativeLayout) findViewById(R.id.layoutZoomImage_Hose);
        layoutZoomImage.getLayoutParams().width = screenWidth;
        imgZoomImage = (ImageView) findViewById(R.id.imgZoomImage_Hose);
        btnZoomImageClose = (Button) findViewById(R.id.btnZoomImageClose_Hose);
        btnZoomImageClose.setTypeface(iconFont);
        btnZoomImageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutMain.closeDrawer(layoutZoomImage);
            }
        });

        setHoseMenu();
        setReturn();
    }

    private void setHoseMenu(){
        layoutDrawer_Hose1 = (DrawerLayout) findViewById(R.id.layoutDrawer_Hose1);
        layoutDrawer_Hose1.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        listViewSelectFittingType = (ListView) findViewById(R.id.listSelectFittingType);
        listViewSelectFittingType.getLayoutParams().width = screenWidth/2;

        listViewSelectHoseType = (ListView) findViewById(R.id.listSelectHoseType);
        listViewSelectHoseType.getLayoutParams().width = screenWidth/2;

        ///////////////////////////////////////////////////////////////////////////////////////////

        layoutDrawer_Hose2 = (DrawerLayout) findViewById(R.id.layoutDrawer_Hose2);
        layoutDrawer_Hose2.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        listViewSelectAngle = (ListView) findViewById(R.id.listSelectAngle);
        listViewSelectAngle.getLayoutParams().width = screenWidth/2;

        listViewSelectFittingSize = (ListView) findViewById(R.id.listSelectFittingSize);
        listViewSelectFittingSize.getLayoutParams().width = screenWidth/2;

        ///////////////////////////////////////////////////////////////////////////////////////////

        layoutDrawer_Hose3 = (DrawerLayout) findViewById(R.id.layoutDrawer_Hose3);
        layoutDrawer_Hose3.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        listViewSelectHoseSize = (ListView) findViewById(R.id.listSelectHoseSize);
        listViewSelectHoseSize.getLayoutParams().width = screenWidth/2;

        listViewAdditionalOptions= (ListView) findViewById(R.id.listSelectAddOptions);
        listViewAdditionalOptions.getLayoutParams().width = screenWidth/2;

        ///////////////////////////////////////////////////////////////////////////////////////////

        layoutDrawer_Hose4 = (DrawerLayout) findViewById(R.id.layoutDrawer_Hose4);
        layoutDrawer_Hose4.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        listViewSelectMaterial = (ListView) findViewById(R.id.listSelectMaterial);
        listViewSelectMaterial.getLayoutParams().width = screenWidth/2;

        listViewSelectAccess = (ListView) findViewById(R.id.listSelectAccess);
        listViewSelectAccess.getLayoutParams().width = screenWidth/2;

        ///////////////////////////////////////////////////////////////////////////////////////////

        btnSelectFittingType = (Button) findViewById(R.id.btnSelectFittingType);
        btnSelectFittingType.setTypeface(gothamFont, Typeface.BOLD);
        SetSelected(btnSelectFittingType);
        btnSelectFittingType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fittingTypeMenuFlag = !(fittingSizeMenuFlag);

                if (fittingTypeMenuFlag) {
                    String apiUrl = "http://www.highgateair.com.au/hosefitting/index/initial?api=true";
                    Ion.with(getApplicationContext())
                            .load(apiUrl)
                            .setTimeout(3000)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result == null || result.isJsonNull()) {
                                        Toast.makeText(HoseActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    fittingTypeList.clear();
                                    Object[] strArray= result.entrySet().toArray();
                                    for(int i=0; i<strArray.length; i++){
                                        String s = strArray[i].toString();
                                        s = s.split("\"")[0];
                                        s = s.substring(0, s.length()-1);
                                        fittingTypeList.add(s);
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(HoseActivity.this, android.R.layout.simple_list_item_1, fittingTypeList){
                                        @Override
                                        public View getView(final int position, View convertView, ViewGroup parent) {
                                            View view =super.getView(position, convertView, parent);
                                            TextView textView=(TextView) view.findViewById(android.R.id.text1);
                                            textView.setTextColor(getResources().getColor(R.color.colorMyBlack));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    hoseFittingType = fittingTypeList.get(position);
                                                    layoutDrawer_Hose1.closeDrawer(listViewSelectFittingType);
                                                    fittingTypeMenuFlag = false;

                                                    btnSelectFittingType.setText(hoseFittingType);
                                                    SetSelected(btnSelectHoseType);
                                                    btnSelectHoseType.setText(getString(R.string.hose_menu_2));
                                                    SetUnselected(btnSelectAngle);
                                                    btnSelectAngle.setText(getString(R.string.hose_menu_3));
                                                    SetUnselected(btnSelectFittingSize);
                                                    btnSelectFittingSize.setText(getString(R.string.hose_menu_4));
                                                    SetUnselected(btnSelectHoseSize);
                                                    btnSelectHoseSize.setText(getString(R.string.hose_menu_5));
                                                    SetUnselected(btnAdditionalOptions);
                                                    btnAdditionalOptions.setText(getString(R.string.hose_menu_6));
                                                    SetUnselected(btnSelectMaterial);
                                                    btnSelectMaterial.setText(getString(R.string.hose_menu_7));
                                                    SetUnselected(btnSelectAccess);
                                                    btnSelectAccess.setText(getString(R.string.hose_menu_8));

                                                }
                                            });
                                            return view;
                                        }
                                    };
                                    listViewSelectFittingType.setAdapter(adapter);
                                    layoutDrawer_Hose1.openDrawer(listViewSelectFittingType);
                                    layoutDrawer_Hose1.closeDrawer(listViewSelectHoseType);
                                    hoseTypeMenuFlag = false;
                                    layoutDrawer_Hose2.closeDrawer(listViewSelectAngle);
                                    angleMenuFlag = false;
                                    layoutDrawer_Hose2.closeDrawer(listViewSelectFittingSize);
                                    fittingSizeMenuFlag = false;
                                    layoutDrawer_Hose3.closeDrawer(listViewSelectHoseSize);
                                    hoseSizeMenuFlag = false;
                                    layoutDrawer_Hose3.closeDrawer(listViewAdditionalOptions);
                                    addOptionMenuFlag = false;
                                    layoutDrawer_Hose4.closeDrawer(listViewSelectMaterial);
                                    materialMenuFlag = false;
                                    layoutDrawer_Hose4.closeDrawer(listViewSelectAccess);
                                    accessMenuFlag = false;
                                }
                            });
                }
                else {
                    layoutDrawer_Hose1.closeDrawer(listViewSelectFittingType);
                }
            }
        });

        btnSelectHoseType = (Button) findViewById(R.id.btnSelectHoseType);
        btnSelectHoseType.setTypeface(gothamFont, Typeface.BOLD);
        SetUnselected(btnSelectHoseType);
        btnSelectHoseType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hoseTypeMenuFlag = !(hoseTypeMenuFlag);

                if (hoseTypeMenuFlag){
                    String apiUrl = "http://www.highgateair.com.au/hosefitting/index/fittingtype?fittingtype=" + encodeStringUri(hoseFittingType) + "&api=true";
                    Ion.with(getApplicationContext())
                            .load(apiUrl)
                            .setTimeout(3000)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result == null || result.isJsonNull()) {
                                        Toast.makeText(HoseActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    hoseTypeList.clear();
                                    Object[] strArray= result.entrySet().toArray();
                                    for(int i=0; i<strArray.length; i++){
                                        String s = strArray[i].toString();
                                        s = s.split("\"")[0];
                                        s = s.substring(0, s.length()-1);
                                        hoseTypeList.add(s);
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(HoseActivity.this, android.R.layout.simple_list_item_1, hoseTypeList){
                                        @Override
                                        public View getView(final int position, View convertView, ViewGroup parent) {
                                            View view =super.getView(position, convertView, parent);
                                            TextView textView=(TextView) view.findViewById(android.R.id.text1);
                                            textView.setTextColor(getResources().getColor(R.color.colorMyBlack));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    hoseHoseType = hoseTypeList.get(position);
                                                    layoutDrawer_Hose1.closeDrawer(listViewSelectHoseType);
                                                    hoseTypeMenuFlag = false;

                                                    btnSelectHoseType.setText(hoseHoseType);
                                                    SetSelected(btnSelectAngle);
                                                    btnSelectAngle.setText(getString(R.string.hose_menu_3));
                                                    SetUnselected(btnSelectFittingSize);
                                                    btnSelectFittingSize.setText(getString(R.string.hose_menu_4));
                                                    SetUnselected(btnSelectHoseSize);
                                                    btnSelectHoseSize.setText(getString(R.string.hose_menu_5));
                                                    SetUnselected(btnAdditionalOptions);
                                                    btnAdditionalOptions.setText(getString(R.string.hose_menu_6));
                                                    SetUnselected(btnSelectMaterial);
                                                    btnSelectMaterial.setText(getString(R.string.hose_menu_7));
                                                    SetUnselected(btnSelectAccess);
                                                    btnSelectAccess.setText(getString(R.string.hose_menu_8));
                                                }
                                            });
                                            return view;
                                        }
                                    };
                                    listViewSelectHoseType.setAdapter(adapter);
                                    layoutDrawer_Hose1.closeDrawer(listViewSelectFittingType);
                                    fittingTypeMenuFlag = false;
                                    layoutDrawer_Hose1.openDrawer(listViewSelectHoseType);
                                    layoutDrawer_Hose2.closeDrawer(listViewSelectAngle);
                                    angleMenuFlag = false;
                                    layoutDrawer_Hose2.closeDrawer(listViewSelectFittingSize);
                                    fittingSizeMenuFlag = false;
                                    layoutDrawer_Hose3.closeDrawer(listViewSelectHoseSize);
                                    hoseSizeMenuFlag = false;
                                    layoutDrawer_Hose3.closeDrawer(listViewAdditionalOptions);
                                    addOptionMenuFlag = false;
                                    layoutDrawer_Hose4.closeDrawer(listViewSelectMaterial);
                                    materialMenuFlag = false;
                                    layoutDrawer_Hose4.closeDrawer(listViewSelectAccess);
                                    accessMenuFlag = false;
                                }
                            });
                }
                else {
                    layoutDrawer_Hose1.closeDrawer(listViewSelectHoseType);
                }
            }
        });

        btnSelectAngle = (Button) findViewById(R.id.btnSelectAngle);
        btnSelectAngle.setTypeface(gothamFont, Typeface.BOLD);
        SetUnselected(btnSelectAngle);
        btnSelectAngle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                angleMenuFlag = !(angleMenuFlag);

                if (angleMenuFlag){
                    String apiUrl = "http://www.highgateair.com.au/hosefitting/index/hosetype?hosetype=" + encodeStringUri(hoseHoseType)
                            + "&fittingtype=" + encodeStringUri(hoseFittingType) + "&api=true";
                    Ion.with(getApplicationContext())
                            .load(apiUrl)
                            .setTimeout(3000)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result == null || result.isJsonNull()) {
                                        Toast.makeText(HoseActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    angleList.clear();
                                    Object[] strArray= result.entrySet().toArray();
                                    for(int i=0; i<strArray.length; i++){
                                        String s = strArray[i].toString();
                                        s = s.split("\"")[0];
                                        s = s.substring(0, s.length()-1);
                                        angleList.add(s);
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(HoseActivity.this, android.R.layout.simple_list_item_1, angleList){
                                        @Override
                                        public View getView(final int position, View convertView, ViewGroup parent) {
                                            View view =super.getView(position, convertView, parent);
                                            TextView textView=(TextView) view.findViewById(android.R.id.text1);
                                            textView.setTextColor(getResources().getColor(R.color.colorMyBlack));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    hoseAngle = angleList.get(position);
                                                    layoutDrawer_Hose2.closeDrawer(listViewSelectAngle);
                                                    angleMenuFlag = false;

                                                    btnSelectAngle.setText(hoseAngle);
                                                    SetSelected(btnSelectFittingSize);
                                                    btnSelectFittingSize.setText(getString(R.string.hose_menu_4));
                                                    SetUnselected(btnSelectHoseSize);
                                                    btnSelectHoseSize.setText(getString(R.string.hose_menu_5));
                                                    SetUnselected(btnAdditionalOptions);
                                                    btnAdditionalOptions.setText(getString(R.string.hose_menu_6));
                                                    SetUnselected(btnSelectMaterial);
                                                    btnSelectMaterial.setText(getString(R.string.hose_menu_7));
                                                    SetUnselected(btnSelectAccess);
                                                    btnSelectAccess.setText(getString(R.string.hose_menu_8));
                                                }
                                            });
                                            return view;
                                        }
                                    };
                                    listViewSelectAngle.setAdapter(adapter);
                                    layoutDrawer_Hose1.closeDrawer(listViewSelectFittingType);
                                    fittingTypeMenuFlag = false;
                                    layoutDrawer_Hose1.closeDrawer(listViewSelectHoseType);
                                    hoseTypeMenuFlag = false;
                                    layoutDrawer_Hose2.openDrawer(listViewSelectAngle);
                                    layoutDrawer_Hose2.closeDrawer(listViewSelectFittingSize);
                                    fittingSizeMenuFlag = false;
                                    layoutDrawer_Hose3.closeDrawer(listViewSelectHoseSize);
                                    hoseSizeMenuFlag = false;
                                    layoutDrawer_Hose3.closeDrawer(listViewAdditionalOptions);
                                    addOptionMenuFlag = false;
                                    layoutDrawer_Hose4.closeDrawer(listViewSelectMaterial);
                                    materialMenuFlag = false;
                                    layoutDrawer_Hose4.closeDrawer(listViewSelectAccess);
                                    accessMenuFlag = false;
                                }
                            });
                }
                else {
                    layoutDrawer_Hose2.closeDrawer(listViewSelectAngle);
                }
            }
        });

        btnSelectFittingSize = (Button) findViewById(R.id.btnSelectFittingSize);
        btnSelectFittingSize.setTypeface(gothamFont, Typeface.BOLD);
        SetUnselected(btnSelectFittingSize);
        btnSelectFittingSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fittingSizeMenuFlag = !(fittingTypeMenuFlag);

                if (fittingSizeMenuFlag) {
                    String apiUrl = "http://www.highgateair.com.au/hosefitting/index/angle?angle=" + encodeStringUri(hoseAngle)
                            + "&fittingtype=" + encodeStringUri(hoseFittingType)
                            + "&hosetype=" + encodeStringUri(hoseHoseType) + "&api=true";
                    Ion.with(getApplicationContext())
                            .load(apiUrl)
                            .setTimeout(3000)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result == null || result.isJsonNull()) {
                                        Toast.makeText(HoseActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    fittingSizeList.clear();
                                    Object[] strArray= result.entrySet().toArray();
                                    for(int i=0; i<strArray.length; i++){
                                        String s = strArray[i].toString();
                                        s = s.split("\"")[0];
                                        s = s.substring(0, s.length()-1);
                                        fittingSizeList.add(s);
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(HoseActivity.this, android.R.layout.simple_list_item_1, fittingSizeList){
                                        @Override
                                        public View getView(final int position, View convertView, ViewGroup parent) {
                                            View view =super.getView(position, convertView, parent);
                                            TextView textView=(TextView) view.findViewById(android.R.id.text1);
                                            textView.setTextColor(getResources().getColor(R.color.colorMyBlack));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    hoseFittingSize = fittingSizeList.get(position);
                                                    layoutDrawer_Hose2.closeDrawer(listViewSelectFittingSize);
                                                    fittingSizeMenuFlag = false;

                                                    btnSelectFittingSize.setText(hoseFittingSize);
                                                    SetSelected(btnSelectHoseSize);
                                                    btnSelectHoseSize.setText(getString(R.string.hose_menu_5));
                                                    SetUnselected(btnAdditionalOptions);
                                                    btnAdditionalOptions.setText(getString(R.string.hose_menu_6));
                                                    SetUnselected(btnSelectMaterial);
                                                    btnSelectMaterial.setText(getString(R.string.hose_menu_7));
                                                    SetUnselected(btnSelectAccess);
                                                    btnSelectAccess.setText(getString(R.string.hose_menu_8));
                                                }
                                            });
                                            return view;
                                        }
                                    };
                                    listViewSelectFittingSize.setAdapter(adapter);
                                    layoutDrawer_Hose1.closeDrawer(listViewSelectFittingType);
                                    fittingTypeMenuFlag = false;
                                    layoutDrawer_Hose1.closeDrawer(listViewSelectHoseType);
                                    hoseTypeMenuFlag = false;
                                    layoutDrawer_Hose2.closeDrawer(listViewSelectAngle);
                                    angleMenuFlag = false;
                                    layoutDrawer_Hose2.openDrawer(listViewSelectFittingSize);
                                    layoutDrawer_Hose3.closeDrawer(listViewSelectHoseSize);
                                    hoseSizeMenuFlag = false;
                                    layoutDrawer_Hose3.closeDrawer(listViewAdditionalOptions);
                                    addOptionMenuFlag = false;
                                    layoutDrawer_Hose4.closeDrawer(listViewSelectMaterial);
                                    materialMenuFlag = false;
                                    layoutDrawer_Hose4.closeDrawer(listViewSelectAccess);
                                    accessMenuFlag = false;
                                }
                            });
                }
                else {
                    layoutDrawer_Hose2.closeDrawer(listViewSelectFittingSize);
                }
            }
        });

        btnSelectHoseSize = (Button) findViewById(R.id.btnSelectHoseSize);
        btnSelectHoseSize.setTypeface(gothamFont, Typeface.BOLD);
        SetUnselected(btnSelectHoseSize);
        btnSelectHoseSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hoseSizeMenuFlag = !(hoseSizeMenuFlag);

                if (hoseSizeMenuFlag) {
                    String apiUrl = "http://www.highgateair.com.au/hosefitting/index/fittingsize?fittingsize=" + encodeNumberUri(hoseFittingSize)
                            + "&angle=" + encodeStringUri(hoseAngle)
                            + "&fittingtype=" + encodeStringUri(hoseFittingType)
                            + "&hosetype=" + encodeStringUri(hoseHoseType) + "&api=true";
                    Ion.with(getApplicationContext())
                            .load(apiUrl)
                            .setTimeout(3000)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result == null || result.isJsonNull()) {
                                        Toast.makeText(HoseActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    hoseSizeList.clear();
                                    Object[] strArray= result.entrySet().toArray();
                                    for(int i=0; i<strArray.length; i++){
                                        String s = strArray[i].toString();
                                        s = s.split("\"")[0];
                                        s = s.substring(0, s.length()-1);
                                        hoseSizeList.add(s);
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(HoseActivity.this, android.R.layout.simple_list_item_1, hoseSizeList){
                                        @Override
                                        public View getView(final int position, View convertView, ViewGroup parent) {
                                            View view =super.getView(position, convertView, parent);
                                            TextView textView=(TextView) view.findViewById(android.R.id.text1);
                                            textView.setTextColor(getResources().getColor(R.color.colorMyBlack));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    hoseHoseSize = hoseSizeList.get(position);
                                                    layoutDrawer_Hose3.closeDrawer(listViewSelectHoseSize);
                                                    hoseSizeMenuFlag = false;

                                                    btnSelectHoseSize.setText(hoseHoseSize);
                                                    SetSelected(btnAdditionalOptions);
                                                    btnAdditionalOptions.setText(getString(R.string.hose_menu_6));
                                                    SetUnselected(btnSelectMaterial);
                                                    btnSelectMaterial.setText(getString(R.string.hose_menu_7));
                                                    SetUnselected(btnSelectAccess);
                                                    btnSelectAccess.setText(getString(R.string.hose_menu_8));
                                                }
                                            });
                                            return view;
                                        }
                                    };
                                    listViewSelectHoseSize.setAdapter(adapter);
                                    layoutDrawer_Hose1.closeDrawer(listViewSelectFittingType);
                                    fittingTypeMenuFlag = false;
                                    layoutDrawer_Hose1.closeDrawer(listViewSelectHoseType);
                                    hoseTypeMenuFlag = false;
                                    layoutDrawer_Hose2.closeDrawer(listViewSelectAngle);
                                    angleMenuFlag = false;
                                    layoutDrawer_Hose2.closeDrawer(listViewSelectFittingSize);
                                    fittingSizeMenuFlag = false;
                                    layoutDrawer_Hose3.openDrawer(listViewSelectHoseSize);
                                    layoutDrawer_Hose3.closeDrawer(listViewAdditionalOptions);
                                    addOptionMenuFlag = false;
                                    layoutDrawer_Hose4.closeDrawer(listViewSelectMaterial);
                                    materialMenuFlag = false;
                                    layoutDrawer_Hose4.closeDrawer(listViewSelectAccess);
                                    accessMenuFlag = false;
                                }
                            });
                }
                else {
                    layoutDrawer_Hose3.closeDrawer(listViewSelectHoseSize);
                }
            }
        });

        btnAdditionalOptions = (Button) findViewById(R.id.btnAdditionalOptions);
        btnAdditionalOptions.setTypeface(gothamFont, Typeface.BOLD);
        SetUnselected(btnAdditionalOptions);
        btnAdditionalOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addOptionMenuFlag = !(addOptionMenuFlag);

                if (addOptionMenuFlag) {
                    String apiUrl = "http://www.highgateair.com.au/hosefitting/index/hosesize?hosesize=" + encodeNumberUri(hoseHoseSize)
                            + "&fittingsize=" + encodeNumberUri(hoseFittingSize)
                            + "&angle=" + encodeStringUri(hoseAngle)
                            + "&fittingtype=" + encodeStringUri(hoseFittingType)
                            + "&hosetype=" + encodeStringUri(hoseHoseType) + "&api=true";
                    Ion.with(getApplicationContext())
                            .load(apiUrl)
                            .setTimeout(3000)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result == null || result.isJsonNull()) {
                                        Toast.makeText(HoseActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    addOptionsList.clear();
                                    Object[] strArray= result.entrySet().toArray();
                                    for(int i=0; i<strArray.length; i++){
                                        String s = strArray[i].toString();
                                        s = s.split("\"")[0];
                                        s = s.substring(0, s.length()-1);
                                        addOptionsList.add(s);
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(HoseActivity.this, android.R.layout.simple_list_item_1, addOptionsList){
                                        @Override
                                        public View getView(final int position, View convertView, ViewGroup parent) {
                                            View view =super.getView(position, convertView, parent);
                                            TextView textView=(TextView) view.findViewById(android.R.id.text1);
                                            textView.setTextColor(getResources().getColor(R.color.colorMyBlack));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    hoseAddOptions = addOptionsList.get(position);
                                                    layoutDrawer_Hose3.closeDrawer(listViewAdditionalOptions);
                                                    addOptionMenuFlag = false;

                                                    btnAdditionalOptions.setText(hoseAddOptions);
                                                    SetSelected(btnSelectMaterial);
                                                    btnSelectMaterial.setText(getString(R.string.hose_menu_7));
                                                    SetUnselected(btnSelectAccess);
                                                    btnSelectAccess.setText(getString(R.string.hose_menu_8));
                                                }
                                            });
                                            return view;
                                        }
                                    };
                                    listViewAdditionalOptions.setAdapter(adapter);
                                    layoutDrawer_Hose1.closeDrawer(listViewSelectFittingType);
                                    fittingTypeMenuFlag = false;
                                    layoutDrawer_Hose1.closeDrawer(listViewSelectHoseType);
                                    hoseTypeMenuFlag = false;
                                    layoutDrawer_Hose2.closeDrawer(listViewSelectAngle);
                                    angleMenuFlag = false;
                                    layoutDrawer_Hose2.closeDrawer(listViewSelectFittingSize);
                                    fittingSizeMenuFlag = false;
                                    layoutDrawer_Hose3.closeDrawer(listViewSelectHoseSize);
                                    hoseSizeMenuFlag = false;
                                    layoutDrawer_Hose3.openDrawer(listViewAdditionalOptions);
                                    layoutDrawer_Hose4.closeDrawer(listViewSelectMaterial);
                                    materialMenuFlag = false;
                                    layoutDrawer_Hose4.closeDrawer(listViewSelectAccess);
                                    accessMenuFlag = false;
                                }
                            });
                }
                else {
                    layoutDrawer_Hose3.closeDrawer(listViewAdditionalOptions);
                }
            }
        });

        btnSelectMaterial= (Button) findViewById(R.id.btnSelectMaterial);
        btnSelectMaterial.setTypeface(gothamFont, Typeface.BOLD);
        SetUnselected(btnSelectMaterial);
        btnSelectMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                materialMenuFlag = !(materialMenuFlag);

                if (materialMenuFlag) {
                    String apiUrl = "http://www.highgateair.com.au/hosefitting/index/other?other=" + encodeStringUri(hoseAddOptions)
                            + "&hosesize=" + encodeNumberUri(hoseHoseSize)
                            + "&fittingsize=" + encodeNumberUri(hoseFittingSize)
                            + "&angle=" + encodeStringUri(hoseAngle)
                            + "&fittingtype=" + encodeStringUri(hoseFittingType)
                            + "&hosetype=" + encodeStringUri(hoseHoseType) + "&api=true";
                    Ion.with(getApplicationContext())
                            .load(apiUrl)
                            .setTimeout(3000)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result == null || result.isJsonNull()) {
                                        Toast.makeText(HoseActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    materialList.clear();
                                    Object[] strArray= result.entrySet().toArray();
                                    for(int i=0; i<strArray.length; i++){
                                        String s = strArray[i].toString();
                                        s = s.split("\"")[0];
                                        s = s.substring(0, s.length()-1);
                                        materialList.add(s);
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(HoseActivity.this, android.R.layout.simple_list_item_1, materialList){
                                        @Override
                                        public View getView(final int position, View convertView, ViewGroup parent) {
                                            View view =super.getView(position, convertView, parent);
                                            TextView textView=(TextView) view.findViewById(android.R.id.text1);
                                            textView.setTextColor(getResources().getColor(R.color.colorMyBlack));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    hoseMaterial = materialList.get(position);
                                                    layoutDrawer_Hose4.closeDrawer(listViewSelectMaterial);
                                                    materialMenuFlag = false;

                                                    btnSelectMaterial.setText(hoseMaterial);
                                                    SetSelected(btnSelectAccess);
                                                    btnSelectAccess.setText(getString(R.string.hose_menu_8));
                                                }
                                            });
                                            return view;
                                        }
                                    };
                                    listViewSelectMaterial.setAdapter(adapter);
                                    layoutDrawer_Hose1.closeDrawer(listViewSelectFittingType);
                                    fittingTypeMenuFlag = false;
                                    layoutDrawer_Hose1.closeDrawer(listViewSelectHoseType);
                                    hoseTypeMenuFlag = false;
                                    layoutDrawer_Hose2.closeDrawer(listViewSelectAngle);
                                    angleMenuFlag = false;
                                    layoutDrawer_Hose2.closeDrawer(listViewSelectFittingSize);
                                    fittingSizeMenuFlag = false;
                                    layoutDrawer_Hose3.closeDrawer(listViewSelectHoseSize);
                                    hoseSizeMenuFlag = false;
                                    layoutDrawer_Hose3.closeDrawer(listViewAdditionalOptions);
                                    addOptionMenuFlag = false;
                                    layoutDrawer_Hose4.openDrawer(listViewSelectMaterial);
                                    layoutDrawer_Hose4.closeDrawer(listViewSelectAccess);
                                    accessMenuFlag = false;
                                }
                            });
                }
                else {
                    layoutDrawer_Hose4.closeDrawer(listViewSelectMaterial);
                }
            }
        });

        btnSelectAccess= (Button) findViewById(R.id.btnSelectAccess);
        btnSelectAccess.setTypeface(gothamFont, Typeface.BOLD);
        SetUnselected(btnSelectAccess);
        btnSelectAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                accessMenuFlag = !(accessMenuFlag);

                if (accessMenuFlag) {
                    String apiUrl = "http://www.highgateair.com.au/hosefitting/index/material?material=" + encodeStringUri(hoseMaterial)
                            + "&other=" + encodeStringUri(hoseAddOptions)
                            + "&hosesize=" + encodeNumberUri(hoseHoseSize)
                            + "&fittingsize=" + encodeNumberUri(hoseFittingSize)
                            + "&angle=" + encodeStringUri(hoseAngle)
                            + "&fittingtype=" + encodeStringUri(hoseFittingType)
                            + "&hosetype=" + encodeStringUri(hoseHoseType) + "&api=true";
                    Ion.with(getApplicationContext())
                            .load(apiUrl)
                            .setTimeout(3000)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result == null || result.isJsonNull()) {
                                        Toast.makeText(HoseActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    accessList.clear();
                                    Object[] strArray= result.entrySet().toArray();
                                    for(int i=0; i<strArray.length; i++){
                                        String s = strArray[i].toString();
                                        s = s.split("\"")[0];
                                        s = s.substring(0, s.length()-1);
                                        accessList.add(s);
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(HoseActivity.this, android.R.layout.simple_list_item_1, accessList){
                                        @Override
                                        public View getView(final int position, View convertView, ViewGroup parent) {
                                            View view =super.getView(position, convertView, parent);
                                            TextView textView=(TextView) view.findViewById(android.R.id.text1);
                                            textView.setTextColor(getResources().getColor(R.color.colorMyBlack));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    hoseAccess = accessList.get(position);
                                                    layoutDrawer_Hose4.closeDrawer(listViewSelectAccess);
                                                    accessMenuFlag = false;
                                                    btnSelectAccess.setText(hoseAccess);
                                                    showContent();
                                                }
                                            });
                                            return view;
                                        }
                                    };
                                    listViewSelectAccess.setAdapter(adapter);
                                    layoutDrawer_Hose1.closeDrawer(listViewSelectFittingType);
                                    fittingTypeMenuFlag = false;
                                    layoutDrawer_Hose1.closeDrawer(listViewSelectHoseType);
                                    hoseTypeMenuFlag = false;
                                    layoutDrawer_Hose2.closeDrawer(listViewSelectAngle);
                                    angleMenuFlag = false;
                                    layoutDrawer_Hose2.closeDrawer(listViewSelectFittingSize);
                                    fittingSizeMenuFlag = false;
                                    layoutDrawer_Hose3.closeDrawer(listViewSelectHoseSize);
                                    hoseSizeMenuFlag = false;
                                    layoutDrawer_Hose3.closeDrawer(listViewAdditionalOptions);
                                    addOptionMenuFlag = false;
                                    layoutDrawer_Hose4.closeDrawer(listViewSelectMaterial);
                                    materialMenuFlag = false;
                                    layoutDrawer_Hose4.openDrawer(listViewSelectAccess);
                                }
                            });
                }
                else {
                    layoutDrawer_Hose4.closeDrawer(listViewSelectAccess);
                }
            }
        });
    }

    private void setReturn(){
        btnRtn = (Button)findViewById(R.id.btnRtn_Hose);
        btnRtn.setTypeface(iconFont);
        btnRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HoseActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtRtn = (TextView)findViewById(R.id.txtRtn_Hose);
        txtRtn.setTypeface(iconFont);
        txtRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HoseActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtTitle = (TextView)findViewById(R.id.txtHoseTitle);
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HoseActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showIntroduction(){
        SetSelected(btnSelectFittingType);
        btnSelectFittingType.setText(getString(R.string.hose_menu_1));
        SetUnselected(btnSelectHoseType);
        btnSelectHoseType.setText(getString(R.string.hose_menu_2));
        SetUnselected(btnSelectAngle);
        btnSelectAngle.setText(getString(R.string.hose_menu_3));
        SetUnselected(btnSelectFittingSize);
        btnSelectFittingSize.setText(getString(R.string.hose_menu_4));
        SetUnselected(btnSelectHoseSize);
        btnSelectHoseSize.setText(getString(R.string.hose_menu_5));
        SetUnselected(btnAdditionalOptions);
        btnAdditionalOptions.setText(getString(R.string.hose_menu_6));
        SetUnselected(btnSelectMaterial);
        btnSelectMaterial.setText(getString(R.string.hose_menu_7));
        SetUnselected(btnSelectAccess);
        btnSelectAccess.setText(getString(R.string.hose_menu_8));

        introduction.setVisibility(View.VISIBLE);
        layoutHoseContent.setVisibility(View.GONE);
    }

    private void showContent(){
        progressBar.setVisibility(View.VISIBLE);
        String apiUrl = "http://www.highgateair.com.au/hosefitting/index/search?access=" + encodeStringUri(hoseAccess)
                + "&material=" + encodeStringUri(hoseMaterial)
                + "&other=" + encodeStringUri(hoseAddOptions)
                + "&hosesize=" + encodeNumberUri(hoseHoseSize)
                + "&fittingsize=" + encodeNumberUri(hoseFittingSize)
                + "&angle=" + encodeStringUri(hoseAngle)
                + "&fittingtype=" + encodeStringUri(hoseFittingType)
                + "&hosetype=" + encodeStringUri(hoseHoseType)
                + "&group=" + String.valueOf(myUser.getGroup_id()) + "&api=true";


        Ion.with(getApplicationContext())
                .load(apiUrl)
                .setTimeout(3000)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        if (result == null || result.isJsonNull()) {
                            Toast.makeText(HoseActivity.this, "Sorry, Retry again...", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<Product> hoseList = new ArrayList<>();
                        String s;
                        for(int i=0; i<result.size(); i++){
                            Product oneHose = new Product();
                            oneHose.setProperty(result.get(i).getAsJsonObject());
                            hoseList.add(oneHose);
                        }

                        adapter = new ProductListAdapter(HoseActivity.this, R.layout.detail);
                        adapter.setActivity(HoseActivity.this);
                        adapter.setList(hoseList);
                        listViewSelectedHose.setAdapter(adapter);
                        progressBar.setVisibility(View.INVISIBLE);
                        introduction.setVisibility(View.GONE);
                        layoutHoseContent.setVisibility(View.VISIBLE);
                    }
                });
    }
}
