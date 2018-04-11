package com.highgate.highgate;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.highgate.highgate.adapters.Product;
import com.highgate.highgate.myUtils.FontManager;
import com.highgate.highgate.myUtils.User;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MH on 7/7/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public int screenWidth;
    public int screenHeight;

    public int unitFontSize;

    public boolean menuFlag = false;

    public static boolean oneClickFlag = true;
    public static List<Product> orderProductList = new ArrayList<>();
    public static List<String> categoriesNameList = new ArrayList<>();
    public static HashMap<String, Integer> categoriesMap = new HashMap<>();

    //Authorization Constants
    public final static String BASE_WWW_URL = "http://www.highgateair.com.au/";
    public final static String BASE_URL = "http://highgateair.com.au/";
    public final static String CONSUMER_KEY = "5620bc66624b935c50bf6e1680c4a4cf";
    public final static String CONSUMER_SECRET = "2c9fde2448834832a5fe4a85307ee703";
    public final static String CALLBACK_URL = "http://httpbin.org/get";


    public Typeface gothamFont, iconFont;

    public static Token requestToken;
    public static Token accessToken;
    public static OAuthService service;
    public static String oauth_header;

    public static final String orderAPI_Authorization = "Basic RVhPTUFHRU5UT0FQSTpFWE9NQUdFTlRPQVBJUEFTU1dPUkQ=";
    public static final String x_myobapi_exotoken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1MzgyMjc4MDAsImlzcyI6IlMtMS01LTIxLTI5MDA3NDAyMjUtMjc4MDc0NzU5MC03MTQ4Nzc4NjpVSGN3SjRvM0RLVU5PM3NURzczQU9nPT0iLCJhdWQiOiJodHRwczovL2V4by5hcGkubXlvYi5jb20vIiwibmFtZSI6IkVYT01BR0VOVE9BUEkiLCJzdGFmZm5vIjoiNDQ0MSIsImFwcGlkIjoiNDQwMCJ9.Kgm1X9mWG9B-T4w3FFzQgsi7_uBh_dILjC_cqXcXWPk";
    public static final String x_myobapi_key = "uqs34xnuptbvt2na5dtq7auz";
    public static final String orderAPI_ContentType = "application/json";

    public static final String SHARE_ID = "Id";
    public static final String SHARE_FIRSTNAME = "FristName";
    public static final String SHARE_LASTNAME = "LastName";
    public static final String SHARE_EMAIL = "Email";
    public static final String SHARE_COMPANY = "Company";
    public static final String SHARE_ADDRESS = "Address";
    public static final String SHARE_SUBURB = "Suburb";
    public static final String SHARE_POSTCODE = "PostCode";
    public static final String SHARE_STATE = "State";
    public static final String SHARE_FREIGHT = "Freight";
    public static final String SHARE_DEBTORID = "DebtorId";
    public static final String SHARE_GROUPID = "GroupId";

    public static final String SHARE_FILE = "Highgate_session";


    public ProgressBar progressBar;
    public LinearLayout layoutOrderReport;
    public Button btnCloseOrderReport;
    public TextView txtOrderReference;
    public DrawerLayout layoutMain;
    public RelativeLayout layoutZoomImage;
    public ImageView imgZoomImage;
    public Button btnZoomImageClose;

    public static User myUser;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gothamFont = FontManager.getTypeface(getApplicationContext(), FontManager.GOTHAM);
        iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        unitFontSize = screenWidth / 100;
    }

    public void SetSelected(Button btn){
        btn.setEnabled(true);
        btn.setTextColor(btn.getContext().getResources().getColor(R.color.colorMyBlack));
        btn.setBackgroundColor(getResources().getColor(R.color.colorMyGray));
    }

    public void SetUnselected(Button btn){
        btn.setEnabled(false);
        btn.setTextColor(btn.getContext().getResources().getColor(R.color.colorMyDarkGray));
        btn.setBackgroundColor(getResources().getColor(R.color.colorMySoftGray));
    }

    public void setMenuEvents(final AppCompatActivity activity){

        menuFlag = false;
        DrawerLayout layoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);
        RelativeLayout menu_content = (RelativeLayout) findViewById(R.id.menu_content);
        menu_content.getLayoutParams().width = screenWidth * 2/3;
        layoutDrawer.closeDrawer(menu_content);

        LinearLayout layoutMenu = (LinearLayout) findViewById(R.id.layoutMenu);
        layoutMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });

        TextView txtMenu = (TextView) findViewById(R.id.txtMenu);
        txtMenu.setTypeface(iconFont);
        txtMenu.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 5);
        txtMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuFlag = !(menuFlag);
                DrawerLayout layoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);
                RelativeLayout menu_content = (RelativeLayout) findViewById(R.id.menu_content);
                if (menuFlag){
                    setShowOneClick(BaseActivity.oneClickFlag);
                    layoutDrawer.openDrawer(menu_content);
                }
                else{
                    layoutDrawer.closeDrawer(menu_content);
                }
            }
        });

        Button btnMenu = (Button) findViewById(R.id.btnMenu);
        btnMenu.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 5);
        btnMenu.setTypeface(gothamFont, Typeface.BOLD);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuFlag = !(menuFlag);
                DrawerLayout layoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);
                RelativeLayout menu_content = (RelativeLayout) findViewById(R.id.menu_content);
                if (menuFlag){
                    setShowOneClick(BaseActivity.oneClickFlag);
                    layoutDrawer.openDrawer(menu_content);
                }
                else{
                    layoutDrawer.closeDrawer(menu_content);
                }
            }
        });

        TextView txtCart = (TextView) findViewById(R.id.txtCart);
        txtCart.setTypeface(iconFont);
        txtCart.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 5);
        txtCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CartActivity.class);
                activity.startActivity(intent);
            }
        });

        Button btnCart = (Button)activity.findViewById(R.id.btnCart);
        btnCart.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 5);
        btnCart.setTypeface(gothamFont, Typeface.BOLD);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CartActivity.class);
                activity.startActivity(intent);
            }
        });

        TextView txtCall = (TextView) findViewById(R.id.txtCall);
        txtCall.setTypeface(iconFont);
        txtCall.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 5);
        txtCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:1800151515");
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });

        Button btnCall = (Button) findViewById(R.id.btnCall);
        btnCall.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 5);
        btnCall.setTypeface(gothamFont, Typeface.BOLD);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:1800151515");
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });

        TextView txtGoDash = (TextView) findViewById(R.id.txtGoDash_Menu);
        txtGoDash.setTypeface(iconFont);
        Button btnGoDash = (Button) findViewById(R.id.btnGoDash_Menu);
        btnGoDash.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 4);
        btnGoDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuFlag = false;
                DrawerLayout layoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);
                RelativeLayout menu_content = (RelativeLayout) findViewById(R.id.menu_content);
                layoutDrawer.closeDrawer(menu_content);
                Intent intent = new Intent(activity, DashboardActivity.class);
                startActivity(intent);
            }
        });

        TextView txtEditProfile = (TextView) findViewById(R.id.txtEditProfile_Menu);
        txtEditProfile.setTypeface(iconFont);
        Button btnEditProfile = (Button) findViewById(R.id.btnEditProFile_Menu);
        btnEditProfile.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 4);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuFlag = false;
                DrawerLayout layoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);
                RelativeLayout menu_content = (RelativeLayout) findViewById(R.id.menu_content);
                layoutDrawer.closeDrawer(menu_content);
                Intent intent = new Intent(activity, ProfileActivity.class);
                startActivity(intent);
            }
        });

        TextView txtOneClick = (TextView) findViewById(R.id.txtOneClick_Menu);
        txtOneClick.setTypeface(iconFont);
        Button btnOneClick0 = (Button) findViewById(R.id.btnOneClick_Menu);
        btnOneClick0.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 4);
        Button btnOneClick1 = (Button) findViewById(R.id.btnOneClick1_Menu);
        btnOneClick1.setTypeface(iconFont);
        Button btnOneClick2 = (Button) findViewById(R.id.btnOneClick2_Menu);
        btnOneClick0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.oneClickFlag=(!BaseActivity.oneClickFlag);
                setShowOneClick(BaseActivity.oneClickFlag);
            }
        });

        btnOneClick1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.oneClickFlag=(!BaseActivity.oneClickFlag);
                setShowOneClick(BaseActivity.oneClickFlag);
            }
        });

        btnOneClick2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.oneClickFlag=(!BaseActivity.oneClickFlag);
                setShowOneClick(BaseActivity.oneClickFlag);
            }
        });

        final Button btnLogout = (Button) findViewById(R.id.btnLogout_Menu);
        btnLogout.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 4);
        final TextView txtLogout = (TextView) findViewById(R.id.txtLogout_Menu);
        txtLogout.setTypeface(iconFont);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogout.setEnabled(false);
                txtLogout.setEnabled(false);
                SharedPreferences sharedPref = activity.getSharedPreferences(SHARE_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(SHARE_ID, -1);
                editor.putString(SHARE_FIRSTNAME, "");
                editor.putString(SHARE_LASTNAME, "");
                editor.putString(SHARE_EMAIL, "");
                editor.putString(SHARE_COMPANY, "");
                editor.putString(SHARE_ADDRESS, "");
                editor.putString(SHARE_SUBURB, "");
                editor.putString(SHARE_POSTCODE, "");
                editor.putString(SHARE_STATE, "");
                editor.putString(SHARE_FREIGHT, "");
                editor.putInt(SHARE_DEBTORID, -1);
                editor.putInt(SHARE_GROUPID, -1);
                editor.commit();

                btnLogout.setEnabled(false);

                Ion.with(activity)
                        .load("https://www.highgateair.com.au/customer/account/logout/")
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                btnLogout.setEnabled(true);
                                myUser = new User();

                                btnLogout.setEnabled(true);
                                txtLogout.setEnabled(true);

                                menuFlag = false;
                                DrawerLayout layoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);
                                RelativeLayout menu_content = (RelativeLayout) findViewById(R.id.menu_content);
                                layoutDrawer.closeDrawer(menu_content);
                                Intent intent = new Intent(activity, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogout.setEnabled(false);
                txtLogout.setEnabled(false);

                SharedPreferences sharedPref = activity.getSharedPreferences(SHARE_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(SHARE_ID, -1);
                editor.putString(SHARE_FIRSTNAME, "");
                editor.putString(SHARE_LASTNAME, "");
                editor.putString(SHARE_EMAIL, "");
                editor.putString(SHARE_COMPANY, "");
                editor.putString(SHARE_ADDRESS, "");
                editor.putString(SHARE_SUBURB, "");
                editor.putString(SHARE_POSTCODE, "");
                editor.putString(SHARE_STATE, "");
                editor.putString(SHARE_FREIGHT, "");
                editor.putInt(SHARE_DEBTORID, -1);
                editor.putInt(SHARE_GROUPID, -1);
                editor.commit();

                btnLogout.setEnabled(false);

                Ion.with(activity)
                        .load("https://www.highgateair.com.au/customer/account/logout/")
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                btnLogout.setEnabled(true);
                                myUser = new User();
                                btnLogout.setEnabled(true);
                                txtLogout.setEnabled(true);
                                Intent intent = new Intent(activity, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });
    }

    private void setShowOneClick(boolean flag){
        Button btnOneClick1_Menu = (Button) findViewById(R.id.btnOneClick1_Menu);
        Button btnOneClick2_Menu = (Button) findViewById(R.id.btnOneClick2_Menu);
        if (flag){
            btnOneClick1_Menu.setText(R.string.fa_check);
            btnOneClick1_Menu.setTextColor(getResources().getColor(R.color.colorMyGreen));
            btnOneClick2_Menu.setText("ON");
            btnOneClick2_Menu.setTextColor(getResources().getColor(R.color.colorMyGreen));
        }
        else {
            btnOneClick1_Menu.setText(R.string.fa_times);
            btnOneClick1_Menu.setTextColor(getResources().getColor(R.color.colorMyDarkGray));
            btnOneClick2_Menu.setText("OFF");
            btnOneClick2_Menu.setTextColor(getResources().getColor(R.color.colorMyDarkGray));
        }
    }



    public String encodeStringUri(String uri){
        return uri.replace(" ", "%20");
    }

    public String encodeSkuStringUri(String uri){
        return uri.replace(" ", "-");
    }

    public  String encodeNumberUri(String uri){
        if (uri.substring(0, 1).equals("#"))
            return uri.substring(1, uri.length());
        return encodeStringUri(uri);
    }

    public void tryToRequestMarshmallowAccessNetWorkStatePermission() {

        if (Build.VERSION.SDK_INT < 23) return;

        final Method checkSelfPermissionMethod = getCheckSelfPermissionMethod();

        if (checkSelfPermissionMethod == null) return;

        try {

            final Integer permissionCheckResult = (Integer) checkSelfPermissionMethod.invoke(this, Manifest.permission.ACCESS_NETWORK_STATE);
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) return;

            final Method requestPermissionsMethod = getRequestPermissionsMethod();
            if (requestPermissionsMethod == null) return;

            requestPermissionsMethod.invoke(this, new String[]{android.Manifest.permission.ACCESS_NETWORK_STATE}, 1);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private Method getCheckSelfPermissionMethod() {
        try {
            return Activity.class.getMethod("checkSelfPermission", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    private Method getRequestPermissionsMethod() {
        try {
            final Class[] parameterTypes = {String[].class, int.class};

            return Activity.class.getMethod("requestPermissions", parameterTypes);

        } catch (Exception e) {
            return null;
        }
    }

}
