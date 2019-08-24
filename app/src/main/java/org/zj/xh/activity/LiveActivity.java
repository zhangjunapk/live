/*
package org.zj.xh.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.squareup.otto.Subscribe;

import org.easydarwin.bus.StreamStat;
import org.easydarwin.bus.SupportResolution;
import org.easydarwin.easypusher.util.Config;
import org.easydarwin.easypusher.util.SPUtil;
import org.easydarwin.util.Util;
import org.zj.xh.BuildConfig;
import org.zj.xh.R;
import org.zj.xh.easyRtmp.BackgroundCameraService;
import org.zj.xh.easyRtmp.RecordService;
import org.zj.xh.easyRtmp.easypusher.PushCallback;
import org.zj.xh.easyRtmp.push.MediaStream;
import org.zj.xh.util.BUSUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static org.zj.xh.Constants.REQUEST_OVERLAY_PERMISSION;


*/
/**
 * 直播的界面,这里需要一个surface,三个按钮就行了 推流摄像头/屏幕/关闭房间
 *//*

public class LiveActivity extends Activity implements BottomNavigationBar.OnTabSelectedListener,TextureView.SurfaceTextureListener{


    public static Intent mResultIntent;
   public static int mResultCode;

    @Override
    public void onTabSelected(int position) {

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.live_camera:
                //点击这个按钮就跳转到直播的页面
                //直播屏幕
                onStartOrStopPush();
                break;
            case R.id.live_screen:
                //直播屏幕
                onPushScreen();
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    static final String TAG = "StreamActivity";

    public static final int REQUEST_MEDIA_PROJECTION = 1002;
    public static final int REQUEST_CAMERA_PERMISSION = 1003;
    public static final int REQUEST_STORAGE_PERMISSION = 1004;

    // 默认分辨率
    int width = 1280, height = 720;

    TextView txtStreamAddress;
    ImageView btnSwitchCemera;
    Spinner spnResolution;
    TextView txtStatus, streamStat;
    TextView textRecordTick;

    List<String> listResolution = new ArrayList<>();

    MediaStream mMediaStream;


    private BackgroundCameraService mService;
    private ServiceConnection conn;

    private boolean mNeedGrantedPermission;

    private static final String STATE = "state";
    private static final int MSG_STATE = 1;

    public static long mRecordingBegin;
    public static boolean mRecording;

    private long mExitTime;//声明一个long类型变量：用于存放上一点击“返回键”的时刻


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_STATE:
                    String state = msg.getData().getString("state");
                    txtStatus.setText(state);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 全屏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_live);

        BUSUtil.BUS.register(this);

        notifyAboutColorChange();

        // 动态获取camera和audio权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, REQUEST_CAMERA_PERMISSION);
            mNeedGrantedPermission = true;
            return;
        } else {
            // resume
        }
        goonWithPermissionGranted();
    }

    @Override
    protected void onPause() {
        if (!mNeedGrantedPermission) {
            unbindService(conn);
            handler.removeCallbacksAndMessages(null);
        }

        boolean isStreaming = mMediaStream != null && mMediaStream.isStreaming();

        if (mMediaStream != null) {
            mMediaStream.stopPreview();

            if (isStreaming && SPUtil.getEnableBackgroundCamera(this)) {
                mService.activePreview();
            } else {
                mMediaStream.stopStream();
                mMediaStream.release();
                mMediaStream = null;

                stopService(new Intent(this, BackgroundCameraService.class));
            }
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mNeedGrantedPermission) {
            goonWithPermissionGranted();
        }
    }

    @Override
    protected void onDestroy() {
        BUSUtil.BUS.unregister(this);
        super.onDestroy();
    }

    */
/*
     * android6.0权限，onRequestPermissionsResult回调
     * *//*

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mNeedGrantedPermission = false;
                    goonWithPermissionGranted();
                } else {
                    finish();
                }

                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode == RESULT_OK) {
                Log.e(TAG, "get capture permission success!");

                mResultCode = resultCode;
                mResultIntent = data;

                startScreenPushIntent();
            }
        }
    }

    */
