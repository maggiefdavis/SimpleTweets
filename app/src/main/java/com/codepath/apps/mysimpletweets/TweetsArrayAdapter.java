package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by mfdavis on 6/27/16.
 */

//Taking Tweet objects and turning them into Views displayed in the list
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    //Tweet tweet;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get tweet
        final Tweet tweet = getItem(position);
        //Find or inflate template
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        //Find subviews to fill with data in template
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
        //Populate data into subviews
        tvName.setText(tweet.getUser().getName());
        tvUsername.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        String dateString = tweet.getCreatedAt();
        String formattedTime = TimeFormatter.getTimeDifference(dateString);
        tvTimestamp.setText(formattedTime);
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchProfileView(tweet);
            }
        });
        //Return view to be inserted into the list
        return convertView;
    }


    private void launchProfileView(Tweet tweet) {
        Intent i = new Intent(getContext(), ProfileActivity.class);
        i.putExtra("user", tweet.getUser());
        i.putExtra("screen_name", tweet.getUser().getScreenName());
        getContext().startActivity(i);

    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    /**
     * Given a date String of the format given by the Twitter API, returns a display-formatted
     * String representing the relative time difference, e.g. "2m", "6d", "23 May", "1 Jan 14"
     * depending on how great the time difference between now and the given date is.
     * This, as of 2016-06-29, matches the behavior of the official Twitter app.
     */
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
