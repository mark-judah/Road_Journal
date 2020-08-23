package com.example.road_journal.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.FileUploadInfo;
import com.example.road_journal.Adapters.postsAdapter;
import com.example.road_journal.R;import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class posts extends AppCompatActivity {
    RecyclerView.Adapter adapter;
    private FirebaseAuth auth;
    DatabaseReference databaseReference;
    LinearLayoutManager linearLayoutManager;
    List<FileUploadInfo> list = new ArrayList();
    RecyclerView updatesrecyler;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_posts);
        updatesrecyler = (RecyclerView) findViewById(R.id.updatesrecycler);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        updatesrecyler.setLayoutManager(linearLayoutManager);
        updatesrecyler.setHasFixedSize(true);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Data").child("Incidents");
        databaseReference.keepSynced(true);
        final Query equalTo = databaseReference.orderByChild("userId").equalTo(currentUser.getUid());
        equalTo.addChildEventListener(new ChildEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String str) {
            }

            public void onChildAdded(DataSnapshot dataSnapshot, String str) {
                equalTo.keepSynced(true);
                equalTo.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                            list.add((FileUploadInfo) dataSnapshot2.getValue(FileUploadInfo.class));
                            StringBuilder sb = new StringBuilder();
                            sb.append("onDataChange: ");
                            sb.append(dataSnapshot2);
                            Log.d("snapshot", sb.toString());
                        }
                        adapter = new postsAdapter(posts.this, list, (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                        updatesrecyler.setAdapter(adapter);
                    }
                });
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String str) {
                equalTo.keepSynced(true);
                equalTo.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            list.add((FileUploadInfo) value.getValue(FileUploadInfo.class));
                        }
                        adapter = new postsAdapter(posts.this, list, (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                        updatesrecyler.setAdapter(adapter);
                    }
                });
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                equalTo.keepSynced(true);
                equalTo.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            list.add((FileUploadInfo) value.getValue(FileUploadInfo.class));
                        }
                        adapter = new postsAdapter(posts.this, list, (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                        updatesrecyler.setAdapter(adapter);
                    }
                });
            }
        });
    }
}
