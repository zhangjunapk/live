package org.zj.xh.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.zj.xh.Constants;
import org.zj.xh.R;
import org.zj.xh.activity.LiveRoomActivity;
import org.zj.xh.bean.MessageEventOpenLiveRoom;
import org.zj.xh.bean.Result;
import org.zj.xh.bean.RoomBean;
import org.zj.xh.httpinterface.ILiveInterface;
import org.zj.xh.httpinterface.RetrofitClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 直播间列表
 */
public class LiveListFragment extends Fragment {

    boolean isFirst=true;

    @BindView(R.id.rv_roomlist)
    RecyclerView mRvRoomList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.layout_fragment_liveroomlist, null);
        ButterKnife.bind(this,inflate);
        init(inflate);
        return inflate;
    }

    /**
     * 初始化里面的事件
     * @param inflate
     */
    private void init(View inflate) {
        getDataAndInflate();
    }

    private void getDataAndInflate(){

        //进来就进行网络请求
        ILiveInterface request = RetrofitClient.getInstance().create(ILiveInterface.class);

// 封装请求
        Observable<Result<List<RoomBean>>> resultObservable = request.getRoomList();

        resultObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<List<RoomBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<List<RoomBean>> objectResult) {
                        //这里直接初始化rv
                        inflateRv(objectResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        //成功后开启
                        Toast.makeText(getContext(),"获取成功",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 初始化rv
     */
    private void inflateRv(Result<List<RoomBean>> result) {
        mRvRoomList.setLayoutManager(new GridLayoutManager(getContext(),2));
        CommonAdapter<RoomBean> commonAdapter = new CommonAdapter<RoomBean>(getActivity(), R.layout.item_rv_liveroom, result.getData()) {
            @Override
            protected void convert(ViewHolder holder, RoomBean bean, int position) {
                holder.setText(R.id.tv_roomname, bean.getRoomName());

            }
        };
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                String roomId=result.getData().get(i).getRoomId();
                System.out.println("房间号发送"+result.getData().get(i).getRoomId());
                Constants.currentRoomId=roomId;
                startActivity(new Intent(getContext(), LiveRoomActivity.class));
                EventBus.getDefault().post(new MessageEventOpenLiveRoom(roomId));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                return false;
            }
        });
        mRvRoomList.setAdapter(commonAdapter);

    }

    @Override
    public void onResume() {
        //继续后重新网络请求一下
        Toast.makeText(getActivity(),"获取数据",Toast.LENGTH_SHORT).show();
        getDataAndInflate();
        //直接在这里重新请求网络
        super.onResume();
    }

    public static LiveListFragment newInstance(String param) {
        LiveListFragment fragment = new LiveListFragment();
        Bundle args = new Bundle();
        args.putString("name", param);
        fragment.setArguments(args);
        return fragment;
    }




}
