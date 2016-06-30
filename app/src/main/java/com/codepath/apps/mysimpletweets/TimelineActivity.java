package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;

public class TimelineActivity extends ActionBarActivity {

    private TweetsListFragment fragmentTweetsList;
    private final int REQUEST_CODE = 20;
    private SmartFragmentStatePagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //Get viewpager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        //Set viewpager adapter for the pager
        adapterViewPager = new TweetsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        //Find the pager sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        //Attach pager tabstrip to the viewpager
        tabStrip.setViewPager(viewPager);

     /*  if (savedInstanceState == null) {
            fragmentTweetsList = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_tweets_list);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

    /*   if(id == R.id.action_settings) {
            return true;
        }

        */

        return super.onOptionsItemSelected(item);
    }
/*
    //Adapter gets manager to inset or remove fragment from activity
    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = { "Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //Order and creation of fragments within the pager
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1){
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        //Return tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        //How many fragments there are to swipe between
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
*/
    public void onProfileView(MenuItem mi) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void onComposeView(MenuItem mi) {
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

   /* public void onImageClick() {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("username", )
        startActivity(i);
    }*/

    //  ArrayList<Tweet> tweets = new ArrayList<>();
    //  tweets.add(newTweet);
    // fragmentTweetsList.addAll(tweets);



    public void onActivityResult(int requestCode, int resultCode, Intent i) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet newTweet = (Tweet) i.getSerializableExtra("new_tweet");
            HomeTimelineFragment fragmentHomeTweets =
                    (HomeTimelineFragment) adapterViewPager.getRegisteredFragment(0);
            fragmentHomeTweets.appendTweet(newTweet);
        }
    }
}