/*
     * 推送屏幕
     * *//*

    private void startScreenPushIntent() {
        if (LiveActivity.mResultIntent != null && LiveActivity.mResultCode != 0) {
            Intent intent = new Intent(getApplicationContext(), RecordService.class);
            startService(intent);
            Toast.makeText(getApplicationContext(),"推流屏幕中",Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 2.创建屏幕捕捉的Intent
                MediaProjectionManager mMpMngr = (MediaProjectionManager) getApplicationContext().getSystemService(MEDIA_PROJECTION_SERVICE);
                startActivityForResult(mMpMngr.createScreenCaptureIntent(), LiveActivity.REQUEST_MEDIA_PROJECTION);
            }
        }
    }

    private void goonWithPermissionGranted() {
       */
/* spnResolution = findViewById(R.id.spn_resolution);
        streamStat = findViewById(R.id.stream_stat);
        txtStatus = findViewById(R.id.txt_stream_status);
        btnSwitchCemera = findViewById(R.id.btn_switchCamera);
        txtStreamAddress = findViewById(R.id.txt_stream_address);
        textRecordTick = findViewById(R.id.tv_start_record);*//*

        final TextureView surfaceView = findViewById(R.id.sv_surfaceview);
      */
/*  View pushScreen = findViewById(R.id.push_screen_container);
        ImageView push_screen = findViewById(R.id.streaming_activity_push_screen);*//*


        //streamStat.setText(null);
    //    btnSwitchCemera.setOnClickListener(this);
        surfaceView.setSurfaceTextureListener(this);
     //   surfaceView.setOnClickListener(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
          //  pushScreen.setVisibility(View.GONE);
        }

        if (RecordService.mEasyPusher != null) {
            //push_screen.setImageResource(R.drawable.push_screen_click);
           // TextView viewById = findViewById(R.id.push_screen_url);
          //  viewById.setText(Config.getServerURL(this));
        }
        // create background service for background use.
        Intent intent = new Intent(this, BackgroundCameraService.class);
        startService(intent);
        System.out.println(" 我开始new一个服务监听");
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mService = ((BackgroundCameraService.LocalBinder) iBinder).getService();
                System.out.println("后台摄像头服务是否为空"+mService==null);
                System.out.println("surfaceView是否可用"+surfaceView.isAvailable());
                if (surfaceView.isAvailable()) {
                    goonWithAvailableTexture(surfaceView.getSurfaceTexture());
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                System.out.println("服务断开连接");
            }

            @Override
            public void onNullBinding(ComponentName name) {
                System.out.println("没有绑定");
            }

            @Override
            public void onBindingDied(ComponentName name) {
                System.out.println("死绑");
            }
        };
        boolean b = bindService(new Intent(getApplicationContext(), BackgroundCameraService.class), conn, 0);
        System.out.println("绑定服务是否成功"+b);

        if (mRecording) {
            //textRecordTick.setVisibility(View.VISIBLE);
           // textRecordTick.removeCallbacks(mRecordTickRunnable);
          //  textRecordTick.post(mRecordTickRunnable);
        } else {
            //textRecordTick.setVisibility(View.INVISIBLE);
          //  textRecordTick.removeCallbacks(mRecordTickRunnable);
        }
    }

    */
/*
     * 显示key有效期
     * *//*

    private void notifyAboutColorChange() {*/
/*
        ImageView iv = findViewById(R.id.toolbar_about);

        if (EasyApplication.activeDays >= 9999) {
            iv.setImageResource(R.drawable.green);
        } else if (EasyApplication.activeDays > 0) {
            iv.setImageResource(R.drawable.yellow);
        } else {
            iv.setImageResource(R.drawable.red);
        }*//*

    }

    */
