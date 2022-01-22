package com.mbl.zaikavendor.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import com.mbl.zaikavendor.Login;
import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.adapter.Myincomeadapter;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.helper.MyPreferenceManager;

/**
 * Created by Anu on 5/31/2020.
 */

public class Incomedetails extends Fragment {
    ArrayList<Actors> actorsList2, actorsList1, actorsList4;

    MyPreferenceManager session2;
    SharedPreferences pref;
    String userid="",userid2="";
    LinearLayout listView;

    int i=0;

    TextView productlist,orderdate,restaurentname,address,price,orderstatus,myorderid,fulltotal;

    WebView webview;

    RecyclerView recyclerView;
    Myincomeadapter recyclerViewAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();
    ArrayList<Actors> actorsList=new ArrayList<Actors>();

    boolean isLoading = false;
    boolean canloadmore=true;

    TextView pendingorder,allorder,delieveredorder;
    String mytotalincome="0";
    TextView incomeval;

    String whichtype2="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_myincomedetails, container, false);



        //listView=(LinearLayout)v.findViewById(R.id.list);
        session2 =new MyPreferenceManager(getActivity());

        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname, "");
            userid2 = pref.getString(EndPoints.prefmainid,"");
        } else{
            session2.logoutUser();
            startActivity(new Intent(getActivity(), Login.class));

        }

        incomeval=(TextView)v.findViewById(R.id.incomeval);
        TextView myincometype = (TextView)v.findViewById(R.id.myincometype);
       Bundle extras = this.getArguments();
        if (extras != null) {
            whichtype2 = extras.getString("earnvamount");
            myincometype.setText(whichtype2);

            String earnvalue = extras.getString("earnvalue");
            incomeval.setText(earnvalue);
        }


        pendingorder = (TextView) v.findViewById(R.id.today) ;
        delieveredorder = (TextView) v.findViewById(R.id.yesterday) ;
        allorder = (TextView) v.findViewById(R.id.lastweek) ;
        fulltotal = (TextView)v.findViewById(R.id.totalin) ;

        pendingorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterorderlist(1);
                pendingorder.setTextColor(Color.parseColor("#dd1100"));
                delieveredorder.setTextColor(Color.parseColor("#000000"));
                allorder.setTextColor(Color.parseColor("#000000"));
                // cancelrder.setBackgroundColor(Color.parseColor("#cccccc"));
                fulltotal.setTextColor(Color.parseColor("#000000"));
            }
        });
        delieveredorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterorderlist(2);
                pendingorder.setTextColor(Color.parseColor("#000000"));
                delieveredorder.setTextColor(Color.parseColor("#dd1100"));
                allorder.setTextColor(Color.parseColor("#000000"));
                // cancelrder.setBackgroundColor(Color.parseColor("#cccccc"));
                fulltotal.setTextColor(Color.parseColor("#000000"));
            }
        });
        allorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterorderlist(3);
                pendingorder.setTextColor(Color.parseColor("#000000"));
                delieveredorder.setTextColor(Color.parseColor("#000000"));
                allorder.setTextColor(Color.parseColor("#dd1100"));
                // cancelrder.setBackgroundColor(Color.parseColor("#cccccc"));
                fulltotal.setTextColor(Color.parseColor("#000000"));
            }
        });

        fulltotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterorderlist(4);
                pendingorder.setTextColor(Color.parseColor("#000000"));
                delieveredorder.setTextColor(Color.parseColor("#000000"));
                allorder.setTextColor(Color.parseColor("#000000"));
                //cancelrder.setBackgroundColor(Color.parseColor("#cccccc"));
                fulltotal.setTextColor(Color.parseColor("#dd1100"));
            }
        });


        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerViewAdapter = new Myincomeadapter(actorsList,getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(recyclerViewAdapter);


        new JSONAsyncTask().execute(EndPoints.MYINCOME + "?category_id=" + userid+"&whichtype2="+ URLEncoder.encode(whichtype2)+"&whichtype=1"+"&ordertype=3"+"&userid2="+userid2+"&loadfrom=0");


        showBackButton();

        return v;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        actorsList.clear();
        Log.e("Frontales","resume");
    }
    public void filterorderlist(int whichtype){
        actorsList.clear();
        new JSONAsyncTask().execute(EndPoints.MYINCOME + "?category_id=" + userid+"&whichtype2="+ URLEncoder.encode(whichtype2)+"&whichtype="+whichtype+"&ordertype=3"+"&userid2="+userid2+"&loadfrom=0");
    }


    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // dialog = new ProgressDialog(getActivity());
//            adapter.clear();
           /* dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);  */
            actorsList.clear();
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
                    System.out.println("catgorylist" + jsono.toString());

                    JSONArray jarray = jsono.getJSONArray("mainmenu");
                    for (int i = 0; i < jarray.length(); i++) {

                        JSONObject object = jarray.getJSONObject(i);
                        System.out.println("catgoryobject" + object.toString());

                        Actors actor = new Actors();

                        actor.setChildren((object.getString("menuid")).trim()); //id
                        actor.setHeight((object.getString("orderdate")).trim());  //orderid
                        actor.setName((object.getString("clientname")).trim());  //subloc

                        actor.setCountry((object.getString("orderid")).trim()); //date
                        actor.setSpouse((object.getString("orderamount")).trim()); //total

                        actor.setString1((object.getString("myamount1")).trim()); //status
                        actor.setImage((object.getString("myamount2")).trim());  //address
                        actor.setString3((object.getString("myamount3")).trim());  //food for 2


                        actorsList.add(actor);
                    }

                    mytotalincome = jsono.getString("totalorder");

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
            //dialog.cancel();
            incomeval.setText(mytotalincome);
            recyclerViewAdapter = new Myincomeadapter(actorsList,getActivity().getSupportFragmentManager());
            recyclerView.setAdapter(recyclerViewAdapter);



        }
    }

}
