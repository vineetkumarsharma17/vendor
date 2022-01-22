package com.mbl.zaikavendor.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mbl.zaikavendor.adapter.Myincomeadapter;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Anu on 7/17/2020.
 */

public class paymenthistory extends Fragment {
    ArrayList<Actors> actorsList2, actorsList1, actorsList4;

    MyPreferenceManager session2;
    SharedPreferences pref;
    String userid="",userid2="";
    LinearLayout listView;

    int i=0;

    TextView productlist,orderdate,restaurentname,address,price,orderstatus,myorderid,fulltotal;

    WebView webview;

    RecyclerView recyclerView;
    Myincomeadapter recyclerViewAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();
    ArrayList<Actors> actorsList=new ArrayList<Actors>();

    boolean isLoading = false;
    boolean canloadmore=true;

    TextView pendingorder,allorder,delieveredorder;
    String mytotalincome="0";

    String whichtype2="";
    Button addpayment;


    View popupInputDialogView;
    AlertDialog alertDialog;


    private int PICK_IMAGE_REQUEST = 1;
    private int PICK_IMAGE_CAMERA = 50;

    private Uri filePath;
    private Bitmap bitmap;
    ImageView myimg;

    int mywhichtypebase=4;

    private static final int PERMISSION_REQUEST_CODE = 200,PERMISSION_REQUEST_CODE2 = 400;

    TextView myincometype;
    String myincometype_txt="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mypaymenthistory, container, false);



        //listView=(LinearLayout)v.findViewById(R.id.list);
        session2 =new MyPreferenceManager(getActivity());

        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname, "");
            userid2 = pref.getString(EndPoints.prefmainid,"");
        } else{
            session2.logoutUser();
            startActivity(new Intent(getActivity(), Login.class));

        }

        addpayment=(Button)v.findViewById(R.id.addpayment);
        addpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortPopup();
            }
        });

        myincometype = (TextView) v.findViewById(R.id.myincometype) ;


        pendingorder = (TextView) v.findViewById(R.id.today) ;
        delieveredorder = (TextView) v.findViewById(R.id.yesterday) ;
        allorder = (TextView) v.findViewById(R.id.lastweek) ;
        fulltotal = (TextView)v.findViewById(R.id.totalin) ;

        pendingorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterorderlist(1);
                pendingorder.setTextColor(Color.parseColor("#dd1100"));
                delieveredorder.setTextColor(Color.parseColor("#000000"));
                allorder.setTextColor(Color.parseColor("#000000"));
                // cancelrder.setBackgroundColor(Color.parseColor("#cccccc"));
                fulltotal.setTextColor(Color.parseColor("#000000"));
            }
        });
        delieveredorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterorderlist(2);
                pendingorder.setTextColor(Color.parseColor("#000000"));
                delieveredorder.setTextColor(Color.parseColor("#dd1100"));
                allorder.setTextColor(Color.parseColor("#000000"));
                // cancelrder.setBackgroundColor(Color.parseColor("#cccccc"));
                fulltotal.setTextColor(Color.parseColor("#000000"));
            }
        });
        allorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterorderlist(3);
                pendingorder.setTextColor(Color.parseColor("#000000"));
                delieveredorder.setTextColor(Color.parseColor("#000000"));
                allorder.setTextColor(Color.parseColor("#dd1100"));
                // cancelrder.setBackgroundColor(Color.parseColor("#cccccc"));
                fulltotal.setTextColor(Color.parseColor("#000000"));
            }
        });

        fulltotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterorderlist(4);
                pendingorder.setTextColor(Color.parseColor("#000000"));
                delieveredorder.setTextColor(Color.parseColor("#000000"));
                allorder.setTextColor(Color.parseColor("#000000"));
                //cancelrder.setBackgroundColor(Color.parseColor("#cccccc"));
                fulltotal.setTextColor(Color.parseColor("#dd1100"));
            }
        });


        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerViewAdapter = new Myincomeadapter(actorsList,getActivity().getSupportFragmentManager());
        recyclerView.setAdapter(recyclerViewAdapter);


        filterorderlist(mywhichtypebase);

        showBackButton();

        return v;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        actorsList.clear();
        Log.e("Frontales","resume");
    }
    public void filterorderlist(int whichtype){
        actorsList.clear();
        mywhichtypebase = whichtype;
        new JSONAsyncTask().execute(EndPoints.MYPAYMENTHISTORY + "?category_id=" + userid+"&whichtype="+whichtype+"&userid2="+userid2);
    }


    private void showSortPopup() {
        // Create a AlertDialog Builder.

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Set title, icon, can not cancel properties.
        // alertDialogBuilder.setTitle("Schedule Callback");
        // alertDialogBuilder.setCancelable(false);

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());


        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.paymentpay, null);

        final EditText amount = (EditText) popupInputDialogView.findViewById(R.id.amount); //person name
        final EditText transactionid = (EditText) popupInputDialogView.findViewById(R.id.transactionid); //person name
        final EditText comment = (EditText) popupInputDialogView.findViewById(R.id.comment); //person name
        final EditText selectimg = (EditText) popupInputDialogView.findViewById(R.id.selectimg); //person name
        myimg = (ImageView) popupInputDialogView.findViewById(R.id.imgview); //person name
        selectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });



        Button btn6=(Button)popupInputDialogView.findViewById(R.id.paynow);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str1 = amount.getText().toString();  //address
                String str2 = transactionid.getText().toString();  //address
                String str3 = comment.getText().toString();  //address


                if(str1.length()<2){
                    Toast.makeText(getActivity(), "Enter Valid Amount" , Toast.LENGTH_LONG).show();
                } else {
                    login(str1,str2,str3);
                    alertDialog.dismiss();
                }


            }
        });



        // Set the inflated layout view object to the AlertDialog builder.
        alertDialogBuilder.setView(popupInputDialogView);
        //ViewGroup parent = (ViewGroup)popupInputDialogView.getParent();
