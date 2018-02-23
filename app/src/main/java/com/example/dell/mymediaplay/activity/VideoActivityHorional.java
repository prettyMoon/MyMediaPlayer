package com.example.dell.mymediaplay.activity;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.dell.mymediaplay.R;
import com.example.dell.mymediaplay.tool.Video;

/**
 * Created by DELL on 2016/5/9.
 */
public class VideoActivityHorional extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

    private SurfaceView surface;
    private SurfaceHolder holder;
    private MediaPlayer player;
    private Video video;
    private ImageView img_play;
    private LinearLayout layout;
    private boolean layoutIsShow = true;
    private SeekBar mseekBar;
    private myThread tt;
    private TextView tv_time;
    private int second = 0;
    private boolean hasMoved = false;
    private int viewWidth = 0, viewHeight = 0;
    private boolean isPlaying = false;
    private boolean canUpdate = false, isthreadrunning = true;
    private boolean firstStart = true;
    private float startx = 0, starty = 0, endx = 0, endy = 0;
    public Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                if (canUpdate) {
                    int pp = (int) msg.obj;
                    mseekBar.setProgress(pp);
                    tv_time.setText(getTime(++second));
                }
            }
        }
    };

    public String getTime(int temp) {
        int s = temp % 60;
        int m = temp / 60;
        if (m >= 10) {
            if (s >= 10) {
                return m + ":" + s;
            } else {
                return m + ":" + "0" + s;
            }
        } else {
            if (s >= 10) {
                return "0" + m + ":" + s;
            } else {
                return "0" + m + ":" + "0" + s;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_horional);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏通知栏
        video = (Video) getIntent().getExtras().get("video");
        tt = new myThread();
        viewWidth = getWindowManager().getDefaultDisplay().getWidth();
        viewHeight = getWindowManager().getDefaultDisplay().getHeight();
        initView();
        initEvent();

    }

    private void initView() {
        tv_time = (TextView) this.findViewById(R.id.play_time);
        mseekBar = (SeekBar) this.findViewById(R.id.seek_progress);
        layout = (LinearLayout) this.findViewById(R.id.btn_layout);
        img_play = (ImageView) this.findViewById(R.id.horizonal_play);
        surface = (SurfaceView) this.findViewById(R.id.surfaceview_horional);
        holder = surface.getHolder();
        holder.addCallback(this);
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(video.getPath());
        String height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
        String width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽
        holder.setFixedSize((int) Float.parseFloat(width), (int) Float.parseFloat(height));
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        player = new MediaPlayer();
    }

    private void initEvent() {
        img_play.setOnClickListener(this);
        mseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int msec = mseekBar.getProgress() * player.getDuration() / 100;
                canUpdate = false;
                second = msec / 1000;
                canUpdate = true;
                player.seekTo(msec);
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                canUpdate = false;
            }
        });
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDisplay(holder);
        try {

            if (video.getPath() == null || video.getPath().equals("")) {
            } else {
                player.setDataSource("file://" + video.getPath());
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
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.horizonal_play:
                if (isPlaying) {
                    isPlaying = !isPlaying;
                    player.pause();
                    canUpdate = false;
                    img_play.setImageResource(R.drawable.play);
                } else {
                    isPlaying = !isPlaying;
                    canUpdate = true;
                    player.start();
                    if (firstStart) {
                        tt.start();
                        firstStart = false;
                    }
                    img_play.setImageResource(R.drawable.pause);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            startx = e.getX();
            starty = e.getY();
            System.out.println("###startx" + startx);
            System.out.println("###starty" + starty);
            if (layoutIsShow) {
                layoutIsShow = !layoutIsShow;
                layout.setVisibility(View.GONE);
            } else {

                layoutIsShow = !layoutIsShow;
                layout.setVisibility(View.VISIBLE);
            }
        }
        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            endx = e.getX();
            endy = e.getY();
            hasMoved = true;
            System.out.println("###endx" + endx);
            System.out.println("###endy" + endy);
        }
        if (e.getAction() == MotionEvent.ACTION_UP) {

            int changex = (int) -(startx - endx);
            int changey = (int) (starty - endy);
            System.out.println("###changex" + changex);
            System.out.println("###changey" + changey);
            if (hasMoved) {
                if (changex > 0) {
                    int progress = mseekBar.getProgress() + changex * (100 - mseekBar.getProgress()) / viewWidth;
                    int msec = player.getCurrentPosition() + changex * (player.getDuration() - player.getCurrentPosition()) / viewWidth;
                    if (progress >= 100 || msec > player.getDuration()) {
                        mseekBar.setProgress(100);
                        player.stop();
                    } else {
                        mseekBar.setProgress(progress);
                        if (msec > player.getDuration()) {
                            player.stop();
                        } else {
                            player.seekTo(msec);
                        }

                    }
                    second = mseekBar.getProgress() * player.getDuration() / 100000;
                    tv_time.setText(getTime(second));

                } else if (changex < 0) {
                    int progress = mseekBar.getProgress() + changex * (100 - mseekBar.getProgress()) / viewWidth;
                    int msec = player.getCurrentPosition() + changex * (player.getDuration() - player.getCurrentPosition()) / viewWidth;

                    if (progress < 0 || msec < 0) {
                        progress = 0;
                        mseekBar.setProgress(progress);
                        player.stop();
                    } else {
                        if (msec > 0) {
                            player.seekTo(msec);
                        } else {
                            player.stop();
                        }
                    }
                    if (msec == 0) {

                    } else {
                        if (second + (msec / 1000) < 0) {
                            second = 0;
                        } else {
                            second += msec / 1000;
                        }
                        tv_time.setText(getTime(second));
                    }
                }
                hasMoved = false;
            }
        }
        return super.onTouchEvent(e);
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
                msg.what = 0x123;
                msg.obj = (int) position;
                myhandler.sendMessage(msg);
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
