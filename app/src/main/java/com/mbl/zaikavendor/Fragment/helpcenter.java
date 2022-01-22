package com.mbl.zaikavendor.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.helper.MyPreferenceManager;

/**
 * Created by Anu on 5/3/2020.
 */

public class helpcenter extends Fragment {
    String orderid;

    MyPreferenceManager session2;
    SharedPreferences pref;
    String userid="",userid2="";

    ArrayAdapter<String> adapter;
    ArrayList<String> actorsList;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_helpcenter, container, false);

        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname, "");
            userid2 = pref.getString(EndPoints.prefmainid, "");
        } else{
            session2.logoutUser();
            startActivity(new Intent(getActivity(), Login.class));
            // getActivity().finish();
        }

        actorsList = new ArrayList<String>();

        listView = (ListView)v.findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, actorsList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String value=adapter.getItem(position);

                FragmentManager fm= getActivity().getSupportFragmentManager();
                Fragment newFragment = new helpcenterdetail();
                Bundle bundle=new Bundle();
                bundle.putString("orderid",value);
                newFragment.setArguments(bundle);
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("helpadd")
                        .commit();


            }
        });

        new JSONAsyncTask().execute(EndPoints.HELPCENTERFETCHQLIST+"?utype=vendor"+"&username=" + userid+"&userid=" + userid2);

        return v;
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
