package com.highgate.highgate.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.highgate.highgate.MyAccountActivity;
import com.highgate.highgate.R;
import com.highgate.highgate.adapters.OrdersItemDetailListAdapter;
import com.highgate.highgate.myUtils.FontManager;

public class MyAccountOrdersFragment extends Fragment {

    private MyAccountActivity myActivity;

    private OrdersItemDetailListAdapter ordersItemDetailListAdapter;

    private ListView ordersItemDetailListView;

    private TextView txtOrderN;
    private TextView txtDate;
    private TextView txtOrderReference;
    private TextView txtDispatchMethod;
    private TextView txtConNoteN;
    private TextView txtSubTotal;
    private TextView txtLabelSubTotal;
    private TextView txtGst;
    private TextView txtLabelGst;
    private TextView txtGrandTotal;
    private TextView txtLabelGrandTotal;
    private TextView txtStatus;
    private TextView txtLabelStatus;

    private TextView txtTableLabelPartN;
    private TextView txtTableLabelDescription;
    private TextView txtTableLabelQty;
    private TextView txtTableLabelPrice;
    private TextView txtTableLabelSubTotal;

    private int detailIndex = 0;

    private Typeface gothamFont;

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

        View view = inflater.inflate(R.layout.fragment_my_account_orders, container, false);

        gothamFont = FontManager.getTypeface(myActivity, FontManager.GOTHAM);
        FontManager.setFontType(view.findViewById(R.id.layout_MyAccountOrder), gothamFont);

        findViews(view);

        setFormula();

        displayData();

        return view;
    }

    private void findViews(View view) {
        ordersItemDetailListView = view.findViewById(R.id.listOrders_MyAccountOrder);
        txtOrderN = view.findViewById(R.id.txtOrderN_MyAccountOrder);
        txtDate = view.findViewById(R.id.txtDate_MyAccountOrder);
        txtOrderReference = view.findViewById(R.id.txtOrderReference_MyAccountOrder);
        txtDispatchMethod = view.findViewById(R.id.txtDispatchMethod_MyAccountOrder);
        txtConNoteN = view.findViewById(R.id.txtConNoteN_MyAccountOrder);
        txtSubTotal = view.findViewById(R.id.txtSubTotal_MyAccountOrder);
        txtLabelSubTotal = view.findViewById(R.id.txtLabelSubTotal_MyAccountOrder);
        txtGst = view.findViewById(R.id.txtGst_MyAccountOrder);
        txtLabelGst = view.findViewById(R.id.txtLabelGst_MyAccountOrder);
        txtGrandTotal = view.findViewById(R.id.txtGrandTotal_MyAccountOrder);
        txtLabelGrandTotal = view.findViewById(R.id.txtLabelGrandTotal_MyAccountOrder);
        txtStatus = view.findViewById(R.id.txtStatus_MyAccountOrder);
        txtLabelStatus = view.findViewById(R.id.txtLabelStatus_MyAccountOrder);

        txtTableLabelPartN = view.findViewById(R.id.txtTableLabelPartN_MyAccountOrder);
        txtTableLabelDescription = view.findViewById(R.id.txtTableLabelDescription_MyAccountOrder);
        txtTableLabelQty = view.findViewById(R.id.txtTableLabelQty_MyAccountOrder);
        txtTableLabelPrice = view.findViewById(R.id.txtTableLabelPrice_MyAccountOrder);
        txtTableLabelSubTotal = view.findViewById(R.id.txtTableLabelSubtotal_MyAccountOrder);

    }

    private void setFormula(){
        txtTableLabelPartN.setTypeface(gothamFont, Typeface.BOLD);
        txtTableLabelDescription.setTypeface(gothamFont, Typeface.BOLD);
        txtTableLabelQty.setTypeface(gothamFont, Typeface.BOLD);
        txtTableLabelPrice.setTypeface(gothamFont, Typeface.BOLD);
        txtTableLabelSubTotal.setTypeface(gothamFont, Typeface.BOLD);

        txtOrderN.setTypeface(gothamFont, Typeface.BOLD);
        txtLabelSubTotal.setTypeface(gothamFont, Typeface.BOLD);
        txtLabelGst.setTypeface(gothamFont, Typeface.BOLD);
        txtLabelGrandTotal.setTypeface(gothamFont, Typeface.BOLD);
        txtLabelStatus.setTypeface(gothamFont, Typeface.BOLD);

    }

    private void displayData(){

        txtOrderN.setText(getResources().getString(R.string.order_n) + " " + String.valueOf(myActivity.orderItemList.get(detailIndex).getId()));
        txtDate.setText("Date: " + myActivity.orderItemList.get(detailIndex).getDate());
        txtOrderReference.setText("Order Reference: " + myActivity.orderItemList.get(detailIndex).getCustomerOrderNumber());
        txtDispatchMethod.setText("Dispatch Method: " + myActivity.orderItemList.get(detailIndex).getDispatchMethod());
        txtConNoteN.setText(getResources().getString(R.string.con_note_n) + ": " + myActivity.orderItemList.get(detailIndex).getConeNoteN());
        txtSubTotal.setText("$" + String.format("%.2f", myActivity.orderItemList.get(detailIndex).getSubTotal()));
        txtGst.setText("$" + String.format("%.2f", myActivity.orderItemList.get(detailIndex).getSubTotal() * 0.1));
        txtGrandTotal.setText("$" + String.format("%.2f", myActivity.orderItemList.get(detailIndex).getOrderTotal()));

        if (myActivity.orderItemList.get(detailIndex).getStatus() == 2) {
            txtLabelStatus.setBackgroundColor(getResources().getColor(R.color.colorMyGreen));
            txtStatus.setBackgroundColor(getResources().getColor(R.color.colorMyGreen));
            txtStatus.setText("Completed");
        }
        else {
            txtLabelStatus.setBackgroundColor(getResources().getColor(R.color.colorMySoftBrown));
            txtStatus.setBackgroundColor(getResources().getColor(R.color.colorMySoftBrown));
            txtStatus.setText("Processing");
        }

        ordersItemDetailListAdapter = new OrdersItemDetailListAdapter(myActivity, R.layout.my_account_order_detail_item);
        ordersItemDetailListAdapter.setList(myActivity.orderItemList.get(detailIndex).getOrderItemDetails());
        ordersItemDetailListAdapter.setMyFragment(this);
        ordersItemDetailListView.setAdapter(ordersItemDetailListAdapter);
    }

}

