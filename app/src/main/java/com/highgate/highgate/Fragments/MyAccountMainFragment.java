package com.highgate.highgate.Fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.highgate.highgate.MyAccountActivity;
import com.highgate.highgate.PayActivity;
import com.highgate.highgate.R;
import com.highgate.highgate.adapters.OrdersItemListAdapter;
import com.highgate.highgate.adapters.InvoicesItemListAdapter;
import com.highgate.highgate.myUtils.FontManager;

public class MyAccountMainFragment extends Fragment {

    private MyAccountActivity myActivity;

    private LinearLayout layoutPayments;
    private LinearLayout layoutAgedBalances;

    private Button btnMakePayment;
    private Button btnPayNow;

    private TextView txtBalance;
    private TextView txtLabelBalance;
    private TextView txtLabelOrderTable;
    private TextView txtLabelInvoiceTable;
    private TextView txtCurrentBalance;
    private TextView txtOneMonthBalance;
    private TextView txtTwoMonthsBalances;
    private TextView txtThreeMonthsBalances;

    private EditText edtPayAmount;

    private ListView listOrders;
    private ListView listInvoices;

    private Typeface gothamFont;

    public void setMyActivity(MyAccountActivity myActivity) {
        this.myActivity = myActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account_main, container, false);

        gothamFont = FontManager.getTypeface(myActivity, FontManager.GOTHAM);

        FontManager.setFontType(view.findViewById(R.id.layout_MyAccountMain), gothamFont);

        findViews(view);

        setFormula();

        setEvents();

        if (myActivity.balanceOrPaymentFlag) {
            layoutPayments.setVisibility(View.GONE);
            btnMakePayment.setBackgroundColor(getResources().getColor(R.color.colorMyGreen));
            btnMakePayment.setText("make a payment");
        }
        else {
            layoutPayments.setVisibility(View.VISIBLE);
            btnMakePayment.setBackgroundColor(getResources().getColor(R.color.colorMyRed));
            btnMakePayment.setText("cancel");
        }

        displayData();

        return view;
    }


    private void findViews(View view){
        layoutPayments = view.findViewById(R.id.layoutPayments_MyAccountMain);
        layoutAgedBalances = view.findViewById(R.id.layoutAgedBalances_MyAccountMain);

        btnMakePayment = view.findViewById(R.id.btnMakePayment_MyAccountMain);
        btnPayNow = view.findViewById(R.id.btnPayNow_MyAccountMain);

        txtBalance = view.findViewById(R.id.txtBalance_MyAccountMain);
        txtLabelBalance = view.findViewById(R.id.txtLabelOfBalance_MyAccountMain);
        txtLabelOrderTable = view.findViewById(R.id.txtLabelOrderTable_MyAccountMain);
        txtLabelInvoiceTable = view.findViewById(R.id.txtLabelInvoiceTable_MyAccountMain);
        txtCurrentBalance = view.findViewById(R.id.txtCurrentBalance_MyAccountMain);
        txtOneMonthBalance = view.findViewById(R.id.txtOneMonthBalance_MyAccountMain);
        txtTwoMonthsBalances = view.findViewById(R.id.txtTwoMonthsBalance_MyAccountMain);
        txtThreeMonthsBalances = view.findViewById(R.id.txtThreeMonthsBalance_MyAccountMain);

        edtPayAmount = view.findViewById(R.id.edtPayAmount_MyAccountMain);

        listOrders = view.findViewById(R.id.listOrders_MyAccountMain);
        listInvoices = view.findViewById(R.id.listInvoices_MyAccountMain);
    }

    private void setFormula(){
        btnMakePayment.setTypeface(gothamFont, Typeface.BOLD);
        btnPayNow.setTypeface(gothamFont, Typeface.BOLD);
        txtLabelBalance.setTypeface(gothamFont, Typeface.BOLD);
        txtLabelOrderTable.setTypeface(gothamFont, Typeface.BOLD);
        txtLabelInvoiceTable.setTypeface(gothamFont, Typeface.BOLD);
    }
    private void setEvents(){

        btnMakePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myActivity.balanceOrPaymentFlag) {
                    layoutPayments.setVisibility(View.VISIBLE);
                    btnMakePayment.setBackgroundColor(getResources().getColor(R.color.colorMyRed));
                    btnMakePayment.setText("cancel");
                    myActivity.balanceOrPaymentFlag = false;
                }
                else {
                    layoutPayments.setVisibility(View.GONE);
                    btnMakePayment.setBackgroundColor(getResources().getColor(R.color.colorMyGreen));
                    btnMakePayment.setText("make a payment");
                    myActivity.balanceOrPaymentFlag = true;
                }

            }
        });

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity, PayActivity.class);
                intent.putExtra("pay_amount", Double.valueOf(noNullObjects(edtPayAmount.getText().toString())));
                intent.putExtra("pay_description", "DEBTOR-" + myActivity.myUser.getDebtor_id() + " " + myActivity.companyName);
                intent.putExtra("pay_reference", "BALANCE PAYMENT");
                startActivity(intent);
            }
        });

    }

    public void displayData(){
        txtBalance.setText("$" + String.format("%.2f", myActivity.balance));
        txtCurrentBalance.setText("Current: $" + String.format("%.2f", myActivity.currentBalance));
        txtOneMonthBalance.setText("1 Month: $" + String.format("%.2f", myActivity.oneMonthBalance));
        txtTwoMonthsBalances.setText("2 Months: $" + String.format("%.2f", myActivity.twoMonthsBalance));
        txtThreeMonthsBalances.setText("3+ Months: $" + String.format("%.2f", myActivity.threeMonthBalance));

        edtPayAmount.setText(String.format("%.2f", myActivity.balance));

        OrdersItemListAdapter ordersItemListAdapter = new OrdersItemListAdapter(myActivity, R.layout.my_account_order_item);
        ordersItemListAdapter.setList(myActivity.orderItemList);
        ordersItemListAdapter.setMyActivity(myActivity);
        listOrders.setAdapter(ordersItemListAdapter);

        InvoicesItemListAdapter invoicesItemListAdapter = new InvoicesItemListAdapter(myActivity, R.layout.my_account_invoice_item);
        invoicesItemListAdapter.setList(myActivity.invoiceItemList);
        invoicesItemListAdapter.setMyActivity(myActivity);
        listInvoices.setAdapter(invoicesItemListAdapter);

    }


    private String noNullObjects(String number){
        if(number.isEmpty()||number == null){
            number = "0";
        }
        return number;
    }

}

