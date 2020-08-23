package com.example.road_journal.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.SpeedPojo;
import com.example.road_journal.Adapters.SpeedReportAdapter;
import com.example.road_journal.R;import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SpeedReports extends AppCompatActivity {
    RecyclerView.Adapter adapter;
    DatabaseReference databaseReference;
    LinearLayoutManager linearLayoutManager;
    List<SpeedPojo> list = new ArrayList();
    RecyclerView speedrecycler;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_speed_reports);
        speedrecycler = (RecyclerView) findViewById(R.id.speedrecycler);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        speedrecycler.setLayoutManager(linearLayoutManager);
        speedrecycler.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("NTSA");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                SpeedReports.this.list.clear();
                for (DataSnapshot value : dataSnapshot.getChildren()) {
                    SpeedReports.this.list.add((SpeedPojo) value.getValue(SpeedPojo.class));
                }
                SpeedReports.this.adapter = new SpeedReportAdapter(SpeedReports.this, SpeedReports.this.list, (LayoutInflater) SpeedReports.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                SpeedReports.this.speedrecycler.setAdapter(SpeedReports.this.adapter);
            }
        });
    }
}
