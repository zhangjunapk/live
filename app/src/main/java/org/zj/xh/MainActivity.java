package org.zj.xh;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import org.zj.xh.activity.LiveYaseaActivity;
import org.zj.xh.bean.Result;
import org.zj.xh.bean.RoomBean;
import org.zj.xh.fragment.HomeFragment;
import org.zj.xh.fragment.LiveListFragment;
import org.zj.xh.fragment.MineFragment;
import org.zj.xh.httpinterface.ILiveInterface;
import org.zj.xh.httpinterface.RetrofitClient;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    public static final int REQUEST_MEDIA_PROJECTION = 1002;
    public static final int REQUEST_CAMERA_PERMISSION = 1003;
    public static final int REQUEST_STORAGE_PERMISSION = 1004;
    private boolean mNeedGrantedPermission;

    private String TAG= this.getClass().getName();
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    @BindView(R.id.container)
    FrameLayout mFrameLayout;
    private HomeFragment mBookFragment;
    private HomeFragment mLikeFragment;
    private LiveListFragment mLiveListFragment;
    private MineFragment mMineFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        InitNavigationBar();
        permissionCheck();
    }

    private void permissionCheck() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, REQUEST_CAMERA_PERMISSION);
            mNeedGrantedPermission = true;
            Toast.makeText(this,"需要授权相机/存储",Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void InitNavigationBar() {
        mBottomNavigationBar.setTabSelectedListener(this);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_3d_rotation_black_24dp, "首页").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.ic_3d_rotation_black_24dp, "动态").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.ic_3d_rotation_black_24dp, "直播间").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.ic_3d_rotation_black_24dp, "我的").setActiveColorResource(R.color.colorPrimary))
                .setFirstSelectedPosition(0)
                .initialise();
    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mBookFragment = HomeFragment.newInstance("首页");
        transaction.replace(R.id.container, mBookFragment);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        Log.d("onTabSelected", "onTabSelected: " + position);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (mBookFragment == null) {
                    mBookFragment = HomeFragment.newInstance("首页");
                }
                transaction.replace(R.id.container, mBookFragment);
                break;
            case 1:
                if (mLikeFragment == null) {
                    mLikeFragment = HomeFragment.newInstance("动态");
                }
                transaction.replace(R.id.container, mLikeFragment);
                break;
            case 2:
                if (mLiveListFragment == null) {
                    mLiveListFragment = LiveListFragment.newInstance("直播间");
                }
                transaction.replace(R.id.container, mLiveListFragment);
                break;

            case 3:
                if (mMineFragment == null) {
                    mMineFragment = MineFragment.newInstance("我的");
                }
                transaction.replace(R.id.container, mMineFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {
        Log.d("onTabUnselected", "onTabUnselected: " + position);
    }

    @Override
    public void onTabReselected(int position) {
        Log.d("onTabReselected", "onTabReselected: " + position);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_layout, menu);//加载menu文件到布局

        return true;
    }
    //菜单点击
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.open_room:
                //点击这个按钮就跳转到直播的页面
                showAlert();
                break;
            default:break;
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * 输入房间名
     */
    private void showAlert() {
        final EditText inputServer = new EditText(MainActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("输入房间名")
                .setView(inputServer)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String text = inputServer.getText().toString();
                //这里就发送网络请求，请求服务器，然后开始直播
                //网络请求成功后就跳转
                startLive(text);
            }});
                builder.show();
        }

    /**
     * 开始直播
     * @param roomName
     */
    private void startLive(String roomName){
            Constants.liveRoomNum= UUID.randomUUID().toString().replaceAll("-","");
            ILiveInterface request = RetrofitClient.getInstance().create(ILiveInterface.class);

// 封装请求
            Observable<Result<Object>> resultObservable = request.startLive(new RoomBean(Constants.liveRoomNum,roomName));

            resultObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Result<Object>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Result<Object> objectResult) {
                            Toast.makeText(getApplicationContext(),objectResult.getMessage(),Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onComplete() {
                            //成功后开启
                            startActivity(new Intent(getApplicationContext(), LiveYaseaActivity.class));
                        }
                    });
        }


    }

