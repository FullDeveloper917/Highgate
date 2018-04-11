package com.highgate.highgate.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.highgate.highgate.Fragments.MyAccountInvoicesFragment;
import com.highgate.highgate.R;
import com.highgate.highgate.myUtils.FontManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 9/22/2017.
 */

public class InvoicesItemDetailListAdapter extends ArrayAdapter {
    private final Context context;
    private List<InvoiceItemDetail> lists = new ArrayList<>();

    private TextView txtStockCode;
    private TextView txtDescription;
    private TextView txtQuantity;
    private TextView txtUnitPrice;
    private TextView txtLineTotal;

    private LinearLayout layoutOneOrdersItem;

    private MyAccountInvoicesFragment myFragment;

    private Typeface gothamFont;

    public void setMyFragment(MyAccountInvoicesFragment myFragment) {
        this.myFragment = myFragment;
    }

    public InvoicesItemDetailListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.context = context;
    }

    public void setList(List<InvoiceItemDetail> lists){
        this.lists = lists;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.my_account_invoice_detail_item, parent, false);

        gothamFont = FontManager.getTypeface(context, FontManager.GOTHAM);
        FontManager.setFontType(rowView.findViewById(R.id.layoutOneInvoiceDetailItem_InvoiceItem), gothamFont);

        txtStockCode = rowView.findViewById(R.id.txtTablePartN_MyAccountInvoice);
        txtDescription = rowView.findViewById(R.id.txtTableDescription_MyAccountInvoice);
        txtQuantity = rowView.findViewById(R.id.txtTableQty_MyAccountInvoice);
        txtUnitPrice = rowView.findViewById(R.id.txtTablePrice_MyAccountInvoice);
        txtLineTotal = rowView.findViewById(R.id.txtTableSubtotal_MyAccountInvoice);

        txtStockCode.setText(lists.get(position).getStockCode());
        txtDescription.setText(lists.get(position).getDescription());
        txtQuantity.setText(String.valueOf(lists.get(position).getQuantity()));
        txtUnitPrice.setText("$" + String.format("%.2f", lists.get(position).getUnitPrice()));
        txtLineTotal.setText("$" + String.format("%.2f", lists.get(position).getLineTotal()));

        return rowView;
    }

    @Override
    public int getCount() {
        return lists.size();
    }
}
