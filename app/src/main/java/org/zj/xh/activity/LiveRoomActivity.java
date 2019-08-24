package org.zj.xh.activity;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.zj.xh.Constants;
import org.zj.xh.R;
import org.zj.xh.bean.MessageEventOpenLiveRoom;

import java.io.IOException;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class LiveRoomActivity extends Activity {
    private String roomId="";

    //  @BindView(R.id.sv_player)
    // SurfaceView surfaceView;
    private IjkMediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.initialize(this);
        setContentView(R.layout.layout_live_room);
        VideoView videoView = (VideoView) findViewById(R.id.vitamio_videoView);
        String path=Constants.PUSH_URL+Constants.currentRoomId;
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

    private void initPlayer() {
        createPlayer();
    }

    private void initExoplayer(){

       /* PlayerView playerView=this.findViewById(R.id.pv_view);

        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this);
        playerView.setPlayer(player);


        //创建一个DataSource对象，通过它来下载多媒体数据
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "yourApplicationName"));
        //这是一个代表将要被播放的媒体的MediaSource
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(Constants.PUSH_URL+roomId));
        //使用资源准备播放器
        player.prepare(videoSource);*/

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false, priority = 0)
    public void handleData(MessageEventOpenLiveRoom b){
        System.out.println("房间号"+b.getRoomId());
        roomId=b.getRoomId();
        // initExoplayer();
        //initPlayer();
        initVitamio();
    }

    private void initVitamio(){
        /*VideoView mVideoView;

        String path = "rtmp://202.69.69.180:443/webcast/bshdlive-pc";//这里写你自己的拉流地址
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        mVideoView = (VideoView) findViewById(R.id.vitamio_videoView);
        mVideoView.setVideoURI( Uri.parse(path));
        //mVideoView.setVideoURI(Uri.parse(path), options);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });
        mVideoView.start();*/

        /*if (!LibsChecker.checkVitamioLibs(this))
            return;
        VideoView mVideoView = (VideoView) findViewById(R.id.vitamio_videoView);
       String path = "rtmp://202.69.69.180:443/webcast/bshdlive-pc";
       Map options = new HashMap<>();
        options.put("rtmp_playpath", "");
        options.put("rtmp_swfurl", "");
        options.put("rtmp_live", "1");
        options.put("rtmp_pageurl", "");
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });
        mVideoView.setVideoURI(Uri.parse(path),options);

        System.out.println("开启了------------>");
      //c  mVideoView.start();
        boolean playing = mVideoView.isPlaying();

        System.out.println("是否在播放"+playing);*/

      /*  VideoView videoView=findViewById(R.id.vitamio_videoView);
        Vitamio.isInitialized(getApplication());
        videoView.setVideoURI(Uri.parse("rtmp://202.69.69.180:443/webcast/bshdlive-pc"));
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                *//*if (count > RETRY_TIMES) {
                    new AlertDialog.Builder(LiveActivity.this)
                            .setMessage("靠嫩妈")
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LiveActivity.this.finish();
                                }
                            }).setCancelable(false).show();
                } else {
                    videoView.stopPlayback();
                    videoView.setVideoURI(Uri.parse(url));
                    videoView.start();
                }
                count++;*//*
                return false;
            }
        });
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        Toast.makeText(getBaseContext(),"开始了，靠嫩妈",Toast.LENGTH_SHORT).show();
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        Toast.makeText(getBaseContext(),"下载，靠嫩妈",Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        videoView.start();*/


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

    private void createPlayer() {
        if (mPlayer == null) {
            mPlayer = new IjkMediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


            try {
                mPlayer.setDataSource(Constants.PUSH_URL+roomId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.prepareAsync();
            mPlayer.start();
        }
    }

    private void release() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        //我就不释放了
        // IjkMediaPlayer.native_profileEnd();
    }

/*    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            createPlayer();
            mPlayer.setDisplay(surfaceView.getHolder());
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (surfaceView != null) {
                surfaceView.getHolder().removeCallback(callback);
                surfaceView = null;
            }
        }
    };*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

}


