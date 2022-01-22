package com.mbl.zaikavendor.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mbl.zaikavendor.Cache;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Anu on 12/19/2019.
 */

public class option_taxation extends Fragment {
    SharedPreferences pref;
    String userid = "",userid2="";
    Boolean isuserloggedin = false;
    MyPreferenceManager session2;

    private int PICK_IMAGE_REQUEST = 1,PICK_IMAGE_REQUEST2=2,PICK_IMAGE_REQUEST3=3;
    private Uri filePath,filePath2,filePath3;
    private Bitmap bitmap,bitmap2,bitmap3;

    EditText ed1, ed2, ed3, ed4, ed5, ed6,ed51,ed71;
    String str1 = "", str2 = "", str3 = "", str4 = "", str5 = "", str6 = "";
    Button btn1;
    Spinner spinner1,spinner2;
    ImageView imageView,imageView2,imageView3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_taxation, container, false);


        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getActivity().onBackPressed();
            }
        });

        session2 = new MyPreferenceManager(getActivity().getApplicationContext());
        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            isuserloggedin = true;
            userid = pref.getString(EndPoints.prefuname, "");
            userid2 = pref.getString(EndPoints.prefmainid,"");
        } else {
            isuserloggedin = false;
            session2.logoutUser();
        }

        ed1 = (EditText) v.findViewById(R.id.ed1);
        ed2 = (EditText) v.findViewById(R.id.ed2);
        ed5 = (EditText) v.findViewById(R.id.ed5);
        imageView=(ImageView) v.findViewById(R.id.ed6);
        imageView2=(ImageView) v.findViewById(R.id.ed61);
        imageView3=(ImageView) v.findViewById(R.id.ed72);

        ed5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


        ed51 = (EditText) v.findViewById(R.id.ed51);
        ed51.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser2();
            }
        });


        ed71 = (EditText) v.findViewById(R.id.ed51);
        ed71.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser3();
            }
        });

        btn1 = (Button) v.findViewById(R.id.btn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new JSONAsyncTask2().execute(EndPoints.VPROFFILE + "?userid=" + userid + "&utype=6&userid2="+userid2);

            }
        });


        new JSONAsyncTask().execute(EndPoints.VPROFFILEFETCH + "?userid=" + userid + "&utype=6&userid2="+userid2);

        showBackButton();

        return v;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void showFileChooser2() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST2);
    }
    private void showFileChooser3() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                String picturePath = getPath( getActivity(), filePath);
                ed5.setText(picturePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else  if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath2 = data.getData();
            try {
                bitmap2 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath2);
                imageView2.setImageBitmap(bitmap2);
                String picturePath = getPath( getActivity(), filePath2);
                ed51.setText(picturePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }  else  if (requestCode == PICK_IMAGE_REQUEST3 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath3 = data.getData();
            try {
                bitmap3 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath3);
                imageView3.setImageBitmap(bitmap3);
                String picturePath = getPath( getActivity(), filePath3);
                ed71.setText(picturePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{}
    }

    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                // Do onlick on menu action here
                int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();
                Toast.makeText(getActivity().getApplicationContext(), "Pl" + count, Toast.LENGTH_LONG).show();

                if (count == 0) {
                    // super.onBackPressed();
                    //additional code
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                } else {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                return true;
        }
        return false;
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
                    JSONArray jarray2 = jsono.getJSONArray("model");
                    for (int i = 0; i < jarray2.length(); i++) {
                        JSONObject object = jarray2.getJSONObject(i);
                        str1 = ((object.getString("fullname")).trim());
                        str2 = ((object.getString("age")).trim());

                        str3 = ((object.getString("sex")).trim());
                        str5 = ((object.getString("image")).trim());
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

            ed1.setText(str1);
            ed2.setText(str2);

            if(str3!=null){
                if(str3.length()>3){
                    new DownloadImageTask(imageView).execute(EndPoints.BASE_URL2+"/"+str3);
                }
            }
            if(str5!=null){
                if(str5.length()>3){
                    new DownloadImageTask(imageView2).execute(EndPoints.BASE_URL2+"/"+str5);
                }
            }

        }

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    class JSONAsyncTask2 extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            str1 = ed1.getText().toString();
            str2 = ed2.getText().toString();

            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait");
            dialog.setTitle("Processing");
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
                String str9= getStringImage(bitmap);
                String str92= getStringImage(bitmap2);
                HttpPost httppost = new HttpPost(urls[0]);
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters = new ArrayList<NameValuePair>();
                //postParameters.add(new BasicNameValuePair("mobilenoss", mobmno));
                postParameters.add(new BasicNameValuePair("userid", userid));
                postParameters.add(new BasicNameValuePair("str1", str1));
                postParameters.add(new BasicNameValuePair("str2", str2));

                postParameters.add(new BasicNameValuePair("str3", str92));
                postParameters.add(new BasicNameValuePair("image", str9));


                httppost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));

                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                System.out.println("catgorylist" + response.toString());
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsono = new JSONObject(data);

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
            dialog.cancel();

        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap = (Bitmap) Cache.getInstance().getLru().get(urls[0]);
            Bitmap mIcon11 = null;
            if(bitmap!=null)
                mIcon11= bitmap;
            else
            {
                String urldisplay = urls[0];

                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                    Cache.getInstance().getLru().put(urls[0], mIcon11);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }


}
