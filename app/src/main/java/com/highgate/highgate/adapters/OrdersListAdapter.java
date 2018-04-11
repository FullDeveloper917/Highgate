package com.highgate.highgate.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.highgate.highgate.CartActivity;
import com.highgate.highgate.myUtils.FontManager;
import com.highgate.highgate.R;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MH on 7/7/2017.
 */

public class OrdersListAdapter extends ArrayAdapter {
    private final Context context;
    private List<Product> lists = new ArrayList<Product>();

    private TextView txtProductDescription;
    private TextView txtProductPrice;
    private TextView txtTotalPrice;
    private TextView txtProductSku;
    private TextView txtProductNumber;
    private ImageView imageProduct;

    private Button btnPlus;
    private Button btnMinus;
    private Button btnDelete;

    private CartActivity myActivity;

    public OrdersListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.context = context;
    }

    public void addItem(Product item){
        lists.add(item);
    }

    public void setProductList(List<Product> lists){
        this.lists = lists;
    }

    public void setMyActivity(AppCompatActivity myActivity){
        this.myActivity = (CartActivity) myActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.result_detail, parent, false);

        Typeface iconFont = FontManager.getTypeface(context, FontManager.FONTAWESOME);
        Typeface gothamFont = FontManager.getTypeface(context, FontManager.GOTHAM);
        FontManager.setFontType(rowView.findViewById(R.id.layout_Result_Detail), gothamFont);

        txtProductPrice= (TextView) rowView.findViewById(R.id.txtPrice_Result);
        txtTotalPrice= (TextView) rowView.findViewById(R.id.txtTotalPrice_Result);
        txtProductSku= (TextView) rowView.findViewById(R.id.txtPart_Result);
        txtProductDescription = (TextView) rowView.findViewById(R.id.txtDesc_Result);
        txtProductNumber = (TextView) rowView.findViewById(R.id.txtNum_Result);
        imageProduct = (ImageView) rowView.findViewById(R.id.imgProduct_Result);
        btnPlus = rowView.findViewById(R.id.btnPlus);
        btnMinus = rowView.findViewById(R.id.btnMinus);
        btnDelete = rowView.findViewById(R.id.btnTrash_Result);

        float productPrice = lists.get(position).getCustomer_price();
        int productNumber = lists.get(position).getNumber();

        txtProductPrice.setText("$" + String.format("%.2f", productPrice));
        txtTotalPrice.setText("$" + String.format("%.2f", productPrice * productNumber));
        txtProductDescription.setText(lists.get(position).getName());
        txtProductSku.setText(txtProductSku.getText() + lists.get(position).getSku());
        txtProductNumber.setText(String.valueOf(productNumber));


        if (lists.get(position).getImages().size() > 0) {
            int currentImageIndex = lists.get(position).getCurrentImageIndex();
            Ion.with(imageProduct)
                    .placeholder(R.drawable.fake_image)
                    .error(R.drawable.fake_image)
                    .load(lists.get(position).getImages().get(currentImageIndex));
        }

        btnDelete.setTag(position);
        btnDelete.setTypeface(iconFont);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (Integer)view.getTag();
                lists.remove(i);
                myActivity.refreshTotal();
                notifyDataSetChanged();
            }
        });

        btnPlus.setTag(position);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (Integer)view.getTag();
                int productNumber = lists.get(i).getNumber();
                productNumber++;
                lists.get(i).setNumber(productNumber);
                myActivity.refreshTotal();
                notifyDataSetChanged();
            }
        });

        btnMinus.setTag(position);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (Integer)view.getTag();
                int productNumber = lists.get(i).getNumber();
                if (productNumber == 1) return;
                productNumber--;
                lists.get(i).setNumber(productNumber);
                myActivity.refreshTotal();
                notifyDataSetChanged();
            }
        });
        return rowView;
    }

    @Override
    public int getCount() {
        return lists.size();
    }
}
