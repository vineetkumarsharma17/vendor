package com.mbl.zaikavendor.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.app.MyApplication;

/**
 * Created by sachin on 1/13/2019.
 */

public class Profile extends Fragment {
    TextView tv1;
    EditText ed1, ed2, ed3, ed4, ed5, ed6, ed7, ed8,ed9;
    Button btn;
    String str1, str2, str3, str4, str5, str6, str7, str8,str9,str14,str15="";
    Spinner spinner;
    SharedPreferences pref;
    String userid="",userid2="";
    ArrayList<Actors> actorsList;

    Switch simpleSwitch0,simpleSwitch1,simpleSwitch2,simpleSwitch3,simpleSwitch4;
    String str11="0", str12="0", str13="0";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        actorsList = new ArrayList<Actors>();
        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname, "");
            userid2 = pref.getString(EndPoints.prefmainid,"");
        } else {

        }
        //   Toast.makeText(getActivity(), userid, Toast.LENGTH_LONG).show();
        new JSONAsyncTask().execute(EndPoints.PROFILEFETCH + "?category_id=" + userid+"&userid2="+userid2);

        ed1 = (EditText) v.findViewById(R.id.ed1);
        ed2 = (EditText) v.findViewById(R.id.ed2);
        ed3 = (EditText) v.findViewById(R.id.ed3);
        ed4 = (EditText) v.findViewById(R.id.ed4);
        ed5 = (EditText) v.findViewById(R.id.ed5);
        ed7 = (EditText) v.findViewById(R.id.ed7);
        ed8 = (EditText) v.findViewById(R.id.ed8);
        ed9 = (EditText) v.findViewById(R.id.ed9);


        spinner = (Spinner) v.findViewById(R.id.ed6);

        simpleSwitch0=(Switch) v.findViewById(R.id.simpleSwitch0);
        simpleSwitch1=(Switch) v.findViewById(R.id.simpleSwitch);
        simpleSwitch2=(Switch) v.findViewById(R.id.simpleSwitch2);
        simpleSwitch3=(Switch) v.findViewById(R.id.simpleSwitch3);
        simpleSwitch4=(Switch) v.findViewById(R.id.simpleSwitch4);
        simpleSwitch0.setEnabled(false);
        simpleSwitch1.setEnabled(false);
        simpleSwitch2.setEnabled(false);
        simpleSwitch3.setEnabled(false);
        simpleSwitch4.setEnabled(false);


        btn = (Button) v.findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str1 = ed1.getText().toString();
                str2 = ed2.getText().toString();
                str3 = ed3.getText().toString();
                str4 = ed4.getText().toString();
                str5 = ed5.getText().toString();
                str6 = spinner.getSelectedItem().toString();
                str7 = ed7.getText().toString();
                str8 = ed8.getText().toString();

                str9 = ed9.getText().toString();
                if(simpleSwitch1.isChecked()){
                    str11="1";
                }
                if(simpleSwitch2.isChecked()){
                    str12="1";
                }
                if(simpleSwitch3.isChecked()){
                    str13="1";
                }

                if(str1.length()<3){
                    Toast.makeText(getActivity(), "Please Enter Valid Name" , Toast.LENGTH_LONG).show();
                } else if(str2.length()!=10){
                    Toast.makeText(getActivity(), "Please Enter Valid Mobile No" , Toast.LENGTH_LONG).show();
                } else if(!isValidEmail(str3)){
                    Toast.makeText(getActivity(), "Please Enter Valid Email" , Toast.LENGTH_LONG).show();
                } else if(str7.length()!=6){
                    Toast.makeText(getActivity(), "Please Enter Valid Pincode" , Toast.LENGTH_LONG).show();
                } else {
                    login();
                }

                login();
            }
        });
        tv1 = (TextView) v.findViewById(R.id.tv1);

        showBackButton();

        return v;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbtn);
        }
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
//            adapter.clear();
           /* dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);  */
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

                HttpClient httpclient = new DefaultHttpClient();

                SchemeRegistry registry = new SchemeRegistry();
                org.apache.http.conn.ssl.SSLSocketFactory socketFactory = org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory();
                socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
                registry.register(new Scheme("https", socketFactory, 443));
                SingleClientConnManager mgr = new SingleClientConnManager(httpclient.getParams(), registry);
                DefaultHttpClient httpClient = new DefaultHttpClient(mgr, httpclient.getParams());

                // Set verifier
                HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                System.out.println("catgorylist" + response.toString());
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsono = new JSONObject(data);
                    System.out.println("catgoryjson" + jsono.toString());
                    JSONArray jarray = jsono.getJSONArray("model");

                    for (int i = 0; i < jarray.length(); i++) {

                        JSONObject object = jarray.getJSONObject(i);
                        System.out.println("catgoryobject" + object.toString());

                        Actors actor = new Actors();

                        str1 = ((object.getString("entity_id")).trim());
                        str2 = ((object.getString("name")).trim());
                        str3 = (object.getString("sku"));
                        str4 = (object.getString("price"));
                        str5 = (object.getString("regular_price_with_tax"));
                        str7 = (object.getString("final_price_with_tax"));  //stae
                        str6 = (object.getString("rating_summary"));  // pin
                        str8 = (object.getString("rating_summary2"));  // gst

                        str9 = (object.getString("fssai"));  // gst
                        str11 = (object.getString("switch1"));  // gst
                        str12 = (object.getString("switch2"));  // gst
                        str13 = (object.getString("switch3"));  // gst
                        str14 = (object.getString("switch0"));  // gst
                        str15 = (object.getString("switch4"));  // gst

                        actorsList.add(actor);
                    }
                    return true;
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            ed1.setText(str1);
            ed2.setText(str2);
            ed3.setText(str3);
            ed4.setText(str4);
            ed5.setText(str5);
            ed7.setText(str6);
            ed8.setText(str8);

            ed9.setText(str9);
            if(str14.equals("1")){
                simpleSwitch0.setChecked(true);
            }
            if(str11.equals("1")){
                simpleSwitch1.setChecked(true);
            }
            if(str12.equals("1")){
                simpleSwitch2.setChecked(true);
            }
            if(str13.equals("1")){
                simpleSwitch3.setChecked(true);
            }
            if(str15.equals("1")){
                simpleSwitch4.setChecked(true);
            }

            spinner.setSelection(getIndex(spinner, str7));

            // ed6.setSelection(0);
            // adapter.notifyDataSetChanged();
            //  adapter2.notifyDataSetChanged();
            if (result == false)
                Toast.makeText(getActivity(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    private void login() {


        tv1.setText("Please wait...");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.PROFILE + "?userid=" + userid +"&userid2="+userid2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
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
                params.put("var1", str1);
                params.put("var2", str2);
                params.put("var3", str3);
                params.put("var4", str4);
                params.put("var5", str5);
                params.put("var6", str6);
                params.put("var7", str7);
                params.put("var8", str8);

                params.put("var9", str9);
                params.put("var11", str11);
                params.put("var12", str12);
                params.put("var13", str13);



                return params;
            }
        };
        //Adding request to request queue
        //RequestQueue queue = Volley.newRequestQueue(this);
        MyApplication.getInstance().addToRequestQueue(strReq, "req_login");
        // queue.add(strReq);
    }
}
