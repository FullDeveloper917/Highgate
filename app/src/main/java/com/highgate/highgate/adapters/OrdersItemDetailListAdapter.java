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

import com.highgate.highgate.Fragments.MyAccountOrdersFragment;
import com.highgate.highgate.R;
import com.highgate.highgate.myUtils.FontManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 9/22/2017.
 */

public class OrdersItemDetailListAdapter extends ArrayAdapter{
    private final Context context;
    private List<OrderItemDetail> lists = new ArrayList<>();

    private TextView txtStockCode;
    private TextView txtDescription;
    private TextView txtQuantity;
    private TextView txtUnitPrice;
    private TextView txtLineTotal;

    private LinearLayout layoutOneOrdersItem;

    private MyAccountOrdersFragment myFragment;

    private Typeface iconFont;
    private Typeface gothamFont;

    public void setMyFragment(MyAccountOrdersFragment myFragment) {
        this.myFragment = myFragment;
    }

    public OrdersItemDetailListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.context = context;
    }


    public void setList(List<OrderItemDetail> lists){
        this.lists = lists;
    }


    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.my_account_order_detail_item, parent, false);

        iconFont = FontManager.getTypeface(context.getApplicationContext(), FontManager.FONTAWESOME);
        gothamFont = FontManager.getTypeface(context, FontManager.GOTHAM);

        FontManager.setFontType(rowView.findViewById(R.id.layoutOneOrderDetailItem_OrderItem), iconFont);

        txtStockCode = rowView.findViewById(R.id.txtTablePartN_MyAccountOrder);
        txtDescription = rowView.findViewById(R.id.txtTableDescription_MyAccountOrder);
        txtQuantity = rowView.findViewById(R.id.txtTableQty_MyAccountOrder);
        txtUnitPrice = rowView.findViewById(R.id.txtTablePrice_MyAccountOrder);
        txtLineTotal = rowView.findViewById(R.id.txtTableSubtotal_MyAccountOrder);


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
