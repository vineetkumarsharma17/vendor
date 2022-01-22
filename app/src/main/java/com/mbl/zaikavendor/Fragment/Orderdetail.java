package com.mbl.zaikavendor.Fragment;

/**
 * Created by Anu on 1/28/2020.
 */


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mbl.zaikavendor.Login;
import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.Welcome;
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
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Anu on 1/25/2020.
 */

public class Orderdetail extends Fragment {

    MyPreferenceManager session2;
    SharedPreferences pref;
    String userid="",userid2="";

    TextView personanme, mobilebike, callbackscheduled, addedondate,lastupdatedate;

    Button acceptbtn,deletebtn,updatebtn;

    TextView morderstatus,ordertype,deliverymob;
    String myordertype="",mydeliveryno="";
    TextView tvorderid,orderdate,restaurentname,address,price,usercity,useraddress,deliveryby,payvia,totamount,packingcharges,deliverycharge,gst,userview;
    String myInt = "", orderstatus="",orderfinalamount="",pname = "", mobile = "", bike = "", nextschedule = "", isfav = "", addedon = "", isclosed = "0",lastupdatedatetxt="";
    LinearLayout reviewbtnlayout;

    String productlisttext="";
    String extracharges="0#0#0#0";

    String myorderid,myorderdate,myorderstatus,myresname,myresaddress,myproductlist,myusername,myusraddeess,myorderamount,mydelivery, mygst,mymypacking,mytotal,mypayvia,mydeliveryby,myuserview,reviewnumber;

