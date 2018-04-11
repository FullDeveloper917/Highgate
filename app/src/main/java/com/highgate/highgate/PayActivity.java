package com.highgate.highgate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.TestLooperManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eway.payment.sdk.android.RapidAPI;
import com.eway.payment.sdk.android.RapidConfigurationException;
import com.eway.payment.sdk.android.beans.Address;
import com.eway.payment.sdk.android.beans.CardDetails;
import com.eway.payment.sdk.android.beans.Customer;
import com.eway.payment.sdk.android.beans.NVPair;
import com.eway.payment.sdk.android.beans.Payment;
import com.eway.payment.sdk.android.beans.Transaction;
import com.eway.payment.sdk.android.beans.TransactionType;
import com.eway.payment.sdk.android.entities.EncryptItemsResponse;
import com.eway.payment.sdk.android.entities.SubmitPayResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.highgate.highgate.myUtils.FontManager;

public class PayActivity extends BaseActivity {

    public double payAmount;
    private int centPayAmount;
    public String payDescription;
    public String payReference;

    private String cardNumber;
    private String cardHoldName;
    private String expiryMonth;
    private String expiryYear;
    private String cvnNumber;

    private EditText edtCardNumber;
    private EditText edtCardHoldName;
    private Spinner spnExpiryMonth;
    private Spinner spnExpiryYear;
    private EditText edtCvnNumber;

    private TextView txtIcon;
    private TextView txtRtn;
    private TextView txtPayAmount;
    private LinearLayout layoutRtn;
    private Button btnPay;

    private TextView txtMonth;
    private TextView txtYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        FontManager.setFontType(findViewById(R.id.layout_Pay), gothamFont);

        RapidAPI.PublicAPIKey = "epk-4A576978-9E3F-404F-9CBC-A563B93351FF";
        RapidAPI.RapidEndpoint = "https://api.ewaypayments.com/";

        Intent intent = getIntent();
        payAmount = intent.getDoubleExtra("pay_amount", 0);
        centPayAmount = (int) (payAmount * 100);
        payDescription = intent.getStringExtra("pay_description");
        payReference = intent.getStringExtra("pay_reference");

        findViews();

        setFormula();

        setMenuEvents(this);


        setEvents();

    }

    private void findViews(){
        txtIcon = (TextView) findViewById(R.id.txtIcon_Pay);
        txtRtn = (TextView) findViewById(R.id.txtRtn_Pay);
        layoutRtn = (LinearLayout) findViewById(R.id.layoutRtn_Pay);
        edtCardNumber = (EditText) findViewById(R.id.edtCardNumber_Pay);
        edtCardHoldName = (EditText) findViewById(R.id.edtCardHolderName_Pay);
        spnExpiryMonth = (Spinner) findViewById(R.id.spnMonth_Pay);
        spnExpiryYear = (Spinner) findViewById(R.id.spnYear_Pay);
        edtCvnNumber = (EditText)findViewById(R.id.edtCvc_Pay);
        txtPayAmount = (TextView) findViewById(R.id.txtAmount_Pay);
        txtMonth = (TextView) findViewById(R.id.txtMonth_Pay);
        txtYear = (TextView) findViewById(R.id.txtYear_Pay);
        btnPay = (Button) findViewById(R.id.btnPay_Pay);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_Pay);
    }

    private void setFormula(){
        FontManager.setFontType(findViewById(R.id.layout_Pay), gothamFont);
        txtIcon.setTypeface(iconFont);
        txtRtn.setTypeface(iconFont);

        btnPay.setTypeface(gothamFont, Typeface.BOLD);
        txtPayAmount.setTypeface(gothamFont, Typeface.BOLD);
        ((TextView)findViewById(R.id.txtDesc_Pay)).setTypeface(gothamFont, Typeface.BOLD);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, makeSequence(1, 12));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnExpiryMonth.setAdapter(dataAdapter);
        spnExpiryMonth.setSelection(Calendar.getInstance().get(Calendar.MONTH));

        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, makeSequence(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.YEAR) + 10));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnExpiryYear.setAdapter(dataAdapter);
    }

    private void setEvents(){

        layoutRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayActivity.this, MyAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });


        txtPayAmount.setText("$" + String.format("%.2f", payAmount));

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                new HttpAsyncTask().execute();

            }
        });

        spnExpiryMonth.setDropDownWidth(screenWidth * 33 / 100);
        txtMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spnExpiryMonth.performClick();
                txtMonth.setVisibility(View.INVISIBLE);
            }
        });

        spnExpiryYear.setDropDownWidth(screenWidth * 33 / 100);
        txtYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spnExpiryYear.performClick();
                txtYear.setVisibility(View.INVISIBLE);
            }
        });

    }

    private String noNullObjects(String number){
        if(number.isEmpty()||number == null){
            number = "0";
        }
        return number;
    }

    private void fetchDataFromForm(){

        cardNumber = noNullObjects(edtCardNumber.getText().toString());
        cardHoldName = noNullObjects(edtCardHoldName.getText().toString());
        expiryMonth = spnExpiryMonth.getSelectedItem().toString();
        expiryYear = spnExpiryYear.getSelectedItem().toString();
        cvnNumber = noNullObjects(edtCvnNumber.getText().toString());

    }

    private List<String> makeSequence(int begin, int end) {
        List<String> ret = new ArrayList(end - begin + 1);

        for (int i = begin; i <= end; ret.add(
                ((end > 12 ? "0000" : "00") + Integer.toString(i)).substring(Integer.toString(i++).length())));

        return ret;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        String errorMessage;
        SubmitPayResponse response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fetchDataFromForm();

        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);

            if (response.getErrors() == null) {
                Intent intent = new Intent(PayActivity.this, PayCompleteActivity.class);
                intent.putExtra("result", true);
                intent.putExtra("amount", payAmount);
                intent.putExtra("reference", payReference);
                intent.putExtra("description", payDescription);
                startActivity(intent);
                finish();
//                return ("Succeed. Submission ID is: " + response.getAccessCode());
            }
            else {
                Intent intent = new Intent(PayActivity.this, PayCompleteActivity.class);
                intent.putExtra("result", false);
                intent.putExtra("amount", payAmount);
                intent.putExtra("reference", payReference);
                intent.putExtra("description", payDescription);
                startActivity(intent);
                finish();
//                errorMessage = errorHandler(RapidAPI.userMessage(Locale.getDefault().getLanguage(), response.getErrors()).getErrorMessages());
            }
