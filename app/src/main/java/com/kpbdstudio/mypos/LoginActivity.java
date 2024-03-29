package com.kpbdstudio.mypos;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kpbdstudio.mypos.entities.LoginObject;
import com.kpbdstudio.mypos.network.GsonRequest;
import com.kpbdstudio.mypos.network.VolleyRequest;
import com.kpbdstudio.mypos.network.VolleySingleton;
import com.kpbdstudio.mypos.util.CustomApplication;
import com.kpbdstudio.mypos.util.Helper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private TextView errorDisplay;
    private TextView signInformation;
    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        if(null != actionBar){
            actionBar.hide();
        }

        isUserLoggedIn();

        errorDisplay = (TextView)findViewById(R.id.login_error);
        signInformation = (TextView)findViewById(R.id.sign_in_notice);
        signInformation.setText(Helper.NEW_ACCOUNT);

        emailInput = (EditText)findViewById(R.id.email);
        passwordInput = (EditText)findViewById(R.id.password);

        Button signUpButton = (Button)findViewById(R.id.sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(signInIntent);
            }
        });

        Button loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String enteredEmail = emailInput.getText().toString().trim();
                String enteredPassword = passwordInput.getText().toString().trim();

                if(TextUtils.isEmpty(enteredEmail) || TextUtils.isEmpty(enteredPassword)){
                    Helper.displayErrorMessage(LoginActivity.this, getString(R.string.fill_all_fields));
                }

                if(!Helper.isValidEmail(enteredEmail)){
                    Helper.displayErrorMessage(LoginActivity.this, getString(R.string.invalid_email));
                }

                //make server call for user authentication
                //authenticateUserInRemoteServer(enteredEmail, enteredPassword);
                userLogin();
            }
        });
    }

    //private void authenticateUserInRemoteServer(String email, String password){
    private void authenticateUserInRemoteServer(String username, String password){
        Map<String, String> params = new HashMap<String,String>();
        //params.put(Helper.EMAIL, email);
        params.put(Helper.USERNAME, username);
        params.put(Helper.PASSWORD, password);

        GsonRequest<LoginObject> serverRequest = new GsonRequest<LoginObject>(
                Request.Method.POST,
                Helper.PATH_TO_SERVER_LOGIN,
                LoginObject.class,
                null,
                createRequestSuccessListener(),
                createRequestErrorListener());

        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(serverRequest);
    }

    private Response.Listener<LoginObject> createRequestSuccessListener() {
        return new Response.Listener<LoginObject>() {
            @Override
            public void onResponse(LoginObject response) {
                try {
                    Log.d(TAG, "Json Response " + response.getLoggedIn());
                    //if(response.getLoggedIn().equals("1")){
                        //save login data to a shared preference
                        String userData = ((CustomApplication)getApplication()).getGsonObject().toJson(response);
                        ((CustomApplication)getApplication()).getShared().setUserData(userData);

                        // navigate to restaurant home
                        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(loginIntent);
                    //}else{
                    //    Toast.makeText(LoginActivity.this, R.string.failed_login, Toast.LENGTH_LONG).show();
                    //}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
    }

    private void isUserLoggedIn(){
        Gson mGson = ((CustomApplication)getApplication()).getGsonObject();
        String storedUser = ((CustomApplication)getApplication()).getShared().getUserData();
        LoginObject userObject = mGson.fromJson(storedUser, LoginObject.class);
        if(userObject != null){
            Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intentMain);
        }
    }


    //nsr


    private void userLogin(){

        VolleyRequest vr = new VolleyRequest(LoginActivity.this);

        String enteredEmail = emailInput.getText().toString().trim();
        String enteredPassword = passwordInput.getText().toString().trim();

        try{
            JSONObject js = new JSONObject();
            js.accumulate("username", enteredEmail);
            js.accumulate("password",enteredPassword);
            vr.VolleyPost(js.toString(),Helper.PATH_TO_SERVER_LOGIN);

        }catch (Exception e){

        }


        vr.setListener(new VolleyRequest.MyServerListener(){


            @Override
            public void onResponse(JSONObject response) {

                //Toast.makeText(LoginActivity.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONObject obj = new JSONObject(response.toString());
                    //if(!obj.getBoolean("error")){
                    //if(obj.getString("loggedIn").equals("1")){
                        /*SharedPrefManager.getInstance(getApplicationContext())
                                .userLogin(
                                        obj.getInt("id"),
                                        obj.getString("username"),
                                        obj.getString("email")
                                );
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        finish();*/
                    String userData = ((CustomApplication)getApplication()).getGsonObject().toJson(response);
                    ((CustomApplication)getApplication()).getShared().setUserData(userData);

                    // navigate to restaurant home
                    Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(loginIntent);

                    //}else{
                    //   Toast.makeText(
                    //           getApplicationContext(),
                    //           obj.getString("message"),
                    //           Toast.LENGTH_LONG
                    //   ).show();
                    //}
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String error) {
                Toast.makeText(LoginActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}
