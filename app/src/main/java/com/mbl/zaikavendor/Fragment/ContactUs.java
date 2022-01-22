package com.mbl.zaikavendor.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mbl.zaikavendor.app.EndPoints;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.mbl.zaikavendor.R;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by sachin on 7/21/2018.
 */

public class ContactUs extends Fragment {
    LinearLayout navigationView;
    AlertDialog.Builder dialog;
    AlertDialog dlg;

    EditText ed1,ed2,ed3;
    TextView tv5;

    String fullname,fullemail,fullpass,fullpass2;
    public ContactUs() {
        // Required empty public constructor
    }

    SharedPreferences pref;
    String userid="",userid2="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contactus, container, false);
        // Inflate the layout for this fragment

        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname,"");
            userid2 = pref.getString(EndPoints.prefmainid,"");
        }


        ed1=(EditText)rootView.findViewById(R.id.ed1);
        ed2=(EditText)rootView.findViewById(R.id.ed2);
        ed3=(EditText)rootView.findViewById(R.id.ed3);

        tv5=(TextView) rootView.findViewById(R.id.textView);

        dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dlg = dialog.create();
        Button btn1=(Button) rootView.findViewById(R.id.btn1) ;
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String    str1 = ed1.getText().toString();
                String   str2 = ed2.getText().toString();
                if(str1.length()<4){
                    tv5.setText("Enter Correct Name");
                }
                else if(str2.length()!=10 || !isNumeric(str2)){
                    tv5.setText("Enter Correct Mobile No");
                }
                else {
                    new JSONAsyncTask().execute(EndPoints.CONTACT_US+"?category_id="+userid+"&userid2="+userid2);
                }
            }
        });
        showBackButton();

        return rootView;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbtn);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv5.setText("Please Wait...");
            dialog.setMessage("Please Wait...");
            dlg.setMessage("Please Wait...");
            dlg.show();
            fullname = ed1.getText().toString();
            fullemail = ed2.getText().toString();
            fullpass = ed3.getText().toString();
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

                // Example send http request
                final String url = urls[0];

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("fullname", fullname));
                params.add(new BasicNameValuePair("password", fullemail));
                params.add(new BasicNameValuePair("email", fullpass));

                HttpPost httppost = new HttpPost(url);
                httppost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse response = httpclient.execute(httppost);
                String result = EntityUtils.toString(response.getEntity());

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    System.out.println("saaaaaa"+result.toString());
                    if(result.toString().equals("success")){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
                //------------------>>
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            int valueIamWaitingFor = 5;
            if(result==true) {
                ed1.setText("");
                ed2.setText("");
                ed3.setText("");
                tv5.setText("Updated");
                dlg.setMessage("Thank Your for your Query");
            }
            else{
                tv5.setText("Connection Error");
                dlg.setMessage("Connection Error");
            }
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    dlg.dismiss(); // when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }, 2000);
        }
    }

    public boolean isNumeric(String s) {
        try
        {
            double d = Double.parseDouble(s);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
