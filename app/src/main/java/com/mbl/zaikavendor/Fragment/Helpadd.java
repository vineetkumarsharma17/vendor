package com.mbl.zaikavendor.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import com.mbl.zaikavendor.Login;
import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.app.MyApplication;
import com.mbl.zaikavendor.helper.MyPreferenceManager;

/**
 * Created by Anu on 5/4/2020.
 */

public class Helpadd extends Fragment {
    String orderid;
    ArrayList<String> actorsList,actorsList2;

    MyPreferenceManager session2;
    SharedPreferences pref;
    String userid="",mobileno="",fullname="",userid2="";

    ListView listView;

    LinearLayout formbox,questionbox;
    EditText ed0,ed1,ed2,ed3;
    Button btn1;

    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_helpadd, container, false);

        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname, "");
            userid2 = pref.getString(EndPoints.prefmainid, "");
            fullname = pref.getString(EndPoints.prefname, "");
            mobileno = pref.getString(EndPoints.prefmob, "");
        } else{
            session2.logoutUser();
            startActivity(new Intent(getActivity(), Login.class));
            // getActivity().finish();
        }



        Bundle b = getArguments();
        if(b!=null) {
            orderid = b.getString("orderid");
        }

        actorsList = new ArrayList<String>();

        questionbox=(LinearLayout) v.findViewById(R.id.questionbox);
        formbox=(LinearLayout) v.findViewById(R.id.formbox);

        ed0 = (EditText)v.findViewById(R.id.ed0);ed0.setText(orderid);
        ed1 = (EditText)v.findViewById(R.id.ed1);ed1.setText(fullname);
        ed2 = (EditText)v.findViewById(R.id.ed2);ed2.setText(mobileno);
        ed3 = (EditText)v.findViewById(R.id.ed3);
        btn1 =(Button)v.findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ed3.getText().toString()!=null){
                    login();
                }
            }
        });

        listView = (ListView)v.findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, actorsList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String value=adapter.getItem(position);
               // Toast.makeText(getActivity(),value,Toast.LENGTH_SHORT).show();
                ed3.setText(value);
                questionbox.setVisibility(View.GONE);
                formbox.setVisibility(View.VISIBLE);

            }
        });

        new JSONAsyncTask().execute(EndPoints.HELPCENTERFETCH+"?utype=vendor"+"&username=" + userid+"&userid=" + userid2);


        return v;
    }


    private void login() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.HELPCENTER + "?username=" + userid+"&utype=vendor"+"&userid=" + userid2, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.e("Orderplace", "response: " + response);

                try {
                    if (response.equals("success")) {

                        FragmentManager fm= getActivity().getSupportFragmentManager();
                        Fragment newFragment = new helpcenter();
                        Bundle bundle=new Bundle();
                        newFragment.setArguments(bundle);
                        fm
                                .beginTransaction()
                                .replace(R.id.container_body, newFragment)
                                .addToBackStack(null)
                                .addToBackStack("helpadd")
                                .commit();

                        // check for error flag
                    } else {
                        //  Toast.makeText(getActivity(), "Wrong" , Toast.LENGTH_LONG).show();

                    }


                } catch (Exception e) {
                    Log.e("Order Placed", "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e("Error", "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                //  Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Please Check Internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                String question =ed3.getText().toString();


                params.put("username", userid);
                params.put("fullname", fullname);
                params.put("usermob", mobileno);
                params.put("usertype", "client");

                params.put("orderid", orderid);
                params.put("question", question);


                Log.e("Paramas", "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        //RequestQueue queue = Volley.newRequestQueue(this);
        MyApplication.getInstance().addToRequestQueue(strReq, "req_login");
        // queue.add(strReq);
    }


    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
//            adapter.clear();
            dialog.setMessage("Loading, please wait");
            // dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
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
                    System.out.print(jsono.toString());
                    try {
                        JSONArray jarray = jsono.getJSONArray("mainmenu");
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject object = jarray.getJSONObject(i);

                            actorsList.add((object.getString("name")).trim());

                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }



                    return true;
                }

                //------------------>>

            } catch (android.net.ParseException e1) {
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
            adapter.notifyDataSetChanged();
        }
    }


}
