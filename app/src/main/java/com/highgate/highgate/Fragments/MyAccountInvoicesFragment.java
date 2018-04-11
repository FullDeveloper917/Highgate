package com.highgate.highgate.Fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.highgate.highgate.MyAccountActivity;
import com.highgate.highgate.PayActivity;
import com.highgate.highgate.R;
import com.highgate.highgate.adapters.InvoicesItemDetailListAdapter;
import com.highgate.highgate.myUtils.FontManager;


public class MyAccountInvoicesFragment extends Fragment {

    private MyAccountActivity myActivity;

    private InvoicesItemDetailListAdapter invoicesItemDetailListAdapter;

    private ListView invoicesItemDetailListView;

    public TextView txtInvoiceN;
    public TextView txtOrderN;
    public TextView txtDate;
    public TextView txtOrderReference;
    public TextView txtDispatchMethod;
    public TextView txtConNoteN;
    public TextView txtSubTotal;
    public TextView txtLabelSubTotal;
    public TextView txtGst;
    public TextView txtLabelGst;
    public TextView txtGrandTotal;
    public TextView txtLabelGrandTotal;
    public Button btnStatus;

    private TextView txtTableLabelPartN;
    private TextView txtTableLabelDescription;
    private TextView txtTableLabelQty;
    private TextView txtTableLabelPrice;
    private TextView txtTableLabelSubTotal;
    private Typeface gothamFont;

    private int detailIndex = 0;

    public void setDetailIndex(int detailIndex) {
        this.detailIndex = detailIndex;
    }

    public void setMyActivity(MyAccountActivity myActivity) {
        this.myActivity = myActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_account_invoices, container, false);

        gothamFont = FontManager.getTypeface(myActivity, FontManager.GOTHAM);

        FontManager.setFontType(view.findViewById(R.id.layout_MyAccountInvoice), gothamFont);

        findViews(view);

        setFormula();

