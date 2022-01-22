package com.mbl.zaikavendor.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;

import java.util.ArrayList;

/**
 * Created by Anu on 1/1/2020.
 */

public class Orderadapter  extends ArrayAdapter<Actors> {
    ArrayList<Actors> actorList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;
    Context mycontext;

    public Orderadapter(Context context, int resource, ArrayList<Actors> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        actorList = objects;
        mycontext = context;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.tvName = (TextView) v.findViewById(R.id.restaurentname);
            holder.tvDescription = (TextView) v.findViewById(R.id.address);
            holder.tvDOB = (TextView) v.findViewById(R.id.price);
            holder.tvwebview = (WebView) v.findViewById(R.id.web1);
            holder.tvHeight = (TextView) v.findViewById(R.id.orderdate);
            holder.tvCountry = (TextView) v.findViewById(R.id.orderstatus);

            holder.linearlayout=(LinearLayout)v.findViewById(R.id.linearlayout);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        // new DownloadImageTask(holder.imageview).execute(actorList.get(position).getImage());
        holder.tvName.setText(actorList.get(position).getName());
        holder.tvName.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/Roboto-Medium.ttf"));

        holder.tvDescription.setText(actorList.get(position).getImage());
        holder.tvDOB.setText("Rs. "+actorList.get(position).getSpouse());

        String productlisttext="<table style='width:100%'>";

        String fullsze[]=actorList.get(position).getString3().split("&&");
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
            // productlisttext=productlisttext+"</tr><tr><td colspan='3'>&nbsp;<br />&nbsp;<br /></td></tr>";

        }
        productlisttext=productlisttext+"</table>";
        WebView webView=new WebView(mycontext);
        webView.loadData(productlisttext, "text/html", "utf-8");
        holder.linearlayout.addView(webView);

        holder.tvHeight.setText(actorList.get(position).getCountry());
        holder.tvCountry.setText(actorList.get(position).getString1());

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
        public Button editbtn;
        public Button deletebtn;
        public WebView tvwebview;
        public LinearLayout linearlayout;

    }

}
