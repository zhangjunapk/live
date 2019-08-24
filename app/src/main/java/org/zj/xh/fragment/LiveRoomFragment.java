package org.zj.xh.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.zj.xh.R;

/**
 * 单个直播间的界面
 */
public class LiveRoomFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.layout_live_room, null);
        init(inflate);
        return inflate;
    }

    /**
     * 初始化里面的点击事件
     * @param inflate
     */
    private void init(View inflate) {
        //这里要给按钮设置事件,
    }
}