    String baseorderid="";
    TextView helpcenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orderdetail_normal, container, false);

        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname, "");
            userid2 = pref.getString(EndPoints.prefmainid,"");
        } else{
            session2.logoutUser();
            startActivity(new Intent(getActivity(), Login.class));
            getActivity().finish();
        }


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            myInt = bundle.getString("menuid");
            baseorderid = bundle.getString("pid");
            pname = bundle.getString("pname"); //pnam,e
            mobile =  bundle.getString("mobno"); //order id
            addedon = bundle.getString("details"); //date
            nextschedule = bundle.getString("price");
            orderstatus=bundle.getString("sfrom");
            lastupdatedatetxt= bundle.getString("address");
            isclosed = bundle.getString("sto"); //status
            orderfinalamount=bundle.getString("servesize");
            extracharges=bundle.getString("extracharge");

            productlisttext="<table style='width:100%'>";
            String fullsze[]=orderfinalamount.split("&&");
            for(int k=0;k<fullsze.length;k++){
                productlisttext=productlisttext+"<tr style='width:100%'>";
                String tddivide[]=fullsze[k].split("#");
                for(int kk=0;kk<tddivide.length;kk++){
                    String width="20%";
                    if(kk==0){
                        width="58%";
                    }
                    productlisttext=productlisttext+"<td style='width:"+width+"'>"+tddivide[kk]+"</td>";
                }

            }
            productlisttext=productlisttext+"</table>";

        }

        helpcenter = (TextView) v.findViewById(R.id.helpcenter);
        helpcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment newFragment = new Helpadd();
                Bundle bundle = new Bundle();
                bundle.putString("orderid", baseorderid);
                newFragment.setArguments(bundle);
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("helpadd")
                        .commit();
            }
        });

        address = (TextView) v.findViewById(R.id.address);

        usercity = (TextView) v.findViewById(R.id.useraddress);
        useraddress = (TextView) v.findViewById(R.id.userfulladdress);

        deliveryby = (TextView) v.findViewById(R.id.deliveryby);
        payvia = (TextView) v.findViewById(R.id.payvia);


        userview = (TextView) v.findViewById(R.id.userview);
        reviewbtnlayout = (LinearLayout) v.findViewById(R.id.myreviews);
        morderstatus=(TextView)v.findViewById(R.id.morderstatus);
        morderstatus.setText(orderstatus);
        ordertype=(TextView)v.findViewById(R.id.ordertype);
        deliverymob =(TextView)v.findViewById(R.id.deliverymob);


        personanme = (TextView) v.findViewById(R.id.personanme);
        personanme.setText(pname);
        mobilebike = (TextView) v.findViewById(R.id.mobilebike); //order no
        mobilebike.setText("#"+baseorderid);
        callbackscheduled = (TextView) v.findViewById(R.id.callbackscheduled);  //amount
        callbackscheduled.setText(nextschedule);
        addedondate = (TextView) v.findViewById(R.id.addedondate); //orderdate time
        addedondate.setText(addedon);
        lastupdatedate=(TextView)v.findViewById(R.id.lastupdatedate);  //address
        lastupdatedate.setText(lastupdatedatetxt);

        WebView mywebview=(WebView)v.findViewById(R.id.mywebview);
        mywebview.loadData(productlisttext, "text/html", "utf-8");

        acceptbtn = (Button) v.findViewById(R.id.orderstatus);
        acceptbtn.setTag(myInt);

        updatebtn = (Button) v.findViewById(R.id.orderstatusupdate);
        updatebtn.setTag(myInt);

        deletebtn = (Button) v.findViewById(R.id.orderstatus1);
        deletebtn.setTag(myInt);

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
                        .setTitle("Are You Sure?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // do something...
                                        login("accepted",v.getTag().toString(),0);
                                        acceptbtn.setVisibility(View.GONE);
                                        deletebtn.setVisibility(View.GONE);
                                        updatebtn.setVisibility(View.VISIBLE);
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
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
                        .setTitle("Are You Sure to Delete?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // do something...
                                        login("Cancelled",v.getTag().toString(),0);
                                        acceptbtn.setVisibility(View.GONE);
                                        deletebtn.setVisibility(View.GONE);
                                        updatebtn.setVisibility(View.GONE);
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
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
                        .setTitle("Are You Preparing Order now?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // do something...
                                        login("Cooking",v.getTag().toString(),0);
                                        acceptbtn.setVisibility(View.GONE);
                                        deletebtn.setVisibility(View.GONE);
                                        updatebtn.setVisibility(View.GONE);
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
        });

        if(orderstatus.equals("placed") || orderstatus.equals("Received")){
            acceptbtn.setVisibility(View.VISIBLE);
            deletebtn.setVisibility(View.VISIBLE);
            updatebtn.setVisibility(View.GONE);
        } else if( orderstatus.equals("accepted")){
            acceptbtn.setVisibility(View.GONE);
            deletebtn.setVisibility(View.GONE);
            updatebtn.setVisibility(View.VISIBLE);
        } else {
            acceptbtn.setVisibility(View.GONE);
            deletebtn.setVisibility(View.GONE);
            updatebtn.setVisibility(View.GONE);
        }


        String myextracharges[]=extracharges.split("#");
        if(myextracharges[1]==null){myextracharges[1]="0";}
        if(myextracharges[2]==null){myextracharges[2]="0";}
        if(myextracharges[3]==null){myextracharges[3]="0";}

        TextView tv1_charge1=v.findViewById(R.id.deliverycharge);tv1_charge1.setText(myextracharges[0]);
        TextView tv1_charge2=v.findViewById(R.id.gstcharge);tv1_charge2.setText(myextracharges[1]);
        TextView tv1_charge3=v.findViewById(R.id.packingcharge);tv1_charge3.setText(myextracharges[2]);
        TextView tv1_charge4=v.findViewById(R.id.processing);tv1_charge4.setText(myextracharges[3]);

        new JSONAsyncTask().execute(EndPoints.MYORDERLISTDETAIL + "?category_id=" + userid+"&orderid="+baseorderid);

        showBackButton();

        return v;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
                    JSONArray jarray = jsono.getJSONArray("mainmenu");
                    for (int i = 0; i < jarray.length(); i++) {

                        JSONObject object = jarray.getJSONObject(i);
                        System.out.println("catgoryobject" + object.toString());

                        myorderid= (object.getString("menuid")).trim();
                        myorderdate= (object.getString("myorderdate")).trim();
                        myorderstatus= (object.getString("myorderstatus")).trim();
                        myresname= (object.getString("myresname")).trim();
                        myresaddress= (object.getString("myresaddress")).trim();

                        myusername= (object.getString("myusername")).trim();
                        myusraddeess= (object.getString("myusraddeess")).trim();
                        myproductlist = (object.getString("myproductlist")).trim();
                        myorderamount= (object.getString("myorderamount")).trim();
                        mydelivery= (object.getString("mydelivery")).trim();

                        mygst= (object.getString("mygst")).trim();
                        mymypacking= (object.getString("mymypacking")).trim();
                        mytotal= (object.getString("mytotal")).trim();
                        mypayvia= (object.getString("mypayvia")).trim();
                        mydeliveryby= (object.getString("mydeliveryby")).trim();

                        reviewnumber = (object.getString("userreview")).trim();
                        myuserview = (object.getString("usercomment")).trim();
                        myordertype = (object.getString("myordertype")).trim();
                        mydeliveryno = (object.getString("mydeliveryno")).trim();
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

                //tvorderid.setText("#"+myorderid);
               // orderdate.setText(myorderdate);
                 ordertype.setText(myordertype);
                 personanme.setText(myresname);
                 address.setText(myresaddress);

                useraddress.setText(myusername);
                usercity.setText(myusraddeess);
                deliverymob.setText(mydeliveryno);


                payvia.setText(mypayvia);
                deliveryby.setText(mydeliveryby);

                userview.setText(myuserview);
                if(myorderstatus.equals("delivered")){
                    reviewbtnlayout.setVisibility(View.VISIBLE);
                }


        }
    }

    private void login(String ostatus,String orderno,final int toexitornot) {

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
                            startActivity(new Intent(getActivity(), Welcome.class));
                        } else{
                          //  acceptbtn.setVisibility(View.GONE);
                          //  deletebtn.setVisibility(View.GONE);
                            new JSONAsyncTask().execute(EndPoints.MYORDERLISTDETAIL + "?category_id=" + userid+"&orderid="+baseorderid);
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
                Toast.makeText(getActivity(), "P"+error, Toast.LENGTH_SHORT).show();
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

