package com.example.dell.mymediaplay.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dell.mymediaplay.R;
import com.example.dell.mymediaplay.adapter.VideoListViewAdapter;
import com.example.dell.mymediaplay.tool.AbstructProvider;
import com.example.dell.mymediaplay.tool.LoadedImage;
import com.example.dell.mymediaplay.tool.Video;
import com.example.dell.mymediaplay.tool.VideoProvider;

import java.util.ArrayList;

public class VideoSerachActivity extends AppCompatActivity {
    private ListView listView;
    private VideoListViewAdapter adapter;
    private ArrayList<Video> listvideos = new ArrayList<Video>();
    private int videoSize = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity_serach);
        listView = (ListView) this.findViewById(R.id.listview);
        AbstructProvider provider = new VideoProvider(VideoSerachActivity.this);
        listvideos = provider.getList();
        adapter = new VideoListViewAdapter(VideoSerachActivity.this, listvideos);
        videoSize = listvideos.size();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(VideoSerachActivity.this, VideoActivityHorional.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("video", listvideos.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        loadImages();
    }


    private void loadImages() {

        new LoadImagesFromSDCard().execute();

    }

    private void addImage(LoadedImage... value) {
        for (LoadedImage image : value) {
            adapter.addPhoto(image);
            adapter.notifyDataSetChanged();
        }
    }

    private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    class LoadImagesFromSDCard extends AsyncTask<Object, LoadedImage, Object> {
        @Override
        protected Object doInBackground(Object... params) {
            Bitmap bitmap = null;
            for (int i = 0; i < videoSize; i++) {
                bitmap = getVideoThumbnail(listvideos.get(i).getPath(), 200, 200, MediaStore.Images.Thumbnails.MICRO_KIND);
                if (bitmap != null) {
                    publishProgress(new LoadedImage(bitmap));
                }
            }
            return params;
        }

        @Override
        public void onProgressUpdate(LoadedImage... value) {
            addImage(value);
        }
    }
}