//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
//            new AlertDialog.Builder(PayActivity.this)
//                    .setTitle("Result")
//                    .setMessage(s)
//                    .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    })
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .create()
//                    .show();
        }

        @Override
        protected String doInBackground(String... params) {

            Transaction transaction = new Transaction();
            Payment payment = new Payment();
            CardDetails cardDetails = new CardDetails();
            Customer customer = new Customer();

            cardDetails.setName(cardHoldName);
            //Encrypt card data before sending
            try {
                EncryptItemsResponse nCryptedData = encryptCard(cardNumber, cvnNumber);
                if (nCryptedData.getErrors() != null)
                    return errorHandler(RapidAPI.userMessage(Locale.getDefault().getLanguage(), nCryptedData.getErrors()).getErrorMessages());
                cardDetails.setNumber(nCryptedData.getItems().get(0).getValue());
                cardDetails.setCVN(nCryptedData.getItems().get(1).getValue());
                cardDetails.setExpiryMonth(expiryMonth);
                cardDetails.setExpiryYear(expiryYear);

                customer.setFirstName(myUser.getFirstName());
                customer.setLastName(myUser.getLastName());
                customer.setEmail(myUser.getEmailAddress());
                Address address = new Address();
                address.setPostalCode(myUser.getPostCode());
                address.setState(myUser.getState());
                address.setStreet1(myUser.getSuburb());
                address.setStreet2(myUser.getAddress());
                customer.setAddress(address);
                customer.setCardDetails(cardDetails);

                payment.setCurrencyCode("AUD");
                payment.setTotalAmount(centPayAmount);
                payment.setInvoiceNumber(UUID.randomUUID().toString());
                payment.setInvoiceReference(payReference);
                payment.setInvoiceDescription(payDescription);


                transaction.setTransactionType(TransactionType.Purchase);
                transaction.setPayment(payment);
                transaction.setCustomer(customer);
                response = RapidAPI.submitPayment(transaction);

            } catch (RapidConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String errorHandler(List<String> response) {
            StringBuilder result = new StringBuilder();
            int i = 0;
            for (String errorMsg : response) {
                result.append("Message ").append(i).append(" = ").append(errorMsg).append("\n");
                i++;
            }
            return result.toString();
        }

        private EncryptItemsResponse encryptCard(String cardNo, String CVN) throws IOException, RapidConfigurationException {
            ArrayList<NVPair> values = new ArrayList<>();
            values.add(new NVPair("Card", cardNo));
            values.add(new NVPair("CVN", CVN));
            return RapidAPI.encryptValues(values);
        }

    }
}
