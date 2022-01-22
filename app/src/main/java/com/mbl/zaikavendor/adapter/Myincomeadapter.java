package com.mbl.zaikavendor.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;

/**
 * Created by Anu on 4/28/2020.
 */

public class Myincomeadapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public List<Actors> mItemList;

    public FragmentManager myacActivity;

    public Myincomeadapter(List<Actors> itemList,FragmentManager activity) {

        mItemList = itemList;
        myacActivity=activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_item, parent, false);
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
        TextView address,price,myorderid,orderdate,orderstatus,deliverytime;
        LinearLayout ttopcard;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItem = itemView.findViewById(R.id.personanme);
            address = itemView.findViewById(R.id.orderid);
            price = itemView.findViewById(R.id.orderdate);
            myorderid = itemView.findViewById(R.id.ordertotal);
            orderstatus = itemView.findViewById(R.id.myamount1);
            orderdate = itemView.findViewById(R.id.myamount2);
            deliverytime = itemView.findViewById(R.id.myamount3);

            ttopcard = itemView.findViewById(R.id.ttopcard);

        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        viewHolder.price.setText(mItemList.get(position).getHeight());
        viewHolder.tvItem.setText(mItemList.get(position).getName());

        viewHolder.address.setText(mItemList.get(position).getCountry());
        viewHolder.myorderid.setText(mItemList.get(position).getSpouse());

        viewHolder.orderstatus.setText(mItemList.get(position).getString1());
        viewHolder.orderdate.setText(mItemList.get(position).getImage());
        viewHolder.deliverytime.setText( mItemList.get(position).getString3());

      /*  String productlisttext="<table style='width:100%'>";
        String fullsze[]=mItemList.get(position).getString3().split("&&");
        for(int k=0;k<fullsze.length;k++){
            productlisttext=productlisttext+"<tr style='width:100%'>";
            String tddivide[]=fullsze[k].split("#");
            for(int kk=1;kk<tddivide.length;kk++){
                String width="20%";
                if(kk==1){
                    width="58%";
                }
                productlisttext=productlisttext+"<td style='width:"+width+"'>"+tddivide[kk]+"</td>";
            }

        }
        productlisttext=productlisttext+"</table>";

        viewHolder.webview.loadData(productlisttext, "text/html", "utf-8"); */


    }


}