/*
     * 初始化MediaStream
     * *//*

    private void goonWithAvailableTexture(SurfaceTexture surface) {
        final File easyPusher = new File(Config.recordPath());
        easyPusher.mkdir();

        MediaStream ms = mService.getMediaStream();

        if (ms != null) { // switch from background to front
            ms.stopPreview();
            mService.inActivePreview();
            ms.setSurfaceTexture(surface);
            ms.startPreview();

            mMediaStream = ms;

            if (ms.isStreaming()) {
                String url = Config.getServerURL(this);
                txtStreamAddress.setText(url);

                sendMessage("推流中");

               */
/* ImageView startPush = findViewById(R.id.streaming_activity_push);
                startPush.setImageResource(R.drawable.start_push_pressed);*//*

            }

            if (ms.getDisplayRotationDegree() != getDisplayRotationDegree()) {
                int orientation = getRequestedOrientation();

                if (orientation == SCREEN_ORIENTATION_UNSPECIFIED || orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        } else {
            boolean enableVideo = SPUtil.getEnableVideo(this);

            ms = new MediaStream(getApplicationContext(), surface, enableVideo);
            ms.setRecordPath(easyPusher.getPath());
            mMediaStream = ms;

            startCamera();

            mService.setMediaStream(ms);
        }
    }

    private void startCamera() {
        mMediaStream.updateResolution(width, height);
        mMediaStream.setDisplayRotationDegree(getDisplayRotationDegree());
        mMediaStream.createCamera();
        mMediaStream.startPreview();

        if (mMediaStream.isStreaming()) {
            sendMessage("推流中");
            txtStreamAddress.setText(Config.getServerURL(this));
        }
    }

    // 屏幕的角度
    private int getDisplayRotationDegree() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break; // Natural orientation
            case Surface.ROTATION_90:
                degrees = 90;
                break; // Landscape left
            case Surface.ROTATION_180:
                degrees = 180;
                break;// Upside down
            case Surface.ROTATION_270:
                degrees = 270;
                break;// Landscape right
        }

        return degrees;
    }

    */
/*
     * 初始化下拉控件的列表（显示分辨率）
     * *//*

    private void initSpinner() {
       */
/* ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spn_item, listResolution);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnResolution.setAdapter(adapter);

        int position = listResolution.indexOf(String.format("%dx%d", width, height));
        spnResolution.setSelection(position, false);

        spnResolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mMediaStream != null && mMediaStream.isStreaming()) {
                    int pos = listResolution.indexOf(String.format("%dx%d", width, height));

                    if (pos == position)
                        return;

                    spnResolution.setSelection(pos, false);

                    Toast.makeText(LiveActivity.this, "正在推送中,无法切换分辨率", Toast.LENGTH_SHORT).show();
                    return;
                }

                String r = listResolution.get(position);
                String[] splitR = r.split("x");

                int wh = Integer.parseInt(splitR[0]);
                int ht = Integer.parseInt(splitR[1]);

                if (width != wh || height != ht) {
                    width = wh;
                    height = ht;

                    if (mMediaStream != null) {
                        mMediaStream.updateResolution(width, height);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*//*

    }

    */
/*
     * 开始推流，获取fps、bps
     * *//*

    @Subscribe
    public void onStreamStat(final StreamStat stat) {
        streamStat.post(() ->
                streamStat.setText(getString(R.string.stream_stat+
                        stat.framePerSecond+
                        stat.bytesPerSecond * 8 / 1024))
        );
    }

    */
/*
     * 获取可以支持的分辨率
     * *//*

    @Subscribe
    public void onSupportResolution(SupportResolution res) {
        runOnUiThread(() -> {
            listResolution = Util.getSupportResolution(getApplicationContext());
            boolean supportdefault = listResolution.contains(String.format("%dx%d", width, height));

            if (!supportdefault) {
                String r = listResolution.get(0);
                String[] splitR = r.split("x");

                width = Integer.parseInt(splitR[0]);
                height = Integer.parseInt(splitR[1]);
            }

            initSpinner();
        });
    }

    */
