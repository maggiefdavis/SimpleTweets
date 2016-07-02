package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    TwitterClient client;
    EditText etTweet;
    Button btnTweet;
    Tweet newTweet;
    Tweet parentTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        parentTweet = (Tweet) getIntent().getSerializableExtra("parent_tweet");
        client = TwitterApplication.getRestClient();
        etTweet = (EditText) findViewById(R.id.etTweet);
        if (parentTweet != null) {
            String screenName = parentTweet.getUser().getScreenName();
            etTweet.setText("@" + screenName + ": ");
        }
        btnTweet = (Button) findViewById(R.id.btnTweet);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = etTweet.getText().toString();
                client.postStatus(status, parentTweet, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        newTweet = Tweet.fromJSON(response);
                        onSubmit();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }


                });
            }
        });
    }

    public void onSubmit() {
        Intent i = new Intent();
        i.putExtra("new_tweet", newTweet);
        setResult(RESULT_OK, i);
        this.finish();
    }
}

