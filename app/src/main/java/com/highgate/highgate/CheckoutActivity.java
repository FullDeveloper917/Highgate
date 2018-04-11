package com.highgate.highgate;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.highgate.highgate.adapters.Product;
import com.highgate.highgate.myUtils.FontManager;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CheckoutActivity extends BaseActivity {

    private Button btnCheckout;
    private ImageView imgCheckout;

    private Button btnRtn;
    private TextView txtRtn;
    private TextView txtTitle;
    private TextView txtTotalPrice;

    private Button btnClearFields;
    private EditText edtReferenceNum;
    private Spinner spnFreight;
    private TextView txtFreight;
    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtAddress;
    private EditText edtSuburb;
    private EditText edtPostCode;
    private Spinner spnState;
    private TextView txtState;
    private EditText edtComments;

    private ProgressBar progressBar;

    private float AllTotalPrice;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Intent intent = getIntent();
        AllTotalPrice = intent.getFloatExtra("AllTotalPrice", 0.0f);

        FontManager.setFontType(findViewById(R.id.layout_Checkout), gothamFont);

        setMenuEvents(this);
        DrawerLayout layoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);
        layoutDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice_Checkout);
        txtTotalPrice.setTypeface(gothamFont, Typeface.BOLD);
        txtTotalPrice.setText("$" + String.format("%.2f", AllTotalPrice));

        btnCheckout = (Button) findViewById(R.id.btnCheckout_Checkout);
        btnCheckout.setTypeface(gothamFont, Typeface.BOLD);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                productOrder();
            }
        });

        imgCheckout = (ImageView) findViewById(R.id.imgCheckout_Checkout);
        imgCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                productOrder();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar_Checkout);

        btnClearFields = (Button) findViewById(R.id.btnClearFields_Checkout);
        edtReferenceNum = (EditText) findViewById(R.id.editReference_Checkout);
        spnFreight = (Spinner) findViewById(R.id.spnFreight_of_Checkout);
        txtFreight = (TextView) findViewById(R.id.txtFreight_of_Checkout);
        edtFirstName = (EditText) findViewById(R.id.editFName_Checkout);
        edtLastName = (EditText) findViewById(R.id.editLName_Checkout);
        edtAddress = (EditText) findViewById(R.id.editAddress_Checkout);
        edtSuburb = (EditText) findViewById(R.id.editSuburb_Checkout);
        edtPostCode = (EditText) findViewById(R.id.editPostCode_Checkout);
        spnState = (Spinner) findViewById(R.id.spnState_of_Checkout);
        txtState = (TextView) findViewById(R.id.txtState_of_Checkout);
        edtComments = (EditText) findViewById(R.id.editComment_Checkout);

        edtFirstName.setText(myUser.getFirstName());
        edtLastName.setText(myUser.getLastName());
        edtAddress.setText(myUser.getAddress());
        edtSuburb.setText(myUser.getSuburb());
        edtPostCode.setText(myUser.getPostCode());
        spnState.setSelection(getIdState(myUser.getState()));
        spnFreight.setSelection(getIdFreight(myUser.getFreightMethod()));
        txtFreight.setVisibility(View.INVISIBLE);
        txtState.setVisibility(View.INVISIBLE);

        btnClearFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtFirstName.setText("");
                edtLastName.setText("");
                edtAddress.setText("");
                edtSuburb.setText("");
                edtPostCode.setText("");
                spnState.setSelected(false);
                spnFreight.setSelected(false);
                txtFreight.setVisibility(View.VISIBLE);
                txtState.setVisibility(View.VISIBLE);
            }
        });

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

        setReturn();
    }

    private void setReturn(){
        btnRtn = (Button)findViewById(R.id.btnRtn_Checkout);
        btnRtn.setTypeface(iconFont);
        btnRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtRtn = (TextView)findViewById(R.id.txtRtn_Checkout);
        txtRtn.setTypeface(iconFont);
        txtRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtTitle = (TextView)findViewById(R.id.txtCheckoutTitle);
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
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


    private void productOrder() {
        if (orderProductList.size() == 0) {
            Toast.makeText(this, "Sorry, your cart is empty.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        String currentDate = getCurrentDate();

        JsonObject rawBody = new JsonObject();

        rawBody.addProperty("DebtorId", String.valueOf(BaseActivity.myUser.getDebtor_id()));
        rawBody.addProperty("status", "0");
        rawBody.addProperty("BranchId", "0");
        rawBody.addProperty("DefaultLocationId", "1");
        rawBody.addProperty("CurrencyId", "0");
        rawBody.addProperty("SalesPersonId", "4419");
        rawBody.addProperty("ExchangeRate", "1");
        rawBody.addProperty("Reference", "Highgate-App-Order");
        rawBody.addProperty("Instructions", "");
        rawBody.addProperty("orderdate", currentDate);
        rawBody.addProperty("duedate", currentDate);
        rawBody.addProperty("customerordernumber", edtReferenceNum.getText().toString());
        rawBody.addProperty("narrative", edtComments.getText().toString());
        rawBody.addProperty("contactid", "1");
        rawBody.addProperty("id", "1");

        JsonObject deliveryAddress = new JsonObject();
        deliveryAddress.addProperty("line1", "");
        deliveryAddress.addProperty("line2", edtAddress.getText().toString());
        deliveryAddress.addProperty("line3", edtSuburb.getText().toString());
        deliveryAddress.addProperty("line4", spnState.getSelectedItem().toString());
        deliveryAddress.addProperty("line5", edtPostCode.getText().toString());
        deliveryAddress.addProperty("line6", "");
        rawBody.add("DeliveryAddress", deliveryAddress);

        JsonArray lines = new JsonArray();
        for (int i = 0; i < orderProductList.size(); i++){
            Product product = orderProductList.get(i);
            JsonObject linesContent = new JsonObject();
            linesContent.addProperty("id", "1");
            linesContent.addProperty("stockcode", product.getSku());
            linesContent.addProperty("Description", product.getName());
            linesContent.addProperty("unitprice", product.getCustomer_price());
            linesContent.addProperty("orderquantity", String.valueOf(product.getNumber()));
            linesContent.addProperty("linetype", "0");
            linesContent.addProperty("IsOriginatedFromTemplate", "false");
            linesContent.addProperty("LocationId", "1");
            linesContent.addProperty("TaxRateId", "10");
            linesContent.addProperty("BomCode", "0");
            linesContent.addProperty("BomGroupId", "0");
            linesContent.addProperty("Discount", "0");
            linesContent.addProperty("IsPriceOverridden", "true");
            linesContent.addProperty("Narrative", "");
            linesContent.addProperty("BatchCode", "");
            linesContent.addProperty("DueDate", currentDate);
            lines.add(linesContent);
        }

        rawBody.add("lines", lines);

        JsonObject extrafieldsContent1 = new JsonObject();
        extrafieldsContent1.addProperty("key", "X_CONTACT");
        extrafieldsContent1.addProperty("value", edtFirstName.getText().toString() + " " + edtLastName.getText().toString());

        JsonObject extrafieldsContent2 = new JsonObject();
        extrafieldsContent2.addProperty("key", "X_HISTORY_NOTES");
        extrafieldsContent2.addProperty("value", spnFreight.getSelectedItem().toString());

        JsonArray extrafields = new JsonArray();
        extrafields.add(extrafieldsContent1);
        extrafields.add(extrafieldsContent2);
        rawBody.add("extrafields", extrafields);


        Ion.with(this)
                .load("POST", "https://exo.api.myob.com/salesorder")
                .setTimeout(5000)
                .setHeader("Authorization", BaseActivity.orderAPI_Authorization)
                .setHeader("x-myobapi-exotoken", BaseActivity.x_myobapi_exotoken)
                .setHeader("x-myobapi-key", BaseActivity.x_myobapi_key)
                .setHeader("Content-Type", BaseActivity.orderAPI_ContentType)
                .setJsonObjectBody(rawBody)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        JSONObject jsonResult = null;
                        progressBar.setVisibility(View.GONE);
                        try {
                            jsonResult = XML.toJSONObject(result);
                            int id = jsonResult.getJSONObject("SalesOrder").getInt("Id");
//                            Toast.makeText(CheckoutActivity.this,"Order Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CheckoutActivity.this, EndActivity.class);
                            intent.putExtra("OrderReference",  id);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e1) {
                            Toast.makeText(CheckoutActivity.this, "Order failed", Toast.LENGTH_SHORT).show();
                            e1.printStackTrace();
                        }
                    }
                });
    }

    private String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c.getTime());
    }
}