        displayData();

        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity, PayActivity.class);
                intent.putExtra("pay_amount", myActivity.invoiceItemList.get(detailIndex).getAmount());
                intent.putExtra("pay_description", "DEBTOR-" + myActivity.myUser.getDebtor_id() + " " + myActivity.companyName);
                intent.putExtra("pay_reference", txtInvoiceN.getText().toString());
                startActivity(intent);
            }
        });

        return view;
    }
    private void findViews(View view) {
        invoicesItemDetailListView = view.findViewById(R.id.listInvoices_MyAccountInvoice);
        txtInvoiceN = view.findViewById(R.id.txtInvoiceN_MyAccountInvoice);
        txtOrderN = view.findViewById(R.id.txtOrderN_MyAccountInvoice);
        txtDate = view.findViewById(R.id.txtDate_MyAccountInvoice);
        txtOrderReference = view.findViewById(R.id.txtOrderReference_MyAccountInvoice);
        txtDispatchMethod = view.findViewById(R.id.txtDispatchMethod_MyAccountInvoice);
        txtConNoteN = view.findViewById(R.id.txtConNoteN_MyAccountInvoice);
        txtSubTotal = view.findViewById(R.id.txtSubTotal_MyAccountInvoice);
        txtLabelSubTotal = view.findViewById(R.id.txtLabelSubTotal_MyAccountInvoice);
        txtGst = view.findViewById(R.id.txtGst_MyAccountInvoice);
        txtLabelGst = view.findViewById(R.id.txtLabelGst_MyAccountInvoice);
        txtGrandTotal = view.findViewById(R.id.txtGrandTotal_MyAccountInvoice);
        txtLabelGrandTotal = view.findViewById(R.id.txtLabelGrandTotal_MyAccountInvoice);
        btnStatus = view.findViewById(R.id.btnStatus_MyAccountInvoice);

        txtTableLabelPartN = view.findViewById(R.id.txtTableLabelPartN_MyAccountInvoice);
        txtTableLabelDescription = view.findViewById(R.id.txtTableLabelDescription_MyAccountInvoice);
        txtTableLabelQty = view.findViewById(R.id.txtTableLabelQty_MyAccountInvoice);
        txtTableLabelPrice = view.findViewById(R.id.txtTableLabelPrice_MyAccountInvoice);
        txtTableLabelSubTotal = view.findViewById(R.id.txtTableLabelSubtotal_MyAccountInvoice);
    }

    private void setFormula(){
        txtTableLabelPartN.setTypeface(gothamFont, Typeface.BOLD);
        txtTableLabelDescription.setTypeface(gothamFont, Typeface.BOLD);
        txtTableLabelQty.setTypeface(gothamFont, Typeface.BOLD);
        txtTableLabelPrice.setTypeface(gothamFont, Typeface.BOLD);
        txtTableLabelSubTotal.setTypeface(gothamFont, Typeface.BOLD);

        txtInvoiceN.setTypeface(gothamFont, Typeface.BOLD);
        txtLabelSubTotal.setTypeface(gothamFont, Typeface.BOLD);
        txtLabelGst.setTypeface(gothamFont, Typeface.BOLD);
        txtLabelGrandTotal.setTypeface(gothamFont, Typeface.BOLD);
        btnStatus.setTypeface(gothamFont, Typeface.BOLD);
    }

    private void displayData(){

        txtInvoiceN.setText(getResources().getString(R.string.invoice_n) + " " + String.valueOf(myActivity.invoiceItemList.get(detailIndex).getInvoiceN()));
        txtOrderN.setText(getResources().getString(R.string.order_n) + " " + String.valueOf(myActivity.invoiceItemList.get(detailIndex).getOrderN()));
        txtDate.setText("Date: " + myActivity.invoiceItemList.get(detailIndex).getDate());
        txtOrderReference.setText("Order Reference: " + myActivity.invoiceItemList.get(detailIndex).getCustomerOrderNumber());
        txtDispatchMethod.setText("Dispatch Method: " + myActivity.invoiceItemList.get(detailIndex).getDispatchMethod());
        txtConNoteN.setText(getResources().getString(R.string.con_note_n) + ": " + myActivity.invoiceItemList.get(detailIndex).getConeNoteN());
        txtSubTotal.setText("$" + String.format("%.2f", myActivity.invoiceItemList.get(detailIndex).getSubTotal()));
        txtGst.setText("$" + String.format("%.2f", myActivity.invoiceItemList.get(detailIndex).getSubTotal() * 0.1));
        txtGrandTotal.setText("$" + String.format("%.2f", myActivity.invoiceItemList.get(detailIndex).getAmount()));

        if (myActivity.invoiceItemList.get(detailIndex).getStatus().equalsIgnoreCase("FullyAllocated")) {
            btnStatus.setBackgroundColor(getResources().getColor(R.color.colorMyBlack));
            btnStatus.setText("paid");
        }

        if (myActivity.invoiceItemList.get(detailIndex).getStatus().equalsIgnoreCase("UnAllocated")) {
            btnStatus.setBackgroundColor(getResources().getColor(R.color.colorMyGreen));
            btnStatus.setText("pay now");
        }

        if (myActivity.invoiceItemList.get(detailIndex).getStatus().equalsIgnoreCase("Current Allocated")) {
            btnStatus.setBackgroundColor(getResources().getColor(R.color.colorMySoftBrown));
            btnStatus.setText("processing");
        }

        invoicesItemDetailListAdapter = new InvoicesItemDetailListAdapter(myActivity, R.layout.my_account_invoice_detail_item);
        invoicesItemDetailListAdapter.setList(myActivity.invoiceItemList.get(detailIndex).getInvoiceItemDetails());
        invoicesItemDetailListAdapter.setMyFragment(this);
        invoicesItemDetailListView.setAdapter(invoicesItemDetailListAdapter);
    }


}
