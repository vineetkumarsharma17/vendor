package com.mbl.zaikavendor.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.helper.MyPreferenceManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by sachin on 1/13/2019.
 */

public class Main extends Fragment {


    String isuserok="0";

    CardView card1, card2, card3, card4,card5,card6,card7,card8,card9;
    Fragment newFragment;
    Bundle bundle;
    FragmentManager fm;

    public String title="Zaika";

    String totalorder="",neworders="",welcomename="",walletamount="",ispaycenter="0";
    TextView tv1,tv2,tv3,tv4;

    String mytoken="";
    SharedPreferences pref;
    String userid="",userid2="";
    Boolean isuserloggedin=false;
    MyPreferenceManager session2;
    String appname="Hans Zaika";


    Switch simpleSwitch ;
    String tocheckornot="0";

    TextView text1,text2,text3,text4;
    int noti_count1=0,noti_count2=0,noti_count3=0,noti_count4=0;




    LinearLayout horlinerarview;

    int imgarr[] ={R.drawable.myicon4,
            R.drawable.myicon2,
            R.drawable.myicon1};
    String amountid[] ={"0","0","0"};
    String earningtype[] ={"Today Order","Total Delivered","Total Earning"};

    String myfullname="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        newFragment = new About();
        fm = getActivity().getSupportFragmentManager();
        bundle = new Bundle();
        bundle.putString("logid", "1");

        tv1=(TextView)v.findViewById(R.id.cmpanyname);
        tv2=(TextView)v.findViewById(R.id.todayorder);
        tv3=(TextView)v.findViewById(R.id.totalorder);
        tv4=(TextView)v.findViewById(R.id.walletamount);

        text1 =(TextView)v.findViewById(R.id.text1);
        text2 =(TextView)v.findViewById(R.id.text2);
        text3 =(TextView)v.findViewById(R.id.text3);
        text4 =(TextView)v.findViewById(R.id.text4);


        horlinerarview = (LinearLayout)v.findViewById(R.id.horlinerarview);

        session2 = new MyPreferenceManager(getActivity().getApplicationContext());

        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            isuserloggedin=true;
            userid = pref.getString(EndPoints.prefuname,"");
            userid2 = pref.getString(EndPoints.prefmainid,"");
            tv1.setText( pref.getString(EndPoints.prefname,""));

