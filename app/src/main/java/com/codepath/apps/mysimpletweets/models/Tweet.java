package com.codepath.apps.mysimpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mfdavis on 6/27/16.
 */
public class Tweet implements Serializable {
    private static JSONObject json;
    private String body;
    private long uid;
    private User user;
    private String createdAt;
    private int favoriteCount;
    private int retweetCount;
    private Boolean favorited;

    public void setRetweeted(Boolean retweeted) {
        this.retweeted = retweeted;
    }

    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    private Boolean retweeted;


    public String getBody() {
        return body;
    }public long getUid() {
        return uid;
    }public String getCreatedAt() {
        return createdAt;
    }
    public User getUser() {
        return user;
    }

    public Boolean getFavorited() {
       return favorited;
    }

    public Boolean getRetweeted() {
        return retweeted;
    }

    public int getFavoriteCount() {
       return favoriteCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }




    //Deserialize Json
    //Tweet.fromJSON

    public static Tweet fromJSON(JSONObject jsonObject) {
        json = jsonObject;
        Tweet tweet = new Tweet();
        //Extract and store values
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.favorited = jsonObject.getBoolean("favorited");
            tweet.retweeted = jsonObject.getBoolean("retweeted");
            tweet.favoriteCount = jsonObject.getInt("favorite_count");
            tweet.retweetCount = jsonObject.getInt("retweet_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }
/*
    public static ArrayList<Tweet> fromSearchJSON (JSONObject jsonObject) {
        try {
            ArrayList<Tweet> tweets = fromJSONArray(jsonObject.getJSONArray("statuses"));
            return tweets;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<Tweet>();
    }*/

    public static ArrayList<Tweet> fromJSONArray (JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i <jsonArray.length(); i++) {
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if(tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }
}
