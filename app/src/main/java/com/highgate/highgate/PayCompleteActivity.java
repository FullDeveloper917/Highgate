package com.highgate.highgate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.highgate.highgate.myUtils.FontManager;

import java.util.concurrent.TimeoutException;

public class PayCompleteActivity extends BaseActivity {
    private PayActivity myPayActivity;

    private boolean result;
    private double payAmount;
    private String payReference;
    private String payDescription;

    private TextView txtMark;
    private TextView txtAmount;
    private TextView txtGreeting;
    private TextView txtFirstDesc;
    private TextView txtSecondDesc;
    private TextView txtThirdDesc;
    private TextView txtReference;

    private TextView txtIcon;
    private TextView txtRtn;
    private TextView txtTitle;
    private LinearLayout layoutRtn;

    private Button btnGo;

    public void setMyPayActivity(PayActivity myPayActivity) {
        this.myPayActivity = myPayActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_complete);

        FontManager.setFontType(findViewById(R.id.layout_PayComplete), gothamFont);

        Intent intent = getIntent();
        result = intent.getBooleanExtra("result", false);
        payAmount = intent.getDoubleExtra("amount", 0);
        payReference = intent.getStringExtra("reference");
        payDescription = intent.getStringExtra("description");

        findViews();

        setFormula();

        setMenuEvents(this);

        setEvents();
    }

    private void findViews(){
        txtIcon= (TextView) findViewById(R.id.txtIcon_PayComplete);
        txtRtn = (TextView) findViewById(R.id.txtRtn_PayComplete);
        txtTitle = (TextView) findViewById(R.id.txtTitle_PayComplete);
        layoutRtn = (LinearLayout) findViewById(R.id.layoutRtn_PayComplete);
        txtMark = (TextView) findViewById(R.id.txtMark_PayComplete);
        txtAmount = (TextView) findViewById(R.id.txtPayAmount_PayComplete);
        txtGreeting = (TextView) findViewById(R.id.txtGreeting_PayComplete);
        txtFirstDesc = (TextView) findViewById(R.id.txtFirstDesc_PayComplete);
        txtSecondDesc = (TextView) findViewById(R.id.txtSecondDec_PayComplete);
        txtThirdDesc = (TextView) findViewById(R.id.txtThirdDesc_PayComplete);
        txtReference = (TextView) findViewById(R.id.txtReference_PayComplete);
        btnGo = (Button) findViewById(R.id.btnGo_PayComplete);
    }

    private void setFormula(){
        txtIcon.setTypeface(iconFont);
        txtRtn.setTypeface(iconFont);
        txtMark.setTypeface(iconFont);
    }

    private void setEvents(){

        if (result){
            txtIcon.setText(getResources().getString(R.string.fa_check));
            txtTitle.setText("Transaction Completed");
            txtMark.setText(getResources().getString(R.string.fa_check));
            txtMark.setTextColor(getResources().getColor(R.color.colorMyMain));
            txtGreeting.setText("Thank you");
            txtFirstDesc.setText("for your payment.");
            txtAmount.setText("$" + String.format("%.2f", payAmount));
            txtSecondDesc.setText("Your transaction has been completed.");
            txtThirdDesc.setText("Your Payment Reference");
            txtReference.setText(payReference);
            btnGo.setText("Back to My Account");
        }
        else {
            txtIcon.setText(getResources().getString(R.string.fa_times));
            txtTitle.setText("Transaction Failed");
            txtMark.setText(getResources().getString(R.string.fa_times));
            txtMark.setTextColor(getResources().getColor(R.color.colorMyRed));
            txtGreeting.setText("Sorry");
            txtFirstDesc.setText("The transaction failed.");
            txtAmount.setText("");
            txtSecondDesc.setText("Please check your credit card details and try again,\nor contact your bank for more information.");
            txtThirdDesc.setText("Your Payment Reference");
            txtReference.setText(payReference);
            btnGo.setText("Back to Credit Card Details");
        }
        layoutRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnGo.performClick();
            }
        });
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result){
                    Intent intent = new Intent(PayCompleteActivity.this, MyAccountActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(PayCompleteActivity.this, PayActivity.class);
                    intent.putExtra("pay_amount", payAmount);
                    intent.putExtra("pay_description", payDescription);
                    intent.putExtra("pay_reference", payReference);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
