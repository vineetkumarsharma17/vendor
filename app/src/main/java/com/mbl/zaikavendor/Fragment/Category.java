package com.mbl.zaikavendor.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.adapter.CategoryAdapter;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.app.MyApplication;

/**
 * Created by sachin on 1/13/2019.
 */

public class Category extends Fragment {

    TextView tv1;
    EditText ed1,ed2,ed3;
    Button btn;
    String str1,str2,str3;
    SharedPreferences pref;
    String userid="",userid2="";
    ArrayList<Actors> actorsList;
    ListView listView;
    CategoryAdapter adapter;
    int addoredit=1;
    int edititemid=0;
    String medeleteid="";

    View popupInputDialogView;
    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category, container, false);


        listView=(ListView)v.findViewById(R.id.list) ;
        actorsList=new ArrayList<Actors>();
        adapter = new CategoryAdapter(v.getContext(), R.layout.category_list_item, actorsList,this);
        listView.setAdapter(adapter);

        tv1=(TextView)v.findViewById(R.id.tv1);

        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname,"");
            userid2 = pref.getString(EndPoints.prefmainid,"");
        }

        ed1=(EditText)v.findViewById(R.id.ed1);

        btn= (Button) v.findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str1 =ed1.getText().toString();
                if(str1.length()<3){
                    tv1.setText("Please Enter Category Name");
                } else {
                    login(str1);
                }

            }
        });



        new JSONAsyncTask().execute(EndPoints.CATEGORYLIST+"?category_id="+userid+"&userid2="+userid2);

        showBackButton();

        return v;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbtn);
        }
    }

    public void onEvent(int data,String btn1) {
        //doSomething(data);
        if(btn1.equals("Edit")){

           /* ed1.setText(actorsList.get(data).getDescription());
            edititemid=Integer.parseInt(actorsList.get(data).getName());
            btn.setText("Update"); */
        } else{




        }
     //   Toast.makeText(getActivity(), "Clicked "+btn+" "+data, Toast.LENGTH_LONG).show();
    }

    public void edititem(int data){
        edititemid = Integer.parseInt(actorsList.get(data).getName());
        addoredit=1;
        showSortPopup(actorsList.get(data).getDescription());
    }
    public void deleteit(int data){
        medeleteid=actorsList.get(data).getChildren();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure to delete?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    deleteit(medeleteid);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
/*
    DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    addoredit=0;
                    edititemid=0;
                    btn.setText("Add New");
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    addoredit=1;
                    btn.setText("Update");
                    break;
            }
        }
    }; */


    private void showSortPopup(String oldcatgname) {
        // Create a AlertDialog Builder.

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Set title, icon, can not cancel properties.
        // alertDialogBuilder.setTitle("Schedule Callback");
        // alertDialogBuilder.setCancelable(false);

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());


        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.categoryedit, null);

        final EditText editText = (EditText) popupInputDialogView.findViewById(R.id.catgname); //person name
        editText.setText(oldcatgname);


        Button btn6=(Button)popupInputDialogView.findViewById(R.id.btn1na);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str1 = editText.getText().toString();  //address


                if(str1.length()<=3){
                    Toast.makeText(getActivity(), "Enter Valid Category Name" , Toast.LENGTH_LONG).show();
                } else {
                    login(str1);
                    alertDialog.dismiss();
                }


            }
        });



        // Set the inflated layout view object to the AlertDialog builder.
        alertDialogBuilder.setView(popupInputDialogView);
        //ViewGroup parent = (ViewGroup)popupInputDialogView.getParent();
//        parent.setPadding(0, 0, 0, 0);

        // Create AlertDialog and show.
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
                    JSONArray jarray = jsono.getJSONArray("model");
                    System.out.println("catgoryjson" + jarray.toString());

                    for (int i = 0; i < jarray.length(); i++) {

                        JSONObject object = jarray.getJSONObject(i);
                        System.out.println("catgoryobject" + object.toString());

                        Actors actor = new Actors();

                        actor.setName((object.getString("entity_id")).trim());
                        actor.setChildren((object.getString("entity_id")).trim());
                        actor.setDescription((object.getString("name")).trim());

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
            adapter.notifyDataSetChanged();
            //  adapter2.notifyDataSetChanged();
            if (result == false)
                Toast.makeText(getActivity(), "No category", Toast.LENGTH_LONG).show();

        }
    }

    private void login(final String str1) {
      //  str1=ed1.getText().toString();

        tv1.setText("Please Wait...");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.CATEGORY+"?utype="+userid+"&updatetype=1"+"&userid2="+userid2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    btn.setText("Add New");
                    tv1.setText("");
                    ed1.setText("");
                    addoredit=0;
                    edititemid=0;

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
                params.put("var2", String.valueOf(addoredit));
                params.put("var3", String.valueOf(edititemid));
                return params;
            }
        };
        //Adding request to request queue
        //RequestQueue queue = Volley.newRequestQueue(this);
        MyApplication.getInstance().addToRequestQueue(strReq,"req_login");
        // queue.add(strReq);
    }


    private void deleteit(final String delteid) {


        // tv1.setText("Please wait...");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.CATEGORY + "?userid=" + userid+"&updatetype=2"+"&userid2="+userid2 , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    actorsList.clear();
                    new JSONAsyncTask().execute(EndPoints.CATEGORYLIST + "?category_id=" + userid+"&userid2="+userid2);
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
                params.put("var1", delteid);


                return params;
            }
        };
        //Adding request to request queue
        //RequestQueue queue = Volley.newRequestQueue(this);
        MyApplication.getInstance().addToRequestQueue(strReq, "req_login");
        // queue.add(strReq);
    }
}
