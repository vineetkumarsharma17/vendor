package com.mbl.zaikavendor.adapter;

/**
 * Created by Anu on 2/11/2020.
 */

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mbl.zaikavendor.Fragment.Orderdetail;
import com.mbl.zaikavendor.Fragment.Orderdetails;
import com.mbl.zaikavendor.Fragment.Tiffin_details;
import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<Actors> mItemList;

    public FragmentManager myacActivity;

    public RecyclerViewAdapter(List<Actors> itemList,FragmentManager activity) {

        mItemList = itemList;
        myacActivity=activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;
        TextView address,price,myorderid,orderdate,orderstatus;
        LinearLayout ttopcard;
        WebView webview;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItem = itemView.findViewById(R.id.restaurentname);
            address = itemView.findViewById(R.id.address);
            price = itemView.findViewById(R.id.price);
            myorderid = itemView.findViewById(R.id.myorderid);
            ttopcard = itemView.findViewById(R.id.ttopcard);
            webview = itemView.findViewById(R.id.web1);
            orderdate = itemView.findViewById(R.id.orderdate);
            orderstatus = itemView.findViewById(R.id.orderstatus);

        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        viewHolder.tvItem.setText(mItemList.get(position).getName());
        viewHolder.address.setText(mItemList.get(position).getImage());
        viewHolder.price.setText("Rs. "+mItemList.get(position).getSpouse());
        viewHolder.myorderid.setText("#"+mItemList.get(position).getHeight());

        String productlisttext="<table style='width:100%'>";
        String fullsze[]=mItemList.get(position).getString3().split("&&");
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

        viewHolder.webview.loadData(productlisttext, "text/html", "utf-8");
        viewHolder.orderdate.setText(mItemList.get(position).getCountry());
        viewHolder.orderstatus.setText(mItemList.get(position).getString1());

        viewHolder.ttopcard.setTag(position);
        viewHolder.ttopcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mypos=Integer.valueOf(v.getTag().toString());
                FragmentManager fm= myacActivity;
                Fragment newFragment = new Orderdetail();
                if (mItemList.get(mypos).getString7().equals("tiffin")) {
                    newFragment = new Tiffin_details();
                }
                if (mItemList.get(mypos).getString7().equals("party")) {
                    newFragment = new Orderdetails();
                }
                Bundle bundle = new Bundle();
                bundle.putString("menuid",mItemList.get(mypos).getChildren());
                bundle.putString("pid", mItemList.get(mypos).getHeight());
                bundle.putString("pname", mItemList.get(mypos).getName());
                bundle.putString("details",mItemList.get(mypos).getCountry());
                bundle.putString("price", mItemList.get(mypos).getSpouse());
                bundle.putString("sfrom", mItemList.get(mypos).getString1());
                bundle.putString("sto", mItemList.get(mypos).getString2());
                bundle.putString("servesize", mItemList.get(mypos).getString3());
                bundle.putString("address", mItemList.get(mypos).getImage());
                bundle.putString("mobileno", mItemList.get(mypos).getString4());
                bundle.putString("extracharge", mItemList.get(mypos).getString5());

                //Toast.makeText(getActivity(),actorsList.get(position).getSpouse() , Toast.LENGTH_LONG).show();
                newFragment.setArguments(bundle);
                fm
                        .beginTransaction()
                        .replace(R.id.container_body, newFragment)
                        .addToBackStack(null)
                        .addToBackStack("tiffindetails")
                        .commit();
            }
        });

        if(mItemList.get(position).getString1().toLowerCase().equals("placed")){
            viewHolder.orderstatus.setBackgroundColor(Color.parseColor("#229922"));
        } else if(mItemList.get(position).getString1().toLowerCase().equals("accepted")){
            viewHolder.orderstatus.setBackgroundColor(Color.parseColor("#999922"));
        } else if(mItemList.get(position).getString1().toLowerCase().equals("delivered")){
            viewHolder.orderstatus.setBackgroundColor(Color.parseColor("#222299"));
        } else if(mItemList.get(position).getString1().toLowerCase().equals("cancelled")){
            viewHolder.orderstatus.setBackgroundColor(Color.parseColor("#dd1100"));
        } else{

        }





    }


}
