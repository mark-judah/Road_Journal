package com.example.road_journal.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.SaccoResponsePojo;
import com.example.road_journal.Adapters.Sacco_Response_Adapter;
import com.example.road_journal.R;import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserInbox extends AppCompatActivity {
    RecyclerView.Adapter adapter;
    DatabaseReference databaseReference;
    LinearLayoutManager linearLayoutManager;
    List<SaccoResponsePojo> list = new ArrayList();
    RecyclerView saccoissuesrecycler;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_user_inbox);
        saccoissuesrecycler = (RecyclerView) findViewById(R.id.inbox_recycler);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        saccoissuesrecycler.setLayoutManager(linearLayoutManager);
        saccoissuesrecycler.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("Sacco Response");
        databaseReference.keepSynced(true);
        databaseReference.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInbox.this.list.clear();
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    UserInbox.this.list.add((SaccoResponsePojo) value.getValue(SaccoResponsePojo.class));
                }
                UserInbox.this.adapter = new Sacco_Response_Adapter(UserInbox.this, UserInbox.this.list, (LayoutInflater) UserInbox.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                UserInbox.this.saccoissuesrecycler.setAdapter(UserInbox.this.adapter);
            }
        });
    }
}