/*
     * 得知推流的状态
     * *//*
*/
/*
    @Subscribe
    public void onPushCallback(final PushCallback cb) {
        switch (cb.code) {
            case EasyRTMP.OnInitPusherCallback.CODE.EASY_ACTIVATE_INVALID_KEY:
                sendMessage("无效Key");
                break;
            case EasyRTMP.OnInitPusherCallback.CODE.EASY_ACTIVATE_SUCCESS:
                sendMessage("激活成功");
                break;
            case EasyRTMP.OnInitPusherCallback.CODE.EASY_RTMP_STATE_CONNECTING:
                sendMessage("连接中");
                break;
            case EasyRTMP.OnInitPusherCallback.CODE.EASY_RTMP_STATE_CONNECTED:
                sendMessage("连接成功");
                break;
            case EasyRTMP.OnInitPusherCallback.CODE.EASY_RTMP_STATE_CONNECT_FAILED:
                sendMessage("连接失败");
                break;
            case EasyRTMP.OnInitPusherCallback.CODE.EASY_RTMP_STATE_CONNECT_ABORT:
                sendMessage("连接异常中断");
                break;
            case EasyRTMP.OnInitPusherCallback.CODE.EASY_RTMP_STATE_PUSHING:
                sendMessage("推流中");
                break;
            case EasyRTMP.OnInitPusherCallback.CODE.EASY_RTMP_STATE_DISCONNECTED:
                sendMessage("断开连接");
                break;
            case EasyRTMP.OnInitPusherCallback.CODE.EASY_ACTIVATE_PLATFORM_ERR:
                sendMessage("平台不匹配");
                break;
            case EasyRTMP.OnInitPusherCallback.CODE.EASY_ACTIVATE_COMPANY_ID_LEN_ERR:
                sendMessage("断授权使用商不匹配");
                break;
            case EasyRTMP.OnInitPusherCallback.CODE.EASY_ACTIVATE_PROCESS_NAME_LEN_ERR:
                sendMessage("进程名称长度不匹配");
                break;
            case EASY_ACTIVATE_VALIDITY_PERIOD_ERR:
                sendMessage("进程名称长度不匹配");
                break;
        }
    }*//*


    */
/*
     * 显示推流的状态
     * *//*

    private void sendMessage(String message) {
        Message msg = Message.obtain();
        msg.what = MSG_STATE;
        Bundle bundle = new Bundle();
        bundle.putString(STATE, message);
        msg.setData(bundle);

        handler.sendMessage(msg);
    }

    */
/* ========================= 点击事件 ========================= *//*


    */
/**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     *//*

    @Override
    public void onBackPressed() {
//        boolean isStreaming = mMediaStream != null && mMediaStream.isStreaming();
//
//        if (isStreaming && SPUtil.getEnableBackgroundCamera(this)) {
//            new AlertDialog.Builder(this).setTitle("是否允许后台上传？")
//                    .setMessage("您设置了使能摄像头后台采集,是否继续在后台采集并上传视频？如果是，记得直播结束后,再回来这里关闭直播。")
//                    .setNeutralButton("后台采集", (dialogInterface, i) -> {
//                        StreamActivity.super.onBackPressed();
//                    })
//                    .setPositiveButton("退出程序", (dialogInterface, i) -> {
//                        mMediaStream.stopStream();
//                        StreamActivity.super.onBackPressed();
//                        Toast.makeText(StreamActivity.this, "程序已退出。", Toast.LENGTH_SHORT).show();
//                    })
//                    .setNegativeButton(android.R.string.cancel, null)
//                    .show();
//            return;
//        } else {
//            super.onBackPressed();
//        }

        boolean isStreaming = mMediaStream != null && mMediaStream.isStreaming();

        if (isStreaming && SPUtil.getEnableBackgroundCamera(this)) {
            new AlertDialog.Builder(this).setTitle("是否允许后台上传？")
                    .setMessage("您设置了使能摄像头后台采集,是否继续在后台采集并上传视频？如果是，记得直播结束后,再回来这里关闭直播。")
                    .setNeutralButton("后台采集", (dialogInterface, i) -> {
                        LiveActivity.super.onBackPressed();
                    })
                    .setPositiveButton("退出程序", (dialogInterface, i) -> {
                        mMediaStream.stopStream();
                        LiveActivity.super.onBackPressed();
                        Toast.makeText(LiveActivity.this, "程序已退出。", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
            return;
        }

        //与上次点击返回键时刻作差
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            //大于2000ms则认为是误操作，使用Toast进行提示
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            //并记录下本次点击“返回键”的时刻，以便下次进行判断
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    //@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sv_surfaceview:
                try {
                    mMediaStream.getCamera().autoFocus(null);
                } catch (Exception e) {

                }
                break;
        }
    }

    */
