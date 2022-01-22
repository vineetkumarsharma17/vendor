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
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import com.mbl.zaikavendor.Login;
import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.adapter.Chatdetailadapter;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.app.MyApplication;
import com.mbl.zaikavendor.helper.MyPreferenceManager;

/**
 * Created by Anu on 5/4/2020.
 */

public class helpcenterdetail extends Fragment {
    ProgressDialog dialog;
    String orderid;
    ArrayList<Actors> actorsList,actorsList2;

    MyPreferenceManager session2;
    SharedPreferences pref;
    String userid="",mobileno="",fullname="",userid2="";

    ImageButton sendbtn;
    ListView messages_view;
    Chatdetailadapter chatdetailadapter;
    EditText editText;
    int msgtype=1;

    LinearLayout hiddenbottomimg;
    ImageView selectedimage;
    ImageView selectimg;

    private int PICK_IMAGE_REQUEST = 1,PICK_IMAGE_REQUEST2 = 2,PICK_IMAGE_REQUEST3 = 3;
    private Uri filePath,filePath2,filePath3;
    private Bitmap bitmap,bitmap2,bitmap3;

    TextView cancelimg;

    String sentimgaeurl="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_helpcenterdetail, container, false);

        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname, "");
            userid2 = pref.getString(EndPoints.prefmainid, "");
            fullname = pref.getString(EndPoints.prefname, "");
            mobileno = pref.getString(EndPoints.prefmob, "");
        } else{
            session2.logoutUser();
            startActivity(new Intent(getActivity(), Login.class));
            // getActivity().finish();
        }



        Bundle b = getArguments();
        if(b!=null) {
            orderid = b.getString("orderid");
        }

        editText = (EditText)v.findViewById(R.id.editText);


        selectimg =(ImageView)v.findViewById(R.id.selectimg);
        selectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        selectedimage  =(ImageView)v.findViewById(R.id.selectedimage);
        hiddenbottomimg = (LinearLayout)v.findViewById(R.id.hiddenbottomimg);


        cancelimg = (TextView) v.findViewById(R.id.cancelimg);
        cancelimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenbottomimg.setVisibility(View.GONE);
                bitmap=null;

            }
        });
        sendbtn = (ImageButton)v.findViewById(R.id.sendbtn);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim().equals("")){

                } else if(bitmap!=null){
                    // only image selected
                    addchatmsg(editText.getText().toString().trim(),"1");
                    editText.setText("");
                }
                else{
                    //text writtedn
                    addchatmsg(editText.getText().toString().trim(),"2");
                    editText.setText("");
                }
                // hiddenbottomimg.setVisibility(View.VISIBLE);
            }
        });

        actorsList = new ArrayList<Actors>();
        messages_view = (ListView)v.findViewById(R.id.messages_view);
        chatdetailadapter = new Chatdetailadapter(getActivity(),R.layout.my_message,actorsList,this);
        messages_view.setAdapter(chatdetailadapter);

        TextView action_bar_title_1 = (TextView) v.findViewById(R.id.action_bar_title_1);
        action_bar_title_1.setText(orderid);


        new JSONAsyncTask().execute(EndPoints.HELPCENTERFETCHQLIST+"?orderid="+orderid+"&username=" + userid+"&userid=" + userid2+"&utype=vendor");


        showBackButton();

        return v;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void addchatmsg(String msg,String ismedia){
        Actors actors = new Actors();
        actors.setName(msg);
        String date = new SimpleDateFormat("dd MMM h:mm a", Locale.getDefault()).format(new Date());
        actors.setDob(date);
        actors.setCountry(date);
        actors.setDescription(String.valueOf(msgtype)); // mymsg
        String myimg="";
        if(bitmap!=null){
            myimg=BitMapToString(bitmap);
        }
        actors.setImage(myimg); // mymsg
        actors.setString1("1"); // mymsg
        actorsList.add(actors);

        if(msgtype==1){
            msgtype--;
        } else{
            msgtype++;
        }

        hiddenbottomimg.setVisibility(View.GONE);
        bitmap=null;

        chatdetailadapter.notifyDataSetChanged();
        login(msg);

    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;

    }

    private void login(final String mymsg) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.HELPCENTER+"?orderid="+orderid+"&username=" + userid+"&userid=" + userid2+"&utype=vendor", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                sentimgaeurl = response;


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                //  Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                //    Toast.makeText(getActivity().getApplicationContext(), "Please Check Internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", userid);
                params.put("fullname", fullname);
                params.put("usermob", mobileno);
                params.put("usertype", "client");

                params.put("orderid", orderid);
                params.put("question", mymsg);
                String str9="";
                if(bitmap!=null) {
                    str9= getStringImage(bitmap);
                }
                params.put("image", str9);
                return params;
            }
        };

        //Adding request to request queue
        //RequestQueue queue = Volley.newRequestQueue(this);
        MyApplication.getInstance().addToRequestQueue(strReq,"req_login");
        // queue.add(strReq);

    }


    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
//            adapter.clear();
            dialog.setMessage("Loading, please wait");
            // dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
            actorsList.clear();

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

                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("userid", userid));
                postParameters.add(new BasicNameValuePair("otherid", orderid));
                httppost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));

                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                System.out.println("catgorylist" + response.toString());
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsono = new JSONObject(data);


                    try {
                        JSONArray jarray = jsono.getJSONArray("mainmenu");
                        for (int i = 0; i < jarray.length(); i++) {

                            JSONObject object = jarray.getJSONObject(i);
                            System.out.println("catgoryobject" + object.toString());

                            Actors actors = new Actors();

                            actors.setHeight(String.valueOf(i));
                            actors.setName((object.getString("fullname")).trim()); //id
                            actors.setDescription((object.getString("otheruserid")).trim()); //id
                            actors.setImage((object.getString("image")).trim()); //id
                            //  actors.setDob((object.getString("myuid")).trim()); //id
                            actors.setDob((object.getString("country")).trim()); //id
                            actors.setCountry((object.getString("msgtiming")).trim()); //id
                            actors.setString1("2"); // mymsg
                            //  actors.setImage((object.getString("image")).trim()); //id
                            // actors.setChildren((object.getString("otheruserid")).trim()); //id

                            actorsList.add(actors);
                        }
                    }
                    catch(Exception e){
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
            dialog.dismiss();

            chatdetailadapter.notifyDataSetChanged();


        }
    }



    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                selectedimage.setImageBitmap(bitmap);
                String picturePath = getPath( getActivity(), filePath);
                hiddenbottomimg.setVisibility(View.VISIBLE);
                // ed5.setText(picturePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


}
