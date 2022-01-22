package com.mbl.zaikavendor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.app.MyApplication;

public class Login extends Activity {
    private String TAG = Login.class.getSimpleName();

    EditText ed1,ed2;
    TextView tv1,tv2;
    Button btn1,btn2;

    SharedPreferences pref ;
    TextView register,forgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        pref= getSharedPreferences(EndPoints.sharedpref, 0);
        if(pref.getString(EndPoints.prefuname,"").length()>0){
            startActivity(new Intent(getApplicationContext(), Welcome.class));
            finish();
        }


        ed1 = (EditText) findViewById(R.id.username);
        ed2 = (EditText) findViewById(R.id.userpass);

        btn1= (Button) findViewById(R.id.bt_login);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });



        forgot = (TextView)findViewById(R.id.forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Forgot.class);
                startActivity(i);
            }
        });

        btn2= (Button) findViewById(R.id.btn_register);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });



    }


    private void login() {

        final String name = ed1.getText().toString();
        final String email = ed2.getText().toString();

        if (name.isEmpty() || email.isEmpty() ) {
            //tv.setText("Please Enter All Field");
            Toast.makeText(getApplicationContext(), "Please Enter All Field" , Toast.LENGTH_LONG).show();
        }
        else {
            btn1.setText("Please Wait...");
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    EndPoints.LOGIN+"?username="+name+"&password="+email+"&utype=vendor", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "response: " + response);
                    btn1.setText("Sign In");
                    try {

                        JSONObject CallBackResult = new JSONObject(response.trim());

                        JSONObject StatusRequest  = CallBackResult.getJSONObject("Result");
                        int Status = Integer.parseInt(StatusRequest.getString("Status"));

                        if(Status==1) {
                            JSONObject UserRequest = StatusRequest.getJSONObject("User");
                            String User_name = UserRequest.getString("User_name");  //Name
                            String User_email = UserRequest.getString("User_email"); // accountid
                            String Role_Id = UserRequest.getString("mobile");
                            String Role_email = UserRequest.getString("email");
                            String isverified = UserRequest.getString("isverified");
                            String Mainvid = UserRequest.getString("mainvid");


                            if(isverified.equals("1")){
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString(EndPoints.prefverifyuser, User_email); // Storing name
                                editor.commit();
                                startActivity(new Intent(getApplicationContext(), Verification.class));
                                finish();
                            } else {
                                SharedPreferences.Editor editor = pref.edit();

                                editor.putString(EndPoints.prefuname, User_email); // Storing string
                                editor.putString(EndPoints.prefname, User_name); // Storing string
                                editor.putString(EndPoints.prefmob, Role_Id); // Storing string
                                editor.putString(EndPoints.prefemail, Role_email); // Storing string
                                editor.putString(EndPoints.prefmainid, Mainvid); // Storing string
                                editor.commit();

                                MyApplication.getInstance().getPrefManager().setLogin(true);

                                // start main activity
                                startActivity(new Intent(getApplicationContext(), Welcome.class));
                                finish();
                            }

                            // check for error flag

                        } else if(Status==2){
                            Toast.makeText(getApplicationContext(), "Wrong  UserId/Password" , Toast.LENGTH_LONG).show();

                        } else{
                            Toast.makeText(getApplicationContext(), "Wrong  Userid/Password" , Toast.LENGTH_LONG).show();

                        }


                    } catch (JSONException e) {
                        Log.e(TAG, "json parsing error: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    btn1.setText("Sign In");
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
