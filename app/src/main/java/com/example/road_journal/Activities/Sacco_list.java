package com.example.road_journal.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.SaccoPOJO;
import com.example.road_journal.Adapters.SaccoAdapter;
import com.example.road_journal.R;import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Sacco_list extends AppCompatActivity {
    private static final String INTRO_CARD = "material_intro1";
    RecyclerView.Adapter adapter;

    DatabaseReference databaseReference;
    private Editor editor;
    FloatingActionButton floatingActionButton;
    GridLayoutManager gridLayoutManager;
    List<SaccoPOJO> list = new ArrayList();
    private SharedPreferences prefs;
    RecyclerView sacco_recycler;
    private int totalCount;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        prefs = getPreferences(0);
        editor = prefs.edit();
        setContentView((int) R.layout.activity_sacco_list);
        totalCount = prefs.getInt("counter4", 0);
        totalCount++;
        editor.putInt("counter4", totalCount);
        editor.commit();

        sacco_recycler = (RecyclerView) findViewById(R.id.saccos);
        gridLayoutManager = new GridLayoutManager(this, 1);
        sacco_recycler.setLayoutManager(gridLayoutManager);
        sacco_recycler.setHasFixedSize(true);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_add);

        floatingActionButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                Sacco_list.this.startActivity(new Intent(Sacco_list.this, RegisterSacco.class));
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("approved saccos");
        databaseReference.keepSynced(true);
        databaseReference.addChildEventListener(new ChildEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String str) {
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String str) {
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            public void onChildAdded(DataSnapshot dataSnapshot, String str) {
                Sacco_list.this.databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Sacco_list.this.list.clear();
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            Sacco_list.this.list.add((SaccoPOJO) value.getValue(SaccoPOJO.class));
                        }
                        Sacco_list.this.adapter = new SaccoAdapter(Sacco_list.this, Sacco_list.this.list);
                        Sacco_list.this.sacco_recycler.setAdapter(Sacco_list.this.adapter);
                    }
                });
            }
        });
    }


}
