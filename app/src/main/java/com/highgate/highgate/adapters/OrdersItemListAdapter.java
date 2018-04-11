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

import com.highgate.highgate.Fragments.MyAccountMainFragment;
import com.highgate.highgate.Fragments.MyAccountOrdersFragment;
import com.highgate.highgate.MyAccountActivity;
import com.highgate.highgate.R;
import com.highgate.highgate.myUtils.FontManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 9/22/2017.
 */
public class OrdersItemListAdapter extends ArrayAdapter {

    private final Context context;
    private List<OrderItem> lists = new ArrayList<>();

    private TextView txtId;
    private TextView txtDate;
    private TextView txtTotal;
    private TextView txtStatus;

    private LinearLayout layoutOneOrderItem;

    private MyAccountActivity myActivity;

    private Typeface iconFront;
    private Typeface gothamFont;

    public OrdersItemListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.context = context;
    }

    public void setMyActivity(MyAccountActivity myActivity) {
        this.myActivity = myActivity;
    }

    public void setList(List<OrderItem> lists){
        this.lists = lists;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.my_account_order_item, parent, false);

        gothamFont = FontManager.getTypeface(myActivity, FontManager.GOTHAM);
        FontManager.setFontType(rowView.findViewById(R.id.layoutOneOrderItem_OrderItem), gothamFont);

        txtId = rowView.findViewById(R.id.txtId_OrderItem);
        txtId.setTypeface(gothamFont, Typeface.BOLD);
        txtDate = rowView.findViewById(R.id.txtCreateDate_OrderItem);
        txtTotal = rowView.findViewById(R.id.txtOrderTotal_OrderItem);
        txtStatus = rowView.findViewById(R.id.txtStatus_OrderItem);

        layoutOneOrderItem = rowView.findViewById(R.id.layoutOneOrderItem_OrderItem);

        txtId.setText(String.valueOf(lists.get(position).getId()));
        txtDate.setText(lists.get(position).getDate());
        txtTotal.setText("$" + String.format("%.2f", lists.get(position).getOrderTotal()));
        if (lists.get(position).getStatus() == 2) {
            txtStatus.setText("Completed");
            txtStatus.setTextColor(context.getResources().getColor(R.color.colorMyDarkGreen));
        }
        else {
            txtStatus.setText("Processing");
            txtStatus.setTextColor(context.getResources().getColor(R.color.colorMySoftBrown));
        }

        layoutOneOrderItem.setTag(position);
        layoutOneOrderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (int) view.getTag();

                myActivity.fragmentMainFlag = false;

                MyAccountOrdersFragment myAccountOrdersFragment = new MyAccountOrdersFragment();
                myAccountOrdersFragment.setMyActivity(myActivity);
                myAccountOrdersFragment.setDetailIndex(i);
                myActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frgMyAccount, myAccountOrdersFragment).commit();
            }
        });

        return rowView;
    }

    @Override
    public int getCount() {
        return lists.size();
    }
}
