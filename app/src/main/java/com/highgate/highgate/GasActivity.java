package com.highgate.highgate;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidessence.lib.RichTextView;
import com.google.gson.JsonObject;
import com.highgate.highgate.myUtils.FontManager;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class GasActivity extends BaseActivity {

    private TextView introduction;
    private Button btnRtn;
    private TextView txtRtn;
    private TextView txtTitle;

    private DrawerLayout layoutDrawer_Gas1;
    private DrawerLayout layoutDrawer_Gas2;
    private DrawerLayout layoutDrawer_Gas3;

    private Button btnSelectVehicleType;
    private Button btnSelectManufacturer;
    private Button btnSelectModelYear;

    /////////////////////////////////

    private List<String> vehicleTypeList = new ArrayList<>();
    private List<String> manufacturerList = new ArrayList<>();
    private List<String> modelYearList = new ArrayList<>();

    /////////////////////////////////

    private ListView listSelectVehicleType;
    private ListView listSelectManufacturer;
    private ListView listSelectModelYear;

    /////////////////////////////////

    private String gasVehicleType = "";
    private String gasManufacturer = "";
    private String gasModelYear = "";

    ////////////////////////////////

    private boolean vehicleTypeMenuFlag = false;
    private boolean manufacturerMenuFlag = false;
    private boolean modelYearMenuFlag = false;

    ////////////////////////////////

    private TextView txtClearGas;
    private Button btnClearGas;

    private LinearLayout layoutGasContent;

    private TextView txtGasCharge;
    private TextView txtOilType;
    private TextView txtOilQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas);

        FontManager.setFontType(findViewById(R.id.layout_Gas), gothamFont);

        setMenuEvents(this);
        DrawerLayout layoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);
        layoutDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        introduction = (TextView) findViewById(R.id.txtIntroduct_Gas);
