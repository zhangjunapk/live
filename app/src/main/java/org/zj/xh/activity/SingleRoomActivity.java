package org.zj.xh.activity;

import android.app.Activity;
import android.os.Bundle;

import org.zj.xh.R;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class SingleRoomActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.initialize(this);
        setContentView(R.layout.layout_live_room);
        VideoView videoView = (VideoView) findViewById(R.id.vitamio_videoView);
        String path="rtmp://202.69.69.180:443/webcast/bshdlive-pc";
        videoView.setVideoPath(path);
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //设置快进的倍速
                mediaPlayer.setPlaybackSpeed(1.0f);
                //设置缓冲大小
                mediaPlayer.setBufferSize(512 * 1024);
            }
        });
    }
}
