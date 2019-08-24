package org.zj.xh.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.github.faucamp.simplertmp.RtmpHandler;

import net.ossrs.yasea.SrsCameraView;
import net.ossrs.yasea.SrsEncodeHandler;
import net.ossrs.yasea.SrsPublisher;
import net.ossrs.yasea.SrsRecordHandler;

import org.zj.xh.Constants;
import org.zj.xh.R;
import org.zj.xh.bean.Result;
import org.zj.xh.bean.RoomBean;
import org.zj.xh.httpinterface.ILiveInterface;
import org.zj.xh.httpinterface.RetrofitClient;

import java.io.IOException;
import java.net.SocketException;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LiveYaseaActivity extends Activity implements RtmpHandler.RtmpListener, SrsRecordHandler.SrsRecordListener, SrsEncodeHandler.SrsEncodeListener {
    private boolean isPublished=false;
    private String rtmpUrl= Constants.PUSH_URL;
    private SrsPublisher mPublisher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_live_yasea);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //声明推流摄像头展示界面对象
        mPublisher = new SrsPublisher((SrsCameraView)
                findViewById(R.id.surfaceViewEx));
//设置编码消息处理
        mPublisher.setEncodeHandler(new SrsEncodeHandler(this));
//设置RTMP消息处理
        mPublisher.setRtmpHandler(new RtmpHandler(this));
//设置记录消息处理
        mPublisher.setRecordHandler(new SrsRecordHandler(this));
//设置展示界面大小
        mPublisher.setPreviewResolution(this.getWindowManager().getDefaultDisplay().getWidth(), this.getWindowManager().getDefaultDisplay().getHeight());
//设置横屏推流 1为竖屏 2为横屏
        mPublisher.setScreenOrientation(1);
//设置输出界面大小
        mPublisher.setOutputResolution(640, 480);
//设置视频高清模式
        mPublisher.setVideoHDMode();
//打开摄像头
        mPublisher.startCamera();

        startPush(mPublisher);
//切换摄像头按钮
/*        btnSwitchCamera.setOnClickListener(new
                                                   View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {           mPublisher.switchCameraFace((mPublisher.getCamraId() + 1) % Camera.getNumberOfCameras());
                                                       }
                                                   });*/
    }

    private void stopPush(SrsPublisher mPublisher) {

        //先发送请求关闭房间，然后才关闭

        ILiveInterface request = RetrofitClient.getInstance().create(ILiveInterface.class);

// 封装请求
        Observable<Result<Object>> resultObservable = request.closeLive(new RoomBean(Constants.liveRoomNum,null));

        resultObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<Object> objectResult) {
                        //如果没有请求成功，告诉他
                        Toast.makeText(getApplicationContext(),objectResult.getMessage(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        //成功后开启
                        mPublisher.stopPublish();
                        mPublisher.stopRecord();

                        //btnPublish.setBackgroundResource(R.drawable.ic_accessibility_black_48dp);
                        setTextShow("停止推流！");
                    }
                });



    }
    private long exitTime = 0;
    public void ExitApp(){
        if ((System.currentTimeMillis() - exitTime) > 2000){
            Toast.makeText(this, "再按一次关闭直播", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else{
            stopPush(mPublisher);
            finish();
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ExitApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void startPush(SrsPublisher mPublisher) {
        //设为硬编码
        mPublisher.switchToHardEncoder();
        //开始推流
        mPublisher.startPublish(rtmpUrl+Constants.liveRoomNum);
        mPublisher.startCamera();

       // btnPublish.setBackgroundResource(R.drawable.ic_3d_rotation_black_24dp);
        setTextShow("开始推流！");
    }

    private void setTextShow(String s) {
        Toast.makeText(this,s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRtmpConnecting(String msg) {

    }

    @Override
    public void onRtmpConnected(String msg) {

    }

    @Override
    public void onRtmpVideoStreaming() {

    }

    @Override
    public void onRtmpAudioStreaming() {

    }

    @Override
    public void onRtmpStopped() {

    }

    @Override
    public void onRtmpDisconnected() {

    }

    @Override
    public void onRtmpVideoFpsChanged(double fps) {

    }

    @Override
    public void onRtmpVideoBitrateChanged(double bitrate) {

    }

    @Override
    public void onRtmpAudioBitrateChanged(double bitrate) {

    }

    @Override
    public void onRtmpSocketException(SocketException e) {

    }

    @Override
    public void onRtmpIOException(IOException e) {

    }

    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {

    }

    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {

    }

    @Override
    public void onNetworkWeak() {

    }

    @Override
    public void onNetworkResume() {

    }

    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {

    }

    @Override
    public void onRecordPause() {

    }

    @Override
    public void onRecordResume() {

    }

    @Override
    public void onRecordStarted(String msg) {

    }

    @Override
    public void onRecordFinished(String msg) {

    }

    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {

    }

    @Override
    public void onRecordIOException(IOException e) {

    }
}
