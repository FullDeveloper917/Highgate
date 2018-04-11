package com.highgate.highgate;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.highgate.highgate.adapters.OrdersListAdapter;
import com.highgate.highgate.myUtils.FontManager;

public class CartActivity extends BaseActivity {

    private Button btnCheckout;
    private ImageView imgCheckout;
    private TextView txtTotalPrice;
//    public ProgressBar progressBar;

    private Button btnRtn;
    private TextView txtRtn;
    private TextView txtTitle;
    private ListView listView;

    private float allTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        FontManager.setFontType(findViewById(R.id.layout_Cart), gothamFont);

        setMenuEvents(this);
        DrawerLayout layoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);
        layoutDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

//        progressBar = (ProgressBar) findViewById(R.id.progressBar_Cart);

        txtTotalPrice = (TextView)findViewById(R.id.txtTotalPrice_Chart);
        txtTotalPrice.setTypeface(gothamFont, Typeface.BOLD);

        listView = (ListView) findViewById(R.id.listProduct_Cart);



        OrdersListAdapter adapter = new OrdersListAdapter(this, R.layout.result_detail);
        adapter.setProductList(orderProductList);
        adapter.setMyActivity(this);
        listView.setAdapter(adapter);

        refreshTotal();

        btnCheckout = (Button) findViewById(R.id.btnCheckout_Cart);
        btnCheckout.setTypeface(gothamFont, Typeface.BOLD);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderProductList.size() == 0) {
                    Toast.makeText(CartActivity.this, getResources().getString(R.string.chart_activity_message), Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                    intent.putExtra("AllTotalPrice", allTotalPrice);
                    startActivity(intent);
                }
            }
        });

        imgCheckout = (ImageView) findViewById(R.id.imgCheckout_Cart);
        imgCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderProductList.size() == 0) {
                    Toast.makeText(CartActivity.this, getResources().getString(R.string.chart_activity_message), Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                    intent.putExtra("AllTotalPrice", allTotalPrice);
                    startActivity(intent);
                    finish();
                }
            }
        });

        setReturn();
    }

    private void setReturn(){
        btnRtn = (Button)findViewById(R.id.btnRtn_Cart);
        btnRtn.setTypeface(iconFont);
        btnRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtRtn = (TextView)findViewById(R.id.txtRtn_Cart);
        txtRtn.setTypeface(iconFont);
        txtRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtTitle = (TextView)findViewById(R.id.txtCartTitle);
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void refreshTotal(){

        allTotalPrice = 0.0f;
        for(int i = 0; i < orderProductList.size(); i++){
            allTotalPrice += orderProductList.get(i).getCustomer_price() * orderProductList.get(i).getNumber();
        }
        txtTotalPrice.setText("$" + String.format("%.2f", allTotalPrice));
    }
}
