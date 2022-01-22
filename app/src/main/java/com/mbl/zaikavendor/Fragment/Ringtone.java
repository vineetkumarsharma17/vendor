package com.mbl.zaikavendor.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.app.EndPoints;

/**
 * Created by Anu on 2/11/2020.
 */

public class Ringtone extends Fragment {

    TextView tv1,tv2;

    SharedPreferences pref;
    String userid="",userid2="";

    String ringtoneid="0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ringtoneset, container, false);

        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname, "");
            userid2 = pref.getString(EndPoints.prefmainid,"");
            ringtoneid = pref.getString(EndPoints.ringtonetype,"0");
        } else {

        }
        //

        youDesirePermissionCode(getActivity());

        tv1 = (TextView)v.findViewById(R.id.ringtone1);
        tv2 = (TextView)v.findViewById(R.id.ringtone2);

        if(ringtoneid.equals("1")){
            tv2.setBackgroundColor(Color.parseColor("#ddaaffaa"));
        } else{
            tv1.setBackgroundColor(Color.parseColor("#ddaaffaa"));
        }

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setBackgroundColor(Color.parseColor("#ddaaffaa"));
                tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                Uri uri=Uri.parse("android.resource://"+getActivity().getPackageName()+"/raw/tone1");
                playNotificationSound(uri);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(EndPoints.ringtonetype, "0"); // Storing string
                editor.commit();

            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setBackgroundColor(Color.parseColor("#ffffff"));
                tv2.setBackgroundColor(Color.parseColor("#ddaaffaa"));
                Uri uri=Uri.parse("android.resource://"+getActivity().getPackageName()+"/raw/tone3");
                playNotificationSound(uri);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(EndPoints.ringtonetype, "1"); // Storing string
                editor.commit();
            }
        });


        showBackButton();

        return v;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbtn);
        }
    }

    public void playNotificationSound(Uri alarmSound) {
        try {
            android.media.Ringtone r = RingtoneManager.getRingtone(getActivity(), alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void youDesirePermissionCode(Activity context){
        boolean permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(context);
        } else {
            permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if (permission) {
            //do your code
        }  else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivityForResult(intent, 200);
            } else {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_SETTINGS}, 200);
            }
        }
    }
    public void settingPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getActivity())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent, 200);

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 200 && Settings.System.canWrite(getActivity())) {
                Log.d("TAG", "MainActivity.CODE_WRITE_SETTINGS_PERMISSION success");
                //do your code
            }
        } catch (Exception e){}
    }
}
