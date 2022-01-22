package com.mbl.zaikavendor.Fragment;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.app.EndPoints;
import com.mbl.zaikavendor.helper.MyPreferenceManager;

/**
 * Created by Anu on 1/15/2020.
 */

public class Agreement extends Fragment {

    MyPreferenceManager session2;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9;
    Fragment newFragment;
    Bundle bundle;
    WebView webView;

    ProgressDialog pDialog;
    Boolean failedLoading = false;

    SharedPreferences pref;
    String userid = "",userid2="";
    Boolean isuserloggedin = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_agreement, container, false);

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

        tv1=(TextView)v.findViewById(R.id.profile);
        tv2=(TextView)v.findViewById(R.id.address);//facility
        webView=(WebView)v.findViewById(R.id.mywebview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(EndPoints.AGREEMENT+"?userid="+userid+"&userid2="+userid2+"&uid=1");

            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(EndPoints.AGREEMENT+"?userid="+userid+"&userid2="+userid2+"&uid=2");
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
    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource (WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }
            public void onPageFinished(WebView view, String url) {
                try{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);

        // Other webview options
        /*
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        */

        /*
         String summary = "<html><body>You scored <b>192</b> points.</body></html>";
         webview.loadData(summary, "text/html", null);
         */

        //Load url in webview
        webView.loadUrl(url);


    }
}
