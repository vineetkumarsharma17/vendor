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

public class Forgot extends Activity {
    private String TAG = Login.class.getSimpleName();

    EditText ed1,ed2;
    TextView tv1,tv2;
    Button btn1;
    TextView register,forgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        ed1 = (EditText) findViewById(R.id.username);

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
                Intent i = new Intent(Forgot.this, Login.class);
                startActivity(i);
            }
        });


    }




    private void login() {

        final String name = ed1.getText().toString();
        if (name.isEmpty()  ) {
            //tv.setText("Please Enter All Field");

            Toast.makeText(getApplicationContext(), "Please Enter Mobile No" , Toast.LENGTH_LONG).show();
        }
        else {
            btn1.setText("Please Wait...");
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    EndPoints.FORGOT+"?username="+name, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "response: " + response);
                    btn1.setText("Submit");

                    try {

                        JSONObject CallBackResult = new JSONObject(response.trim());

                        JSONObject StatusRequest  = CallBackResult.getJSONObject("Result");
                        int Status = Integer.parseInt(StatusRequest.getString("Status"));

                        if(Status==1) {

                            // start main activity
                            String msg = (StatusRequest.getString("msg"));
                            Toast.makeText(getApplicationContext(), msg , Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();
                            // check for error flag
                        }  else{
                            Toast.makeText(getApplicationContext(), "No User Account Found" , Toast.LENGTH_LONG).show();

                        }


                    } catch (JSONException e) {
                        Log.e(TAG, "json parsing error: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    btn1.setText("Submit");
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    Toast.makeText(getApplicationContext(), "Please check internet connection" , Toast.LENGTH_LONG).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", name);
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
