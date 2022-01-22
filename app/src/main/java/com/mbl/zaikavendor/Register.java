package com.mbl.zaikavendor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.app.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Register extends AppCompatActivity {
    private String TAG = Login.class.getSimpleName();

    EditText ed1,ed2,ed3,ed4,otpedit,refer;
    TextView tv1,tv2;
    Button btn1,otpbtn,referbtn;

    TextView register,forgot;
    LinearLayout regbox,verbox,referbox;

    String myotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        int min = 1000;
        int max = 9999;
        int randomv = new Random().nextInt((max - min) + 1) + min;

        myotp = String.valueOf(randomv);

        regbox=(LinearLayout) findViewById(R.id.regbox);
        verbox=(LinearLayout) findViewById(R.id.verificationbox);
        referbox=(LinearLayout) findViewById(R.id.refererbox);

        ed1 = (EditText) findViewById(R.id.ed1);
        ed2 = (EditText) findViewById(R.id.ed2);
        ed3 = (EditText) findViewById(R.id.ed3);
        ed4 = (EditText) findViewById(R.id.ed4);
        otpedit =(EditText) findViewById(R.id.otp);
        refer =(EditText) findViewById(R.id.otp);

        btn1= (Button) findViewById(R.id.bt_login);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValidPhone(ed2.getText().toString())){
                    login();
                    //  Toast.makeText(getApplicationContext(),"Phone number is valid",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Phone number is not valid",Toast.LENGTH_SHORT).show();
                }
                //
            }
        });

        otpbtn= (Button) findViewById(R.id.otpbtn);
        otpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(myotp.equals(otpedit.getText().toString())){
                    login3();
                    //  Toast.makeText(getApplicationContext(),"Phone number is valid",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Invalid valid",Toast.LENGTH_SHORT).show();
                }
                //
            }
        });
        referbtn= (Button) findViewById(R.id.referbtn);
        referbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login2();
            }
        });



        forgot = (TextView)findViewById(R.id.forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this, Forgot.class);
                startActivity(i);
            }
        });

        register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent i = new Intent(Register.this, Login.class);
                // startActivity(i);
                finish();
            }
        });



    }

    public boolean isValidPhone(CharSequence phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            if(phone.length()!=10){
                return false;
            } else {
                return android.util.Patterns.PHONE.matcher(phone).matches();
            }
        }
    }

    private void login() {

        final String name = ed1.getText().toString();
        final String email = ed2.getText().toString();
        final String pass = ed3.getText().toString();
        final String confirmpass = ed4.getText().toString();

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmpass.isEmpty()) {
            //tv.setText("Please Enter All Field");
            Toast.makeText(getApplicationContext(), "Please Enter All Field" , Toast.LENGTH_LONG).show();
        } else if(!pass.equals(confirmpass)){
            Toast.makeText(getApplicationContext(), "Password Doesn't Match" , Toast.LENGTH_LONG).show();
        }
        else {
            btn1.setText("Please Wait...");
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    EndPoints.SENDOTP, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "response: " + response);
                    btn1.setText("Sign Up");

                    try {

                        JSONObject CallBackResult = new JSONObject(response.trim());

                        JSONObject StatusRequest  = CallBackResult.getJSONObject("Result");
                        int Status = Integer.parseInt(StatusRequest.getString("Status"));

                        if(Status==1) {
                            verbox.setVisibility(View.VISIBLE);
                            regbox.setVisibility(View.GONE);
                            referbox.setVisibility(View.GONE);

                        } else if(Status==2){
                            Toast.makeText(getApplicationContext(), "Already registered" , Toast.LENGTH_LONG).show();

                        } else{
                            String msg = (StatusRequest.getString("msg"));
                            Toast.makeText(getApplicationContext(), msg , Toast.LENGTH_LONG).show();

                        }


                    } catch (JSONException e) {
                        Log.e(TAG, "json parsing error: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    btn1.setText("Sign Up");
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    //  Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Please Check Internet connection", Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", name);
                    params.put("password", email);
                    params.put("newpass", pass);
                    params.put("otpc", myotp);
                    //  params.put("token", token);

                    Log.e(TAG, "params: " + params.toString());
                    return params;
                }
            };

            //Adding request to request queue
            //RequestQueue queue = Volley.newRequestQueue(this);
            MyApplication.getInstance().addToRequestQueue(strReq,"req_login");
            // queue.add(strReq);
        }
    }

    public void login3(){
        verbox.setVisibility(View.GONE);
        regbox.setVisibility(View.GONE);
        referbox.setVisibility(View.VISIBLE);
    }

    private void login2() {

        final String name = ed1.getText().toString();
        final String email = ed2.getText().toString();
        final String pass = ed3.getText().toString();
        final String confirmpass = ed4.getText().toString();
        final String myrefercode = refer.getText().toString();

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmpass.isEmpty()) {
            //tv.setText("Please Enter All Field");
            Toast.makeText(getApplicationContext(), "Please Enter All Field" , Toast.LENGTH_LONG).show();
        } else if(!pass.equals(confirmpass)){
            Toast.makeText(getApplicationContext(), "Password Doesn't Match" , Toast.LENGTH_LONG).show();
        }
        else {
            btn1.setText("Please Wait...");
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    EndPoints.REGISTER, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "response: " + response);
                    btn1.setText("Sign Up");

                    try {

                        JSONObject CallBackResult = new JSONObject(response.trim());

                        JSONObject StatusRequest  = CallBackResult.getJSONObject("Result");
                        int Status = Integer.parseInt(StatusRequest.getString("Status"));

                        if(Status==1) {
                            // start main activity
                            // startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();

                            // check for error flag

                        } else if(Status==2){
                            Toast.makeText(getApplicationContext(), "Already registered" , Toast.LENGTH_LONG).show();

                        } else{
                            String msg = (StatusRequest.getString("msg"));
                            Toast.makeText(getApplicationContext(), msg , Toast.LENGTH_LONG).show();

                        }


                    } catch (JSONException e) {
                        Log.e(TAG, "json parsing error: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    btn1.setText("Sign Up");
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    //  Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Please Check Internet connection", Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", name);
                    params.put("password", email);
                    params.put("newpass", pass);
                    params.put("myotp", myotp);
                    params.put("refercode", myrefercode);
                    //  params.put("token", token);

                    Log.e(TAG, "params: " + params.toString());
                    return params;
                }
            };

            //Adding request to request queue
            //RequestQueue queue = Volley.newRequestQueue(this);
            MyApplication.getInstance().addToRequestQueue(strReq,"req_login");
            // queue.add(strReq);
        }
    }

}
