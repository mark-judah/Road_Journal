package com.example.road_journal.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.FileUploadInfo;
import com.example.road_journal.Adapters.IncidentsAdapter;
import com.example.road_journal.R;import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class IncidentsActivity extends AppCompatActivity {
    private static final String MENU_SEARCH_ID_TAG = "menuSearchIdTag1";
    public static String TAG = "TAG";
    int PERMISSION_ALL = 1;
    RecyclerView.Adapter adapter;
    private FirebaseAuth auth;

    DatabaseReference databaseReference;
    private Editor editor;
    RecyclerView eventsrecycler;
    ImageView imageview;
    List<FileUploadInfo> list = new ArrayList();
    private SharedPreferences prefs;
    ProgressBar progressBar;
    Toolbar toolbar;
    private int totalCount;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
       prefs = getPreferences(0);
       editor =prefs.edit();
        setContentView((int) R.layout.activity_events);
       totalCount =prefs.getInt("counter1", 0);
       totalCount++;
       editor.putInt("counter1",totalCount);
       editor.commit();

        String[] strArr = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"};
        if (!hasPermissions(this, strArr)) {
            ActivityCompat.requestPermissions(this, strArr,PERMISSION_ALL);
        }
       toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       imageview = (ImageView) findViewById(R.id.filter);
        FirebaseApp.initializeApp(this);
       auth = FirebaseAuth.getInstance();
       eventsrecycler = (RecyclerView) findViewById(R.id.eventsrecycler);
       eventsrecycler.setLayoutManager(new LinearLayoutManager(this));
       eventsrecycler.setHasFixedSize(true);
        fetchdata("Traffic");

       imageview.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(IncidentsActivity.this, IncidentsActivity.this.imageview);
                popupMenu.getMenuInflater().inflate(R.menu.filter_events, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.traffic) {
                            IncidentsActivity.this.fetchdata("Traffic");
                        }
                        if (itemId == R.id.weather) {
                            IncidentsActivity.this.fetchdata("Weather");
                        }
                        if (itemId == R.id.accident) {
                            IncidentsActivity.this.fetchdata("Accident");
                        }
                        if (itemId == R.id.security) {
                            IncidentsActivity.this.fetchdata("Security");
                        }
                        if (itemId == R.id.road_divertion) {
                            IncidentsActivity.this.fetchdata("Road divertion");
                        }
                        if (itemId == R.id.dangerous_driving) {
                            IncidentsActivity.this.fetchdata("Dangerous driving");
                        }
                        if (itemId == R.id.construction_works) {
                            IncidentsActivity.this.fetchdata("Construction Works");
                        }
                        if (itemId == R.id.fire) {
                            IncidentsActivity.this.fetchdata("Fire");
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
       progressBar = (ProgressBar) findViewById(R.id.progressBar3);
       progressBar.setVisibility(0);
    }



    public static boolean hasPermissions(Context context, String... strArr) {
        if (!(context == null || strArr == null)) {
            for (String checkSelfPermission : strArr) {
                if (ContextCompat.checkSelfPermission(context, checkSelfPermission) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void fetchdata(String str) {
        if (auth.getCurrentUser() != null) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("we are here: ");
            sb.append(str);
            Log.d(str2, sb.toString());
           databaseReference = FirebaseDatabase.getInstance().getReference().child("Data").child("Incidents");
           databaseReference.orderByChild("incident").equalTo(str).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(DatabaseError databaseError) {
                }

                public void onDataChange(DataSnapshot dataSnapshot) {
                    IncidentsActivity.this.list.clear();
                    IncidentsActivity.this.eventsrecycler.getRecycledViewPool().clear();
                    IncidentsActivity.this.adapter.notifyDataSetChanged();
                    IncidentsActivity.this.progressBar.setVisibility(4);
                    for (DataSnapshot value : dataSnapshot.getChildren()) {
                        IncidentsActivity.this.list.add(value.getValue(FileUploadInfo.class));
                    }
                }
            });
           adapter = new IncidentsAdapter(this,list, (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
           eventsrecycler.setAdapter(adapter);
        }
    }
}
