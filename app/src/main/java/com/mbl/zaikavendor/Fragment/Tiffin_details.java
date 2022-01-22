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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
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
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.app.MyApplication;
import com.mbl.zaikavendor.helper.MyPreferenceManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Anu on 1/25/2020.
 */

public class Tiffin_details extends Fragment {
    ArrayList<Actors> actorsList, actorsList2, actorsList1, actorsList4;

    MyPreferenceManager session2;
    SharedPreferences pref;
    String userid="",userid2="";
    LinearLayout listView;

    WebView mywebWebView;
    String cancelreason="";

    TextView personanme, mobilebike, callbackscheduled, addedondate,lastupdatedate,orddertype;
    String myInt = "", orderstatus="",orderfinalamount="",pname = "", mobile = "", bike = "", nextschedule = "", isfav = "", addedon = "", isclosed = "0",lastupdatedatetxt="",myordertype="";
    String myorderid,myorderdate,myorderstatus,myresname,myresaddress,myproductlist,myusername,myusraddeess,myorderamount,mydelivery, mygst,mymypacking,mytotal,mypayvia,mydeliveryby,myuserview,reviewnumber;

    String extracharges="0#0#0#0";
    EditText edt;
    TextView helpcenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tiffindetails, container, false);


        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname, "");
            userid2 = pref.getString(EndPoints.prefmainid,"");
        }

        actorsList=new ArrayList<Actors>();
        actorsList2=new ArrayList<Actors>();

        listView=(LinearLayout)v.findViewById(R.id.list);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            myInt = bundle.getString("pid");
            pname = bundle.getString("pname"); //pnam,e
            mobile =  bundle.getString("pid"); //order id
            addedon = bundle.getString("details"); //date
            nextschedule = bundle.getString("price");
            orderstatus=bundle.getString("sfrom");
            lastupdatedatetxt= bundle.getString("address");
            isclosed = bundle.getString("sto"); //status
            orderfinalamount=bundle.getString("servesize");
            extracharges=bundle.getString("extracharge");
        }


        helpcenter = (TextView) v.findViewById(R.id.helpcenter);
        helpcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment newFragment = new Helpadd();
                Bundle bundle = new Bundle();
                bundle.putString("orderid", myInt);
                newFragment.setArguments(bundle);
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("helpadd")
                        .commit();
            }
        });
        personanme = (TextView) v.findViewById(R.id.personanme);
        personanme.setText(pname);
        mobilebike = (TextView) v.findViewById(R.id.mobilebike);
        mobilebike.setText("#"+mobile);
        callbackscheduled = (TextView) v.findViewById(R.id.callbackscheduled);
        callbackscheduled.setText(nextschedule);
        addedondate = (TextView) v.findViewById(R.id.addedondate);
        addedondate.setText(addedon);
        lastupdatedate=(TextView)v.findViewById(R.id.lastupdatedate);
        lastupdatedate.setText(lastupdatedatetxt);


        String productlisttext="<table style='width:100%'>";
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

        mywebWebView = (WebView) v.findViewById(R.id.web1);
        mywebWebView.loadData(productlisttext, "text/html", "utf-8");




        String myextracharges[]=extracharges.split("#");
        if(myextracharges[1]==null){myextracharges[1]="0";}
        if(myextracharges[2]==null){myextracharges[2]="0";}
        if(myextracharges[3]==null){myextracharges[3]="0";}

        TextView tv1_charge1=v.findViewById(R.id.deliverycharge);tv1_charge1.setText(myextracharges[0]);
        TextView tv1_charge2=v.findViewById(R.id.gstcharge);tv1_charge2.setText(myextracharges[1]);
        TextView tv1_charge3=v.findViewById(R.id.packingcharge);tv1_charge3.setText(myextracharges[2]);
        TextView tv1_charge4=v.findViewById(R.id.processing);tv1_charge4.setText(myextracharges[3]);

        if(orderstatus.equals("accepted")) {
            new JSONAsyncTask().execute(EndPoints.TIFFINDETAILS + "?category_id=" + myInt + "&ordertype=7" + "&userid2=" + userid2);
        }
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
                    //JSONObject basobj = jsono.getJSONObject("model");


                    try {
                        actorsList.clear();
                        JSONArray jarray = jsono.getJSONArray("mainmenu");
                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject object = jarray.getJSONObject(i);
                            System.out.println("catgoryobject" + object.toString());

                            Actors actor = new Actors();

                            actor.setChildren((object.getString("entity_id")).trim()); //id
                            actor.setHeight((object.getString("name")).trim());  //orderid
                            actor.setName((object.getString("cdate")).trim());  //subloc

                            actorsList.add(actor);
                        }
                    }catch (Exception e){}

                    try {
                        actorsList2.clear();
                        JSONArray jarray = jsono.getJSONArray("mainmenu2");
                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject object = jarray.getJSONObject(i);
                            System.out.println("catgoryobject" + object.toString());

                            Actors actor = new Actors();

                            actor.setChildren((object.getString("entity_id")).trim()); //id
                            actor.setHeight((object.getString("name")).trim());  //orderid
                            actor.setName((object.getString("cdate")).trim());  //subloc
                            actor.setDob((object.getString("ispreparing")).trim());  //subloc

                            actor.setCountry((object.getString("stage")).trim());  //subloc
                            actor.setSpouse((object.getString("review")).trim());  //subloc

                            actorsList2.add(actor);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
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
            if(actorsList.size()>0 || actorsList2.size()>0) {
                try {
                   // createview(actorsList);
                    createview2(actorsList2);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }


        }
    }

    public void createview(ArrayList<Actors> actorsList1){
        listView.removeAllViews();
        TextView textView=new TextView(getActivity());
        textView.setTextSize(16);
        textView.setPadding(5,0,0,5);
        textView.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/Roboto-Medium.ttf"));

        if(actorsList1.size()<1){
            textView.setText("No Days");
            listView.addView(textView);
        } else{


            for(int i=0;i<actorsList1.size();i++){
                //Toast.makeText(getActivity(), "Please Enter All Field" , Toast.LENGTH_LONG).show();

                LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                CardView card = new CardView(getActivity());
                card.setLayoutParams(params);
                // Set CardView corner radius
                card.setRadius(9);
                card.setTag(actorsList1.get(i).getHeight());
                // Set cardView content padding
                card.setContentPadding(0, 0, 0, 0);
                // Set a background color for CardView
                card.setCardBackgroundColor(Color.parseColor("#FFC6D6C3"));

                LayoutInflater layoutInflater = (LayoutInflater)
                        getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View vvvv = layoutInflater.inflate(R.layout.tiffin_delivery_row, null);

                TextView restaurentname = (TextView) vvvv.findViewById(R.id.datename);
                restaurentname.setText(actorsList1.get(i).getName());

                TextView address = (TextView) vvvv.findViewById(R.id.updatestatus);
                String upstatus=actorsList1.get(i).getHeight();
                if(upstatus.equals("accepted")){
                    upstatus="Preparing";
                }
                address.setText(upstatus);
                if(actorsList1.get(i).getChildren().equals("0")){

                    Button acceptbtn = (Button) vvvv.findViewById(R.id.cookingbtn);
                    acceptbtn.setTag(actorsList1.get(i).getName());
                    acceptbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {

                            AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
                                    .setTitle("Are You Preparing Tiffin now?")
                                    .setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    // do something...
                                                    login("accepted",v.getTag().toString(),0);
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

                    Button updatebtn = (Button) vvvv.findViewById(R.id.updatebtn);
                    updatebtn.setTag(actorsList1.get(i).getName());
                    updatebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {

                            showRadioButtonDialog(v.getTag().toString());

                        }
                    });

                } else{
                    LinearLayout hiddenlinear = (LinearLayout)vvvv.findViewById(R.id.hiddenlinear);
                    hiddenlinear.setVisibility(View.GONE);
                }




                card.addView(vvvv);

                listView.addView(card);
            }



        }


    }

    public void createview2(ArrayList<Actors> actorsList1){
        listView.removeAllViews();

        TextView textView=new TextView(getActivity());
        textView.setTextSize(16);
        textView.setPadding(5,0,0,5);
        textView.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/Roboto-Medium.ttf"));

        if(actorsList1.size()<1){
            textView.setText("No Days");
            listView.addView(textView);
        } else{


            for(int i=0;i<actorsList1.size();i++){
                //Toast.makeText(getActivity(), "Please Enter All Field" , Toast.LENGTH_LONG).show();

                LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                CardView card = new CardView(getActivity());
                card.setLayoutParams(params);
                // Set CardView corner radius
                card.setRadius(9);
                card.setTag(actorsList1.get(i).getHeight());
                // Set cardView content padding
                card.setContentPadding(0, 0, 0, 0);
                // Set a background color for CardView
                card.setCardBackgroundColor(Color.parseColor("#FFC6D6C3"));

                LayoutInflater layoutInflater = (LayoutInflater)
                        getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View vvvv = layoutInflater.inflate(R.layout.tiffin_delivery_row, null);

                TextView restaurentname = (TextView) vvvv.findViewById(R.id.datename);
                restaurentname.setText(actorsList1.get(i).getName());

                TextView ordeid = (TextView) vvvv.findViewById(R.id.ordeid);
                ordeid.setText(actorsList1.get(i).getHeight());

                TextView ureview = (TextView) vvvv.findViewById(R.id.review);
               // ureview.setText(actorsList1.get(i).getSpouse());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ureview.setText(Html.fromHtml(actorsList1.get(i).getSpouse(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    ureview.setText(Html.fromHtml(actorsList1.get(i).getSpouse()));
                }

                TextView address = (TextView) vvvv.findViewById(R.id.updatestatus);
                String upstatus=actorsList1.get(i).getCountry();
                if(upstatus.equals("accepted")){
                   // upstatus="Preparing";
                }

               // Log.e("sdfdsfsdf",actorsList1.get(i).getDob().equals("0")+"###"+actorsList1.get(i).getHeight());
                if(actorsList1.get(i).getDob().equals("0")){

                    Button acceptbtn = (Button) vvvv.findViewById(R.id.cookingbtn);
                    acceptbtn.setTag(actorsList1.get(i).getChildren());
                    acceptbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {

                            AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
                                    .setTitle("Are You Preparing Tiffin now?")
                                    .setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    // do something...
                                                    login("Cooking",v.getTag().toString(),0);
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

                    Button updatebtn = (Button) vvvv.findViewById(R.id.updatebtn);
                    updatebtn.setVisibility(View.GONE);
                    updatebtn.setTag(actorsList1.get(i).getChildren());
                    updatebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {

                            showRadioButtonDialog(v.getTag().toString());

                        }
                    });

                } else{
                    LinearLayout hiddenlinear = (LinearLayout)vvvv.findViewById(R.id.hiddenlinear);
                    hiddenlinear.setVisibility(View.GONE);
                    address.setText(upstatus);
                }




                card.addView(vvvv);

                listView.addView(card);
            }



        }


    }

    private void showRadioButtonDialog(final  String upid) {

        // custom dialog

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_dialog);
        List<String> stringList=new ArrayList<>();  // here is list
        stringList.add("User Canceled");
        stringList.add("Vendor Canceled");

        final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        for(int i=0;i<stringList.size();i++){
            RadioButton rb=new RadioButton(getActivity()); // dynamically creating RadioButton and adding to RadioGroup.
            LinearLayout.LayoutParams linearLayoutparams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            rb.setPadding(10,5,0,10);

            rb.setText(stringList.get(i));
            rg.addView(rb);
        }

        edt=(EditText)dialog.findViewById(R.id.mycomment);

        Button btns=(Button)dialog.findViewById(R.id.updatebutton);
        btns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedRadioButtonId = rg.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    // No item selected
                    Toast.makeText(getActivity(), "Select Reason" , Toast.LENGTH_LONG).show();

                } else {
                    int index = rg.indexOfChild(dialog.findViewById(rg.getCheckedRadioButtonId()));
                    RadioButton rb=(RadioButton) rg.findViewById(checkedRadioButtonId);
                    String radioText=rb.getText().toString();
                    cancelreason=edt.getText().toString();
                    login(radioText, upid,1);
                    dialog.hide();
                }
            }
        });

        dialog.show();
    }




    private void login(String ostatus,final String orderno,int donenotdone ) {

        ostatus= URLEncoder.encode(ostatus);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.TIFFINDETAILS+"?username="+ostatus+"&utype=vendor"+"&userid2="+userid2+"&myupdate="+donenotdone+"&orderid="+myInt, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("sasa", "response: " + response);

                try {

                    JSONObject CallBackResult = new JSONObject(response.trim());

                    JSONObject StatusRequest  = CallBackResult.getJSONObject("Result");
                    int Status = Integer.parseInt(StatusRequest.getString("Status"));

                    new JSONAsyncTask().execute(EndPoints.TIFFINDETAILS + "?category_id=" + myInt + "&ordertype=7" + "&userid2=" + userid2);

                    if(Status==1) {
                        // start main activity
                       // startActivity(new Intent(getActivity(), Welcome.class));

                        // check for error flag
                    }  else{
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
                  Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(), "Please Check Internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cancelreason",cancelreason);
                params.put("cdate",orderno);
                return params;
            }
        };

        //Adding request to request queue
        //RequestQueue queue = Volley.newRequestQueue(this);
        MyApplication.getInstance().addToRequestQueue(strReq,"req_login");
        // queue.add(strReq);
    }


}
