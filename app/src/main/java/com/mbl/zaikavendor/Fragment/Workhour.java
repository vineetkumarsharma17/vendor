package com.mbl.zaikavendor.Fragment;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.app.MyApplication;

/**
 * Created by sachin on 1/13/2019.
 */

public class Workhour extends Fragment {
    SharedPreferences pref;
    String userid="",userid2="";

    Spinner spinner1,spinner2,spinner3,spinner4,spinner5,spinner6,spinner7;
    EditText editText1,editText2,editText3,editText4,editText5,editText6,editText7;
    EditText editText11,editText22,editText33,editText44,editText55,editText66,editText77;

    String str10="monday",str20="tuesday",str30="wednesday",str40="thursday",str50="friday",str60="saturday",str70="sunday";
    String str1,str2,str3,str4,str5,str6,str7;
    String str11,str22,str33,str44,str55,str66,str77;
    String str111,str222,str333,str444,str555,str666,str777;

    Button btn;
    TextView tv1;
    ArrayList<Actors> actorsList;


    private Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workhour, container, false);


        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname,"");
            userid2 = pref.getString(EndPoints.prefmainid,"");
        }

        calendar = Calendar.getInstance();


        spinner1 = (Spinner) v.findViewById(R.id.spinner1);
        spinner2 = (Spinner) v.findViewById(R.id.spinner2);
        spinner3 = (Spinner) v.findViewById(R.id.spinner3);
        spinner4 = (Spinner) v.findViewById(R.id.spinner4);
        spinner5 = (Spinner) v.findViewById(R.id.spinner5);
        spinner6 = (Spinner) v.findViewById(R.id.spinner6);
        spinner7 = (Spinner) v.findViewById(R.id.spinner7);


        editText1=(EditText)v.findViewById(R.id.ed11);
        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settimeinthis(editText1);
            }
        });
        editText2=(EditText)v.findViewById(R.id.ed21);
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settimeinthis(editText2);
            }
        });
        editText3=(EditText)v.findViewById(R.id.ed31);
        editText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settimeinthis(editText3);
            }
        });
        editText4=(EditText)v.findViewById(R.id.ed41);
        editText4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settimeinthis(editText4);
            }
        });
        editText5=(EditText)v.findViewById(R.id.ed51);
        editText5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settimeinthis(editText5);
            }
        });
        editText6=(EditText)v.findViewById(R.id.ed61);
        editText6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settimeinthis(editText6);
            }
        });
        editText7=(EditText)v.findViewById(R.id.ed71);
        editText7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settimeinthis(editText7);
            }
        });


        editText11=(EditText)v.findViewById(R.id.ed12);
        editText11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settimeinthis(editText11);
            }
        });
        editText22=(EditText)v.findViewById(R.id.ed22);
        editText22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settimeinthis(editText22);
            }
        });
        editText33=(EditText)v.findViewById(R.id.ed32);
        editText33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settimeinthis(editText33);
            }
        });
        editText44=(EditText)v.findViewById(R.id.ed42);
        editText44.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settimeinthis(editText44);
            }
        });
        editText55=(EditText)v.findViewById(R.id.ed52);
        editText55.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settimeinthis(editText55);
            }
        });
        editText66=(EditText)v.findViewById(R.id.ed62);
        editText66.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settimeinthis(editText66);
            }
        });
        editText77=(EditText)v.findViewById(R.id.ed72);
        editText77.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settimeinthis(editText77);
            }
        });

        btn =(Button)v.findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str1=spinner1.getSelectedItem().toString();
                str2=spinner2.getSelectedItem().toString();
                str3=spinner3.getSelectedItem().toString();
                str4=spinner4.getSelectedItem().toString();
                str5=spinner5.getSelectedItem().toString();
                str6=spinner6.getSelectedItem().toString();
                str7=spinner7.getSelectedItem().toString();

                str11=editText1.getText().toString();
                str22=editText2.getText().toString();
                str33=editText3.getText().toString();
                str44=editText4.getText().toString();
                str55=editText5.getText().toString();
                str66=editText6.getText().toString();
                str77=editText7.getText().toString();

                str111=editText11.getText().toString();
                str222=editText22.getText().toString();
                str333=editText33.getText().toString();
                str444=editText44.getText().toString();
                str555=editText55.getText().toString();
                str666=editText66.getText().toString();
                str777=editText77.getText().toString();

                login();
            }
        });

        tv1=(TextView)v.findViewById(R.id.tv1);

        new JSONAsyncTask().execute(EndPoints.WORKHOURFETCH+"?category_id="+userid+"&userid2="+userid2);


        showBackButton();

        return v;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbtn);
        }
    }

    public void settimeinthis(final EditText eds){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // Create a new instance of TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String hourval =String.valueOf(selectedHour);
                String minval =String.valueOf(selectedMinute);
                if(selectedHour<10){
                    hourval="0"+hourval;
                }
                if(selectedMinute<10){
                    minval="0"+minval;
                }
                eds.setText(hourval+ ":" + minval);
            }
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
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

                    JSONObject object = new JSONObject(data);
                  //  JSONArray jarray = jsono.getJSONArray("model");
                   // System.out.println("catgoryjson" + jarray.toString());

                    //for (int i = 0; i < jarray.length(); i++) {

                        System.out.println("catgoryobject" + object.toString());

                        Actors actor = new Actors();

                        str10=((object.getString("entity_id1")).trim());
                        str1=((object.getString("name1")).trim());
                        str11=(object.getString("sku1"));
                        str111=(object.getString("price1"));


                        str20=((object.getString("entity_id2")).trim());
                        str2=((object.getString("name2")).trim());
                        str22=(object.getString("sku2"));
                        str222=(object.getString("price2"));


                        str30=((object.getString("entity_id3")).trim());
                        str3=((object.getString("name3")).trim());
                        str33=(object.getString("sku3"));
                        str333=(object.getString("price3"));


                        str40=((object.getString("entity_id4")).trim());
                        str4=((object.getString("name4")).trim());
                        str44=(object.getString("sku4"));
                        str444=(object.getString("price4"));


                        str50=((object.getString("entity_id5")).trim());
                        str5=((object.getString("name5")).trim());
                        str55=(object.getString("sku5"));
                        str555=(object.getString("price5"));


                        str60=((object.getString("entity_id6")).trim());
                        str6=((object.getString("name6")).trim());
                        str66=(object.getString("sku6"));
                        str666=(object.getString("price6"));


                        str70=((object.getString("entity_id7")).trim());
                        str7=((object.getString("name7")).trim());
                        str77=(object.getString("sku7"));
                        str777=(object.getString("price7"));





                    //    actorsList.add(actor);
                  //  }
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
            //adapter.notifyDataSetChanged();
            //  adapter2.notifyDataSetChanged();

            editText1.setText(str1);
            editText2.setText(str2);
            editText3.setText(str3);
            editText4.setText(str4);
            editText5.setText(str5);
            editText6.setText(str6);
            editText7.setText(str7);

            editText11.setText(str11);
            editText22.setText(str22);
            editText33.setText(str33);
            editText44.setText(str44);
            editText55.setText(str55);
            editText66.setText(str66);
            editText77.setText(str77);

            if(str111.equals("1")){spinner1.setSelection(1);}
            if(str222.equals("1")){spinner2.setSelection(1);}
            if(str333.equals("1")){spinner3.setSelection(1);}
            if(str444.equals("1")){spinner4.setSelection(1);}
            if(str555.equals("1")){spinner5.setSelection(1);}
            if(str666.equals("1")){spinner6.setSelection(1);}
            if(str777.equals("1")){spinner7.setSelection(1);}




        }
    }


    private void login() {

        tv1.setText("Please Wait...");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.WORKHOUR+"?utype="+userid+"&userid2="+userid2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    tv1.setText("");
                    Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();


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


                params.put("day1", str10);
                params.put("day2", str20);
                params.put("day3", str30);
                params.put("day4", str40);
                params.put("day5", str50);
                params.put("day6", str60);
                params.put("day7", str70);

                params.put("onoff1", str1);
                params.put("onoff2", str2);
                params.put("onoff3", str3);
                params.put("onoff4", str4);
                params.put("onoff5", str5);
                params.put("onoff6", str6);
                params.put("onoff7", str7);

                params.put("ont1", str11);
                params.put("ont2", str22);
                params.put("ont3", str33);
                params.put("ont4", str44);
                params.put("ont5", str55);
                params.put("ont6", str66);
                params.put("ont7", str77);

                params.put("tont1", str111);
                params.put("tont2", str222);
                params.put("tont3", str333);
                params.put("tont4", str444);
                params.put("tont5", str555);
                params.put("tont6", str666);
                params.put("tont7", str777);

                return params;
            }
        };

        //Adding request to request queue
        //RequestQueue queue = Volley.newRequestQueue(this);
        MyApplication.getInstance().addToRequestQueue(strReq,"req_login");
        // queue.add(strReq);

    }
}
