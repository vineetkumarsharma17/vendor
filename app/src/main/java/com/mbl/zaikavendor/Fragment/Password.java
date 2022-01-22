package com.mbl.zaikavendor.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.app.MyApplication;

/**
 * Created by sachin on 1/13/2019.
 */

public class Password extends Fragment {

    TextView tv1;
    EditText ed1,ed2,ed3;
    Button btn;
    String str1,str2,str3;
    SharedPreferences pref;
    String userid="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_password, container, false);


        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname,"");
        }

        ed1=(EditText)v.findViewById(R.id.ed1);
        ed2=(EditText)v.findViewById(R.id.ed2);
        ed3=(EditText)v.findViewById(R.id.ed3);

        btn= (Button) v.findViewById(R.id.btn1);

        tv1=(TextView)v.findViewById(R.id.textid);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str1 =ed1.getText().toString();
                str2 =ed2.getText().toString();
                str3 =ed3.getText().toString();
                if(str1.length()<3){
                    tv1.setText("Please Enter Old Password");
                } else if(str2.length()<5){
                    tv1.setText("Please Enter New Password MinLength 5");
                } else if(str3.length()<5){
                    tv1.setText("Please Enter Confirm Password MinLength 5");
                } else  if(str2.equals(str3)){
                    login();
                }
                else {
                    tv1.setText("New and Confirm Password dont match");
                }

            }
        });


        showBackButton();

        return v;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbtn);
        }
    }


    private void login() {

            tv1.setText("Please Wait...");
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    EndPoints.PASSWORD+"?username="+str1+"&password="+str2+"&utype="+userid, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    try {
                        tv1.setText(response);

                        Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();


                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                  //  Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                      Toast.makeText(getActivity().getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                //    Toast.makeText(getActivity().getApplicationContext(), "Please Check Internet connection", Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                     //  params.put("token", token);

                    return params;
                }
            };

            //Adding request to request queue
            //RequestQueue queue = Volley.newRequestQueue(this);
            MyApplication.getInstance().addToRequestQueue(strReq,"req_login");
            // queue.add(strReq);

    }
}
