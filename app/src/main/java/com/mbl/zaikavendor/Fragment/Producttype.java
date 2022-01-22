package com.mbl.zaikavendor.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.helper.MyPreferenceManager;

/**
 * Created by Anu on 1/4/2020.
 */

public class Producttype extends Fragment {

    MyPreferenceManager session2;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
    Fragment newFragment;
    Bundle bundle;
    String userid="",userid2="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_producttype, container, false);

        session2 = new MyPreferenceManager(getActivity().getApplicationContext());

        tv1=(TextView)v.findViewById(R.id.profile);
        tv2=(TextView)v.findViewById(R.id.address);//facility
        tv3=(TextView)v.findViewById(R.id.helpcenter); //hours
        tv4=(TextView)v.findViewById(R.id.aboutus); //hours


        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new Products();
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
                newFragment = new Partylist();
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
                newFragment = new Medicatedlist();
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
                newFragment = new Tiffinlist();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("addproducts")
                        .commit();
            }
        });

        TextView tv5 = (TextView)v.findViewById(R.id.grocery);
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm= getActivity().getSupportFragmentManager();
                newFragment = new Grocerylist();
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("addproducts")
                        .commit();
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
}
