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
import com.mbl.zaikavendor.adapter.RecyclerViewAdapter2;
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
 * Created by Anu on 1/15/2020.
 */

public class Partyorder extends Fragment {
    ArrayList<Actors>  actorsList2, actorsList1, actorsList4;

    MyPreferenceManager session2;
    SharedPreferences pref;
    String userid="",userid2="";
    LinearLayout listView;
    Button acceptbtn,deletebtn,updatebtn;

    int i=0;

    TextView productlist,orderdate,restaurentname,address,price,orderstatus,myorderid;

    WebView webview;

    RecyclerView recyclerView;
    RecyclerViewAdapter2 recyclerViewAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();
    ArrayList<Actors> actorsList=new ArrayList<Actors>();

    boolean isLoading = false;
    boolean canloadmore=true;
    TextView pendingorder,allorder,delieveredorder,cancelledorder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_todayorder, container, false);


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
        editor.putInt(EndPoints.ordertype2, 0); // Storing name
        editor.commit();


        pendingorder = (TextView) v.findViewById(R.id.pendings) ;
        delieveredorder = (TextView) v.findViewById(R.id.delivered) ;
        allorder = (TextView) v.findViewById(R.id.all) ;
        cancelledorder = (TextView) v.findViewById(R.id.cancelled) ;

        pendingorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterorderlist(1);
                pendingorder.setTextColor(Color.parseColor("#dd1100"));
                delieveredorder.setTextColor(Color.parseColor("#000000"));
                allorder.setTextColor(Color.parseColor("#000000"));
                cancelledorder.setTextColor(Color.parseColor("#000000"));
            }
        });
        delieveredorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterorderlist(2);
                pendingorder.setTextColor(Color.parseColor("#000000"));
                delieveredorder.setTextColor(Color.parseColor("#dd1100"));
                allorder.setTextColor(Color.parseColor("#000000"));
                cancelledorder.setTextColor(Color.parseColor("#000000"));
            }
        });
        allorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterorderlist(3);
                pendingorder.setTextColor(Color.parseColor("#000000"));
                delieveredorder.setTextColor(Color.parseColor("#000000"));
                allorder.setTextColor(Color.parseColor("#dd1100"));
                cancelledorder.setTextColor(Color.parseColor("#000000"));
            }
        });
        cancelledorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterorderlist(4);
                pendingorder.setTextColor(Color.parseColor("#000000"));
                delieveredorder.setTextColor(Color.parseColor("#000000"));
                allorder.setTextColor(Color.parseColor("#000000"));
                cancelledorder.setTextColor(Color.parseColor("#dd1100"));
            }
        });

        recyclerView = v.findViewById(R.id.recyclerView);
        filterorderlist(3);
        initAdapter();



        // new JSONAsyncTask().execute(EndPoints.MYORDERLIST + "?category_id=" + userid+"&ordertype=7"+"&userid2="+userid2);


        showBackButton();

        return v;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbtn);
        }
    }


    public void filterorderlist(int whichtype){
        actorsList.clear();
        new JSONAsyncTask().execute(EndPoints.MYORDERLIST + "?category_id=" + userid+"&whichtype="+whichtype+"&ordertype=7"+"&userid2="+userid2+"&loadfrom=0"+"&pagename=party");
    }



    private void initAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter2(actorsList,getActivity().getSupportFragmentManager(),this,"party");
        recyclerView.setAdapter(recyclerViewAdapter);
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
                        actor.setHeight((object.getString("pid")).trim());  //orderid
                        actor.setName((object.getString("name")).trim());  //subloc
                        actor.setCountry((object.getString("details")).trim()); //date
                        actor.setSpouse((object.getString("price")).trim()); //total
                        actor.setString1((object.getString("sfrom")).trim()); //status
                        actor.setImage((object.getString("image_url")).trim());  //address
                        actor.setString3((object.getString("servesize")).trim());  //food for 2

                        actor.setString2((object.getString("sto")).trim());  //address
                        actor.setString4((object.getString("mobileno")).trim());  //address
                        actor.setString5((object.getString("extracharge")).trim());  //address

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
           // dialog.cancel();
            recyclerViewAdapter.notifyDataSetChanged();

            /*if(actorsList.size()>0) {
               try {
                   createview(actorsList);
               } catch (Exception e){
                   e.printStackTrace();
               }
            } */


        }
    }


    public void acceptbtn(final String passedstring,final int myposition){
        AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
                .setTitle("Are You Sure?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do something...


                                login("accepted",passedstring,0,myposition);
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
    public void deletebtn(final String passedstring,final int myposition){


        AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
                .setTitle("Are You Sure to Delete?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do something...


                                login("Cancelled",passedstring,0,myposition);
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
    public void updatebtn(final String passedstring){
        AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
                .setTitle("Are You Preparing Order now?")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do something...
                                login("Cooking",passedstring,1,0);
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

        // showRadioButtonDialog(v.getTag().toString());
    }


    private void login(String ostatus,String orderno,final int toexitornot,final int mythisposition ) {

        StringRequest strReq = new StringRequest(Request.Method.GET,
                EndPoints.MYORDERLISTUPDATE+"?username="+ostatus+"&password="+orderno+"&utype=vendor"+"&userid2="+userid2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("sasa", "response: " + response);

                try {

                    JSONObject CallBackResult = new JSONObject(response.trim());

                    JSONObject StatusRequest  = CallBackResult.getJSONObject("Result");
                    int Status = Integer.parseInt(StatusRequest.getString("Status"));

                    if(Status==1) {
                        // start main activity
                        if(toexitornot==1) {
                            // startActivity(new Intent(getActivity(), Welcome.class));
                            filterorderlist(3);
                        } else{
                            int mypos = mythisposition;
                            Fragment newFragment = new Orderdetails();
                            Bundle bundle = new Bundle();
                            bundle.putString("menuid", actorsList.get(mypos).getChildren());
                            bundle.putString("pid", actorsList.get(mypos).getHeight());
                            bundle.putString("pname", actorsList.get(mypos).getName());
                            bundle.putString("details", actorsList.get(mypos).getCountry());
                            bundle.putString("price", actorsList.get(mypos).getSpouse());
                            bundle.putString("sfrom", "accepted");
                            bundle.putString("sto", actorsList.get(mypos).getString2());
                            bundle.putString("servesize", actorsList.get(mypos).getString3());
                            bundle.putString("address", actorsList.get(mypos).getImage());
                            bundle.putString("mobileno", actorsList.get(mypos).getString4());
                            bundle.putString("extracharge", actorsList.get(mypos).getString5());

                            //Toast.makeText(getActivity(),actorsList.get(position).getSpouse() , Toast.LENGTH_LONG).show();
                            newFragment.setArguments(bundle);
                            FragmentManager fm= getActivity().getSupportFragmentManager();
                            fm
                                    .beginTransaction()
                                    .replace(R.id.container_body, newFragment)
                                    .addToBackStack(null)
                                    .addToBackStack("tiffindetails")
                                    .commit();
                        }
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