            noti_count1=(pref.getInt(EndPoints.ordertype1,0));
            noti_count2=(pref.getInt(EndPoints.ordertype2,0));
            noti_count3=(pref.getInt(EndPoints.ordertype3,0));
            noti_count4=(pref.getInt(EndPoints.ordertype4,0));

        } else{
            isuserloggedin=false;
            session2.logoutUser();
            getActivity().finish();
        }

        if(noti_count1==0){text1.setVisibility(View.GONE); }else{text1.setText(String.valueOf(noti_count1));}
        if(noti_count2==0){text3.setVisibility(View.GONE); }else{text3.setText(String.valueOf(noti_count2));}
        if(noti_count3==0){text2.setVisibility(View.GONE); }else{text2.setText(String.valueOf(noti_count3));}
        if(noti_count4==0){text4.setVisibility(View.GONE); }else{text4.setText(String.valueOf(noti_count4));}

        card1 = (CardView) v.findViewById(R.id.card1);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFragment = new PendingOrders();
                newFragment.setArguments(bundle);
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack(title)
                        .commit();
            }
        });

        card2 = (CardView) v.findViewById(R.id.card2);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFragment = new TotalOrders();
                newFragment.setArguments(bundle);
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack(title)
                        .commit();
            }
        });

        card3 = (CardView) v.findViewById(R.id.card3);
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFragment = new Category();
                newFragment.setArguments(bundle);
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack(title)
                        .commit();
            }
        });

        card4 = (CardView) v.findViewById(R.id.card4);
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFragment = new Producttype();
                newFragment.setArguments(bundle);
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack(title)
                        .commit();
            }
        });

        card5 = (CardView) v.findViewById(R.id.card21);
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFragment = new Todayorder();
                newFragment.setArguments(bundle);
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack(title)
                        .commit();
            }
        });

        card6 = (CardView) v.findViewById(R.id.card41);
        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFragment = new Profilelist();
                newFragment.setArguments(bundle);
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack(title)
                        .commit();
            }
        });



        card7 = (CardView) v.findViewById(R.id.card3a);
        card7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFragment = new Tiffinorder();
                newFragment.setArguments(bundle);
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack(title)
                        .commit();
            }
        });

        card8 = (CardView) v.findViewById(R.id.card4a);
        card8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFragment = new Partyorder();
                newFragment.setArguments(bundle);
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack(title)
                        .commit();
            }
        });

        card9 = (CardView) v.findViewById(R.id.card41a);
        card9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFragment = new Medicatedorder();
                newFragment.setArguments(bundle);
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack(title)
                        .commit();
            }
        });



        simpleSwitch = (Switch) v.findViewById(R.id.simpleSwitch);
        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(simpleSwitch.isChecked()){

                    new JSONAsyncTask2().execute(EndPoints.UPDATEDUTY+"?userid="+userid2+"&onoroff=1");
                } else{
                    new JSONAsyncTask2().execute(EndPoints.UPDATEDUTY+"?userid="+userid2+"&onoroff=0");
                }
            }
        });


        if((noti_count1+noti_count2+noti_count3+noti_count4)>0){

            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.hmerderppup);
            Button dialogButton = (Button) dialog.findViewById(R.id.buttonid);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        new JSONAsyncTask().execute(EndPoints.WELCOME+"?category_id="+userid+"&userid2="+userid2);




        return v;
    }


    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Loading data");
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
                 /*   JSONArray jarray = jsono.getJSONArray("model");
                    for (int i = 0; i < jarray.length(); i++) {
                        if(i<9) {
                            JSONObject object = jarray.getJSONObject(i);

                            Actors actor = new Actors();

                            actor.setName((object.getString("myid")).trim());
                            actor.setDescription((object.getString("fullname")).trim());
                            if (TextUtils.isEmpty(object.getString("photo")) || object.getString("photo") == "" || object.getString("photo") == null || object.getString("photo") == "null") {
                                actor.setImage("http://www.mekasu.com/images/default_img.png");
                            } else {
                                actor.setImage(EndPoints.BASE_URL2 + "/" + object.getString("photo"));
                            }
                            actorsList.add(actor);
                        }
                    } */
                    try {
                        isuserok = jsono.getString("isuserok");

                    } catch (Exception e) {
                    }


                    try{

                        amountid[0]=jsono.getString("amountid1");
                        amountid[1]=jsono.getString("amountid2");
                        amountid[2]=jsono.getString("amountid3");
                      //  amountid[3]=jsono.getString("amountid4");


                    } catch (Exception e){}



                    totalorder = jsono.getString("totalorder");
                    neworders = jsono.getString("todayorder");
                    walletamount = jsono.getString("walletamount");
                    ispaycenter =  jsono.getString("ispaycenter");
                    tocheckornot =jsono.getString("isopen");
                    myfullname= jsono.getString("myfullnane");


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

            if(isuserok.equals("0") && isuserloggedin){
                isuserloggedin=false;
                session2.logoutUser();
               // startActivity(new Intent(getActivity(), Login.class));
                getActivity().finish();
            }

            tv1.setText(myfullname);
            tv2.setText(neworders);
            tv3.setText(totalorder);
            tv4.setText(walletamount);


            if(tocheckornot.equals("1")){
                simpleSwitch.setChecked(true);
            }

            SharedPreferences.Editor editor = pref.edit();
            editor.putString(EndPoints.ispaycenter, ispaycenter); // Storing name
            editor.commit();


            createview();


        }

    }

    public void createview(){
        horlinerarview.removeAllViews();
        for(int i=0;i<amountid.length;i++){
            //Toast.makeText(getActivity(), "Please Enter All Field" , Toast.LENGTH_LONG).show();

            LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            CardView card = new CardView(getActivity());
            card.setLayoutParams(params);
            // Set CardView corner radius
            card.setRadius(9);
            card.setTag(i);
            // Set cardView content padding
            card.setContentPadding(0, 0, 0, 0);
            // Set a background color for CardView
            card.setCardBackgroundColor(Color.parseColor("#FFC6D6C3"));

            LayoutInflater layoutInflater = (LayoutInflater)
                    getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vvvv = layoutInflater.inflate(R.layout.home_income_card, null);

            TextView amountidtype = (TextView) vvvv.findViewById(R.id.amount);
            amountidtype.setText(amountid[i]);

            TextView earningname = (TextView) vvvv.findViewById(R.id.earningtype);
            earningname.setText(earningtype[i]);


            ImageView cardimg = (ImageView) vvvv.findViewById(R.id.cardimg);
            cardimg.setImageResource(imgarr[i]);

            CardView ttopcard=(CardView) vvvv.findViewById(R.id.mycardid);
            ttopcard.setTag(i);
            ttopcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mypos=Integer.valueOf(v.getTag().toString());
                    newFragment = new Incomedetails();
                    bundle =new Bundle();
                    bundle.putString("earnvalue",amountid[mypos]);
                    bundle.putString("earnvamount", earningtype[mypos]);
                    bundle.putString("pname", String.valueOf(mypos));
                    newFragment.setArguments(bundle);
                    fm
                            .beginTransaction()
                            .replace(R.id.container_body, newFragment)
                            .addToBackStack(null)
                            .addToBackStack(title)
                            .commit();



                }
            });

            card.addView(vvvv);

            horlinerarview.addView(card);
        }



    }





    class JSONAsyncTask2 extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
                HttpPost httppost = new HttpPost(urls[0]);
                ArrayList<NameValuePair> postParameters  = new ArrayList<NameValuePair>();
                postParameters = new ArrayList<NameValuePair>();
                //postParameters.add(new BasicNameValuePair("mobilenoss", mobmno));


                httppost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));

                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                System.out.println("catgorylist" + response.toString());
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                   // JSONObject jsono = new JSONObject(data);

                    return true;
                }

                //------------------>>

            } catch (android.net.ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

        }

    }


}
