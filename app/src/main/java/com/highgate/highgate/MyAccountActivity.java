package com.highgate.highgate;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.highgate.highgate.Fragments.MyAccountMainFragment;
import com.highgate.highgate.adapters.OrderItem;
import com.highgate.highgate.adapters.OrderItemDetail;
import com.highgate.highgate.adapters.InvoiceItem;
import com.highgate.highgate.adapters.InvoiceItemDetail;
import com.highgate.highgate.myUtils.FontManager;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MyAccountActivity extends BaseActivity {

    private Button btnRtn;
    private TextView txtRtn;
    private TextView txtTitle;

    public boolean balanceOrPaymentFlag = true;
    public boolean fragmentMainFlag = true;

    private MyAccountMainFragment myAccountMainFragment;

    public double balance = 0.0;
    public double currentBalance = 0.0;
    public double oneMonthBalance = 0.0;
    public double twoMonthsBalance = 0.0;
    public double threeMonthBalance = 0.0;
    public String companyName = "";

    public List<OrderItem> orderItemList = new ArrayList<>();
    public List<InvoiceItem> invoiceItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        FontManager.setFontType(findViewById(R.id.layout_MyAccount), gothamFont);

        setMenuEvents(this);

        fragmentMainFlag = true;
        balanceOrPaymentFlag = true;

        myAccountMainFragment = new MyAccountMainFragment();
        myAccountMainFragment.setMyActivity(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frgMyAccount, myAccountMainFragment).commit();
        setReturn();

        dataDownload();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //do your stuff
            btnRtn.performClick();
            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void setReturn(){

        btnRtn = (Button)findViewById(R.id.btnRtn_MyAccount);
        btnRtn.setTypeface(iconFont);
        btnRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentMainFlag) {
                    Intent intent = new Intent(MyAccountActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frgMyAccount, myAccountMainFragment).commit();
                    fragmentMainFlag = true;
                }
            }
        });

        txtRtn = (TextView)findViewById(R.id.txtRtn_MyAccount);
        txtRtn.setTypeface(iconFont);
        txtRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRtn.performClick();
            }
        });

        txtTitle = (TextView)findViewById(R.id.txtMyAccountTitle);
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRtn.performClick();
            }
        });
    }

    private void dataDownload(){
        Ion.with(this)
                .load("GET", "https://exo.api.myob.com/debtor/" + myUser.getDebtor_id())
                .setHeader("Authorization", BaseActivity.orderAPI_Authorization)
                .setHeader("x-myobapi-exotoken", BaseActivity.x_myobapi_exotoken)
                .setHeader("x-myobapi-key", BaseActivity.x_myobapi_key)
                .setHeader("Content-Type", BaseActivity.orderAPI_ContentType)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (result == null){
                            Toast.makeText(MyAccountActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JSONObject jsonResult = null;
                        try {
                            jsonResult = XML.toJSONObject(result);
                            currentBalance = jsonResult.getJSONObject("Debtor").getDouble("AgedBal0");
                            oneMonthBalance = jsonResult.getJSONObject("Debtor").getDouble("AgedBal1");
                            twoMonthsBalance = jsonResult.getJSONObject("Debtor").getDouble("AgedBal2");
                            threeMonthBalance = jsonResult.getJSONObject("Debtor").getDouble("AgedBal3");
                            balance = jsonResult.getJSONObject("Debtor").getDouble("Balance");
                            companyName = jsonResult.getJSONObject("Debtor").getString("AccountName");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        myAccountMainFragment.displayData();
                    }
                });

        Ion.with(this)
                .load("GET", "https://exo.api.myob.com/salesorder?$filter=OrderDate+le+'" + getCurrentDate()
                                    + "'+and+debtorid+eq+'"+ myUser.getDebtor_id()+"'&$orderby=Id+desc&pagesize=50")
                .setHeader("Authorization", BaseActivity.orderAPI_Authorization)
                .setHeader("x-myobapi-exotoken", BaseActivity.x_myobapi_exotoken)
                .setHeader("x-myobapi-key", BaseActivity.x_myobapi_key)
                .setHeader("Content-Type", BaseActivity.orderAPI_ContentType)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (result == null){
                            Toast.makeText(MyAccountActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            JSONObject jsonResult = XML.toJSONObject(result);
                            JSONArray jsonArray = jsonResult.getJSONObject("ArrayOfSalesOrder").getJSONArray("SalesOrder");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                OrderItem oneOrderItem = new OrderItem();

                                if (!jsonArray.getJSONObject(i).isNull("CreateDate"))
                                oneOrderItem.setDate(convertDateStyle(jsonArray.getJSONObject(i).getString("CreateDate")));

                                if (!jsonArray.getJSONObject(i).isNull("Id"))
                                oneOrderItem.setId(jsonArray.getJSONObject(i).getInt("Id"));

                                if (!jsonArray.getJSONObject(i).isNull("Status"))
                                oneOrderItem.setStatus(jsonArray.getJSONObject(i).getInt("Status"));

                                if (!jsonArray.getJSONObject(i).isNull("OrderTotal"))
                                oneOrderItem.setOrderTotal(jsonArray.getJSONObject(i).getDouble("OrderTotal"));

                                if (!jsonArray.getJSONObject(i).isNull("SubTotal"))
                                oneOrderItem.setSubTotal(jsonArray.getJSONObject(i).getDouble("SubTotal"));

                                if (!jsonArray.getJSONObject(i).isNull("CustomerOrderNumber"))
                                oneOrderItem.setCustomerOrderNumber(jsonArray.getJSONObject(i).getString("CustomerOrderNumber"));

                                if (!jsonArray.getJSONObject(i).isNull("ExtraFields")) {
                                    JSONArray extraFieldsJsonArray = jsonArray.getJSONObject(i).getJSONArray("ExtraFields");

                                    if (!extraFieldsJsonArray.getJSONObject(1).isNull("value"))
                                        oneOrderItem.setDispatchMethod(extraFieldsJsonArray.getJSONObject(1).getString("value"));

                                    if (!extraFieldsJsonArray.getJSONObject(3).isNull("value"))
                                        oneOrderItem.setConeNoteN(extraFieldsJsonArray.getJSONObject(3).getString("value"));
                                }

                                if (!jsonArray.getJSONObject(i).isNull("Href"))
                                oneOrderItem.setHref(jsonArray.getJSONObject(i).getString("Href"));

                                try {
                                    JSONArray lineJsonArray = jsonArray.getJSONObject(i).getJSONArray("Lines");

                                    for (int j = 0; j < lineJsonArray.length(); j++) {

                                        OrderItemDetail oneOrderItemDetail = new OrderItemDetail();

                                        if (!lineJsonArray.getJSONObject(j).isNull("StockCode"))
                                            oneOrderItemDetail.setStockCode(lineJsonArray.getJSONObject(j).getString("StockCode"));

                                        if (!lineJsonArray.getJSONObject(j).isNull("Description"))
                                            oneOrderItemDetail.setDescription(lineJsonArray.getJSONObject(j).getString("Description"));

                                        if (!lineJsonArray.getJSONObject(j).isNull("OrderQuantity"))
                                            oneOrderItemDetail.setQuantity(lineJsonArray.getJSONObject(j).getInt("OrderQuantity"));

                                        if (!lineJsonArray.getJSONObject(j).isNull("UnitPrice"))
                                            oneOrderItemDetail.setUnitPrice(lineJsonArray.getJSONObject(j).getDouble("UnitPrice"));

                                        if (!lineJsonArray.getJSONObject(j).isNull("LineTotal"))
                                            oneOrderItemDetail.setLineTotal(lineJsonArray.getJSONObject(j).getDouble("LineTotal"));

                                        oneOrderItem.addListItem(oneOrderItemDetail);
                                    }
                                }
                                catch (JSONException e2) {

                                    OrderItemDetail oneOrderItemDetail = new OrderItemDetail();

                                    if (!jsonArray.getJSONObject(i).getJSONObject("Lines").isNull("StockCode"))
                                        oneOrderItemDetail.setStockCode(jsonArray.getJSONObject(i).getJSONObject("Lines").getString("StockCode"));

                                    if (!jsonArray.getJSONObject(i).getJSONObject("Lines").isNull("Description"))
                                        oneOrderItemDetail.setDescription(jsonArray.getJSONObject(i).getJSONObject("Lines").getString("Description"));

                                    if (!jsonArray.getJSONObject(i).getJSONObject("Lines").isNull("OrderQuantity"))
                                        oneOrderItemDetail.setQuantity(jsonArray.getJSONObject(i).getJSONObject("Lines").getInt("OrderQuantity"));

                                    if (!jsonArray.getJSONObject(i).getJSONObject("Lines").isNull("UnitPrice"))
                                        oneOrderItemDetail.setUnitPrice(jsonArray.getJSONObject(i).getJSONObject("Lines").getDouble("UnitPrice"));

                                    if (!jsonArray.getJSONObject(i).getJSONObject("Lines").isNull("LineTotal"))
                                        oneOrderItemDetail.setLineTotal(jsonArray.getJSONObject(i).getJSONObject("Lines").getDouble("LineTotal"));

                                    oneOrderItem.addListItem(oneOrderItemDetail);
                                }

                                orderItemList.add(oneOrderItem);
                                myAccountMainFragment.displayData();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

        Ion.with(this)
                .load("GET", "https://exo.api.myob.com/debtor/" + myUser.getDebtor_id()
                        + "/transaction?$filter=Ref3+eq+'Invoice'&$orderby=InvoiceNumber+desc&pagesize=50")

                .setHeader("Authorization", BaseActivity.orderAPI_Authorization)
                .setHeader("x-myobapi-exotoken", BaseActivity.x_myobapi_exotoken)
                .setHeader("x-myobapi-key", BaseActivity.x_myobapi_key)
                .setHeader("Content-Type", BaseActivity.orderAPI_ContentType)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (result == null){
                            Toast.makeText(MyAccountActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JSONArray jsonArray = null;
                        JSONObject jsonResult = null;
                        try {
                            jsonResult = XML.toJSONObject(result);
                            jsonArray = jsonResult.getJSONObject("ArrayOfDebtorTransaction").getJSONArray("DebtorTransaction");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                InvoiceItem oneInvoiceItem = new InvoiceItem();

                                if (!jsonArray.getJSONObject(i).isNull("TransactionDate"))
                                    oneInvoiceItem.setDate(convertDateStyle(jsonArray.getJSONObject(i).getString("TransactionDate")));


                                if (!jsonArray.getJSONObject(i).isNull("Id"))
                                    oneInvoiceItem.setId(jsonArray.getJSONObject(i).getInt("Id"));

                                if (!jsonArray.getJSONObject(i).isNull("TransactionType"))
                                    oneInvoiceItem.setType(jsonArray.getJSONObject(i).getString("TransactionType"));

                                if (!jsonArray.getJSONObject(i).isNull("Amount"))
                                    oneInvoiceItem.setAmount(jsonArray.getJSONObject(i).getDouble("Amount"));

                                if (!jsonArray.getJSONObject(i).isNull("InvoiceNumber"))
                                    oneInvoiceItem.setInvoiceN(jsonArray.getJSONObject(i).getString("InvoiceNumber"));

                                if (!jsonArray.getJSONObject(i).isNull("ExtraFields")) {
                                    JSONArray extraFieldsJsonArray = jsonArray.getJSONObject(i).getJSONArray("ExtraFields");

                                    if (!extraFieldsJsonArray.getJSONObject(1).isNull("value"))
                                        oneInvoiceItem.setDispatchMethod(extraFieldsJsonArray.getJSONObject(1).getString("value"));

                                    if (!extraFieldsJsonArray.getJSONObject(2).isNull("value"))
                                        oneInvoiceItem.setConeNoteN(extraFieldsJsonArray.getJSONObject(2).getString("value"));
                                }

                                if (!jsonArray.getJSONObject(i).isNull("Status"))
                                    oneInvoiceItem.setStatus(jsonArray.getJSONObject(i).getString("Status"));

                                if (!jsonArray.getJSONObject(i).isNull("Href"))
                                    oneInvoiceItem.setHref(jsonArray.getJSONObject(i).getString("Href"));

                                if (!jsonArray.getJSONObject(i).isNull("Reference1"))
                                    oneInvoiceItem.setOrderN(jsonArray.getJSONObject(i).getString("Reference1"));

                                if (!jsonArray.getJSONObject(i).isNull("Reference2"))
                                    oneInvoiceItem.setCustomerOrderNumber(jsonArray.getJSONObject(i).getString("Reference2"));

                                double itemSubtotal = 0.0;

                                try {
                                    JSONArray lineJsonArray = jsonArray.getJSONObject(i).getJSONArray("Lines");

                                    for (int j = 0; j < lineJsonArray.length(); j++) {

                                        InvoiceItemDetail transactionItemDetail = new InvoiceItemDetail();

                                        if (!lineJsonArray.getJSONObject(j).isNull("StockCode"))
                                            transactionItemDetail.setStockCode(lineJsonArray.getJSONObject(j).getString("StockCode"));


                                        if (!lineJsonArray.getJSONObject(j).isNull("Description"))
                                            transactionItemDetail.setDescription(lineJsonArray.getJSONObject(j).getString("Description"));

                                        if (!lineJsonArray.getJSONObject(j).isNull("Quantity"))
                                            transactionItemDetail.setQuantity(lineJsonArray.getJSONObject(j).getInt("Quantity"));

                                        if (!lineJsonArray.getJSONObject(j).isNull("UnitPrice"))
                                            transactionItemDetail.setUnitPrice(lineJsonArray.getJSONObject(j).getDouble("UnitPrice"));

                                        if (!lineJsonArray.getJSONObject(j).isNull("Total")) {
                                            double itemLineTotal = lineJsonArray.getJSONObject(j).getDouble("Total");
                                            transactionItemDetail.setLineTotal(lineJsonArray.getJSONObject(j).getDouble("Total"));
                                            itemSubtotal += itemLineTotal;
                                        }

                                        oneInvoiceItem.addListItem(transactionItemDetail);
                                    }
                                }
                                catch (JSONException e2) {

                                    InvoiceItemDetail transactionItemDetail = new InvoiceItemDetail();

                                    if (!jsonArray.getJSONObject(i).getJSONObject("Lines").isNull("StockCode"))
                                        transactionItemDetail.setStockCode(jsonArray.getJSONObject(i).getJSONObject("Lines").getString("StockCode"));


                                    if (!jsonArray.getJSONObject(i).getJSONObject("Lines").isNull("Description"))
                                        transactionItemDetail.setDescription(jsonArray.getJSONObject(i).getJSONObject("Lines").getString("Description"));

                                    if (!jsonArray.getJSONObject(i).getJSONObject("Lines").isNull("Quantity"))
                                        transactionItemDetail.setQuantity(jsonArray.getJSONObject(i).getJSONObject("Lines").getInt("Quantity"));

                                    if (!jsonArray.getJSONObject(i).getJSONObject("Lines").isNull("UnitPrice"))
                                        transactionItemDetail.setUnitPrice(jsonArray.getJSONObject(i).getJSONObject("Lines").getDouble("UnitPrice"));

                                    if (!jsonArray.getJSONObject(i).getJSONObject("Lines").isNull("Total")) {
                                        double itemLineTotal = jsonArray.getJSONObject(i).getJSONObject("Lines").getDouble("Total");
                                        transactionItemDetail.setLineTotal(jsonArray.getJSONObject(i).getJSONObject("Lines").getDouble("Total"));
                                        itemSubtotal += itemLineTotal;
                                    }

                                    oneInvoiceItem.addListItem(transactionItemDetail);
                                }

                                oneInvoiceItem.setSubTotal(itemSubtotal);

                                invoiceItemList.add(oneInvoiceItem);
                                myAccountMainFragment.displayData();
                            }

                        } catch (JSONException e1) {
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

    private String convertDateStyle(String oldStyle){
        return oldStyle.substring(8, 10) + "-" + oldStyle.substring(5, 7) + "-" + oldStyle.substring(0, 4);
    }

}
