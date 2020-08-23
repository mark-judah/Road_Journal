package com.example.road_journal.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.road_journal.R;import com.jsibbold.zoomage.ZoomageView;

public class FullscreenActivity extends AppCompatActivity {
    ZoomageView imageView;
    String string;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_fullscreen);
        string = getIntent().getStringExtra("imageurl");
        StringBuilder sb = new StringBuilder();
        sb.append("onCreate: ");
        sb.append(string);
        Log.d("333333", sb.toString());
        imageView = (ZoomageView) findViewById(R.id.full_screen_imageview);
        Glide.with((FragmentActivity) this).load(string).into((ImageView) imageView);
    }
}