/*
     * 推送屏幕
     * *//*

    public void onPushScreen() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            new AlertDialog.Builder(this).setMessage("推送屏幕需要安卓5.0以上,您当前系统版本过低,不支持该功能。").setTitle("抱歉").show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                new AlertDialog.Builder(this)
                        .setMessage("推送屏幕需要APP出现在顶部.是否确定?")
                        .setPositiveButton(android.R.string.ok,
                                (dialogInterface, i) -> {
                                    // 在Android 6.0后，Android需要动态获取权限，若没有权限，提示获取.
                                    final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                                    startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
                                })
                        .setNegativeButton(android.R.string.cancel,null)
                        .setCancelable(false)
                        .show();
                return;
            }
        }

        if (!SPUtil.getScreenPushing(this)) {
            new AlertDialog.Builder(this).setTitle("提醒").setMessage("屏幕直播将要开始,直播过程中您可以切换到其它屏幕。不过记得直播结束后,再进来停止直播哦!").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SPUtil.setScreenPushing(LiveActivity.this, true);
                    onPushScreen();
                }
            }).show();
            return;
        }

        if (RecordService.mEasyPusher != null) {
            Intent intent = new Intent(getApplicationContext(), RecordService.class);
            stopService(intent);

        } else {
            startScreenPushIntent();
        }
    }

    */
/*
     * 切换分辨率
     * *//*

    public void onClickResolution(View view) {
       // findViewById(R.id.spn_resolution).performClick();
    }

    */
/*
     * 切换屏幕方向
     * *//*

    public void onSwitchOrientation(View view) {
        if (mMediaStream != null) {
            if (mMediaStream.isStreaming()){
                Toast.makeText(this,"正在推送中,无法更改屏幕方向", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        int orientation = getRequestedOrientation();

        if (orientation == SCREEN_ORIENTATION_UNSPECIFIED || orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

//        if (mMediaStream != null)
//            mMediaStream.setDisplayRotationDegree(getDisplayRotationDegree());
    }

    */
/*
     * 推流or停止
     * *//*

    public void onStartOrStopPush() {
        //ImageView ib = findViewById(R.id.streaming_activity_push);

        if (mMediaStream != null && !mMediaStream.isStreaming()) {
            String url = Config.getServerURL(this);

            try {
                mMediaStream.startStream(url,
                        code -> BUSUtil.BUS.post(new PushCallback(code))
                );

                txtStreamAddress.setText(url);
            } catch (IOException e) {
                e.printStackTrace();
                sendMessage("激活失败，无效Key");
            }
        } else {
            mMediaStream.stopStream();
            sendMessage("断开连接");
        }
    }




    */
/* ========================= TextureView.SurfaceTextureListener ========================= *//*


    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
        System.out.println("surface可用了，我需要初始化");
        if (mService != null) {
            goonWithAvailableTexture(surface);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

}
*/
