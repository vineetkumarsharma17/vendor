package com.mbl.zaikavendor.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


import com.mbl.zaikavendor.R;
/**
 * Created by sachin on 7/19/2018.
 */

public class About extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);




        WebView web = (WebView)v.findViewById(R.id.web1);
        String str ="<p style=\"text-align:justify;font-size:13px\">\"Online food ordering is the process of food delivery or takeout from a local restaurant or food cooperative through a web page or app</p>";
        web.loadData(str, "text/html", "utf-8");

        WebView web1 = (WebView)v.findViewById(R.id.web2);
        String str1 ="<p style=\"text-align:justify;font-size:13px\">\"Our team gathers information from every restaurant on a regular basis to ensure our data is fresh. Our vast community of food lovers share their reviews and photos, so you have all that you need to make an informed choice.</p>";
        web1.loadData(str1, "text/html", "utf-8");

        WebView web3 = (WebView)v.findViewById(R.id.web3);
        String str3 ="<p style=\"text-align:justify;font-size:13px\">\"Choose to order food online because it's literally at their fingertips. Virtually anyone with a Smartphone can order food online from your favorite restaurant.</p>";
        web3.loadData(str3, "text/html", "utf-8");


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
