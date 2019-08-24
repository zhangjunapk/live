/*
package org.zj.xh.activity;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.TextureView;

import androidx.core.app.ActivityCompat;

import org.easydarwin.easypusher.util.SPUtil;
import org.easydarwin.push.InitCallback;
import org.zj.xh.Constants;
import org.zj.xh.R;
import org.zj.xh.easyRtmp.push.MediaStream;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static org.zj.xh.activity.LiveActivity.REQUEST_CAMERA_PERMISSION;

public class LiveOriginActivity extends Activity {
    @BindView(R.id.sv_surfaceview)
    TextureView texture;
    private boolean mNeedGrantedPermission=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_live);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        permissionCheck();
        boolean enableVideo = SPUtil.getEnableVideo(this);
        MediaStream mediaStream = new MediaStream(getApplicationContext(), texture.getSurfaceTexture(),enableVideo);
        mediaStream.createCamera();
        mediaStream.startPreview();
        try {
            mediaStream.startStream(Constants.PUSH_URL, new InitCallback() {
                @Override
                public void onCallback(int code) {
                    System.out.println("代码"+code);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void permissionCheck() {
        // 动态获取camera和audio权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, REQUEST_CAMERA_PERMISSION);
            mNeedGrantedPermission = true;
            return;
        } else {
            // resume
        }
    }
}
*/
