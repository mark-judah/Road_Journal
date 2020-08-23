package com.example.road_journal.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.FileUploadInfo;
import com.example.road_journal.Adapters.RegionsAdapter;
import com.example.road_journal.R;import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegionsActivity extends AppCompatActivity {
    public static String TAG = "TAG";
    int PERMISSION_ALL = 1;
    RecyclerView.Adapter adapter;
    DatabaseReference databaseReference;
    LinearLayoutManager linearLayoutManager;
    List<FileUploadInfo> list = new ArrayList();
    RecyclerView regionsrecycler;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_regions);
        FirebaseApp.initializeApp(this);
        String stringExtra = getIntent().getStringExtra("town");
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onCreate: ");
        sb.append(stringExtra);
        Log.d(str, sb.toString());
        getWindow().getDecorView().setSystemUiVisibility(1024);
        String[] strArr = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"};
        if (!hasPermissions(this, strArr)) {
            ActivityCompat.requestPermissions(this, strArr, PERMISSION_ALL);
        }
        regionsrecycler = (RecyclerView) findViewById(R.id.regionsrecycler);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        regionsrecycler.setLayoutManager(linearLayoutManager);
        regionsrecycler.setHasFixedSize(true);
        fetchdata(stringExtra);
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
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Data").child("Incidents");
            databaseReference.orderByChild("city").equalTo(str).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(DatabaseError databaseError) {
                }

                public void onDataChange(DataSnapshot dataSnapshot) {
                    RegionsActivity.this.list.clear();
                    RegionsActivity.this.regionsrecycler.getRecycledViewPool().clear();
                    RegionsActivity.this.adapter.notifyDataSetChanged();
                    for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                        RegionsActivity.this.list.add(dataSnapshot2.getValue(FileUploadInfo.class));
                        String str = RegionsActivity.this.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onDataChange: ");
                        sb.append(dataSnapshot2);
                        Log.d(str, sb.toString());
                    }
                }
            });
            adapter = new RegionsAdapter(this, list, (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
            regionsrecycler.setAdapter(adapter);
        }
    }
}
