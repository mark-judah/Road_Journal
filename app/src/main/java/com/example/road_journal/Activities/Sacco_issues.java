package com.example.road_journal.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.Sacco_issues_pojo;
import com.example.road_journal.Adapters.Sacco_issues_adapter;
import com.example.road_journal.R;import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Sacco_issues extends AppCompatActivity {
    RecyclerView.Adapter adapter;
    DatabaseReference databaseReference;
    LinearLayoutManager linearLayoutManager;
    List<Sacco_issues_pojo> list = new ArrayList();
    RecyclerView saccoissuesrecycler;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_sacco_issues);
        saccoissuesrecycler = (RecyclerView) findViewById(R.id.sacco_issues_recycler);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        saccoissuesrecycler.setLayoutManager(linearLayoutManager);
        saccoissuesrecycler.setHasFixedSize(true);
        final String stringExtra = getIntent().getStringExtra("issue");
        final String displayName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        databaseReference = FirebaseDatabase.getInstance().getReference("All_Issues");
        databaseReference.keepSynced(true);
        databaseReference.orderByChild("sacconame").equalTo(displayName).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                Sacco_issues.this.list.clear();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    Sacco_issues_pojo sacco_issues_pojo = (Sacco_issues_pojo) dataSnapshot2.getValue(Sacco_issues_pojo.class);
                    if (sacco_issues_pojo.getIssue().equals(stringExtra)) {
                        Sacco_issues.this.list.add(sacco_issues_pojo);
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("values:Sacco name is ");
                    sb.append(displayName);
                    sb.append("datasnapshot values are ");
                    sb.append(dataSnapshot);
                    sb.append("datasnapshot children are ");
                    sb.append(dataSnapshot2);
                    Log.d("TAG", sb.toString());
                }
           adapter = new Sacco_issues_adapter(Sacco_issues.this, Sacco_issues.this.list, (LayoutInflater) Sacco_issues.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
               saccoissuesrecycler.setAdapter(Sacco_issues.this.adapter);
            }
        });
    }
}
