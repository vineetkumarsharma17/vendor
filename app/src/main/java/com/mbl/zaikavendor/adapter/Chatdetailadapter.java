package com.mbl.zaikavendor.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import com.mbl.zaikavendor.Cache;
import com.mbl.zaikavendor.Model.Actors;
import com.mbl.zaikavendor.R;
import com.mbl.zaikavendor.Welcome;
import com.mbl.zaikavendor.app.EndPoints;

/**
 * Created by Anu on 4/17/2020.
 */

public class Chatdetailadapter extends ArrayAdapter<Actors> {
    ArrayList<Actors> actorList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;
    Context mycontext;

    Fragment myactivity;
    public Chatdetailadapter(Context context, int resource, ArrayList<Actors> objects,Fragment activity) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        actorList = objects;
        mycontext=context;
        myactivity = activity;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) mycontext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int viewType = getItemViewType(position);
        int layoutResource = 0; // determined by view type
        Log.e("errorpos","ssaa"+viewType);
        if(viewType==0){
            // mymsg
            layoutResource = R.layout.my_message;
        } else{
            layoutResource = R.layout.their_message;
        }
        if (v == null) {
            v = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();

            //v = vi.inflate(layoutResource, null);

        //    holder.imageview = (ImageView) v.findViewById(R.id.iv_overlay);

            holder.tvName = (TextView) v.findViewById(R.id.message_body);
            holder.Media = (ImageView) v.findViewById(R.id.msgbigimg);
            holder.msgtiming = (TextView) v.findViewById(R.id.msgtiming);

           // holder.onlineoffline = (TextView) v.findViewById(R.id.onlinestatus);
          //  holder.country = (TextView) v.findViewById(R.id.country);


            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        // new DownloadImageTask(holder.imageview).execute(actorList.get(position).getImage());
        holder.tvName.setText(actorList.get(position).getName());
        holder.msgtiming.setText(actorList.get(position).getCountry());
        holder.Media.setVisibility(View.GONE);
        if(actorList.get(position).getImage().length()>8){
            if(actorList.get(position).getString1().equals("2")) {
                new DownloadImageTask(holder.Media).execute(EndPoints.BASE_URL2 + actorList.get(position).getImage());
            } else{
               // new DownloadImageTask(holder.Media).execute(EndPoints.BASE_URL2 + actorList.get(position).getImage());
                holder.Media.setImageBitmap(StringToBitMap(actorList.get(position).getImage()));

            }
            holder.Media.setVisibility(View.VISIBLE);
        }

        return v;

    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    @Override
    public int getItemViewType(int position) {
       String chatMessage = actorList.get(position).getDescription();

        if (chatMessage.equals("1")) {
            return 0;
        } else {
            return 1;
        }
    }
    static class ViewHolder {
        public ImageView imageview;
        public TextView tvName;
        public TextView onlineoffline;
        public TextView country;
        public TextView code;
        public TextView msgtiming;
        public ImageView Media;

    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap bitmap = (Bitmap) Cache.getInstance().getLru().get(urls[0]);
            Bitmap mIcon11 = null;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if(bitmap!=null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 0, out);
                mIcon11 = bitmap;
            }
            else {
                String urldisplay = urls[0];

                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                    mIcon11.compress(Bitmap.CompressFormat.JPEG, 50, out);
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
