package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mfdavis on 6/30/16.
 */
public class SearchTweetsFragment extends TweetsListFragment {
    private SwipeRefreshLayout swipeContainer;
    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient(); //singleton client
        populateResults();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeContainer = getSwipeContainer();
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateResults();
                swipeContainer.setRefreshing(false);

            }
        });
    }


    public static SearchTweetsFragment newInstance(String query) {
        SearchTweetsFragment searchFragment = new SearchTweetsFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        searchFragment.setArguments(args);
        return searchFragment;
    }

    public void populateResults() {
        clear();
        String query = getArguments().getString("query");
        client.searchTweets(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    ArrayList<Tweet> tweets = Tweet.fromJSONArray(response.getJSONArray("statuses"));
                    addAll(tweets);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

        });
    }
}
