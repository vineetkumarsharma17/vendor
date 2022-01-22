package com.mbl.zaikavendor.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mbl.zaikavendor.Login;
import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.Welcome;
import com.mbl.zaikavendor.adapter.Myofferadapter;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.app.MyApplication;
import com.mbl.zaikavendor.helper.MyPreferenceManager;

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

/**
 * Created by Anu on 3/14/2020.
 */

public class Myoffers extends Fragment {
    ArrayList<Actors> actorsList2, actorsList1, actorsList4;

    MyPreferenceManager session2;
    SharedPreferences pref;
    String userid="",userid2="";
    LinearLayout listView;

    Button acceptbtn,deletebtn,updatebtn;
    WebView webview;
    int i=0;


    RecyclerView recyclerView;
    Myofferadapter recyclerViewAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();
    ArrayList<Actors> actorsList=new ArrayList<Actors>();

    boolean isLoading = false;
    boolean canloadmore=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_myoffers, container, false);

        //  listView=(LinearLayout)v.findViewById(R.id.list);
        session2 =new MyPreferenceManager(getActivity());

        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname, "");
            userid2 = pref.getString(EndPoints.prefmainid,"");
        } else{
            session2.logoutUser();
            startActivity(new Intent(getActivity(), Login.class));
            getActivity().finish();
        }
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(EndPoints.ordertype1, 0); // Storing name
        editor.commit();



        recyclerView = v.findViewById(R.id.recyclerView);
        populateData();
        initAdapter();
        initScrollListener();

        showBackButton();

        return v;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbtn);
        }
    }


    private void populateData() {
        int i = 0;
        new JSONAsyncTask().execute(EndPoints.MYOFFERSLIST + "?category_id=" + userid+"&ordertype=0"+"&userid2="+userid2+"&loadfrom=0");

    }

    private void initAdapter() {
        Fragment newFragment = new Orderdetail();
        recyclerViewAdapter = new Myofferadapter(actorsList,getFragmentManager(),this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == actorsList.size() - 1) {
                        //bottom of list!
                        if(canloadmore) {
                            loadMore();
                            isLoading = true;
                        }

                    }
                }
            }
        });


    }

    private void loadMore() {


    }

    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog = new ProgressDialog(getActivity());
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
                    JSONObject basobj = jsono.getJSONObject("model");


                    JSONArray jarray = basobj.getJSONArray("mainmenu");
                    for (int i = 0; i < jarray.length(); i++) {

                        JSONObject object = jarray.getJSONObject(i);
                        System.out.println("catgoryobject" + object.toString());

                        Actors actor = new Actors();

                        actor.setChildren((object.getString("menuid")).trim()); //id
                        actor.setName((object.getString("name")).trim());  //subloc
                        actor.setCountry((object.getString("details")).trim()); //date
                        actor.setSpouse((object.getString("acceptedornot")).trim()); //total
                        actor.setImage((object.getString("myimg")).trim());  //subloc
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
            //dialog.cancel();
            recyclerViewAdapter.notifyDataSetChanged();
           /* if(actorsList.size()>0) {
                try {
                    createview(actorsList);
                } catch (Exception e){
                    e.printStackTrace();
                }
            } */

        }
    }


    public void acceptbtn(final String passedstring){
        AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
                .setTitle("Are You Sure?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do something...


                                login("accepted",String.valueOf(passedstring));
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );
        AlertDialog alert = b.create();
        //Setting the title manually
        //alert.setTitle("AlertDialogExample");
        alert.show();
    }
    public void deletebtn(final String passedstring){


        AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
                .setTitle("Are You Sure to Delete?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do something...
                                login("Cancelled",String.valueOf(passedstring));
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );
        AlertDialog alert = b.create();
        //Setting the title manually
        //alert.setTitle("AlertDialogExample");
        alert.show();


    }



    private void login(String ostatus,String orderno) {

        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.MYOFFERSLIST+"?username="+ostatus+"&password="+orderno+"&utype=vendor"+"&userid2="+userid2+"&myuserid="+userid, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("sasa", "response: " + response);

                try {

                    JSONObject CallBackResult = new JSONObject(response.trim());

                    JSONObject StatusRequest  = CallBackResult.getJSONObject("Result");
                    int Status = Integer.parseInt(StatusRequest.getString("Status"));

                    if(Status==1) {
                        // start main activity
                        startActivity(new Intent(getActivity(), Welcome.class));
                        // check for error flag
                    } else if(Status==2){
                        Toast.makeText(getActivity(), "No updated" , Toast.LENGTH_LONG).show();

                    } else{
                        Toast.makeText(getActivity(), "Wrong " , Toast.LENGTH_LONG).show();

                    }


                } catch (JSONException e) {
                    Log.e("error", "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                //  Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Please Check Internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        //Adding request to request queue
        //RequestQueue queue = Volley.newRequestQueue(this);
        MyApplication.getInstance().addToRequestQueue(strReq,"req_login");
        // queue.add(strReq);
    }




}
