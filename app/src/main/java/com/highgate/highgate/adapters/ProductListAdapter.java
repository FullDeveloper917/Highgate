package com.highgate.highgate.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.highgate.highgate.BaseActivity;
import com.highgate.highgate.myUtils.FontManager;
import com.highgate.highgate.R;
import com.highgate.highgate.myUtils.ImageZoomTouchListener;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MH on 7/7/2017.
 */

public class ProductListAdapter extends ArrayAdapter {
    private final Context context;
    private List<Product> lists = new ArrayList<>();

    private BaseActivity activity;

    private TextView txtProductName;
    private TextView txtRrp;
    private TextView txtProductPrice;
    private TextView txtProductSku;
    private TextView txtProductStoke;
    private TextView txtImageNumber;
    private ImageView imageProduct;

    private Button btnOneClickOrder;
    private Button btnCheck;
    private Button btnClose;
    private Button btnAddCart;

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    private Button btnImage_left;
    private Button btnImage_right;
    private Button btnImage_Zoom;

    public ProductListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.context = context;
    }

    public void addItem(Product item) {
        lists.add(item);
    }

    public void setList(List<Product> lists) {
        this.lists = new ArrayList<>();
        for (Product oneProduct : lists){
            this.lists.add(new Product(oneProduct));
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.detail, parent, false);

        Typeface iconFont = FontManager.getTypeface(context.getApplicationContext(), FontManager.FONTAWESOME);
        Typeface gothamFont = FontManager.getTypeface(context.getApplicationContext(), FontManager.GOTHAM);

        FontManager.setFontType(rowView.findViewById(R.id.layout_Detail), gothamFont);

        txtProductName = (TextView) rowView.findViewById(R.id.txtProductName);
        txtProductSku = (TextView) rowView.findViewById(R.id.txtProductSku);
        txtRrp = (TextView) rowView.findViewById(R.id.txtRrp_Detail);
        txtProductPrice = (TextView) rowView.findViewById(R.id.txtProductPrice);
        txtProductStoke = (TextView) rowView.findViewById(R.id.txtProductStock);
        txtImageNumber = (TextView) rowView.findViewById(R.id.txtImageNumber);
        imageProduct = (ImageView) rowView.findViewById(R.id.imageProduct);
        imageProduct.setDrawingCacheEnabled(true);

        btnAddCart = (Button) rowView.findViewById(R.id.btnAddCart);
        btnOneClickOrder = (Button) rowView.findViewById(R.id.btnOneClickOrder);

        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);

        if (BaseActivity.oneClickFlag) {
            p1.weight = 1;
            btnAddCart.setLayoutParams(p1);
            p2.weight = 1;
            p2.setMargins(0, 0, 40, 0);
            btnOneClickOrder.setLayoutParams(p2);
        } else {
            p1.weight = 0;
            btnOneClickOrder.setLayoutParams(p1);
            p2.weight = 2;
            btnAddCart.setLayoutParams(p2);
        }

        btnAddCart.setTag(position);
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (Integer) view.getTag();
                BaseActivity.orderProductList.add(lists.get(i));
                Toast.makeText(activity, "Added", Toast.LENGTH_SHORT).show();

            }
        });

        btnOneClickOrder.setTag(position);
        btnOneClickOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (Integer) view.getTag();
                Product orderingProduct = lists.get(i);
                activity.progressBar.setVisibility(View.VISIBLE);
                view.setEnabled(false);
                productOrder(orderingProduct, view);
            }
        });

        txtProductName.setText(lists.get(position).getName());
        txtProductSku.setText(txtProductSku.getText() + lists.get(position).getSku());

        float rrp = lists.get(position).getPrice();
        txtRrp.setText("$" + String.format("%.2f", rrp));

        final float price = lists.get(position).getCustomer_price();
        txtProductPrice.setText("$" + String.format("%.2f", price));


        if (lists.get(position).getOn_hand() >= 1)
            txtProductStoke.setText("Available");
        else
            txtProductStoke.setText("Call for Availability");

        btnImage_Zoom = rowView.findViewById(R.id.btnImage_Zoom);
        btnImage_Zoom.setTypeface(iconFont);
        btnImage_Zoom.setTag(position);

        btnImage_Zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (int) view.getTag();

                if (lists.get(i).getImages().size() == 0) {
                    activity.imgZoomImage.setImageResource(R.drawable.fake_image);
                }
                else {
                    int currentImageIndex = lists.get(i).getCurrentImageIndex();
                    Ion.with(activity.imgZoomImage)
                            .placeholder(R.drawable.fake_image)
                            .error(R.drawable.fake_image)
                            .load(lists.get(i).getImages().get(currentImageIndex))
                            .setCallback(new FutureCallback<ImageView>() {
                                @Override
                                public void onCompleted(Exception e, ImageView result) {

                                    Matrix matrix = new Matrix();
                                    Drawable drawable = result.getDrawable();
                                    RectF imageRectF = new RectF(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                    RectF viewRectF = new RectF(0, 0, result.getWidth(), result.getHeight());
                                    matrix.setRectToRect(imageRectF, viewRectF, Matrix.ScaleToFit.CENTER);
                                    result.setImageMatrix(matrix);
                                }
                            });
                    activity.imgZoomImage.setOnTouchListener(new ImageZoomTouchListener());
                    activity.layoutMain.openDrawer(activity.layoutZoomImage);
                }
            }
        });

        if (lists.get(position).getImages().size() > 0) {
            int currentImageIndex = lists.get(position).getCurrentImageIndex();
            if (lists.get(position).getImages().size() > 0)
                txtImageNumber.setText(String.valueOf(currentImageIndex + 1) + "/" + String.valueOf(lists.get(position).getImages().size()));

            imageProduct.setTag(position);
            Ion.with(imageProduct)
                    .placeholder(R.drawable.fake_image)
                    .error(R.drawable.fake_image)
                    .load(lists.get(position).getImages().get(currentImageIndex));

        }

        final LinearLayout layoutOne = (LinearLayout) rowView.findViewById(R.id.layoutOne_Detail);
        final LinearLayout layoutTwo = (LinearLayout) rowView.findViewById(R.id.layoutTwo_Detail);

        btnCheck = (Button) rowView.findViewById(R.id.btnCheck_Detail);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOne.setVisibility(View.GONE);
                layoutTwo.setVisibility(View.VISIBLE);
            }
        });

        btnClose = (Button) rowView.findViewById(R.id.btnClose_Detail);
        btnClose.setTypeface(iconFont);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOne.setVisibility(View.VISIBLE);
                layoutTwo.setVisibility(View.GONE);
            }
        });

        btnImage_left =  rowView.findViewById(R.id.btnImage_left);
        btnImage_left.setTypeface(iconFont);
        btnImage_left.setTag(R.id.position_id, position);
        btnImage_left.setTag(R.id.image_product_id, imageProduct);
        btnImage_left.setTag(R.id.text_image_number_id, txtImageNumber);
        btnImage_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = (int) view.getTag(R.id.position_id);
                final ImageView currentImageProduct = (ImageView) view.getTag(R.id.image_product_id);
                TextView currentTxtImageNumber = (TextView) view.getTag(R.id.text_image_number_id);

                if (lists.get(i).getImages().size() < 2) return;

                int currentImageIndex = lists.get(i).decreaseImageIndex();
                currentTxtImageNumber.setText(String.valueOf(currentImageIndex + 1) + "/" + String.valueOf(lists.get(i).getImages().size()));

                Ion.with(currentImageProduct)
                        .placeholder(R.drawable.fake_image)
                        .error(R.drawable.fake_image)
                        .load(lists.get(i).getImages().get(currentImageIndex));
            }
        });

        btnImage_right = rowView.findViewById(R.id.btnImage_right);
        btnImage_right.setTypeface(iconFont);
        btnImage_right.setTag(R.id.position_id, position);
        btnImage_right.setTag(R.id.image_product_id, imageProduct);
        btnImage_right.setTag(R.id.text_image_number_id, txtImageNumber);
        btnImage_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = (int) view.getTag(R.id.position_id);
                final ImageView currentImageProduct = (ImageView) view.getTag(R.id.image_product_id);
                TextView currentTxtImageNumber = (TextView) view.getTag(R.id.text_image_number_id);

                if (lists.get(i).getImages().size() < 2) return;

                int currentImageIndex = lists.get(i).increaseImageIndex();
                currentTxtImageNumber.setText(String.valueOf(currentImageIndex + 1) + "/" + String.valueOf(lists.get(i).getImages().size()));

                Ion.with(currentImageProduct)
                        .placeholder(R.drawable.fake_image)
                        .error(R.drawable.fake_image)
                        .load(lists.get(i).getImages().get(currentImageIndex));

            }
        });

        imageProduct.setTag(R.id.btn_left_id, btnImage_left);
        imageProduct.setTag(R.id.btn_right_id, btnImage_right);
        imageProduct.setOnTouchListener(new View.OnTouchListener() {

            private float pre_x;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        pre_x = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        float dx = motionEvent.getX() - pre_x;

                        if (dx < 0) {
                            ((Button) view.getTag(R.id.btn_left_id)).performClick();
                        }
                        else {
                            ((Button) view.getTag(R.id.btn_right_id)).performClick();
                        }
                        break;
                }
                return true;
            }
        });

        return rowView;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    private boolean productOrder(Product product, final View view) {

        String currentDate = getCurrentDate();

        JsonObject rawBody = new JsonObject();

        rawBody.addProperty("DebtorId", String.valueOf(BaseActivity.myUser.getDebtor_id()));
        rawBody.addProperty("status", "0");
        rawBody.addProperty("BranchId", "0");
        rawBody.addProperty("DefaultLocationId", "1");
        rawBody.addProperty("CurrencyId", "0");
        rawBody.addProperty("SalesPersonId", "4419");
        rawBody.addProperty("ExchangeRate", "1");
        rawBody.addProperty("Reference", "Highgate-App-Order");
        rawBody.addProperty("Instructions", "");
        rawBody.addProperty("orderdate", currentDate);
        rawBody.addProperty("duedate", currentDate);
        rawBody.addProperty("customerordernumber", "1-CLICK-ORDER");
        rawBody.addProperty("narrative", "");
        rawBody.addProperty("contactid", "1");
        rawBody.addProperty("id", "1");

        JsonObject deliveryAddress = new JsonObject();
        deliveryAddress.addProperty("line1", "");
        deliveryAddress.addProperty("line2", BaseActivity.myUser.getAddress());
        deliveryAddress.addProperty("line3", BaseActivity.myUser.getSuburb());
        deliveryAddress.addProperty("line4", BaseActivity.myUser.getState());
        deliveryAddress.addProperty("line5", BaseActivity.myUser.getPostCode());
        deliveryAddress.addProperty("line6", "");
        rawBody.add("DeliveryAddress", deliveryAddress);

        JsonObject linesContent = new JsonObject();
        linesContent.addProperty("id", "1");
        linesContent.addProperty("stockcode", product.getSku());
        linesContent.addProperty("Description", product.getName());
        linesContent.addProperty("unitprice", product.getCustomer_price());
        linesContent.addProperty("orderquantity", "1");
        linesContent.addProperty("linetype", "0");
        linesContent.addProperty("IsOriginatedFromTemplate", "false");
        linesContent.addProperty("LocationId", "1");
        linesContent.addProperty("TaxRateId", "10");
        linesContent.addProperty("BomCode", "0");
        linesContent.addProperty("BomGroupId", "0");
        linesContent.addProperty("Discount", "0");
        linesContent.addProperty("IsPriceOverridden", "true");
        linesContent.addProperty("Narrative", "");
        linesContent.addProperty("BatchCode", "");
        linesContent.addProperty("DueDate", currentDate);

        JsonArray lines = new JsonArray();
        lines.add(linesContent);
        rawBody.add("lines", lines);

        JsonObject extrafieldsContent = new JsonObject();
        extrafieldsContent.addProperty("key", "X_CONTACT");
        extrafieldsContent.addProperty("value", BaseActivity.myUser.getFirstName() + " " + BaseActivity.myUser.getLastName());

        JsonArray extrafields = new JsonArray();
        extrafields.add(extrafieldsContent);
        rawBody.add("extrafields", extrafields);


        Ion.with(context)
                .load("POST", "https://exo.api.myob.com/salesorder")
                .setTimeout(5000)
                .setHeader("Authorization", BaseActivity.orderAPI_Authorization)
                .setHeader("x-myobapi-exotoken", BaseActivity.x_myobapi_exotoken)
                .setHeader("x-myobapi-key", BaseActivity.x_myobapi_key)
                .setHeader("Content-Type", BaseActivity.orderAPI_ContentType)
                .setJsonObjectBody(rawBody)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        JSONObject jsonResult = null;
                        try {
                            jsonResult = XML.toJSONObject(result);
                            int id = jsonResult.getJSONObject("SalesOrder").getInt("Id");
//                            Toast.makeText(getContext(), String.valueOf(id) + ": Order Successful", Toast.LENGTH_SHORT).show();

                            activity.layoutOrderReport.setVisibility(View.VISIBLE);
                            activity.txtOrderReference.setText(String.valueOf(id));
                        } catch (JSONException e1) {
                            Toast.makeText(getContext(), "Order failed", Toast.LENGTH_SHORT).show();
                            e1.printStackTrace();
                        }

                        activity.progressBar.setVisibility(View.GONE);

                        view.setEnabled(true);

                    }
                });

        return true;
    }

    private String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c.getTime());
    }

}
