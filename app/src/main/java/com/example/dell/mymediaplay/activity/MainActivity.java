package com.example.dell.mymediaplay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.dell.mymediaplay.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_video, btn_music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    public void initView() {
        btn_video = (Button) this.findViewById(R.id.btn_video);
        btn_music = (Button) this.findViewById(R.id.btn_music);
    }

    public void initEvent() {
        btn_video.setOnClickListener(this);
        btn_music.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_music:
                intent = new Intent(MainActivity.this, MusicSerachActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_video:
                intent = new Intent(MainActivity.this, VideoSerachActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
