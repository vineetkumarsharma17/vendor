package com.mbl.zaikavendor.helper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.constant.Config;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String REG_TOKEN = "REG_TOKEN";
    private static final String TAG = MyFirebaseInstanceIdService.class.getSimpleName();
    String mytoken="";
    SharedPreferences pref;
    String userid="";

    public void updatetoken(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        mytoken=refreshedToken;
        sendRegistrationToServer(refreshedToken,refreshedToken);
    }



    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        mytoken=refreshedToken;
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken,refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    public void sendRegistrationToServer(final String token,String userid) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);

        new JSONAsyncTask().execute(EndPoints.UPDATETOKEN);

    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }



      class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            pref= getSharedPreferences(EndPoints.sharedpref, 0);
            userid = pref.getString(EndPoints.prefuname,"");

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

                // Example send http request
                final String url = urls[0];

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("fullname", mytoken));
                params.add(new BasicNameValuePair("password", userid));

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

        }
    }

}