//        parent.setPadding(0, 0, 0, 0);

        // Create AlertDialog and show.
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission2() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission2() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE2);
    }


    private void selectImage() {
        try {
            PackageManager pm = getActivity().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getActivity().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            if (checkPermission()) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, PICK_IMAGE_CAMERA);
                            } else {
                                requestPermission();
                            }

                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            if (checkPermission2()) {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST);
                            } else {
                                requestPermission2();
                            }

                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else {
                requestPermission();
                Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Camera Permission error2", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                myimg.setImageBitmap(bitmap);
                String picturePath = getPath( getActivity(), filePath);
                // ed5.setText(picturePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                myimg.setImageBitmap(bitmap);
            } catch (Exception e) {
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

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    //selectImage();
                    // main logic
                } else {
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
            case PERMISSION_REQUEST_CODE2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    //selectImage();
                    // main logic
                } else {
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission2();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
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
                HttpGet httppost = new HttpGet(urls[0]);
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                System.out.println("catgorylist" + response.toString());
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);

                    JSONObject jsono = new JSONObject(data);
                    System.out.println("catgorylist" + jsono.toString());

                    try{
                        myincometype_txt = jsono.getString("myincometype");
                    } catch (Exception e){}

                    JSONArray jarray = jsono.getJSONArray("mainmenu");
                    for (int i = 0; i < jarray.length(); i++) {

                        JSONObject object = jarray.getJSONObject(i);
                        System.out.println("catgoryobject" + object.toString());

                        Actors actor = new Actors();

                        actor.setChildren((object.getString("menuid")).trim()); //id
                        actor.setHeight((object.getString("orderdate")).trim());  //orderid
                        actor.setName((object.getString("clientname")).trim());  //subloc

                        actor.setCountry((object.getString("orderid")).trim()); //date
                        actor.setSpouse((object.getString("orderamount")).trim()); //total

                        actor.setString1((object.getString("myamount1")).trim()); //status
                        actor.setImage((object.getString("myamount2")).trim());  //address
                        actor.setString3((object.getString("myamount3")).trim());  //food for 2


                        actorsList.add(actor);
                    }

                   // mytotalincome = jsono.getString("totalorder");

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
            //dialog.cancel();
            myincometype.setText(myincometype_txt);
            recyclerViewAdapter = new Myincomeadapter(actorsList,getActivity().getSupportFragmentManager());
            recyclerView.setAdapter(recyclerViewAdapter);

        }
    }


    private void login(final String str1,final String str2,final String str3) {
        //  str1=ed1.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.MYPAYMENTHISTORY+"?utype="+userid+"&updatetype=1"+"&userid2="+userid2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    filterorderlist(mywhichtypebase);

                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                //  Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getActivity().getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                //    Toast.makeText(getActivity().getApplicationContext(), "Please Check Internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("var1", str1);
                params.put("var2", str2);
                params.put("var3", str3);
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


}
