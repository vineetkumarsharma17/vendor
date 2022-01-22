package com.mbl.zaikavendor.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.mbl.zaikavendor.R;

/**
 * Created by Anu on 1/25/2020.
 */

public class Orderdetails extends Fragment {
    TextView personanme, mobilebike, callbackscheduled, addedondate,lastupdatedate;

    String myInt = "", orderstatus="",orderfinalamount="",pname = "", mobile = "", bike = "", nextschedule = "", isfav = "", addedon = "", isclosed = "0",lastupdatedatetxt="";
    String productlisttext="";
    String extracharges="0#0#0#0";
    TextView helpcenter;
    String myorderid,myorderdate,myorderstatus,myresname,myresaddress,myproductlist,myusername,myusraddeess,myorderamount,mydelivery, mygst,mymypacking,mytotal,mypayvia,mydeliveryby,myuserview,reviewnumber;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orderdetails, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            myInt = bundle.getString("menuid");
            pname = bundle.getString("pname"); //pnam,e
            mobile =  bundle.getString("pid"); //order id
            addedon = bundle.getString("details"); //date
            nextschedule = bundle.getString("price");
            orderstatus=bundle.getString("sfrom");
            lastupdatedatetxt= bundle.getString("address");
            isclosed = bundle.getString("sto"); //status
            orderfinalamount=bundle.getString("servesize");
            extracharges=bundle.getString("extracharge");

            productlisttext="<table style='width:100%'>";
            String fullsze[]=orderfinalamount.split("&&");
            for(int k=0;k<fullsze.length;k++){
                productlisttext=productlisttext+"<tr style='width:100%'>";
                String tddivide[]=fullsze[k].split("#");
                for(int kk=0;kk<tddivide.length;kk++){
                    String width="20%";
                    if(kk==0){
                        width="58%";
                    }
                    productlisttext=productlisttext+"<td style='width:"+width+"'>"+tddivide[kk]+"</td>";
                }

            }
            productlisttext=productlisttext+"</table>";

        }


        personanme = (TextView) v.findViewById(R.id.personanme);
        personanme.setText(pname);
        mobilebike = (TextView) v.findViewById(R.id.mobilebike); //order no
        mobilebike.setText(mobile);
        callbackscheduled = (TextView) v.findViewById(R.id.callbackscheduled);  //amount
        callbackscheduled.setText(nextschedule);
        addedondate = (TextView) v.findViewById(R.id.addedondate); //orderdate time
        addedondate.setText(addedon);
        lastupdatedate=(TextView)v.findViewById(R.id.lastupdatedate);  //address
        lastupdatedate.setText(lastupdatedatetxt);

        WebView mywebview=(WebView)v.findViewById(R.id.mywebview);
        mywebview.loadData(productlisttext, "text/html", "utf-8");



        String myextracharges[]=extracharges.split("#");
        if(myextracharges[1]==null){myextracharges[1]="0";}
        if(myextracharges[2]==null){myextracharges[2]="0";}
        if(myextracharges[3]==null){myextracharges[3]="0";}

        TextView tv1_charge1=v.findViewById(R.id.deliverycharge);tv1_charge1.setText(myextracharges[0]);
        TextView tv1_charge2=v.findViewById(R.id.gstcharge);tv1_charge2.setText(myextracharges[1]);
        TextView tv1_charge3=v.findViewById(R.id.packingcharge);tv1_charge3.setText(myextracharges[2]);
        TextView tv1_charge4=v.findViewById(R.id.processing);tv1_charge4.setText(myextracharges[3]);



        helpcenter = (TextView) v.findViewById(R.id.helpcenter);
        helpcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment newFragment = new Helpadd();
                Bundle bundle = new Bundle();
                bundle.putString("orderid", mobile);
                newFragment.setArguments(bundle);
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("helpadd")
                        .commit();
            }
        });

        showBackButton();

        return v;
    }

    public void showBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
