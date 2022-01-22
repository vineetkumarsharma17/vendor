package com.mbl.zaikavendor.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.adapter.ProductAdapter;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.app.MyApplication;

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

/**
 * Created by Anu on 6/23/2020.
 */

public class Grocerylist extends Fragment {

    TextView tv1;
    SharedPreferences pref;
    String userid="",userid2="";
    ArrayList<Actors> actorsList;
    ListView listView;
    ProductAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_products, container, false);


        listView=(ListView)v.findViewById(R.id.list) ;
        actorsList=new ArrayList<Actors>();
        adapter = new ProductAdapter(v.getContext(), R.layout.product_list_item, actorsList,this);
        listView.setAdapter(adapter);


        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname,"");
            userid2 = pref.getString(EndPoints.prefmainid,"");
        }

        tv1=(TextView)v.findViewById(R.id.tvadd);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment;
                Bundle bundle;
                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new Groceryadd();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("addproducts")
                        .commit();
            }
        });


        new JSONAsyncTask().execute(EndPoints.PRODUCTLIST+"?category_id="+userid +"&prodtype=5"+"&userid2="+userid2 );

        showBackButton();

        return v;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbtn);
        }
    }

    public void deleteit(final int position){
        new AlertDialog.Builder(getActivity())
                .setTitle("Title")
                .setMessage("Do you really want to Delete?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteitp(actorsList.get(position).getHeight());
                        //Toast.makeText(getActivity(), "Yes", Toast.LENGTH_SHORT).show();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }
    public void enabledisbale(final int position){
        enabledisbaleitnow(actorsList.get(position).getHeight(),actorsList.get(position).getString6());
    }
    public void editit(int position){
        Fragment newFragment;
        Bundle bundle=new Bundle();
        FragmentManager fm= getActivity().getSupportFragmentManager();
        newFragment = new Groceryedit();
        bundle.putString("prodcode",actorsList.get(position).getHeight());
        newFragment.setArguments(bundle);
        fm
                .beginTransaction()
                .replace(R.id.container_body, newFragment)
                .addToBackStack(null)
                .addToBackStack("editproducts")
                .commit();
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

                        actor.setName((object.getString("entity_id")).trim());//product
                        actor.setDescription((object.getString("name")).trim());//vendr
                        actor.setDob(object.getString("sku"));//price
                        actor.setCountry(object.getString("price"));//size
                        actor.setHeight(object.getString("regular_price_with_tax"));//code
                        actor.setSpouse(object.getString("final_price_with_tax"));//details

                        actor.setString1(object.getString("vegnonveg"));//details
                        actor.setString2(object.getString("size"));//details
                        actor.setString3(object.getString("webview"));//details
                        actor.setString5("Basic");//details
                        actor.setString6(object.getString("enabledisable"));//details

                        actor.setImage(EndPoints.BASE_URL2+object.getString("image_url"));

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
                Toast.makeText(getActivity(), "No Product", Toast.LENGTH_LONG).show();

        }
    }


    private void deleteitp(final String delteid) {


        // tv1.setText("Please wait...");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.PRODUCTDELETE + "?userid=" + userid+"&updatetype=5"+"&userid2="+userid2 , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    actorsList.clear();
                    new JSONAsyncTask().execute(EndPoints.PRODUCTLIST+"?category_id="+userid +"&prodtype=1"+"&userid2="+userid2 );
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
    public void enabledisbaleitnow(final String delteid,final  String nowval){

        // tv1.setText("Please wait...");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.PRODUCTENABLEDISABLE + "?userid=" + userid+"&updatetype=5"+"&userid2="+userid2 , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    actorsList.clear();
                    new JSONAsyncTask().execute(EndPoints.PRODUCTLIST+"?category_id="+userid +"&prodtype=1"+"&userid2="+userid2 );
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
                params.put("var2", nowval);

                return params;
            }
        };
        //Adding request to request queue
        //RequestQueue queue = Volley.newRequestQueue(this);
        MyApplication.getInstance().addToRequestQueue(strReq, "req_login");
        // queue.add(strReq);
    }

}
