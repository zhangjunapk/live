package org.zj.xh.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class MineFragment extends Fragment {

    public static MineFragment newInstance(String param) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString("name", param);
        fragment.setArguments(args);
        return fragment;
    }

}
