package com.mbl.zaikavendor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;

import java.util.ArrayList;

/**
 * Created by Anu on 1/28/2020.
 */

public class Notificationadapter  extends ArrayAdapter<Actors> {
    ArrayList<Actors> actorList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public Notificationadapter(Context context, int resource, ArrayList<Actors> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        actorList = objects;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.tvName = (TextView) v.findViewById(R.id.adapter_text_title);
            holder.tvDOB = (TextView) v.findViewById(R.id.editbtn);
            holder.tvDescription = (TextView) v.findViewById(R.id.details);
            holder.tvCountry = (TextView) v.findViewById(R.id.trno);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        // new DownloadImageTask(holder.imageview).execute(actorList.get(position).getImage());
        holder.tvName.setText(actorList.get(position).getHeight());
        holder.tvDOB.setText(actorList.get(position).getName());
        holder.tvDescription.setText(actorList.get(position).getCountry());
        holder.tvCountry.setText(actorList.get(position).getSpouse());

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

    }
}
