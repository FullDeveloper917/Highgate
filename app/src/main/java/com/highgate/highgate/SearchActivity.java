package com.highgate.highgate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.highgate.highgate.adapters.Product;
import com.highgate.highgate.adapters.ProductListAdapter;
import com.highgate.highgate.myUtils.FontManager;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    private TextView btnRtn;
    private TextView txtRtn;
    private LinearLayout layoutRtn;

    private ProductListAdapter adapter;

    private TextView txtFilterUp;
    private TextView txtFilterDown;

    private TextView txtLabelFoundResult;
    private TextView txtFoundResult;

    private ListView listProducts;
    private LinearLayout layoutFilterCategory;
    private ListView listFilterCategory;
    private FilterCategoryAdapter filter_adapter;
    private boolean filterFlag = false;

    private List<Product> searchedProducts = new ArrayList<>();
    private List<String> resultListOfNamePerCategory = new ArrayList<>();
    private List<Integer> resultListOfNumberPerCategory = new ArrayList<>();

    private int searchPages = 0;
    private int totalResultNum = 0;
    private int resultNumPerCategory = 0;
    private int category_index = 0;

    private String searchKey;
    private boolean searchFlag;
    private Button btnSearchSku;
    private Button btnSearchWord;
    private EditText edtSearchKey;
    private TextView txtSearchImg;
    private Button btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        FontManager.setFontType(findViewById(R.id.layout_Search), gothamFont);

        setMenuEvents(this);
        DrawerLayout layoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);
        layoutDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        progressBar = (ProgressBar) findViewById(R.id.progressBar_Search);

        listProducts = (ListView) findViewById(R.id.listProduct_Search);
        adapter = new ProductListAdapter(this, R.layout.detail);

        layoutFilterCategory = (LinearLayout) findViewById(R.id.layoutFilterCategory);
        listFilterCategory = (ListView) findViewById(R.id.listFilterCategory);
        filter_adapter = new FilterCategoryAdapter(this, R.layout.one_filter_category);

        setFormula();

        setReturn();

        txtFilterUp = (TextView) findViewById(R.id.txtFilterUp_Search);
        txtFilterUp.setTypeface(iconFont);
        txtFilterUp.setOnClickListener(new OnClickFilter());

        txtFilterDown = (TextView) findViewById(R.id.txtFilterDown_Search);
        txtFilterDown.setTypeface(gothamFont, Typeface.BOLD);
        txtFilterDown.setOnClickListener(new OnClickFilter());

        TextView txtSelectFilterByCategory = (TextView) findViewById(R.id.txtSelectFilterbyCategory);
        txtSelectFilterByCategory.setTypeface(gothamFont, Typeface.BOLD);
        TextView txtFilterCategoryResults = (TextView) findViewById(R.id.txtFilterCategoryResults);
        txtFilterCategoryResults.setTypeface(gothamFont, Typeface.BOLD);

        edtSearchKey = (EditText)findViewById(R.id.editSearch_Search);
