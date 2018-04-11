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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.highgate.highgate.adapters.Product;
import com.highgate.highgate.adapters.ProductListAdapter;
import com.highgate.highgate.myUtils.FontManager;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class VehicleActivity extends BaseActivity {

    private Button btnRtn;
    private TextView txtRtn;
    private TextView txtTitle;


    private Button btnSelectMake;
    private Button btnSelectModel;
    private Button btnSelectType;
    private Button btnSelectEngine;

    private DrawerLayout layoutDrawer_Vehicle1;
    private DrawerLayout layoutDrawer_Vehicle2;

    private ListView listViewSelectMake;
    private ListView listViewSelectModel;
    private ListView listViewSelectType;
    private ListView listViewSelectEngine;

    /////////////////////////////////

    private List<String> makeList = new ArrayList<>();
    private List<String> modelList = new ArrayList<>();
    private List<String> typeList = new ArrayList<>();
    private List<String> engineList = new ArrayList<>();

    //////////////////////////////////

    private String vehicleMake = "";
    private String vehicleModel = "";
    private String vehicleType = "";
    private String vehicleEngine = "";

    /////////////////////////////////

    private boolean makeMenuFlag = false;
    private boolean modelMenuFlag = false;
    private boolean typeMenuFlag = false;
    private boolean engineMenuFlag = false;

    private TextView introduction;
    private LinearLayout layoutVehicleContent;

    private List<Product> listSelectedVehicle;
    private ListView listViewSelectedVehicle;

    private TextView txtClearVehicle;
    private Button btnClearVehicle;



    private ProductListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        FontManager.setFontType(findViewById(R.id.layout_Vehicle), gothamFont);

        setMenuEvents(this);
        DrawerLayout layoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);
        layoutDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        introduction = (TextView) findViewById(R.id.txtIntroduct_Vehicle);
