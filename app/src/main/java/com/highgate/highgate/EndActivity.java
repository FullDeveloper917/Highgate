package com.highgate.highgate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.highgate.highgate.myUtils.FontManager;

public class EndActivity extends BaseActivity {

    private Button btnGoDash;

    private Button btnRtn;
    private TextView txtRtn;
    private TextView txtTitle;

    private TextView txtOrderReference;

    private int orderReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        FontManager.setFontType(findViewById(R.id.layout_End), gothamFont);

        Intent intent = getIntent();
        orderReference = intent.getIntExtra("OrderReference", 0);

        btnGoDash = (Button) findViewById(R.id.btnGoDash_End);
        btnGoDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView txtMark = (TextView) findViewById(R.id.txtMark_End);
        txtMark.setTypeface(iconFont);

        txtOrderReference = (TextView) findViewById(R.id.txtOrderReference_End);
        txtOrderReference.setText(String.valueOf(orderReference));

        setMenuEvents(this);
        DrawerLayout layoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);
        layoutDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        orderProductList.clear();

        setReturn();
    }

    private void setReturn(){
        btnRtn = (Button)findViewById(R.id.btnRtn_End);
        btnRtn.setTypeface(iconFont);
        btnRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtRtn = (TextView)findViewById(R.id.txtRtn_End);
        txtRtn.setTypeface(iconFont);
        txtRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtTitle = (TextView)findViewById(R.id.txtEndTitle);
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
