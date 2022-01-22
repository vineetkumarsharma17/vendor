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

public class Verification extends AppCompatActivity {
    private String TAG = Login.class.getSimpleName();

    EditText ed1,ed2;
    TextView tv1,tv2;
    Button btn1;

    SharedPreferences pref ;
    TextView register,forgot;
    String userid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);



        pref= getSharedPreferences(EndPoints.sharedpref, 0);
        if(pref.getString(EndPoints.prefverifyuser,"").length()>0){
            if(pref.getString(EndPoints.prefverifyuser,"").equals("1")) {
                startActivity(new Intent(getApplicationContext(), Welcome.class));
                finish();
            } else{
                userid=pref.getString(EndPoints.prefverifyuser,"");
            }
        }


        ed1 = (EditText) findViewById(R.id.username);

        btn1= (Button) findViewById(R.id.bt_login);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isValidPhone(ed1.getText().toString())){
                    login();
                    //  Toast.makeText(getApplicationContext(),"Phone number is valid",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Code number is not valid",Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    public boolean isValidPhone(CharSequence phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            if(phone.length()!=4){
                return false;
            } else {
                return android.util.Patterns.PHONE.matcher(phone).matches();
            }
        }
    }

    private void login() {

        final String name = ed1.getText().toString().replaceAll(" ","%20");

        if (name.isEmpty() ) {
            //tv.setText("Please Enter All Field");
            Toast.makeText(getApplicationContext(), "Please Enter All Field" , Toast.LENGTH_LONG).show();
        }
        else {
            btn1.setText("Please Wait...");
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    EndPoints.VERIFICATION+"?username="+name+"&utype=client&userid="+userid, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "response: " + response);
                    btn1.setText("Submit");

                    try {

                        JSONObject CallBackResult = new JSONObject(response.trim());

                        JSONObject StatusRequest  = CallBackResult.getJSONObject("Result");
                        int Status = Integer.parseInt(StatusRequest.getString("Status"));

                        if(Status==1) {
                            JSONObject UserRequest = StatusRequest.getJSONObject("User");
                            String User_name = UserRequest.getString("User_name");
                            String User_email = UserRequest.getString("User_email");//accountid
                            String Role_Id = UserRequest.getString("mobile");
                            String Role_email = UserRequest.getString("email");
                            String Mainvid = UserRequest.getString("mainvid");



                            SharedPreferences.Editor editor = pref.edit();

                            editor.putString(EndPoints.prefuname, User_email); // Storing name
                            editor.putString(EndPoints.prefname, User_name); // Storing accountid
                            editor.putString(EndPoints.prefmob, Role_Id); // Storing Mobile
                            editor.putString(EndPoints.prefemail, Role_email); // Storing Email
                            editor.putString(EndPoints.prefmainid, Mainvid); // Storing string
                            editor.commit();
                            // Toast.makeText(getApplicationContext(), User_name+Role_Id+Role_email , Toast.LENGTH_LONG).show();

                            MyApplication.getInstance().getPrefManager().setLogin(true);


                            // start main activity
                            startActivity(new Intent(getApplicationContext(), Welcome.class));
                            finish();


                            // check for error flag

                        } else if(Status==2){
                            Toast.makeText(getApplicationContext(), "Wrong  Code" , Toast.LENGTH_LONG).show();

                        } else{
                            Toast.makeText(getApplicationContext(), "Invalid Code Entered" , Toast.LENGTH_LONG).show();

                        }


                    } catch (JSONException e) {
                        Log.e(TAG, "json parsing error: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "E"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    btn1.setText("Sign In");
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "Please Check Internet connection", Toast.LENGTH_SHORT).show();
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
