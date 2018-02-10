package com.xrone.julis.compous.HomeFragment.communication.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


import com.xrone.julis.compous.HomeFragment.communication.Model.User;
import com.xrone.julis.compous.HomeFragment.communication.view.TopicSimpleListFragment;

import java.util.ArrayList;
import java.util.List;

public class UserDetailPagerAdapter extends FragmentPagerAdapter {

    private static final String[] titles = {
            "最近回复",
            "最新发布",
    };
    
    private final List<TopicSimpleListFragment> fragmentList = new ArrayList<>();

    public UserDetailPagerAdapter(@NonNull FragmentManager manager) {
        super(manager);
        fragmentList.add(new TopicSimpleListFragment());
        fragmentList.add(new TopicSimpleListFragment());
    }

    public void update(@NonNull User user) {
        fragmentList.get(0).notifyDataSetChanged(user.getRecentReplyList());
        Log.e("user",user.getRecentReplyList().toString());
        fragmentList.get(1).notifyDataSetChanged(user.getRecentTopicList());
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
