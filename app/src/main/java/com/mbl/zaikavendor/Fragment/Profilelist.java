package com.mbl.zaikavendor.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.mbl.zaikavendor.MapLocationActivity;
import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.Welcome;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.helper.MyPreferenceManager;

/**
 * Created by Anu on 1/1/2020.
 */

public class Profilelist extends Fragment {

    MyPreferenceManager session2;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11,tv12,tv13,tv14,tv15;
    Fragment newFragment;
    Bundle bundle;

    SharedPreferences pref;
    String userid="",userid2="",ispaycenter="0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profilelist, container, false);

        session2 = new MyPreferenceManager(getActivity().getApplicationContext());

        pref = getActivity().getSharedPreferences(EndPoints.sharedpref, 0);
        if (pref.getString(EndPoints.prefname, "").length() > 0) {
            userid = pref.getString(EndPoints.prefuname, "");
            userid2 = pref.getString(EndPoints.prefmainid,"");
            ispaycenter =  pref.getString(EndPoints.ispaycenter,"");
        }

        tv1=(TextView)v.findViewById(R.id.profile);
        tv2=(TextView)v.findViewById(R.id.address);//facility
        tv3=(TextView)v.findViewById(R.id.helpcenter); //hours
        tv4=(TextView)v.findViewById(R.id.aboutus);//pass
        tv5=(TextView)v.findViewById(R.id.contactus);//pass
        tv6=(TextView)v.findViewById(R.id.logout);//pass

        tv7=(TextView)v.findViewById(R.id.location);//pass
        tv8=(TextView)v.findViewById(R.id.mykyc);//pass
        tv9=(TextView)v.findViewById(R.id.bank);//pass
        tv10=(TextView)v.findViewById(R.id.agreement);//pass
        tv13=(TextView)v.findViewById(R.id.ringtone);//pass

        tv14=(TextView)v.findViewById(R.id.myoffers);//pass
        tv15=(TextView)v.findViewById(R.id.gallery);//pass


        tv11=(TextView)v.findViewById(R.id.mywallet);//pass
        tv12=(TextView)v.findViewById(R.id.paycenter);//pass
        if(ispaycenter.equals("1")){
            tv12.setVisibility(View.VISIBLE);
        }



        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new Profile();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("addproducts")
                        .commit();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new Facility();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("addproducts")
                        .commit();
            }
        });

        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new Workhour();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("addproducts")
                        .commit();
            }
        });

        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new Password();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("addproducts")
                        .commit();
            }
        });

        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new ContactUs();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("addproducts")
                        .commit();
            }
        });

        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session2.logoutUser();
            }
        });

        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), MapLocationActivity.class);
                startActivity(i);
            }
        });

        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new kyc();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("mykyc")
                        .commit();
            }
        });
        tv9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new Bank();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("mykyc")
                        .commit();
            }
        });

        tv10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new Agreement();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("agreement")
                        .commit();
            }
        });

        tv13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new Ringtone();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("mykyc")
                        .commit();
            }
        });

        tv11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new Mywallet();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("mywallet")
                        .commit();
            }
        });

        tv12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new Paymentcenter();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("paymentcenter")
                        .commit();
            }
        });

        tv14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new Myoffers();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("myoffers")
                        .commit();
            }
        });

        tv15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new Gallery();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("myoffers")
                        .commit();
            }
        });

        TextView tv16=(TextView)v.findViewById(R.id.mypayments);
        tv16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new paymenthistory();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("paymenthistory")
                        .commit();
            }
        });
        return v;
    }
}
