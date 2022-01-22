package com.mbl.zaikavendor.adapter;

/**
 * Created by Anu on 2/11/2020.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mbl.zaikavendor.Fragment.Myoffers;
import com.mbl.zaikavendor.Fragment.Orderdetail;
import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;

import java.util.List;

public class Myofferadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<Actors> mItemList;

    public FragmentManager myacActivity;
    public Fragment myfrangemtn;

    public Myofferadapter(List<Actors> itemList,FragmentManager activity,Fragment thisfragment) {

        mItemList = itemList;
        myacActivity=activity;
        myfrangemtn = thisfragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myofferitem, parent, false);
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
        ImageView imageview;
        Button acbtn;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageview=  itemView.findViewById(R.id.imageview);
            ttopcard = itemView.findViewById(R.id.ttopcard);
            webview = itemView.findViewById(R.id.webview);
            acbtn = itemView.findViewById(R.id.acbtn);

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


        viewHolder.webview.loadData(mItemList.get(position).getCountry(), "text/html", "utf-8");
        viewHolder.acbtn.setText(mItemList.get(position).getSpouse());


        viewHolder.acbtn.setTag(position);
        viewHolder.acbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mypos=Integer.valueOf(v.getTag().toString());
                if(mItemList.get(mypos).getSpouse().equals("Accept")) {
                    ((Myoffers) myfrangemtn).acceptbtn(mItemList.get(mypos).getChildren());
                } else{
                    ((Myoffers) myfrangemtn).deletebtn(mItemList.get(mypos).getChildren());
                }

            }
        });

    }


}
