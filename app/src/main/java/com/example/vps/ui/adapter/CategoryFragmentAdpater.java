package com.example.vps.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.vps.R;
import com.example.vps.ui.home.HomeFragment;
import com.example.vps.ui.util.common.Category;
import com.example.vps.ui.util.common.Constants;

public class CategoryFragmentAdpater extends FragmentPagerAdapter {
    private Context context;

    public CategoryFragmentAdpater(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case Constants.HOME:
                return HomeFragment.getInstance(null);
            case Constants.WORLD:
                return HomeFragment.getInstance(Category.world.title);
            case Constants.SCIENCE:
                return HomeFragment.getInstance(Category.science.title);
            case Constants.SPORT:
                return HomeFragment.getInstance(Category.sports.title);
            case Constants.ENVIRONMENT:
                return HomeFragment.getInstance(Category.environment.title);
            case Constants.SOCIETY:
                return HomeFragment.getInstance(Category.society.title);
            case Constants.FASHION:
                return HomeFragment.getInstance(Category.fashion.title);
            case Constants.BUSINESS:
                return HomeFragment.getInstance(Category.business.title);
            case Constants.CULTURE:
                return HomeFragment.getInstance(Category.culture.title);
            case Constants.ENTERTAINMENT:
                return HomeFragment.getInstance(Category.entertainment.title);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 10;
    }


    /**
     * Return page title of the tap
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        int titleResId;
        switch (position) {
            case Constants.HOME:
                titleResId = R.string.ic_title_home;
                break;
            case Constants.WORLD:
                titleResId = R.string.ic_title_world;
                break;
            case Constants.SCIENCE:
                titleResId = R.string.ic_title_science;
                break;
            case Constants.SPORT:
                titleResId = R.string.ic_title_sport;
                break;
            case Constants.ENVIRONMENT:
                titleResId = R.string.ic_title_environment;
                break;
            case Constants.ENTERTAINMENT:
                titleResId = R.string.ic_title_entertainment;
                break;
            case Constants.SOCIETY:
                titleResId = R.string.ic_title_society;
                break;
            case Constants.FASHION:
                titleResId = R.string.ic_title_fashion;
                break;
            case Constants.BUSINESS:
                titleResId = R.string.ic_title_business;
                break;
            default:
                titleResId = R.string.ic_title_culture;
                break;
        }
        return context.getString(titleResId);
    }
}
