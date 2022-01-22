package com.mbl.zaikavendor.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mbl.zaikavendor.Cache;
import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.app.MyApplication;

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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Anu on 1/16/2020.
 */

public class MedicatedEdit extends Fragment {
    TextView tv1;
    EditText ed1,ed2,ed3,ed4,ed5,ed6,ed7,ed8,packchargetxt,mrpprice;
    Button btn;
    String str1,str2,str3,str4,str5,str6,str7,str8,str9,str10,str11,str12,str13,packingcharge,mrptext;
    Spinner spinner,spinner2,spinner3,spinner4;
    SharedPreferences pref;
    String userid="",userid2="";
    ArrayList<String> actorsList,actorsList2,actorsList3;
    ArrayList<Actors> v_actorsList,v_actorsList2,v_actorsList3;
    ArrayAdapter<String> adp,adp2,adp3;
    String selectedcuisine="";

    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private Bitmap bitmap;

    EditText imgchooser;
    ImageView imgselected;

    String myprodcode="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_medicatedadd, container, false);

        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname,"");
            userid2 = pref.getString(EndPoints.prefmainid,"");
        }


        Bundle b = getArguments();
        if(b!=null) {
            myprodcode = b.getString("prodcode");

        }

        spinner=(Spinner)v.findViewById(R.id.ed0);
        actorsList = new ArrayList<String>();
        adp = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, actorsList);
        spinner.setAdapter(adp);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                selectedcuisine=spinner.getSelectedItem().toString();
                int index = actorsList.indexOf(selectedcuisine);
                changesecondone(v_actorsList.get(index).getDescription());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        v_actorsList = new ArrayList<Actors>();
        v_actorsList2 = new ArrayList<Actors>();
        v_actorsList3 = new ArrayList<Actors>();

        spinner2=(Spinner)v.findViewById(R.id.ed0a);
        actorsList2 = new ArrayList<String>();
        adp2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, actorsList2);
        spinner2.setAdapter(adp2);

        spinner3=(Spinner)v.findViewById(R.id.ed0b);
        actorsList3 = new ArrayList<String>();
        adp3 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, actorsList3);
        spinner3.setAdapter(adp3);


        ed1=(EditText)v.findViewById(R.id.ed1);
        ed2=(EditText)v.findViewById(R.id.ed2);
        ed3=(EditText)v.findViewById(R.id.ed3);
        ed4=(EditText)v.findViewById(R.id.ed4);
        ed5=(EditText)v.findViewById(R.id.ed5);
        ed7=(EditText)v.findViewById(R.id.ed5a);
        ed8=(EditText)v.findViewById(R.id.ed7);
        packchargetxt = (EditText)v.findViewById(R.id.packingcharge);
        mrpprice = (EditText)v.findViewById(R.id.mrpprice);

        spinner4=(Spinner)v.findViewById(R.id.ed6);

        ed5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        ed5.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        ed7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        ed7.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        imgchooser = (EditText) v.findViewById(R.id.imgchooser);
        imgchooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        imgselected = (ImageView) v.findViewById(R.id.imagselected);


        btn= (Button) v.findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        tv1=(TextView)v.findViewById(R.id.tv1);

        new JSONAsyncTask().execute(EndPoints.PRODUCTEDITFETCH+"?category_id="+userid+"&prodtype=3"+"&userid2="+userid2+"&myprodcode="+myprodcode);


        showBackButton();

        return v;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public  void changesecondone(String pvalue){
        actorsList2.clear();
        for(int i=0;i<v_actorsList2.size();i++){
            if(v_actorsList2.get(i).getCountry().equals(pvalue)) {
                actorsList2.add(v_actorsList2.get(i).getName());
            }
        }
        adp2.notifyDataSetChanged();
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
                imgselected.setImageBitmap(bitmap);
                String picturePath = getPath( getActivity(), filePath);
                imgchooser.setText(picturePath);
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

                    System.out.println("catgoryjson" + jsono.toString());
                    str4=(jsono.getString("packname")).trim();
                    str5=(jsono.getString("details")).trim();
                    str6=(jsono.getString("price")).trim();
                    str7=(jsono.getString("bfmenu")).trim();
                    str8=(jsono.getString("luncjmenu")).trim();
                    str9=(jsono.getString("vegnonveg")).trim();
                    str10=(jsono.getString("dinnermenu")).trim();
                    str11=(jsono.getString("description")).trim();
                    str12=(jsono.getString("fullimg")).trim();
                    packingcharge = (jsono.getString("packingcharge")).trim();
                    mrptext = (jsono.getString("mrpprice")).trim();


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
            adp.notifyDataSetChanged();
            adp2.notifyDataSetChanged();
            adp3.notifyDataSetChanged();

            ed1.setText(str4);
            ed2.setText(str5);
            ed3.setText(str6); //price

            ed4.setText(str7);
            ed5.setText(str8);
            ed7.setText(str10);
            ed8.setText(str11);
            packchargetxt.setText(packingcharge);
            mrpprice.setText(mrptext);

            if(str12!=null) {
                if (str12.length() > 4) {
                    new DownloadImageTask(imgselected).execute(EndPoints.BASE_URL2 + str12);

                }
            }

            if (result == false)
                Toast.makeText(getActivity(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }

    private void login() {

        str1="";//spinner.getSelectedItem().toString();
        str2="";//spinner2.getSelectedItem().toString();
        str3="";//spinner3.getSelectedItem().toString();

        str4=ed1.getText().toString();
        str5=ed2.getText().toString();
        str6=ed3.getText().toString();
        str7=ed4.getText().toString();
        str8=ed5.getText().toString();
        str9=spinner4.getSelectedItem().toString();
        str10=ed7.getText().toString();
        str11=ed8.getText().toString();
        packingcharge = packchargetxt.getText().toString();
        mrptext = mrpprice.getText().toString();


        tv1.setText("Please Wait...");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.PRODUCTEDIT+"?utype="+userid+"&prodtype=3"+"&userid2="+userid2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    if(response.equals("added")){
                        Fragment  newFragment = new Medicatedlist();
                        FragmentManager fm= getActivity().getSupportFragmentManager();
                        fm
                                .beginTransaction()
                                .replace(R.id.container_body, newFragment)
                                .addToBackStack(null)
                                .addToBackStack("addproducts")
                                .commit();
                    }




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

                params.put("var4", str4);
                params.put("var5", str5);
                params.put("var6", str6);
                params.put("var7", str7);
                params.put("var8", str8);
                params.put("var9", str9);
                params.put("var10", str10);
                params.put("var11", str11);
                params.put("packingcharge", packingcharge);
                params.put("mrpprice", mrptext);

                params.put("prodcode", myprodcode);

                String str99= "";
                if(bitmap!=null){str99=getStringImage(bitmap);}
                params.put("image", str99);
                return params;
            }
        };
        //Adding request to request queue
        //RequestQueue queue = Volley.newRequestQueue(this);
        MyApplication.getInstance().addToRequestQueue(strReq,"req_login");
        // queue.add(strReq);
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
