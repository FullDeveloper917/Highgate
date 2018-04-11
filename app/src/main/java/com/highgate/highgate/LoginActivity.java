package com.highgate.highgate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.os.StrictMode;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.highgate.highgate.myUtils.FontManager;
import com.highgate.highgate.myUtils.User;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;

import java.util.Map;

public class LoginActivity extends BaseActivity {

    private ImageView imgLogo;
    private Button btnLogin1;
    private Button btnLogin2;
    private Button btnRegister;
    private TextView txtRegister;
    private Button btnForgot;
    private EditText edtUsername;
    private EditText edtPassword;
    private ProgressBar progressBar;

    private String username;
    private String password;

    private String form_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setFormula();

        imgLogo = (ImageView) findViewById(R.id.imgLogo_Login);
        imgLogo.getLayoutParams().height = screenWidth * 556 / 1280;

        edtUsername = (EditText) findViewById(R.id.editEmail_Login);
        edtUsername.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 5);
//        edtUsername.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                    if (i == EditorInfo.IME_ACTION_DONE)  {
////                    // hide virtual keyboard
//                        InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(edtUsername.getWindowToken(), 0);
//                        isLogin();
//                        return true;
//                    }
//                    return false;
//                }
//            }
//        );



        edtPassword = (EditText) findViewById(R.id.editPass_Login);
        edtPassword.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 5);
//        edtPassword.setImeActionLabel("GO", KeyEvent.KEYCODE_ENTER);
        edtPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
