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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
 * Created by Anu on 1/15/2020.
 */

public class kyc extends Fragment {

    SharedPreferences pref;
    String userid = "",userid2="";
    Boolean isuserloggedin = false;
    MyPreferenceManager session2;

    private int PICK_IMAGE_REQUEST =1,PICK_IMAGE_REQUEST2=2,PICK_IMAGE_REQUEST3=3;
    private Uri filePath;
    private Bitmap bitmap,bitmap2,bitmap3;

    EditText ed1, ed2, ed3, ed4, ed5, ed6,ed7,ed9;
    String str1 = "", str2 = "", str3 = "", str4 = "", str5 = "", str6 = "",str7 = "",str8 = "",str9 = "";
    Button btn1;
    Spinner spinner1,spinner2;
    ImageView imageView,imageView2,imageView3;

    private static final int PERMISSION_REQUEST_CODE = 200,PERMISSION_REQUEST_CODE2 = 400;
    private int PICK_IMAGE_CAMERA = 50,PICK_IMAGE_CAMERA2 = 51,PICK_IMAGE_CAMERA3 = 52;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_kyc, container, false);


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
        spinner1=(Spinner)v.findViewById(R.id.ed3);

        ed5 = (EditText) v.findViewById(R.id.ed5);
        imageView=(ImageView) v.findViewById(R.id.ed6);

        ed7 = (EditText) v.findViewById(R.id.ed5);
        imageView2=(ImageView) v.findViewById(R.id.ed8);


        ed9 = (EditText) v.findViewById(R.id.ed5);
        imageView3=(ImageView) v.findViewById(R.id.ed10);


        ed5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        ed7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage2();
            }
        });
        ed9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage3();
            }
        });

        btn1 = (Button) v.findViewById(R.id.btn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new JSONAsyncTask2().execute(EndPoints.VPROFFILE + "?userid=" + userid + "&utype=4&userid2="+userid2);

            }
        });


        new JSONAsyncTask().execute(EndPoints.VPROFFILEFETCH + "?userid=" + userid + "&utype=4&userid2="+userid2);

        showBackButton();

        return v;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

    private void selectImage2() {
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
                                startActivityForResult(intent, PICK_IMAGE_CAMERA2);
                            } else {
                                requestPermission();
                            }

                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            if (checkPermission2()) {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST2);
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

    private void selectImage3() {
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
                                startActivityForResult(intent, PICK_IMAGE_CAMERA3);
                            } else {
                                requestPermission();
                            }

                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            if (checkPermission2()) {
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST3);
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
        } else if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap2 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView2.setImageBitmap(bitmap2);
                String picturePath = getPath( getActivity(), filePath);
                ed7.setText(picturePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST3 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap3 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView3.setImageBitmap(bitmap3);
                String picturePath = getPath( getActivity(), filePath);
                ed9.setText(picturePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }  else if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_CAMERA2) {
            try {
                bitmap2 = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                imageView2.setImageBitmap(bitmap2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_CAMERA3) {
            try {
                bitmap3 = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap3.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                imageView3.setImageBitmap(bitmap3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{}
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
                        str6 = ((object.getString("image2")).trim());
                        str7 = ((object.getString("image3")).trim());
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
            spinner1.setSelection(getIndex(spinner1, str3));

            if(str5!=null){
                if(str5.length()>3){
                    new DownloadImageTask(imageView).execute(EndPoints.BASE_URL2+"/"+str5);
                }
            }
            if(str6!=null){
                if(str6.length()>3){
                    new DownloadImageTask(imageView2).execute(EndPoints.BASE_URL2+"/"+str6);
                }
            }
            if(str7!=null){
                if(str7.length()>3){
                    new DownloadImageTask(imageView3).execute(EndPoints.BASE_URL2+"/"+str7);
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
            str3 = spinner1.getSelectedItem().toString();

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
                String str91= getStringImage(bitmap);
                String str92= getStringImage(bitmap2);
                String str93= getStringImage(bitmap3);

                HttpPost httppost = new HttpPost(urls[0]);
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters = new ArrayList<NameValuePair>();
                //postParameters.add(new BasicNameValuePair("mobilenoss", mobmno));
                postParameters.add(new BasicNameValuePair("userid", userid));
                postParameters.add(new BasicNameValuePair("str1", str1));
                postParameters.add(new BasicNameValuePair("str2", str2));
                postParameters.add(new BasicNameValuePair("str3", str3));
                postParameters.add(new BasicNameValuePair("image", str91));
                postParameters.add(new BasicNameValuePair("image2", str92));
                postParameters.add(new BasicNameValuePair("image3", str93));


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

}
