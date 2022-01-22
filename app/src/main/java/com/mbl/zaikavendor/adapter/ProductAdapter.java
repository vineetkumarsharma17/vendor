package com.mbl.zaikavendor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.mbl.zaikavendor.Fragment.Medicatedlist;
import com.mbl.zaikavendor.Fragment.Partylist;
import com.mbl.zaikavendor.Fragment.Products;
import com.mbl.zaikavendor.Fragment.Tiffinlist;
import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;

/**
 * Created by sachin on 1/14/2019.
 */

public class ProductAdapter extends ArrayAdapter<Actors> {
    ArrayList<Actors> actorList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;
    Fragment pfragment;
    public ProductAdapter(Context context, int resource, ArrayList<Actors> objects,Fragment myfrag) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        actorList = objects;
        pfragment=myfrag;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);

            holder.tvName = (TextView) v.findViewById(R.id.adapter_text_title);
            holder.tvDescription = (TextView) v.findViewById(R.id.price);
            holder.tvwebcview = (WebView) v.findViewById(R.id.webview);
            holder.tvDOB = (TextView) v.findViewById(R.id.adapter_text_desc);
            holder.tvCountry = (TextView) v.findViewById(R.id.rating);

            holder.tvedit = (TextView) v.findViewById(R.id.editetext);
            holder.tvdelete = (TextView) v.findViewById(R.id.deletetext);
            holder.enabledisbale = (TextView) v.findViewById(R.id.enabledisable);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        // new DownloadImageTask(holder.imageview).execute(actorList.get(position).getImage());
        holder.tvName.setText(actorList.get(position).getName());
        holder.tvDescription.setText(actorList.get(position).getDob());
        holder.tvwebcview.loadData(actorList.get(position).getString3(), "text/html", "utf-8");
        holder.tvDOB.setText(actorList.get(position).getString1());
        holder.tvCountry.setText(actorList.get(position).getString2());

        if(actorList.get(position).getString6().equals("0")){
            holder.enabledisbale.setText("Disable");
            holder.enabledisbale.setTextColor(Color.parseColor("#dd0000"));
        } else{
            holder.enabledisbale.setText("Enable");
            holder.enabledisbale.setTextColor(Color.parseColor("#00dd00"));
        }

        holder.tvedit.setTag(position);
        holder.tvedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actorList.get(position).getString5().equals("Basic")) {
                    ((Products) pfragment).editit(position);
                } else  if(actorList.get(position).getString5().equals("Medicated")) {
                    ((Medicatedlist) pfragment).editit(position);
                } else  if(actorList.get(position).getString5().equals("Party")) {
                    ((Partylist) pfragment).editit(position);
                } else  if(actorList.get(position).getString5().equals("Tiffin")) {
                    ((Tiffinlist) pfragment).editit(position);
                } else{}
            }
        });

        holder.tvdelete.setTag(position);
        holder.tvdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actorList.get(position).getString5().equals("Basic")) {
                    ((Products) pfragment).deleteit(position);
                } else  if(actorList.get(position).getString5().equals("Medicated")) {
                    ((Medicatedlist) pfragment).deleteit(position);
                } else  if(actorList.get(position).getString5().equals("Party")) {
                    ((Partylist) pfragment).deleteit(position);
                } else  if(actorList.get(position).getString5().equals("Tiffin")) {
                    ((Tiffinlist) pfragment).deleteit(position);
                } else{}
            }
        });


        holder.enabledisbale.setTag(position);
        holder.enabledisbale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actorList.get(position).getString5().equals("Basic")) {
                    ((Products) pfragment).enabledisbale(position);
                } else  if(actorList.get(position).getString5().equals("Medicated")) {
                    ((Medicatedlist) pfragment).enabledisbale(position);
                } else  if(actorList.get(position).getString5().equals("Party")) {
                    ((Partylist) pfragment).enabledisbale(position);
                } else  if(actorList.get(position).getString5().equals("Tiffin")) {
                    ((Tiffinlist) pfragment).enabledisbale(position);
                } else{}
            }
        });

        //holder.tvChildren.setPaintFlags(holder.tvChildren.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
/*
        holder.wishlisticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player player1 = new Player(1, actorList.get(position).getName(), actorList.get(position).getImage(),actorList.get(position).getDescription(),actorList.get(position).getDob(),"",actorList.get(position).getCountry(),"1" ,203);
                db.addPlayer7(player1);
                Toast.makeText(getContext(), "Added To Wish List", Toast.LENGTH_SHORT).show();

            }
        });  */
        return v;

    }

    static class ViewHolder {
        public ImageView imageview;
        public TextView tvName;
        public TextView tvDescription;
        public TextView tvDOB;
        public TextView tvCountry;
        public TextView tvHeight;
        public TextView tvSpouse;
        public TextView tvChildren;
        public WebView tvwebcview;

        public TextView tvedit;
        public TextView tvdelete;
        public TextView enabledisbale;

        public ImageView wishlisticon;

    }

}