//                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtPassword.getWindowToken(), 0);
                    isLogin();
                    return true;
                }
                return false;
            }
        });


        btnLogin1 = (Button)findViewById(R.id.btnLogin_Login1);
        btnLogin1.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 8);
        btnLogin1.setTypeface(iconFont);
        btnLogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin();
            }
        });

        btnLogin2 = (Button)findViewById(R.id.btnLogin_Login2);
        btnLogin2.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 6);
        btnLogin2.setTypeface(gothamFont, Typeface.BOLD);
        btnLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogin();
            }
        });

        btnForgot = (Button)findViewById(R.id.btnForgot_Login);
        btnForgot.setTypeface(gothamFont, Typeface.BOLD);
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                intent.putExtra("url", "https://www.highgateair.com.au/customer/account/forgotpassword/");
                startActivity(intent);
            }
        });

        btnRegister = (Button)findViewById(R.id.btnRegister_Login);
        btnRegister.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 6);
        btnRegister.setTypeface(gothamFont, Typeface.BOLD);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                intent.putExtra("url", "https://www.highgateair.com.au/customer/account/create/");
                startActivity(intent);
            }
        });

        txtRegister = (TextView)findViewById(R.id.txtRegister_Login);
        txtRegister.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 5);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                intent.putExtra("url","https://www.highgateair.com.au/customer/account/create/");
                startActivity(intent);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar_Login);
    }

    private void setFormula(){

        FontManager.setFontType(findViewById(R.id.layout_Login), gothamFont);

        TextView txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtEmail.setTypeface(iconFont);
        txtEmail.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 6);
        txtEmail.setPadding(unitFontSize * 3, unitFontSize * 2, unitFontSize * 3, unitFontSize * 2);

        TextView txtPass = (TextView) findViewById(R.id.txtPass);
        txtPass.setTypeface(iconFont);
        txtPass.setTextSize(TypedValue.COMPLEX_UNIT_PX, unitFontSize * 6);
        txtPass.setPadding(unitFontSize * 3, unitFontSize * 2, unitFontSize * 3, unitFontSize * 2);
    }

    private void isLogin(){

        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please insert mail and password...", Toast.LENGTH_SHORT).show();
            return;
        }

        btnLogin1.setEnabled(false);
        btnLogin2.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        String apiUrl = "http://www.highgateair.com.au/multisearch/index/update?api=true";
        Ion.with(getApplicationContext())
                .load(apiUrl)
                .setTimeout(3000)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (result == null || result.equals("")){
                            Toast.makeText(LoginActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                            btnLogin1.setEnabled(true);
                            btnLogin2.setEnabled(true);
                            progressBar.setVisibility(View.INVISIBLE);
                            return;
                        }

                        getRequestToken();
                    }
                });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void getRequestToken(){

        // To override error of execution of network thread on the main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            service = new ServiceBuilder()
                    .provider(MagentoThreeLeggedOAuth.class)
                    .apiKey(CONSUMER_KEY)
                    .apiSecret(CONSUMER_SECRET)
                    .callback(CALLBACK_URL)
                    .debug()
                    .build();

            requestToken = service.getRequestToken();
            String authorizationUrl = service.getAuthorizationUrl(requestToken);

            Ion.with(getApplicationContext())
                    .load(authorizationUrl)
                    .setTimeout(3000)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if (result == null || result.equals("")){
                                Toast.makeText(LoginActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                                btnLogin1.setEnabled(true);
                                btnLogin2.setEnabled(true);
                                progressBar.setVisibility(View.INVISIBLE);
                                return;
                            }
                            // do stuff with the result or error
                            form_key = getForm_key(result);
                            if(form_key == null)
                                getVerifier();
                            else
                                postCredential();
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }
    }


    private String getForm_key(String Url)
    {
        int i = Url.indexOf("form_key");
        if (i == -1) return null;
        return Url.substring(i + 17, i + 33);
    }

    public static final class MagentoThreeLeggedOAuth extends DefaultApi10a {
        private static final String BASE_URL = "http://www.highgateair.com.au/";

        @Override
        public String getRequestTokenEndpoint() {
            return BASE_URL + "oauth/initiate";
        }

        @Override
        public String getAccessTokenEndpoint() {
            return BASE_URL + "oauth/token";
        }

        @Override
        public String getAuthorizationUrl(Token requestToken) {
            return BASE_URL + "oauth/authorize?oauth_token="
                    + requestToken.getToken(); //this implementation is for admin roles only...
        }

    }

    private void postCredential(){

        Ion.with(getApplicationContext())
                .load("POST", "https://www.highgateair.com.au/customer/account/loginPost/")
                .setBodyParameter("form_key", form_key)
                .setBodyParameter("login[username]", username)
                .setBodyParameter("login[password]", password)
                .setBodyParameter("oauth_token", requestToken.getToken())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (result.indexOf("form_key") == -1)
                             getVerifier();
                        else {
                            Toast.makeText(getApplicationContext(), "Wrong mail or password...", Toast.LENGTH_SHORT).show();
                            btnLogin1.setEnabled(true);
                            btnLogin2.setEnabled(true);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void getVerifier(){

        Ion.with(getApplicationContext())
                .load("http://www.highgateair.com.au/oauth/authorize/confirm?oauth_token=" + requestToken.getToken())
                .setTimeout(3000)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {

                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result == null || result.equals("")){
                            Toast.makeText(LoginActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String verifierCode = result.getAsJsonObject("args").get("oauth_verifier").getAsString();
                        getAccessToken(verifierCode);
                    }
                });
    }

    private void getAccessToken(String verifierCode){

        Verifier verifier = new Verifier(verifierCode);
        accessToken = service.getAccessToken(requestToken, verifier);

        final OAuthRequest request = new OAuthRequest(Verb.GET,  "http://www.highgateair.com.au/api/rest/customers?filter[0][attribute]=email&filter[0][in][0]=" + username);
        service.signRequest(accessToken, request);

        Map<String, String> parameters = request.getOauthParameters();

        oauth_header = "OAuth oauth_consumer_key=\"" + CONSUMER_KEY
                + "\",oauth_token=\"" + accessToken.getToken()
                + "\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"" + parameters.get("oauth_timestamp")
                + "\",oauth_nonce=\"" + parameters.get("oauth_nonce")
                + "\",oauth_version=\"1.0\",oauth_signature=\"" + parameters.get("oauth_signature") +"\"";

        Ion.with(getApplicationContext())
                .load("GET", "http://www.highgateair.com.au/api/rest/customers?filter[0][attribute]=email&filter[0][in][0]=" + username)
                .setTimeout(3000)
                .addHeader("Authorization", oauth_header)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result == null) {
                            Toast.makeText(getApplicationContext(), "Wrong mail or password...", Toast.LENGTH_SHORT).show();
                            btnLogin1.setEnabled(true);
                            btnLogin2.setEnabled(true);
                            progressBar.setVisibility(View.INVISIBLE);
                            return;
                        }
                        String id = result.toString();
                        id = id.split("\"")[1];

                        myUser.setId(Integer.valueOf(id));
                        myUser.setEmailAddress(result.get(id).getAsJsonObject().get("email").getAsString());
                        myUser.setFirstName(result.get(id).getAsJsonObject().get("firstname").getAsString());
                        myUser.setLastName(result.get(id).getAsJsonObject().get("lastname").getAsString());

                        SharedPreferences sharedPref = getSharedPreferences(SHARE_FILE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(SHARE_ID, myUser.getId());
                        editor.putString(SHARE_FIRSTNAME, myUser.getFirstName());
                        editor.putString(SHARE_LASTNAME, myUser.getLastName());
                        editor.putString(SHARE_EMAIL, myUser.getEmailAddress());
                        editor.commit();

                        getExtraInfo();
                    }
                });

    }

    private void getExtraInfo(){

        Ion.with(getApplicationContext())
                .load("GET", "http://www.highgateair.com.au/productcategory/index/customer?customer_id=" + String.valueOf(myUser.getId()))
                .setTimeout(3000)
                .addHeader("Authorization", oauth_header)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result == null || result.equals("")){
                            Toast.makeText(LoginActivity.this, "Network error!!!\nAfter check your network, please retry again.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        myUser.setDebtor_id(Integer.valueOf(result.get("debtor_id").getAsString()));
                        myUser.setGroup_id(Integer.valueOf(result.get("group_id").getAsString()));

                        SharedPreferences sharedPref = getSharedPreferences(SHARE_FILE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(SHARE_DEBTORID, myUser.getDebtor_id());
                        editor.putInt(SHARE_GROUPID, myUser.getGroup_id());
                        editor.commit();

                        btnLogin1.setEnabled(true);
                        btnLogin2.setEnabled(true);
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

}
