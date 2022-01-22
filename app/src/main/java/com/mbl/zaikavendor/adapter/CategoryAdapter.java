package com.mbl.zaikavendor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.EventListener;

import com.mbl.zaikavendor.Fragment.Category;
import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;

/**
 * Created by sachin on 1/14/2019.
 */

public class CategoryAdapter extends ArrayAdapter<Actors> {
    ArrayList<Actors> actorList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    Fragment listener;

    public CategoryAdapter(Context context, int resource, ArrayList<Actors> objects,Fragment listener) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        actorList = objects;
        this.listener = listener;
    }

    public interface EventListener {
        void onEvent(int data, String btn);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.tvName = (TextView) v.findViewById(R.id.adapter_text_title);
            holder.editbtn = (Button) v.findViewById(R.id.editbtn);
            holder.deletebtn = (Button) v.findViewById(R.id.deletebtn);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        // new DownloadImageTask(holder.imageview).execute(actorList.get(position).getImage());
        holder.tvName.setText(actorList.get(position).getDescription());
        holder.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Category)listener).edititem(position);
            }
        });
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Category)listener).deleteit(position);
            }
        });
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
