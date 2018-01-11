package com.jiaoyu.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jiaoyu.main.BookFragment;
import com.jiaoyu.main.CommunityFragment;
import com.jiaoyu.main.FindFragment;
import com.jiaoyu.main.MyFragment;


public class FragmentControllerNew {

    private int containerId;
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private static FragmentControllerNew controller;
    private Fragment findFragment, bookFragment,communityFragment, myFragment;

    public static FragmentControllerNew getInstance(FragmentActivity activity, int containerId) {
        if (controller == null) {
            controller = new FragmentControllerNew(activity, containerId);
        }
        return controller;
    }

    public static void onDestroy() {
        controller = null;
    }

    private FragmentControllerNew(FragmentActivity activity, int containerId) {
        this.containerId = containerId;
        fm = activity.getSupportFragmentManager();
    }


    public void showFragment(int position) {
        // 开启一个Fragment的事务
        transaction = fm.beginTransaction();
        // 隐藏掉所有的fragment
        hideFragments(transaction);
            switch (position) {
                case 0:
                    if (findFragment == null) {
                        findFragment = FindFragment.getInstance();
                        transaction.add(containerId, findFragment);
                    } else {
                        transaction.show(findFragment);
                    }
                    break;
                case 1:
                    if (bookFragment == null) {
                        bookFragment = BookFragment.getInstance();
                        transaction.add(containerId, bookFragment);
                    } else {
                        transaction.show(bookFragment);
                    }
                    break;
                case 2:
                    if (communityFragment == null) {
                        communityFragment = CommunityFragment.getInstance();
                        transaction.add(containerId, communityFragment);
                    } else {
                        transaction.show(communityFragment);
                    }
                    break;
                case 3:
                    if (myFragment == null) {
                        myFragment = MyFragment.getInstance();
                        transaction.add(containerId, myFragment);
                    } else {
                        transaction.show(myFragment);
                    }
                    break;
                default:
                    break;
            }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (bookFragment != null) {
            transaction.hide(bookFragment);
        }
        if (findFragment != null) {
            transaction.hide(findFragment);
        }
        if (communityFragment != null) {
            transaction.hide(communityFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }

    }
}