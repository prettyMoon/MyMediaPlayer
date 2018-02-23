package com.example.dell.mymediaplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.mymediaplay.R;
import com.example.dell.mymediaplay.tool.LoadedImage;
import com.example.dell.mymediaplay.tool.Video;

import java.util.ArrayList;

public class VideoListViewAdapter extends BaseAdapter {

    private ArrayList<Video> list_media = new ArrayList<Video>();
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<LoadedImage> photos = new ArrayList<LoadedImage>();

    public VideoListViewAdapter(Context context, ArrayList<Video> list_media) {
        this.list_media = list_media;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void addPhoto(LoadedImage img) {
        photos.add(img);
    }


    @Override
    public int getCount() {
        return list_media.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.video_listitem, null);
        ViewHolder holder = new ViewHolder(convertView);
        holder.tv_name.setText(list_media.get(position).getTitle());
        long ms = list_media.get(position).getDuration();
        int min = (int) ((ms / 1000) / 60);
        int sec = (int) (ms / 1000) % 60;
        holder.tv_time.setText(min + "分钟" + sec + "秒");
        if (photos.size() > position) {
            holder.img.setImageBitmap(photos.get(position).getBitmap());
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView img;
        public TextView tv_name, tv_time;

        public ViewHolder(View view) {
            img = (ImageView) view.findViewById(R.id.video_img);
            tv_name = (TextView) view.findViewById(R.id.video_name);
            tv_time = (TextView) view.findViewById(R.id.video_time);
        }
    }
}