//        introduction.setTypeface(gothamFont);
//        String str = introduction.getText().toString();
//        introduction.formatSpan(str.length() - 16, str.length(), RichTextView.FormatType.BOLD);

        progressBar = (ProgressBar) findViewById(R.id.progressBar_Vehicle);

        ///////////////////////////////////////////

        adapter = new ProductListAdapter(VehicleActivity.this, R.layout.detail);

        listViewSelectedVehicle = (ListView) findViewById(R.id.listViewSelectedVehicle);
        layoutVehicleContent = (LinearLayout) findViewById(R.id.layoutVehicleContent);

        ///////////////////////////////////////////

        txtClearVehicle = (TextView) findViewById(R.id.txtClearVehicle);
        txtClearVehicle.setTypeface(iconFont);
        txtClearVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIntroduction();
            }
        });

        btnClearVehicle = (Button) findViewById(R.id.btnClearVehicle);
        btnClearVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIntroduction();
            }
        });

        TextView txtMark = (TextView) findViewById(R.id.txtMark_OrderReport);
        txtMark.setTypeface(iconFont);
        layoutOrderReport = (LinearLayout) findViewById(R.id.layoutOrderReport_Vehicle);
        txtOrderReference = (TextView) findViewById(R.id.txtOrderReference_OrderReport);
        btnCloseOrderReport = (Button) findViewById(R.id.btnClose_OrderReport);
        btnCloseOrderReport.setTypeface(iconFont);
        btnCloseOrderReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutOrderReport.setVisibility(View.GONE);
            }
        });

        layoutMain = (DrawerLayout) findViewById(R.id.layoutMain_Vehicle);
        layoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        layoutZoomImage = (RelativeLayout) findViewById(R.id.layoutZoomImage_Vehicle);
        layoutZoomImage.getLayoutParams().width = screenWidth;
        imgZoomImage = (ImageView) findViewById(R.id.imgZoomImage_Vehicle);
        btnZoomImageClose = (Button) findViewById(R.id.btnZoomImageClose_Vehicle);
        btnZoomImageClose.setTypeface(iconFont);
        btnZoomImageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutMain.closeDrawer(layoutZoomImage);
            }
        });

        setVehicleMenu();
        setReturn();
    }

    private void setReturn(){
        btnRtn = (Button)findViewById(R.id.btnRtn_Vehicle);
        btnRtn.setTypeface(iconFont);
        btnRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehicleActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtRtn = (TextView)findViewById(R.id.txtRtn_Vehicle);
        txtRtn.setTypeface(iconFont);
        txtRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehicleActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtTitle = (TextView)findViewById(R.id.txtVehicleTitle);
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehicleActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setVehicleMenu(){
        layoutDrawer_Vehicle1 = (DrawerLayout) findViewById(R.id.layoutDrawer_Vehicle1);
        layoutDrawer_Vehicle1.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        listViewSelectMake = (ListView) findViewById(R.id.listSelectMake);
        listViewSelectMake.getLayoutParams().width = screenWidth/2;

        listViewSelectModel = (ListView) findViewById(R.id.listSelectModel);
        listViewSelectModel.getLayoutParams().width = screenWidth/2;

        ///////////////////////////////////////////////////////////////////////////////////////////

        layoutDrawer_Vehicle2 = (DrawerLayout) findViewById(R.id.layoutDrawer_Vehicle2);
        layoutDrawer_Vehicle2.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        listViewSelectType = (ListView) findViewById(R.id.listSelectType);
        listViewSelectType.getLayoutParams().width = screenWidth / 2;

        listViewSelectEngine = (ListView) findViewById(R.id.listSelectEngine);
        listViewSelectEngine.getLayoutParams().width = screenWidth/2;

        ///////////////////////////////////////////////////////////////////////////////////////////

        btnSelectMake = (Button) findViewById(R.id.btnSelectMake);
        SetSelected(btnSelectMake);
        btnSelectMake.setTypeface(gothamFont, Typeface.BOLD);
        btnSelectMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeMenuFlag = !(makeMenuFlag);
                if(makeMenuFlag){
                    String apiUrl = "http://www.highgateair.com.au/multisearch/index/update?api=true";
                    Ion.with(getApplicationContext())
                            .load(apiUrl)
                            .setTimeout(3000)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result == null || result.isJsonNull()) {
                                            Toast.makeText(VehicleActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                                            return;
                                    }

                                    makeList.clear();
                                    Object[] strArray= result.entrySet().toArray();
                                    for(int i = 0; i < strArray.length; i++){
                                        String s = strArray[i].toString();
                                        s = s.split("\"")[0];
                                        s = s.substring(0, s.length()-1);
                                        makeList.add(s);
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(VehicleActivity.this, android.R.layout.simple_list_item_1, makeList){
                                        @Override
                                        public View getView(final int position, View convertView, ViewGroup parent) {
                                            View view =super.getView(position, convertView, parent);
                                            TextView textView=(TextView) view.findViewById(android.R.id.text1);
                                            textView.setTextColor(getResources().getColor(R.color.colorMyBlack));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    vehicleMake = makeList.get(position);
                                                    layoutDrawer_Vehicle1.closeDrawer(listViewSelectMake);
                                                    makeMenuFlag = false;
                                                    btnSelectMake.setText(vehicleMake);
                                                    SetSelected(btnSelectModel);
                                                    btnSelectModel.setText(getString(R.string.vehicle_menu_2));
                                                    SetUnselected(btnSelectType);
                                                    btnSelectType.setText(getString(R.string.vehicle_menu_3));
                                                    SetUnselected(btnSelectEngine);
                                                    btnSelectEngine.setText(getString(R.string.vehicle_menu_4));
                                                }
                                            });
                                            return view;
                                        }
                                    };
                                    listViewSelectMake.setAdapter(adapter);
                                    layoutDrawer_Vehicle1.openDrawer(listViewSelectMake);
                                    layoutDrawer_Vehicle1.closeDrawer(listViewSelectModel);
                                    modelMenuFlag = false;
                                    layoutDrawer_Vehicle2.closeDrawer(listViewSelectType);
                                    typeMenuFlag = false;
                                    layoutDrawer_Vehicle2.closeDrawer(listViewSelectEngine);
                                    engineMenuFlag = false;
                                }
                            });
                }
                else {
                    layoutDrawer_Vehicle1.closeDrawer(listViewSelectMake);
                }
            }
        });

        btnSelectModel = (Button) findViewById(R.id.btnSelectModel);
        btnSelectModel.setTypeface(gothamFont, Typeface.BOLD);
        SetUnselected(btnSelectModel);
        btnSelectModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                modelMenuFlag = !(modelMenuFlag);

                if(modelMenuFlag){
                    String apiUrl = "http://www.highgateair.com.au/multisearch/index/update2?make=" + encodeStringUri(vehicleMake) + "&api=true";
                    Ion.with(getApplicationContext())
                            .load(apiUrl)
                            .setTimeout(3000)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result == null || result.isJsonNull()) {
                                        Toast.makeText(VehicleActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    modelList.clear();
                                    Object[] strArray= result.entrySet().toArray();
                                    for(int i = 0; i < strArray.length; i++){
                                        String s = strArray[i].toString();
                                        s = s.split("\"")[0];
                                        s = s.substring(0, s.length()-1);
                                        modelList.add(s);
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(VehicleActivity.this, android.R.layout.simple_list_item_1, modelList){
                                        @Override
                                        public View getView(final int position, View convertView, ViewGroup parent) {
                                            final View view =super.getView(position, convertView, parent);
                                            TextView textView=(TextView) view.findViewById(android.R.id.text1);
                                            textView.setTextColor(getResources().getColor(R.color.colorMyBlack));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    vehicleModel = modelList.get(position);
                                                    layoutDrawer_Vehicle1.closeDrawer(listViewSelectModel);
                                                    modelMenuFlag = false;
                                                    btnSelectModel.setText(vehicleModel);
                                                    SetSelected(btnSelectType);
                                                    btnSelectType.setText(getString(R.string.vehicle_menu_3));
                                                    SetUnselected(btnSelectEngine);
                                                    btnSelectEngine.setText(getString(R.string.vehicle_menu_4));
                                                }
                                            });
                                            return view;
                                        }
                                    };
                                    listViewSelectModel.setAdapter(adapter);
                                    layoutDrawer_Vehicle1.closeDrawer(listViewSelectMake);
                                    makeMenuFlag = false;
                                    layoutDrawer_Vehicle1.openDrawer(listViewSelectModel);
                                    layoutDrawer_Vehicle2.closeDrawer(listViewSelectType);
                                    typeMenuFlag = false;
                                    layoutDrawer_Vehicle2.closeDrawer(listViewSelectEngine);
                                    engineMenuFlag = false;
                                }
                            });
                }
                else {
                    layoutDrawer_Vehicle1.closeDrawer(listViewSelectModel);
                }
            }
        });

        btnSelectType = (Button) findViewById(R.id.btnSelectType);
        btnSelectType.setTypeface(gothamFont, Typeface.BOLD);
        SetUnselected(btnSelectType);
        btnSelectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                typeMenuFlag = !(typeMenuFlag);

                if (typeMenuFlag){
                    String apiUrl = "http://www.highgateair.com.au/multisearch/index/update3?make="+ encodeStringUri(vehicleMake)
                            + "&model=" + encodeStringUri(vehicleModel) + "&api=true";
                    Ion.with(getApplicationContext())
                            .load(apiUrl)
                            .setTimeout(3000)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result == null || result.isJsonNull()) {
                                        Toast.makeText(VehicleActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    typeList.clear();
                                    Object[] strArray= result.entrySet().toArray();
                                    for(int i = 0; i < strArray.length; i++){
                                        String s = strArray[i].toString();
                                        s = s.split("\"")[0];
                                        s = s.substring(0, s.length()-1);
                                        typeList.add(s);
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(VehicleActivity.this, android.R.layout.simple_list_item_1, typeList){
                                        @Override
                                        public View getView(final int position, View convertView, ViewGroup parent) {
                                            View view =super.getView(position, convertView, parent);
                                            TextView textView=(TextView) view.findViewById(android.R.id.text1);
                                            textView.setTextColor(getResources().getColor(R.color.colorMyBlack));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    vehicleType = typeList.get(position);
                                                    layoutDrawer_Vehicle2.closeDrawer(listViewSelectType);
                                                    typeMenuFlag = false;
                                                    btnSelectType.setText(vehicleType);
                                                    SetSelected(btnSelectEngine);
                                                    btnSelectEngine.setText(getString(R.string.vehicle_menu_4));
                                                }
                                            });
                                            return view;
                                        }
                                    };
                                    listViewSelectType.setAdapter(adapter);
                                    layoutDrawer_Vehicle1.closeDrawer(listViewSelectMake);
                                    makeMenuFlag = false;
                                    layoutDrawer_Vehicle1.closeDrawer(listViewSelectModel);
                                    modelMenuFlag = false;
                                    layoutDrawer_Vehicle2.openDrawer(listViewSelectType);
                                    layoutDrawer_Vehicle2.closeDrawer(listViewSelectEngine);
                                    engineMenuFlag = false;
                                }
                            });
                }
                else {
                    layoutDrawer_Vehicle2.closeDrawer(listViewSelectType);
                }
            }
        });

        btnSelectEngine = (Button) findViewById(R.id.btnSelectEngine);
        btnSelectEngine.setTypeface(gothamFont, Typeface.BOLD);
        SetUnselected(btnSelectEngine);
        btnSelectEngine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                engineMenuFlag = !(engineMenuFlag);

                if(engineMenuFlag){
                    String apiUrl = "http://www.highgateair.com.au/multisearch/index/update4?make=" + encodeStringUri(vehicleMake)
                            + "&model=" + encodeStringUri(vehicleModel)
                            + "&type=" + encodeStringUri(vehicleType) + "&api=true";
                    Ion.with(getApplicationContext())
                            .load(apiUrl)
                            .setTimeout(3000)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result == null || result.isJsonNull()) {
                                        Toast.makeText(VehicleActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    engineList.clear();
                                    Object[] strArray= result.entrySet().toArray();
                                    for(int i = 0; i < strArray.length; i++){
                                        String s = strArray[i].toString();
                                        s = s.split("\"")[0];
                                        s = s.substring(0, s.length()-1);
                                        engineList.add(s);
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(VehicleActivity.this, android.R.layout.simple_list_item_1, engineList){
                                        @Override
                                        public View getView(final int position, View convertView, ViewGroup parent) {
                                            View view =super.getView(position, convertView, parent);
                                            TextView textView=(TextView) view.findViewById(android.R.id.text1);
                                            textView.setTextColor(getResources().getColor(R.color.colorMyBlack));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    vehicleEngine = engineList.get(position);;
                                                    layoutDrawer_Vehicle2.closeDrawer(listViewSelectEngine);
                                                    engineMenuFlag = false;
                                                    btnSelectEngine.setText(vehicleEngine);
                                                    showContent();
                                                }
                                            });
                                            return view;
                                        }
                                    };
                                    listViewSelectEngine.setAdapter(adapter);
                                    layoutDrawer_Vehicle1.closeDrawer(listViewSelectMake);
                                    makeMenuFlag = false;
                                    layoutDrawer_Vehicle1.closeDrawer(listViewSelectModel);
                                    modelMenuFlag = false;
                                    layoutDrawer_Vehicle2.closeDrawer(listViewSelectType);
                                    typeMenuFlag = false;
                                    layoutDrawer_Vehicle2.openDrawer(listViewSelectEngine);
                                }
                            });
                }
                else {
                    layoutDrawer_Vehicle2.closeDrawer(listViewSelectEngine);
                }
            }
        });
    }

    private void showIntroduction(){

        SetSelected(btnSelectMake);
        btnSelectMake.setText(getString(R.string.vehicle_menu_1));
        SetUnselected(btnSelectModel);
        btnSelectModel.setText(getString(R.string.vehicle_menu_2));
        SetUnselected(btnSelectType);
        btnSelectType.setText(getString(R.string.vehicle_menu_3));
        SetUnselected(btnSelectEngine);
        btnSelectEngine.setText(getString(R.string.vehicle_menu_4));

        introduction.setVisibility(View.VISIBLE);
        layoutVehicleContent.setVisibility(View.GONE);
    }

    private void showContent(){
        progressBar.setVisibility(View.VISIBLE);
        String apiUrl = "http://www.highgateair.com.au/multisearch/index/search?engine=" + encodeStringUri(vehicleEngine)
                + "&make=" + encodeStringUri(vehicleMake)
                + "&model=" + encodeStringUri(vehicleModel)
                + "&type=" + encodeStringUri(vehicleType)
                + "&limit=100" + "&group=" + String.valueOf(myUser.getGroup_id()) + "&api=true";

        Ion.with(getApplicationContext())
                .load(apiUrl)
                .setTimeout(5000)
                .asJsonArray()
                .setCallback( new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        if (result == null || result.isJsonNull()) {
                            Toast.makeText(VehicleActivity.this, "Sorry, Retry again...", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<Product> vehicleList = new ArrayList<>();
                        String s;
                        for(int i=0; i<result.size(); i++){
                            Product oneVehicle = new Product();
                            oneVehicle.setProperty(result.get(i).getAsJsonObject());
                            vehicleList.add(oneVehicle);
                        }

                        adapter.setList(vehicleList);
                        adapter.setActivity(VehicleActivity.this);

                        listViewSelectedVehicle.setAdapter(adapter);
                        progressBar.setVisibility(View.INVISIBLE);
                        introduction.setVisibility(View.GONE);
                        layoutVehicleContent.setVisibility(View.VISIBLE);
                    }
                });
   }
}
