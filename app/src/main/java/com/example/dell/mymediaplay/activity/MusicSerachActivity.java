package com.example.dell.mymediaplay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dell.mymediaplay.R;
import com.example.dell.mymediaplay.adapter.MusicListViewAdapter;
import com.example.dell.mymediaplay.tool.MusicInfo;
import com.example.dell.mymediaplay.tool.MusicProvider;

import java.util.ArrayList;

/**
 * Created by DELL on 2016/5/22.
 */
public class MusicSerachActivity extends AppCompatActivity {


    private ListView list_music;
    private ArrayList<MusicInfo> mlist;
    private MusicListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_activity_serach);
        initView();
        initEvent();
    }

    private void initView() {
        mlist = new ArrayList<MusicInfo>();
        list_music = (ListView) this.findViewById(R.id.listview_music);
        mlist = MusicProvider.getMusicInfos(MusicSerachActivity.this);
        adapter = new MusicListViewAdapter(MusicSerachActivity.this, mlist);
        list_music.setAdapter(adapter);
    }

    private void initEvent() {
        list_music.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MusicSerachActivity.this, MusicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("music", mlist);
                bundle.putInt("current", position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


}