//        edtSearchKey.setImeActionLabel("GO", KeyEvent.KEYCODE_ENTER);

        edtSearchKey.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO){

                    InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtSearchKey.getWindowToken(), 0);

                    btnGo.performClick();
                    return true;
                }
                return false;
            }
        });


        btnSearchSku = (Button) findViewById(R.id.btnSearchSku_Search);
        btnSearchWord = (Button) findViewById(R.id.btnSearchWord_Search);

        Intent intent = getIntent();
        searchFlag = intent.getBooleanExtra("SearchFlag", false);
        if (searchFlag) {
            btnSearchSku.setBackgroundColor(getResources().getColor(R.color.colorMyBlack));
            btnSearchSku.setTextColor(getResources().getColor(R.color.colorMyDarkGray));
            btnSearchWord.setBackgroundColor(getResources().getColor(R.color.colorMyMain));
            btnSearchWord.setTextColor(getResources().getColor(R.color.colorMyWhite));
            edtSearchKey.setHint(getResources().getString(R.string.search_word_hint));
        }
        else {
            btnSearchSku.setBackgroundColor(getResources().getColor(R.color.colorMyMain));
            btnSearchSku.setTextColor(getResources().getColor(R.color.colorMyWhite));
            btnSearchWord.setBackgroundColor(getResources().getColor(R.color.colorMyBlack));
            btnSearchWord.setTextColor(getResources().getColor(R.color.colorMyDarkGray));
            edtSearchKey.setHint(getResources().getString(R.string.search_sku_hint));
        }
        searchKey = intent.getStringExtra("SearchKey").trim();
        if (!searchKey.equals("")){
            edtSearchKey.setText(searchKey);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

        btnSearchSku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSearchSku.setBackgroundColor(getResources().getColor(R.color.colorMyMain));
                btnSearchSku.setTextColor(getResources().getColor(R.color.colorMyWhite));
                btnSearchWord.setBackgroundColor(getResources().getColor(R.color.colorMyBlack));
                btnSearchWord.setTextColor(getResources().getColor(R.color.colorMyDarkGray));
                edtSearchKey.setHint(getResources().getString(R.string.search_sku_hint));
                searchFlag = false;
            }
        });

        btnSearchWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSearchSku.setBackgroundColor(getResources().getColor(R.color.colorMyBlack));
                btnSearchSku.setTextColor(getResources().getColor(R.color.colorMyDarkGray));
                btnSearchWord.setBackgroundColor(getResources().getColor(R.color.colorMyMain));
                btnSearchWord.setTextColor(getResources().getColor(R.color.colorMyWhite));
                edtSearchKey.setHint(getResources().getString(R.string.search_word_hint));
                searchFlag = true;
            }
        });

        btnGo = (Button)findViewById(R.id.btnGo_Search);
        btnGo.setTypeface(gothamFont, Typeface.BOLD);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPages =0;
                totalResultNum = 0;
                resultNumPerCategory = 0;
                category_index = 0;
                searchedProducts.clear();
                resultListOfNamePerCategory.clear();
                resultListOfNumberPerCategory.clear();
                searchKey = edtSearchKey.getText().toString().trim();
                txtFoundResult.setText(searchKey);
                listProducts.setAdapter(null);
                listFilterCategory.setAdapter(null);

                totalResultNum = 0;
                txtLabelFoundResult.setText("We have found " + String.valueOf(totalResultNum) + " results for:");

                if (searchKey.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Please insert search keyword!", Toast.LENGTH_SHORT).show();
                }
                else {
                    btnGo.setEnabled(false);
                    edtSearchKey.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    searchProducts(categoriesNameList.get(0), 1);
                }

            }
        });

        TextView txtMark = (TextView) findViewById(R.id.txtMark_OrderReport);
        txtMark.setTypeface(iconFont);
        layoutOrderReport = (LinearLayout) findViewById(R.id.layoutOrderReport_Search);
        txtOrderReference = (TextView) findViewById(R.id.txtOrderReference_OrderReport);
        btnCloseOrderReport = (Button) findViewById(R.id.btnClose_OrderReport);
        btnCloseOrderReport.setTypeface(iconFont);
        btnCloseOrderReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutOrderReport.setVisibility(View.GONE);
            }
        });

        searchPages =0;
        totalResultNum = 0;
        resultNumPerCategory = 0;
        category_index = 0;
        searchedProducts.clear();
        resultListOfNamePerCategory.clear();
        resultListOfNumberPerCategory.clear();
        searchKey = edtSearchKey.getText().toString();

        txtFoundResult.setText(searchKey);
        if (searchKey.isEmpty()) {
            Toast.makeText(this, "Please insert search keyword!", Toast.LENGTH_SHORT).show();
        }
        else {
            btnGo.setEnabled(false);
            edtSearchKey.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            searchProducts(categoriesNameList.get(0), 1);
        }

        layoutMain = (DrawerLayout) findViewById(R.id.layoutMain_Search);
        layoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        layoutZoomImage = (RelativeLayout) findViewById(R.id.layoutZoomImage_Search);
        layoutZoomImage.getLayoutParams().width = screenWidth;
        imgZoomImage = (ImageView) findViewById(R.id.imgZoomImage_Search);
        btnZoomImageClose = (Button) findViewById(R.id.btnZoomImageClose_Search);
        btnZoomImageClose.setTypeface(iconFont);
        btnZoomImageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutMain.closeDrawer(layoutZoomImage);
            }
        });

    }

    private void searchProducts(final String currentCategory, final int page) {

        String apiUrl;

        if (searchFlag)
            apiUrl = "http://www.highgateair.com.au/productcategory/index/search?q=" + encodeStringUri(searchKey)
                + "&group=" + myUser.getGroup_id()
                + "&category=" + categoriesMap.get(currentCategory).toString()
                + "&page=" + String.valueOf(page);
        else
            apiUrl= "http://www.highgateair.com.au/productcategory/index/search?q=" + encodeSkuStringUri(searchKey)
                    + "&group=" + myUser.getGroup_id()
                    + "&category=" + categoriesMap.get(currentCategory).toString()
                    + "&page=" + String.valueOf(page) + "&type=sku";


        Ion.with(getApplicationContext())
                .load(apiUrl)
                .setTimeout(5000)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        if (result == null || result.isJsonNull()){
                            Toast.makeText(SearchActivity.this, "Sorry, Search Failed. Retry again...", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            edtSearchKey.setEnabled(true);
                            btnGo.setEnabled(true);
                            return;
                        }
                        resultNumPerCategory += result.size() - 1;

                        for(int i = 0; i < result.size() - 1; i++){
                            Product oneProduct = new Product();

                            oneProduct.setProperty(result.get(i).getAsJsonObject());

                            searchedProducts.add(oneProduct);
//                            adapter.addItem(oneProduct);
//                            adapter.setActivity(SearchActivity.this);
//                            adapter.notifyDataSetChanged();
//                            listProducts.setAdapter(adapter);

                            totalResultNum ++;
                            txtLabelFoundResult.setText("We have found " + String.valueOf(totalResultNum) + " results for:");
                        }

                        if ( page == 1){
                            searchPages = result.get(result.size() - 1).getAsJsonObject().get("total_page").getAsInt();
                        }

                        if (searchPages - 1 > page){
                            searchProducts(currentCategory, page + 1);
                        }
                        else {
                            category_index ++;
                            if (category_index < categoriesNameList.size()){
                                if (resultNumPerCategory != 0){
                                    resultListOfNamePerCategory.add(categoriesNameList.get(category_index - 1));
                                    resultListOfNumberPerCategory.add(resultNumPerCategory);
                                }
                                resultNumPerCategory = 0;
                                searchPages = 0;
//
//                                adapter.setList(searchedProducts);
//                                adapter.setActivity(SearchActivity.this);
//                                adapter.notifyDataSetChanged();
//                                listProducts.setAdapter(adapter);

                                filter_adapter.setCategoryNameList(resultListOfNamePerCategory);
                                filter_adapter.setNumberList(resultListOfNumberPerCategory);

                                listFilterCategory.setAdapter(filter_adapter);

                                searchProducts(categoriesNameList.get(category_index), 1);
                            }
                            else {
                                if (resultNumPerCategory != 0){
                                    resultListOfNamePerCategory.add(categoriesNameList.get(category_index - 1));
                                    resultListOfNumberPerCategory.add(resultNumPerCategory);
                                }
                                txtLabelFoundResult.setText("We have found " + String.valueOf(totalResultNum) + " results for:");

                                adapter.setList(searchedProducts);
                                adapter.setActivity(SearchActivity.this);
                                listProducts.setAdapter(adapter);

                                filter_adapter.setCategoryNameList(resultListOfNamePerCategory);
                                filter_adapter.setNumberList(resultListOfNumberPerCategory);

                                listFilterCategory.setAdapter(filter_adapter);

                                edtSearchKey.setEnabled(true);
                                btnGo.setEnabled(true);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });
    }

    private void searchProductsByCategory(final String currentCategory, final int page){
        progressBar.setVisibility(View.VISIBLE);

        String apiUrl = "http://www.highgateair.com.au/productcategory/index/search?q=" + encodeStringUri(searchKey)
                + "&group=" + myUser.getGroup_id()
                + "&category=" + categoriesMap.get(currentCategory).toString()
                + "&page=" + String.valueOf(page);


        if (!searchFlag) apiUrl += "&type=sku";

        Ion.with(getApplicationContext())
                .load(apiUrl)
                .setTimeout(3000)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        if (result == null || result.isJsonNull()){
                            Toast.makeText(SearchActivity.this, "Sorry, Search Failed. Retry again...", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            edtSearchKey.setEnabled(true);
                            btnGo.setEnabled(true);
                            return;
                        }
                        for(int i = 0; i < result.size() - 1; i++){
                            Product oneProduct = new Product();
                            oneProduct.setProperty(result.get(i).getAsJsonObject());
                            searchedProducts.add(oneProduct);

                            totalResultNum ++;
                            txtLabelFoundResult.setText("We have found " + String.valueOf(totalResultNum) + " results for:");

                        }

                        if ( page == 1){
                            searchPages = result.get(result.size() - 1).getAsJsonObject().get("total_page").getAsInt();
                        }

                        if (searchPages - 1 > page){

                            adapter.setList(searchedProducts);
                            adapter.setActivity(SearchActivity.this);
                            listProducts.setAdapter(adapter);

                            searchProductsByCategory(currentCategory, page + 1);
                        }
                        else {
                            adapter.setList(searchedProducts);
                            adapter.setActivity(SearchActivity.this);
                            listProducts.setAdapter(adapter);

                            progressBar.setVisibility(View.INVISIBLE);
                            edtSearchKey.setEnabled(true);
                            btnGo.setEnabled(true);
                        }

                    }
                });
    }

    private class OnClickFilter implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            filterFlag = !(filterFlag);
            if(filterFlag){
                listProducts.setVisibility(View.GONE);
                layoutFilterCategory.setVisibility(View.VISIBLE);

                txtFilterUp.setBackgroundColor(getResources().getColor(R.color.colorMyMain));
                txtFilterUp.setTextColor(getResources().getColor(R.color.colorMyWhite));
                txtFilterUp.setText(getResources().getText(R.string.fa_times_circle));
                txtFilterDown.setBackgroundColor(getResources().getColor(R.color.colorMyMain));
                txtFilterDown.setTextColor(getResources().getColor(R.color.colorMyWhite));
            }
            else{
                listProducts.setVisibility(View.VISIBLE);
                layoutFilterCategory.setVisibility(View.GONE);

                txtFilterUp.setBackgroundColor(getResources().getColor(R.color.colorMyGray));
                txtFilterUp.setTextColor(getResources().getColor(R.color.colorMyBlack));
                txtFilterUp.setText(getResources().getText(R.string.fa_filter));
                txtFilterDown.setBackgroundColor(getResources().getColor(R.color.colorMyGray));
                txtFilterDown.setTextColor(getResources().getColor(R.color.colorMyBlack));
            }

        }
    }

    private void setFormula(){
        txtLabelFoundResult = (TextView) findViewById(R.id.txtLabelFoundResult_Search);
        txtLabelFoundResult.setTypeface(gothamFont, Typeface.BOLD);
        txtFoundResult = (TextView) findViewById(R.id.txtFoundResult_Search);

        txtSearchImg = (TextView) findViewById(R.id.txtSearchImg_Search);
        txtSearchImg.setTypeface(iconFont);
        txtSearchImg.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 5);
        txtSearchImg.setPadding(unitFontSize * 3, unitFontSize * 2, unitFontSize * 3, unitFontSize * 2);
    }

    private void setReturn(){
        btnRtn = (TextView) findViewById(R.id.btnRtn_Search);
        btnRtn.setTypeface(iconFont);

        txtRtn = (TextView) findViewById(R.id.txtRtn_Search);
        txtRtn.setTypeface(iconFont);

        layoutRtn = (LinearLayout) findViewById(R.id.layoutRtn_Search);
        layoutRtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class FilterCategoryAdapter extends ArrayAdapter{

        private Context context;
        private List<String> categoryNameList = new ArrayList<>();
        private List<Integer> numberList = new ArrayList<>();

        public FilterCategoryAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
            this.context = context;
        }

        public void setCategoryNameList(List<String> categoryNameList) {
            this.categoryNameList = categoryNameList;
        }

        public void setNumberList(List<Integer> numberList) {
            this.numberList = numberList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.one_filter_category, parent, false);

            final Button btnFilterCategory = (Button)view.findViewById(R.id.btnFilterCategory);
            btnFilterCategory.setText(categoryNameList.get(position));
            btnFilterCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    searchedProducts.clear();
                    totalResultNum = 0;
                    txtLabelFoundResult.setText("We have found " + String.valueOf(totalResultNum) + " results for:");
                    searchProductsByCategory(btnFilterCategory.getText().toString(), 1);
                    filterFlag = false;
                    listProducts.setVisibility(View.VISIBLE);
                    layoutFilterCategory.setVisibility(View.GONE);

                    txtFilterUp.setBackgroundColor(getResources().getColor(R.color.colorMyGray));
                    txtFilterUp.setTextColor(getResources().getColor(R.color.colorMyBlack));
                    txtFilterUp.setText(getResources().getText(R.string.fa_filter));
                    txtFilterDown.setBackgroundColor(getResources().getColor(R.color.colorMyGray));
                    txtFilterDown.setTextColor(getResources().getColor(R.color.colorMyBlack));
                }
            });

            LinearLayout layout_one_filter_category = (LinearLayout) view.findViewById(R.id.layout_one_filter_category);
            FontManager.setFontType(layout_one_filter_category, gothamFont);
            layout_one_filter_category.setBackgroundColor(getResources().getColor(R.color.colorMySoftGray));

            TextView txtProductNum = (TextView) view.findViewById(R.id.txtProductNumCategory);

            txtProductNum.setText(String.valueOf(numberList.get(position)));

            return view;
        }

        @Override
        public int getCount() {
            return this.categoryNameList.size();
        }
    }
}
