package com.example.dell.mymediaplay.activity;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.dell.mymediaplay.R;
import com.example.dell.mymediaplay.tool.MusicInfo;

import java.io.IOException;
import java.util.ArrayList;

public class MusicActivity extends Activity implements View.OnClickListener, SurfaceHolder.Callback {
    private SurfaceView surfaceView;
    private ImageView iv_back, iv_play, iv_forward, iv_music_back, iv_to_show;
    private TextView tv_title, tv_playtime;
    private MediaPlayer player;
    private SurfaceHolder surfaceholder;
    private ArrayList<MusicInfo> minfo;
    private SeekBar seekbar;
    private RotateAnimation myanimation;
    private int second = 0, current = 0, position = 0;
    private myThread tt;
    private boolean isplaying = false, canUpdate = false, isFirstPlay = true, isthreadrunning = true;
    private Handler mymusichandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1234) {
                if (canUpdate) {
                    int pp = (int) msg.obj;
                    seekbar.setProgress(pp);
                    int s = (++second) % 60;
                    int m = second / 60;
                    if (m >= 10) {
                        if (s >= 10) {
                            tv_playtime.setText(m + ":" + s);
                        } else {
                            tv_playtime.setText(m + ":" + "0" + s);
                        }
                    } else {
                        if (s >= 10) {
                            tv_playtime.setText("0" + m + ":" + s);
                        } else {
                            tv_playtime.setText("0" + m + ":" + "0" + s);
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video);
        current = getIntent().getExtras().getInt("current");
        position = current;
        minfo = (ArrayList<MusicInfo>) getIntent().getExtras().get("music");
        tt = new myThread();
        initView();
        initEvent();
        myanimation = getAnimation();
        iv_to_show.startAnimation(myanimation);
//        SystemClock.sleep(1000);
//        iv_play.setImageResource(R.drawable.pause);
//        player.start();
    }

    public void initView() {
        player = new MediaPlayer();
        iv_to_show = (ImageView) this.findViewById(R.id.iv_to_show);
        iv_music_back = (ImageView) this.findViewById(R.id.music_iv_back);
        tv_title = (TextView) this.findViewById(R.id.tv_music_title);
        tv_title.setText(minfo.get(current).getTitle());
        iv_music_back = (ImageView) this.findViewById(R.id.iv_back);
        tv_playtime = (TextView) this.findViewById(R.id.mplay_time);
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceview);
        iv_play = (ImageView) this.findViewById(R.id.iv_play);
        iv_back = (ImageView) this.findViewById(R.id.iv_back);
        iv_forward = (ImageView) this.findViewById(R.id.iv_forward);
        seekbar = (SeekBar) this.findViewById(R.id.mseek_progress);

        surfaceholder = surfaceView.getHolder();
        surfaceholder.addCallback(this);
        surfaceholder.setFixedSize(320, 220);
        surfaceholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void initEvent() {
        iv_play.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_forward.setOnClickListener(this);
        iv_music_back.setOnClickListener(this);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int msec = seekbar.getProgress() * player.getDuration() / 100;
                canUpdate = false;
                second = msec / 1000;
                player.seekTo(msec);
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                canUpdate = false;
                iv_play.setImageResource(R.drawable.play);
                isplaying = false;
                isFirstPlay = true;
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDisplay(surfaceholder);
        try {

            if (minfo.get(position).getUrl() == null || minfo.get(position).getUrl().equals("")) {

            } else {
                player.setDataSource("file://" + minfo.get(position).getUrl());
            }
            player.prepare();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play:
                if (isplaying) {
                    canUpdate = false;
                    isplaying = !isplaying;
                    player.pause();
                    iv_play.setImageResource(R.drawable.play);
                } else {
                    isplaying = !isplaying;
                    canUpdate = true;
                    player.start();
                    if (isFirstPlay) {
                        tt.start();
                        isFirstPlay = !isFirstPlay;
                    }
                    iv_play.setImageResource(R.drawable.pause);
                }
                break;
            case R.id.music_iv_back:
                isthreadrunning = false;
                SystemClock.sleep(1000);
                if (player.isPlaying()) {
                    player.stop();
                }
                player.release();
                finish();
                break;
            case R.id.iv_back:
                lastMusic();
                break;
            case R.id.iv_forward:
                nextMusic();
                break;
            default:
                break;
        }
    }

    public RotateAnimation getAnimation() {
        RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(30000);
        animation.setRepeatCount(20);
        animation.setInterpolator(new LinearInterpolator());
        animation.setFillAfter(true);
        return animation;
    }

    public void nextMusic() {
        goBack();
        player.stop();
        iv_play.setImageResource(R.drawable.play);
        player.reset();
        try {
            player.setDataSource("file://" + minfo.get((++position) % minfo.size()).getUrl());
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tv_title.setText(minfo.get((position) % minfo.size()).getTitle());
        player.start();
        tv_playtime.setText("00:00");
        canUpdate = true;
        isplaying = true;
        iv_play.setImageResource(R.drawable.pause);
    }

    public void lastMusic() {
        goBack();
        player.stop();
        iv_play.setImageResource(R.drawable.play);
        player.reset();
        try {
            if (--position < 0) {
                position = minfo.size() - 1;
            }
            player.setDataSource("file://" + minfo.get(position).getUrl());
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tv_title.setText(minfo.get((position) % minfo.size()).getTitle());
        player.start();
        tv_playtime.setText("00:00");
        canUpdate = true;
        isplaying = true;
        iv_play.setImageResource(R.drawable.pause);
    }

    public void goBack() {
        second = 0;
        isplaying = false;
        canUpdate = false;
        seekbar.setProgress(0);
    }

    class myThread extends Thread {
        @Override
        public void run() {
            while (isthreadrunning) {
                super.run();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                double current = player.getCurrentPosition();
                double duration = player.getDuration();
                double position = (current / duration) * 100;
                Message msg = new Message();
                msg.what = 0x1234;
                msg.obj = (int) position;
                mymusichandler.sendMessage(msg);
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isthreadrunning = false;
        SystemClock.sleep(1000);
        if (player.isPlaying()) {
            player.stop();
        }
        player.release();
        finish();
    }
}
