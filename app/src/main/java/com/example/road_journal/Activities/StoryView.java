package com.example.road_journal.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.road_journal.R;import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class StoryView extends AppCompatActivity {
    Date date;
    FloatingActionButton floatingActionButton;
    ImageView imageView;
    String intentpostid;
    String intentstory;
    String intenttitle;
    String intenturl;
    String key;
    TextView story;
    TextView time;
    TextView title;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    long val;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_story_view);
        title = (TextView) findViewById(R.id.title);
        story = (TextView) findViewById(R.id.story);
        imageView = (ImageView) findViewById(R.id.image);
        time = (TextView) findViewById(R.id.time);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.editstory);
        intenturl = getIntent().getStringExtra("imageurl");
        intenttitle = getIntent().getStringExtra("title");
        intentstory = getIntent().getStringExtra("story");
        intentpostid = getIntent().getStringExtra("postuserid");
        key = getIntent().getStringExtra("key");
        date = new Date();
        date.setTime(getIntent().getLongExtra("time", val));
        StringBuilder sb = new StringBuilder();
        sb.append("posted by: ");
        sb.append(intentpostid);
        sb.append(" current user id:");
        sb.append(user.getUid());
        Log.d("uidcomp", sb.toString());
        if (intentpostid.equals(user.getUid())) {
            floatingActionButton.setVisibility(0);
            floatingActionButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(StoryView.this, Edit_travelstory.class);
                    long time = StoryView.this.date.getTime();
                    intent.putExtra("imageurl", StoryView.this.intenturl);
                    intent.putExtra("title", StoryView.this.intenttitle);
                    intent.putExtra("story", StoryView.this.intentstory);
                    intent.putExtra("time", time);
                    intent.putExtra("postuserid", StoryView.this.intentpostid);
                    intent.putExtra("key", StoryView.this.key);
                    StoryView.this.startActivity(intent);
                }
            });
        }
        title.setText(intenttitle);
        Glide.with((FragmentActivity) this).load(intenturl).into(imageView);
        story.setText(intentstory);
        TextView textView = time;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Posted: ");
        sb2.append(date);
        textView.setText(sb2.toString());
    }
}
