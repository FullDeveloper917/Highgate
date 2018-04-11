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
import com.highgate.highgate.MyAccountActivity;
import com.highgate.highgate.R;
import com.highgate.highgate.myUtils.FontManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 9/22/2017.
 */

public class InvoicesItemListAdapter extends ArrayAdapter{
    private final Context context;
    private List<InvoiceItem> lists = new ArrayList<>();

    private TextView txtInvoiceN;
    private TextView txtDate;
    private TextView txtAmount;
    private TextView txtStatus;

    private LinearLayout layoutOneInvoiceItem;

    private MyAccountActivity myActivity;

    private Typeface gothamFont;

    public InvoicesItemListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.context = context;
    }

    public void setMyActivity(MyAccountActivity myActivity) {
        this.myActivity = myActivity;
    }

    public void setList(List<InvoiceItem> lists){
        this.lists = lists;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.my_account_invoice_item, parent, false);

        gothamFont = FontManager.getTypeface(myActivity, FontManager.GOTHAM);
        FontManager.setFontType(rowView.findViewById(R.id.layoutOneInvoiceItem_InvoiceItem), gothamFont);

        txtInvoiceN = rowView.findViewById(R.id.txtInvoiceNumber_InvoiceItem);
        txtInvoiceN.setTypeface(gothamFont, Typeface.BOLD);
        txtDate = rowView.findViewById(R.id.txtInvoiceDate_InvoiceItem);
        txtAmount = rowView.findViewById(R.id.txtAmount_InvoiceItem);
        txtStatus = rowView.findViewById(R.id.txtStatus_InvoiceItem);
        layoutOneInvoiceItem = rowView.findViewById(R.id.layoutOneInvoiceItem_InvoiceItem);

        txtInvoiceN.setText(lists.get(position).getInvoiceN());
        txtDate.setText(lists.get(position).getDate());
        if (lists.get(position).getAmount() < 0.0)
            txtAmount.setText("-$" + String.format("%.2f", -lists.get(position).getAmount()));
        else
            txtAmount.setText("$" + String.format("%.2f", lists.get(position).getAmount()));

        if (lists.get(position).getStatus().equalsIgnoreCase("Unallocated")){
            txtStatus.setText("Unpaid");
            txtStatus.setTextColor(context.getResources().getColor(R.color.colorMySoftBrown));
        }
        if (lists.get(position).getStatus().equalsIgnoreCase("FullyAllocated")){
            txtStatus.setText("Paid");
            txtStatus.setTextColor(context.getResources().getColor(R.color.colorMyDarkGreen));
        }
        if (lists.get(position).getStatus().equalsIgnoreCase("Current Allocated")){
            txtStatus.setText("Processing");
            txtStatus.setTextColor(context.getResources().getColor(R.color.colorMyBlue));
        }


        layoutOneInvoiceItem.setTag(position);
        layoutOneInvoiceItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (int) view.getTag();

                myActivity.fragmentMainFlag = false;

                MyAccountInvoicesFragment myAccountInvoicesFragment = new MyAccountInvoicesFragment();
                myAccountInvoicesFragment.setMyActivity(myActivity);
                myAccountInvoicesFragment.setDetailIndex(i);
                myActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frgMyAccount, myAccountInvoicesFragment).commit();
            }
        });

        return rowView;
    }

    @Override
    public int getCount() {
        return lists.size();
    }
}
