package com.example.road_journal.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.FileUploadInfo;
import com.example.road_journal.Pojos.travel_pojo;
import com.example.road_journal.Adapters.ExternalLinkAdapter;
import com.example.road_journal.R;import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExternalLink extends AppCompatActivity {
    RecyclerView.Adapter adapter;
    DatabaseReference databaseReference;
    RecyclerView linkrecycler;
    List<FileUploadInfo> list = new ArrayList();
    List<travel_pojo> list2 = new ArrayList();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_external_link);
        linkrecycler = (RecyclerView) findViewById(R.id.linkrecycler);
        linkrecycler.setLayoutManager(new LinearLayoutManager(this));
        linkrecycler.setHasFixedSize(true);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        String[] split = String.valueOf(getIntent().getData()).split("/");
        String str = split[4];
        int parseInt = Integer.parseInt(split[6]);
        StringBuilder sb = new StringBuilder();
        sb.append("onResume: ");
        sb.append(split[6]);
        sb.append(">>>>>");
        sb.append(split[5]);
        sb.append(">>>>>");
        sb.append(split[4]);
        Log.d("vals", sb.toString());
        if (parseInt == 1) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Data").child("Incidents");
            databaseReference.orderByChild("original_key").equalTo(str).addValueEventListener(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(DataSnapshot dataSnapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("onChildChanged: ");
                        sb.append(dataSnapshot2);
                        Log.d("snapshot", sb.toString());
                        list.add((FileUploadInfo) dataSnapshot2.getValue(FileUploadInfo.class));
                    }
                    adapter = new ExternalLinkAdapter(ExternalLink.this, list, (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                    linkrecycler.setAdapter(adapter);
                }
            });
        }
        if (parseInt == 2) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("TravelStory");
            databaseReference.orderByChild("key").equalTo(str).addValueEventListener(new ValueEventListener() {
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

                public void onDataChange(DataSnapshot dataSnapshot) {
                    list2.clear();
                    for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("onChildChanged: ");
                        sb.append(dataSnapshot2);
                        Log.d("snapshot", sb.toString());
                        list2.add((travel_pojo) dataSnapshot2.getValue(travel_pojo.class));
                    }
                    adapter = new ExternalLinkAdapter(ExternalLink.this, list, (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                    linkrecycler.setAdapter(adapter);
                }
            });
        }
    }
}
