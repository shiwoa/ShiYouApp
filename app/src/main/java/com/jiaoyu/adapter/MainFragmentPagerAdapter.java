package com.jiaoyu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jiaoyu.main.BookFragment;
import com.jiaoyu.main.CommunityFragment;
import com.jiaoyu.main.FindFragment;
import com.jiaoyu.main.MyFragment;

/**
 * Created by bishuang on 2018/1/5.
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"发现", "书城", "社区","我的"};

    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new BookFragment();
        } else if (position == 2) {
            return new CommunityFragment();
        }else if (position==3){
            return new MyFragment();
        }
        return new FindFragment();
    }
    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
