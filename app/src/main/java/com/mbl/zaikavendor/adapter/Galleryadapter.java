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

import com.mbl.zaikavendor.Cache;
import com.mbl.zaikavendor.Fragment.Gallery;
import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Anu on 3/15/2020.
 */

public class Galleryadapter extends ArrayAdapter<Actors> {
    ArrayList<Actors> actorList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    Fragment listener;

    public Galleryadapter(Context context, int resource, ArrayList<Actors> objects, Fragment listener) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        actorList = objects;
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.imageview = (ImageView) v.findViewById(R.id.gridimg);
            holder.deletebtn = (ImageView) v.findViewById(R.id.deletebtn);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        try {
            new DownloadImageTask(holder.imageview).execute(actorList.get(position).getImage());
        } catch (Exception e){}


        holder.deletebtn.setTag(position);
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mypos = Integer.valueOf(v.getTag().toString());
                ((Gallery)listener).deleteitem(mypos);
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
        public ImageView deletebtn;

    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap = (Bitmap) Cache.getInstance().getLru().get(urls[0]);
            Bitmap mIcon11 = null;
            if(bitmap!=null)
                mIcon11= bitmap;
            else
            {
                String urldisplay = urls[0];

                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                    Cache.getInstance().getLru().put(urls[0], mIcon11);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }

}
