package com.example.road_journal.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.FileUploadInfo;
import com.example.road_journal.Adapters.FetchAdapter;
import com.example.road_journal.R;import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FetchFavorite extends AppCompatActivity {
    RecyclerView.Adapter adapter;
    private FirebaseAuth auth;
    DatabaseReference databaseReference;
    RecyclerView fetchrecycler;
    LinearLayoutManager linearLayoutManager;
    List<FileUploadInfo> list = new ArrayList();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_fetch_favorite);
        fetchrecycler = (RecyclerView) findViewById(R.id.fetchrecycler);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        fetchrecycler.setLayoutManager(linearLayoutManager);
        fetchrecycler.setHasFixedSize(true);
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Favorites");
        databaseReference.keepSynced(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                FetchFavorite.this.list.clear();
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    for (DataSnapshot dataSnapshot2 : children.getChildren()) {
                        if (dataSnapshot2.child("userId").getValue().equals(currentUser.getUid())) {
                            FetchFavorite.this.list.add((FileUploadInfo) dataSnapshot2.getValue(FileUploadInfo.class));
                            StringBuilder sb = new StringBuilder();
                            sb.append("onDataChange: ");
                            sb.append(dataSnapshot2);
                            Log.d("snapshot1", sb.toString());
                        }
                        FetchFavorite.this.adapter = new FetchAdapter(FetchFavorite.this, FetchFavorite.this.list, (LayoutInflater) FetchFavorite.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                        FetchFavorite.this.fetchrecycler.setAdapter(FetchFavorite.this.adapter);
                    }
                }
            }
        });
    }
}