//        introduction.setTypeface(gothamFont);
//        String str = introduction.getText().toString();
//        introduction.formatSpan(0, 12, RichTextView.FormatType.BOLD);
//        introduction.formatSpan(str.length()-36, str.length()-22, RichTextView.FormatType.BOLD);

        ////////////////////////////////////////////////////////

        layoutGasContent = (LinearLayout) findViewById(R.id.layoutGasContent);

        txtClearGas = (TextView) findViewById(R.id.txtClearGas);
        txtClearGas.setTypeface(iconFont);
        txtClearGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showIntroduction();
            }
        });

        btnClearGas = (Button) findViewById(R.id.btnClearGas);
        btnClearGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showIntroduction();
            }
        });

        txtGasCharge = (TextView) findViewById(R.id.txtGasCharge);
        txtGasCharge.setTypeface(gothamFont, Typeface.BOLD);
        txtOilType = (TextView) findViewById(R.id.txtOilType);
        txtOilType.setTypeface(gothamFont, Typeface.BOLD);
        txtOilQty = (TextView) findViewById(R.id.txtOilQty);
        txtOilQty.setTypeface(gothamFont, Typeface.BOLD);

        setGasMenu();
        setReturn();
    }

    private void setReturn(){
        btnRtn = (Button)findViewById(R.id.btnRtn_Gas);
        btnRtn.setTypeface(iconFont);
        btnRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GasActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtRtn = (TextView)findViewById(R.id.txtRtn_Gas);
        txtRtn.setTypeface(iconFont);
        txtRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GasActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtTitle = (TextView)findViewById(R.id.txtGasTitle);
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GasActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setGasMenu(){
        layoutDrawer_Gas1 = (DrawerLayout) findViewById(R.id.layoutDrawer_Gas1);
        layoutDrawer_Gas1.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        listSelectVehicleType = (ListView) findViewById(R.id.listSelectVehicleType);
        listSelectVehicleType.getLayoutParams().width = screenWidth;

        ///////////////////////////////////////////////////////////////////////////////////////////

        layoutDrawer_Gas2 = (DrawerLayout) findViewById(R.id.layoutDrawer_Gas2);
        layoutDrawer_Gas2.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        listSelectManufacturer = (ListView) findViewById(R.id.listSelectManufacturer);
        listSelectManufacturer.getLayoutParams().width = screenWidth;

        ///////////////////////////////////////////////////////////////////////////////////////////

        layoutDrawer_Gas3 = (DrawerLayout) findViewById(R.id.layoutDrawer_Gas3);
        layoutDrawer_Gas3.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        listSelectModelYear = (ListView) findViewById(R.id.listSelectModelYear);
        listSelectModelYear.getLayoutParams().width = screenWidth;

        ///////////////////////////////////////////////////////////////////////////////////////////

        btnSelectVehicleType = (Button) findViewById(R.id.btnSelectVehicleType);
        btnSelectVehicleType.setTypeface(gothamFont, Typeface.BOLD);
        SetSelected(btnSelectVehicleType);
        btnSelectVehicleType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vehicleTypeMenuFlag = !(vehicleTypeMenuFlag);

                if (vehicleTypeMenuFlag) {
                    String apiUrl = "http://www.highgateair.com.au/gascharge/index/update?api=true";
                    Ion.with(getApplicationContext())
                            .load(apiUrl)
                            .setTimeout(3000)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result == null || result.isJsonNull()) {
                                        Toast.makeText(GasActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    vehicleTypeList.clear();
                                    Object[] strArray= result.entrySet().toArray();
                                    for(int i=0; i<strArray.length; i++){
                                        String s = strArray[i].toString();
                                        s = s.split("\"")[0];
                                        s = s.substring(0, s.length()-1);
                                        vehicleTypeList.add(s);
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(GasActivity.this, android.R.layout.simple_list_item_1, vehicleTypeList){
                                        @Override
                                        public View getView(final int position, View convertView, ViewGroup parent) {
                                            View view =super.getView(position, convertView, parent);
                                            TextView textView=(TextView) view.findViewById(android.R.id.text1);
                                            textView.setTextColor(getResources().getColor(R.color.colorMyBlack));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    gasVehicleType = vehicleTypeList.get(position);
                                                    layoutDrawer_Gas1.closeDrawer(listSelectVehicleType);
                                                    vehicleTypeMenuFlag = false;
                                                    btnSelectVehicleType.setText(gasVehicleType);
                                                    SetSelected(btnSelectManufacturer);
                                                    btnSelectManufacturer.setText(getString(R.string.gas_menu_2));
                                                    SetUnselected(btnSelectModelYear);
                                                    btnSelectModelYear.setText(getString(R.string.gas_menu_3));
                                                }
                                            });
                                            return view;
                                        }
                                    };
                                    listSelectVehicleType.setAdapter(adapter);
                                    layoutDrawer_Gas1.openDrawer(listSelectVehicleType);
                                    layoutDrawer_Gas2.closeDrawer(listSelectManufacturer);
                                    manufacturerMenuFlag = false;
                                    layoutDrawer_Gas3.closeDrawer(listSelectModelYear);
                                    modelYearMenuFlag =false;
                                }
                            });
                }
                else {
                    layoutDrawer_Gas1.closeDrawer(listSelectVehicleType);
                }
            }
        });

        btnSelectManufacturer = (Button) findViewById(R.id.btnSelectManufacturer);
        btnSelectManufacturer.setTypeface(gothamFont, Typeface.BOLD);
        SetUnselected(btnSelectManufacturer);
        btnSelectManufacturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                manufacturerMenuFlag = !(manufacturerMenuFlag);

                if (manufacturerMenuFlag) {
                    String apiUrl = "http://www.highgateair.com.au/gascharge/index/update2?category1=" + encodeStringUri(gasVehicleType) + "&api=true";
                    Ion.with(getApplicationContext())
                            .load(apiUrl)
                            .setTimeout(3000)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result == null || result.isJsonNull()) {
                                        Toast.makeText(GasActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    manufacturerList.clear();
                                    Object[] strArray= result.entrySet().toArray();
                                    for(int i=0; i<strArray.length; i++){
                                        String s = strArray[i].toString();
                                        s = s.split("\"")[0];
                                        s = s.substring(0, s.length()-1);
                                        manufacturerList.add(s);
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(GasActivity.this, android.R.layout.simple_list_item_1, manufacturerList){
                                        @Override
                                        public View getView(final int position, View convertView, ViewGroup parent) {
                                            View view =super.getView(position, convertView, parent);
                                            TextView textView=(TextView) view.findViewById(android.R.id.text1);
                                            textView.setTextColor(getResources().getColor(R.color.colorMyBlack));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    gasManufacturer = manufacturerList.get(position);
                                                    layoutDrawer_Gas2.closeDrawer(listSelectManufacturer);
                                                    manufacturerMenuFlag = false;
                                                    btnSelectManufacturer.setText(gasManufacturer);
                                                    SetSelected(btnSelectModelYear);
                                                    btnSelectModelYear.setText(getString(R.string.gas_menu_3));
                                                }
                                            });
                                            return view;
                                        }
                                    };
                                    listSelectManufacturer.setAdapter(adapter);
                                    layoutDrawer_Gas1.closeDrawer(listSelectVehicleType);
                                    vehicleTypeMenuFlag = false;
                                    layoutDrawer_Gas2.openDrawer(listSelectManufacturer);
                                    layoutDrawer_Gas3.closeDrawer(listSelectModelYear);
                                    modelYearMenuFlag =false;
                                }
                            });
                }
                else {
                    layoutDrawer_Gas2.closeDrawer(listSelectManufacturer);
                }
            }
        });

        btnSelectModelYear = (Button) findViewById(R.id.btnSelectModelYear);
        btnSelectModelYear.setTypeface(gothamFont, Typeface.BOLD);
        SetUnselected(btnSelectModelYear);
        btnSelectModelYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                modelYearMenuFlag = !(modelYearMenuFlag);

                if (modelYearMenuFlag) {
                    String apiUrl = "http://www.highgateair.com.au/gascharge/index/update3?category2=" + encodeStringUri(gasManufacturer)
                            + "&category1=" + encodeStringUri(gasVehicleType) + "&api=true";
                    Ion.with(getApplicationContext())
                            .load(apiUrl)
                            .setTimeout(3000)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {

                                    if (result == null || result.isJsonNull()) {
                                        Toast.makeText(GasActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    modelYearList.clear();
                                    Object[] strArray= result.entrySet().toArray();
                                    for(int i=0; i<strArray.length; i++){
                                        String s = strArray[i].toString();
                                        s = s.split("\"")[0];
                                        s = s.substring(0, s.length()-1);
                                        modelYearList.add(s);
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(GasActivity.this, android.R.layout.simple_list_item_1, modelYearList){
                                        @Override
                                        public View getView(final int position, View convertView, ViewGroup parent) {
                                            View view =super.getView(position, convertView, parent);
                                            TextView textView=(TextView) view.findViewById(android.R.id.text1);
                                            textView.setTextColor(getResources().getColor(R.color.colorMyBlack));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    gasModelYear = modelYearList.get(position);
                                                    layoutDrawer_Gas3.closeDrawer(listSelectModelYear);
                                                    modelYearMenuFlag = false;
                                                    btnSelectModelYear.setText(gasModelYear);
                                                    showContent();
                                                }
                                            });
                                            return view;
                                        }
                                    };
                                    listSelectModelYear.setAdapter(adapter);
                                    layoutDrawer_Gas1.closeDrawer(listSelectVehicleType);
                                    vehicleTypeMenuFlag = false;
                                    layoutDrawer_Gas2.closeDrawer(listSelectManufacturer);
                                    modelYearMenuFlag = false;
                                    layoutDrawer_Gas3.openDrawer(listSelectModelYear);
                                }
                            });
                }
                else {
                    layoutDrawer_Gas3.closeDrawer(listSelectModelYear);
                }
            }
        });
    }

    private void showIntroduction(){
        SetSelected(btnSelectVehicleType);
        btnSelectVehicleType.setText(getString(R.string.gas_menu_1));
        SetUnselected(btnSelectManufacturer);
        btnSelectManufacturer.setText(getString(R.string.gas_menu_2));
        SetUnselected(btnSelectModelYear);
        btnSelectModelYear.setText(getString(R.string.gas_menu_3));

        introduction.setVisibility(View.VISIBLE);
        layoutGasContent.setVisibility(View.GONE);
    }

    private void showContent(){
        String apiUrl = "http://www.highgateair.com.au/gascharge/index/search";

        Ion.with(getApplicationContext())
                .load("POST", apiUrl)
                .setTimeout(5000)
                .setBodyParameter("category3", gasModelYear)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (result == null || result.equals("")){
                            Toast.makeText(GasActivity.this, "Sorry, Retry again...", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String[] gasInfo = getGasInfo(result);
                        txtGasCharge.setText(gasInfo[0]);
                        txtOilType.setText(gasInfo[1]);
                        txtOilQty.setText(gasInfo[2]);
                        introduction.setVisibility(View.GONE);
                        layoutGasContent.setVisibility(View.VISIBLE);
                    }
                });
    }

    private String[] getGasInfo(String text){
        String[] gasInfo = new String[3];
        int i = text.indexOf("Gas Charge:");
        int j = text.indexOf("Oil Type:");
        int k = text.indexOf("Oil Qty:");

        gasInfo[0] = replaceV(text.substring(i + 12, j - 12));
        gasInfo[1] = replaceV(text.substring(j + 10, k - 12));
        gasInfo[2] = replaceV(text.substring(k + 9, text.length() - 21));

        return gasInfo;
    }

    private String replaceV(String text){
        int i = text.indexOf("\\/");
        if ( i != -1 ) {
            text = text.substring(0, i) + " " + text.substring(i + 2, text.length()); //.replace("\\/", "V");
        }
        return text;
    }
}
