package com.mbl.zaikavendor.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import com.mbl.zaikavendor.Login;
import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.adapter.Orderadapter;
import com.mbl.zaikavendor.adapter.RecyclerViewAdapter;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.helper.MyPreferenceManager;

/**
 * Created by sachin on 1/13/2019.
 */

public class TotalOrders extends Fragment {

    MyPreferenceManager session2;
    SharedPreferences pref;
    String userid="",userid2="";
    LinearLayout listView;

    int i=0;

    TextView productlist,orderdate,restaurentname,address,price,orderstatus,myorderid;
    WebView webview;

    ListView listView1;
    Orderadapter adapter;

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();
    ArrayList<Actors> actorsList=new ArrayList<Actors>();

    boolean isLoading = false;
    boolean canloadmore=true;

    TextView pendingorder,allorder,delieveredorder,cancelledorder;
    int base_whichtype=3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_totalorder, container, false);




        //listView=(LinearLayout)v.findViewById(R.id.list);
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


        recyclerView = v.findViewById(R.id.recyclerView);
        populateData();
        initAdapter();
        initScrollListener();


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



/*
        listView1=(ListView)v.findViewById(R.id.listview);
        adapter = new Orderadapter(v.getContext(), R.layout.order_item, actorsList);
        listView1.setAdapter(adapter);

        new JSONAsyncTask().execute(EndPoints.MYORDERLIST + "?category_id=" + userid+"&ordertype=1"+"&userid2="+userid2);
*/



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
        base_whichtype = whichtype;
        new JSONAsyncTask().execute(EndPoints.MYORDERLIST + "?category_id=" + userid+"&whichtype="+whichtype+"&ordertype=8"+"&userid2="+userid2+"&loadfrom=0"+"&pagename=total");
    }
    private void populateData() {
        int i = 0;

        new JSONAsyncTask().execute(EndPoints.MYORDERLIST + "?category_id=" + userid+"&whichtype=3"+"&ordertype=1"+"&userid2="+userid2+"&loadfrom=0"+"&pagename=total");
       /* while (i < 10) {
            rowsArrayList.add("Item " + i);
            i++;
        } */
    }

    private void initAdapter() {

        recyclerViewAdapter = new RecyclerViewAdapter(actorsList,getActivity().getSupportFragmentManager());
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
        actorsList.add(null);
        recyclerViewAdapter.notifyItemInserted(actorsList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               try {
                   actorsList.remove(actorsList.size() - 1);
                   int scrollPosition = actorsList.size();
                   recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                   int currentSize = scrollPosition;
                   int nextLimit = currentSize + 10;

               /* while (currentSize - 1 < nextLimit) {
                    rowsArrayList.add("Item " + currentSize);
                    currentSize++;
                } */

                   new JSONAsyncTask().execute(EndPoints.MYORDERLIST + "?category_id=" + userid+"&whichtype="+base_whichtype+"&ordertype=1"+"&userid2="+userid2+"&loadfrom="+currentSize+"&pagename=total");

                   recyclerViewAdapter.notifyDataSetChanged();
                   isLoading = false;
               } catch (Exception e){}
            }
        }, 2000);


    }

    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  dialog = new ProgressDialog(getActivity());
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

                    if(basobj.getJSONArray("mainmenu")==null){
                        canloadmore=false;
                    }

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
                        actor.setString7((object.getString("myordertype")).trim());  //address

                        rowsArrayList.add((object.getString("name")).trim());
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
          //  dialog.cancel();
            recyclerViewAdapter.notifyDataSetChanged();

            //adapter.notifyDataSetChanged();
          /*  if(actorsList.size()>0) {
                try {
                    createview(actorsList);
                } catch (Exception e){
                    e.printStackTrace();
                }
            } */

        }
    }

    public void createview(ArrayList<Actors> actorsList1){

        TextView textView=new TextView(getActivity());
        textView.setTextSize(16);
        textView.setPadding(5,0,0,5);
        textView.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/Roboto-Medium.ttf"));

        if(actorsList1.size()<1){
            textView.setText("No Recent Orders");
            listView.addView(textView);
        } else{


            for(i=0;i<actorsList1.size();i++){
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
                View vvvv = layoutInflater.inflate(R.layout.order_item, null);

                restaurentname = (TextView) vvvv.findViewById(R.id.restaurentname);
                restaurentname.setText(actorsList1.get(i).getName());
                restaurentname.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/Roboto-Medium.ttf"));
                restaurentname.setTag(i);

                LinearLayout ttopcard=(LinearLayout)vvvv.findViewById(R.id.ttopcard);
                ttopcard.setTag(i);
                ttopcard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int mypos=Integer.valueOf(v.getTag().toString());
                        FragmentManager fm= getActivity().getSupportFragmentManager();
                        Fragment newFragment = new Orderdetail();
                        Bundle bundle = new Bundle();
                        bundle.putString("menuid",actorsList.get(mypos).getChildren());
                        bundle.putString("pid", actorsList.get(mypos).getHeight());
                        bundle.putString("pname", actorsList.get(mypos).getName());
                        bundle.putString("details",actorsList.get(mypos).getCountry());
                        bundle.putString("price", actorsList.get(mypos).getSpouse());
                        bundle.putString("sfrom", actorsList.get(mypos).getString1());
                        bundle.putString("sto", actorsList.get(mypos).getString2());
                        bundle.putString("servesize", actorsList.get(mypos).getString3());
                        bundle.putString("address", actorsList.get(mypos).getImage());
                        bundle.putString("mobileno", actorsList.get(mypos).getString4());
                        bundle.putString("extracharge", actorsList.get(mypos).getString5());

                        //Toast.makeText(getActivity(),actorsList.get(position).getSpouse() , Toast.LENGTH_LONG).show();
                        newFragment.setArguments(bundle);
                        fm
                                .beginTransaction()
                                .replace(R.id.container_body, newFragment)
                                .addToBackStack(null)
                                .addToBackStack("tiffindetails")
                                .commit();
                    }
                });
                address = (TextView) vvvv.findViewById(R.id.address);
                address.setText(actorsList1.get(i).getImage());


                price = (TextView) vvvv.findViewById(R.id.price);
                price.setText("Rs. "+actorsList1.get(i).getSpouse());


                myorderid  = (TextView) vvvv.findViewById(R.id.myorderid);
                myorderid.setText("#"+actorsList1.get(i).getHeight());


                // productlist = (TextView) vvvv.findViewById(R.id.productlist);
                String productlisttext="<table style='width:100%'>";
                String fullsze[]=actorsList1.get(i).getString3().split("&&");
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
                   // productlisttext=productlisttext+"</tr><tr><td colspan='3'>&nbsp;<br />&nbsp;<br /></td></tr>";

                }
                productlisttext=productlisttext+"</table>";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                  //  productlist.setText(Html.fromHtml(productlisttext, Html.FROM_HTML_MODE_COMPACT));
                } else {
                 //   productlist.setText(Html.fromHtml(productlisttext));
                }
                //  productlist.setText(actorsList1.get(i).getString3());
              //  productlist.setPadding(10,0,0,2);


                webview= (WebView) vvvv.findViewById(R.id.web1);
                webview.loadData(productlisttext, "text/html", "utf-8");

                orderdate = (TextView) vvvv.findViewById(R.id.orderdate);
                orderdate.setText(actorsList1.get(i).getCountry());


                orderstatus = (TextView) vvvv.findViewById(R.id.orderstatus);
                orderstatus.setText(actorsList1.get(i).getString1());

                card.addView(vvvv);

                listView.addView(card);
            }



        }


    }




}
