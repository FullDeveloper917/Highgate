package com.highgate.highgate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.highgate.highgate.myUtils.FontManager;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class DashboardActivity extends BaseActivity {

    private ImageView imgLogo;
    private ImageView imgDownLogo;

    private Button btnVehicle;
    private Button btnHose;
    private Button btnGas;
    private Button btnBrowse;

    private TextView txtVehicle;
    private TextView txtHose;
    private TextView txtGas;
    private TextView txtBrowse;

    private boolean searchFlag;
    private Button btnSearchSku;
    private Button btnSearchWord;
    private EditText edtSearchKey;
    private Button btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getCategories();

        DrawerLayout layoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);
        layoutDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        edtSearchKey = (EditText) findViewById(R.id.editSearch_Dashboard);

        setEvents();
        setFormula();

        setMenuEvents(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        edtSearchKey.setText("");
    }

    private void setFormula(){

        FontManager.setFontType(findViewById(R.id.layout_Dashboard), gothamFont);

        imgLogo = (ImageView) findViewById(R.id.imgLogo_Dashboard);
        imgLogo.getLayoutParams().height = screenWidth * 578 / 1911;

        imgDownLogo = (ImageView) findViewById(R.id.imgDownLogo_Dashboard);
        imgDownLogo.getLayoutParams().height = screenWidth * 259 / 1911;

        TextView txtSearchImg = (TextView) findViewById(R.id.txtSearchImg);
        txtSearchImg.setTypeface(iconFont);
        txtSearchImg.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 5);
        txtSearchImg.setPadding(unitFontSize * 3, unitFontSize * 2, unitFontSize * 3, unitFontSize * 2);

        txtVehicle.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 13);
        txtVehicle.setTypeface(iconFont);
        txtVehicle.setPadding(unitFontSize, unitFontSize, unitFontSize, unitFontSize);

        txtHose.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 13);
        txtHose.setTypeface(iconFont);
        txtHose.setPadding(unitFontSize, unitFontSize, unitFontSize, unitFontSize);

        txtGas.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 13);
        txtGas.setTypeface(iconFont);
        txtGas.setPadding(unitFontSize, unitFontSize, unitFontSize, unitFontSize);

        txtBrowse.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 13);
        txtBrowse.setTypeface(iconFont);
        txtBrowse.setPadding(unitFontSize, unitFontSize, unitFontSize, unitFontSize);

        btnVehicle.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 4);
        btnVehicle.setTypeface(gothamFont, Typeface.BOLD);
        btnHose.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 4);
        btnHose.setTypeface(gothamFont, Typeface.BOLD);
        btnGas.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 4);
        btnGas.setTypeface(gothamFont, Typeface.BOLD);
        btnBrowse.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 4);
        btnBrowse.setTypeface(gothamFont, Typeface.BOLD);
    }

    private void setEvents(){
        btnVehicle = (Button)findViewById(R.id.btnVehicle);
        btnVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, VehicleActivity.class);
                startActivity(intent);
            }
        });

        btnHose = (Button)findViewById(R.id.btnHose);
        btnHose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, HoseActivity.class);
                startActivity(intent);
            }
        });

        btnGas = (Button)findViewById(R.id.btnGas);
        btnGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, GasActivity.class);
                startActivity(intent);
            }
        });


        btnBrowse = (Button)findViewById(R.id.btnBrowse);
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MyAccountActivity.class);
                startActivity(intent);
            }
        });
//
//        edtSearchKey.setImeActionLabel("GO", KeyEvent.KEYCODE_ENTER);
        edtSearchKey.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO)  {

                    btnGo.performClick();
                    return true;
                }
                return false;
            }
        });

        btnGo = (Button) findViewById(R.id.btnGo_Dashboard);
        btnGo.setTypeface(gothamFont, Typeface.BOLD);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hide virtual keyboard
                InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtSearchKey.getWindowToken(), 0);

                Intent intent = new Intent(DashboardActivity.this, SearchActivity.class);
                intent.putExtra("SearchFlag", searchFlag);
                intent.putExtra("SearchKey", edtSearchKey.getText().toString());
                startActivity(intent);
            }
        });

        txtVehicle = (TextView) findViewById(R.id.txtVehicle);
        txtVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, VehicleActivity.class);
                startActivity(intent);
            }
        });

        txtHose = (TextView) findViewById(R.id.txtHose);
        txtHose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, HoseActivity.class);
                startActivity(intent);
            }
        });

        txtGas = (TextView) findViewById(R.id.txtGas);
        txtGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, GasActivity.class);
                startActivity(intent);
            }
        });

        txtBrowse = (TextView) findViewById(R.id.txtBrowse);
        txtBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MyAccountActivity.class);
                startActivity(intent);
            }
        });

        searchFlag = false;
        btnSearchSku = (Button) findViewById(R.id.btnSearchSku_Dashboard);
        btnSearchSku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSearchSku.setBackgroundColor(getResources().getColor(R.color.colorMyMain));
                btnSearchSku.setTextColor(getResources().getColor(R.color.colorMyWhite));
                btnSearchWord.setBackgroundColor(getResources().getColor(R.color.colorMyBlack));
                btnSearchWord.setTextColor(getResources().getColor(R.color.colorMyDarkGray));
                edtSearchKey.setHint(getResources().getString(R.string.search_sku_hint));
                searchFlag = false;
            }
        });

        btnSearchWord = (Button) findViewById(R.id.btnSearchWord_Dashboard);
        btnSearchWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSearchSku.setBackgroundColor(getResources().getColor(R.color.colorMyBlack));
                btnSearchSku.setTextColor(getResources().getColor(R.color.colorMyDarkGray));
                btnSearchWord.setBackgroundColor(getResources().getColor(R.color.colorMyMain));
                btnSearchWord.setTextColor(getResources().getColor(R.color.colorMyWhite));
                edtSearchKey.setHint(getResources().getString(R.string.search_word_hint));
                searchFlag = true;
            }
        });

    }

    private void getCategories(){
        categoriesMap.clear();
        categoriesNameList.clear();
        Ion.with(getApplicationContext())
                .load("http://www.highgateair.com.au/productcategory/index")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (result == null || result.isJsonNull()){
                            Toast.makeText(DashboardActivity.this, "Network error!!!\nAfter check your network, please restart.", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                        for (int i = 0; i < result.size(); i++){
                            int id = result.get(i).getAsJsonObject().get("id").getAsInt();
                            String name = result.get(i).getAsJsonObject().get("name").getAsString();
                            categoriesMap.put(name, id);
                            categoriesNameList.add(name);

                        }
                    }

                });
    }

}
