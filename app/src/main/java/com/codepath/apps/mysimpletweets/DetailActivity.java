package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class DetailActivity extends AppCompatActivity {
    TwitterClient client;
    Tweet tweet;
    private final int REQUEST_CODE = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tweet = (Tweet) getIntent().getSerializableExtra("tweet");
        client = TwitterApplication.getRestClient();

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
        TextView tvBody = (TextView) findViewById(R.id.tvBody);
        TextView tvTimestamp = (TextView) findViewById(R.id.tvTimestamp);
        TextView tvRetweets = (TextView) findViewById(R.id.tvRetweets);
        TextView tvLikes = (TextView) findViewById(R.id.tvLikes);
        final ImageView ivReply = (ImageView) findViewById(R.id.ivReply);
        final ImageView ivFavorite = (ImageView) findViewById(R.id.ivFavorite);
        final ImageView ivRetweet = (ImageView) findViewById(R.id.ivRetweet);

        tvName.setText(tweet.getUser().getName());
        tvUsername.setText("@" + tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());

        String dateString = tweet.getCreatedAt();
        String formattedTime = TimeFormatter.getTimeDifference(dateString);
        tvTimestamp.setText(formattedTime);

        tvRetweets.setText(tweet.getRetweetCount() + " RETWEETS");
        tvLikes.setText(tweet.getFavoriteCount() + " FAVORITES");

        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivReply.setBackgroundResource(R.drawable.ic_reply_on);
                launchReply(tweet);
            }
        });

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tweet.getFavorited()) {
                    client.favorite(tweet.getUid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            tweet.setFavorited(true);
                            ivFavorite.setBackgroundResource(R.drawable.ic_favorite_on);
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                } else {
                    Toast.makeText(DetailActivity.this, "Already favorited!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tweet.getRetweeted()) {
                    client.retweet(tweet.getUid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            tweet.setRetweeted(true);
                            ivRetweet.setBackgroundResource(R.drawable.ic_retweet_on);
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }else {
                    Toast.makeText(DetailActivity.this, "Already retweeted!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void launchReply(Tweet tweet) {
        Intent i = new Intent(this, ComposeActivity.class);
        i.putExtra("parent_tweet", tweet);
        startActivityForResult(i, REQUEST_CODE);
    }


    public static class TimeFormatter {
        public static String getTimeDifference(String rawJsonDate) {
            String time = "";
            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat format = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            format.setLenient(true);
            try {
                long diff = (System.currentTimeMillis() - format.parse(rawJsonDate).getTime()) / 1000;
                if (diff < 5)
                    time = "Just now";
                else if (diff < 60)
                    time = String.format(Locale.ENGLISH, "%ds",diff);
                else if (diff < 60 * 60)
                    time = String.format(Locale.ENGLISH, "%dm", diff / 60);
                else if (diff < 60 * 60 * 24)
                    time = String.format(Locale.ENGLISH, "%dh", diff / (60 * 60));
                else if (diff < 60 * 60 * 24 * 30)
                    time = String.format(Locale.ENGLISH, "%dd", diff / (60 * 60 * 24));
                else {
                    Calendar now = Calendar.getInstance();
                    Calendar then = Calendar.getInstance();
                    then.setTime(format.parse(rawJsonDate));
                    if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR)) {
                        time = String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
                                + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);
                    } else {
                        time = String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
                                + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)
                                + " " + String.valueOf(then.get(Calendar.YEAR) - 2000);
                    }
                }
            }  catch (ParseException e) {
                e.printStackTrace();
            }
            return time;
        }
    }
}
