package com.codepath.apps.mysimpletweets;

/**
 * Created by mfdavis on 6/28/16.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;


// Extend from SmartFragmentStatePagerAdapter now instead for more dynamic ViewPager items
public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {
    private static int NUM_ITEMS = 2;

    public TweetsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                //return HomeTimelineFragment.newInstance(0, "Page # 1");
                return new HomeTimelineFragment();
            case 1: // Fragment # 1
                //return MentionsTimelineFragment.newInstance(1, "Page # 2");
                return new MentionsTimelineFragment();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                //return HomeTimelineFragment.newInstance(0, "Page # 1");
                return "HOME";
            case 1: // Fragment # 1
                //return MentionsTimelineFragment.newInstance(1, "Page # 2");
                return "MENTIONS";
            default:
                return null;
        }

    }

}

