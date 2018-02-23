package com.example.dell.mymediaplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell.mymediaplay.R;
import com.example.dell.mymediaplay.tool.MusicInfo;

import java.util.ArrayList;

/**
 * Created by DELL on 2016/5/22.
 */
public class MusicListViewAdapter extends BaseAdapter {
    private ArrayList<MusicInfo> datalist=new ArrayList<MusicInfo>();
    private LayoutInflater inflater;
    private Context context;

    public MusicListViewAdapter(Context context, ArrayList<MusicInfo> datalist) {
        this.datalist = datalist;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datalist.size();
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
        ViewHolder holder = new ViewHolder();
        convertView = inflater.inflate(R.layout.music_listitem, null);
        holder.tv_title = (TextView) convertView.findViewById(R.id.music_title);
        holder.tv_artist = (TextView) convertView.findViewById(R.id.music_artist);
        holder.tv_title.setText(datalist.get(position).getTitle());
        holder.tv_artist.setText(datalist.get(position).getArtist());
        return convertView;
    }

    class ViewHolder {
        public TextView tv_title, tv_artist;

        public ViewHolder() {
        }
    }
}
