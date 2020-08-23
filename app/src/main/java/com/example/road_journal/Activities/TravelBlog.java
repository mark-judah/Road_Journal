package com.example.road_journal.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.travel_pojo;
import com.example.road_journal.Adapters.TravelAdapter;
import com.example.road_journal.R;import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TravelBlog extends AppCompatActivity {
    private static final String INTRO_CARD = "material_intro1";
    RecyclerView.Adapter adapter;
    private FirebaseAuth auth;

    DatabaseReference databaseReference;
    DatabaseReference dbref;
    private Editor editor;
    FloatingActionButton fab;
    LinearLayoutManager linearLayoutManager;
    List<travel_pojo> list = new ArrayList();
    private SharedPreferences prefs;
    ProgressDialog progressDialog;
    private int totalCount;
    RecyclerView travelrecycler;
    FirebaseUser user;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.prefs = getPreferences(0);
        this.editor = this.prefs.edit();
        setContentView((int) R.layout.activity_travel_stories);
        this.totalCount = this.prefs.getInt("counter3", 0);
        this.totalCount++;
        this.editor.putInt("counter3", this.totalCount);
        this.editor.commit();
        this.progressDialog = new ProgressDialog(this);
        this.user = FirebaseAuth.getInstance().getCurrentUser();

        this.travelrecycler = (RecyclerView) findViewById(R.id.travel_recycler);
        this.linearLayoutManager = new LinearLayoutManager(this);
        this.linearLayoutManager.setReverseLayout(true);
        this.linearLayoutManager.setStackFromEnd(true);
        this.travelrecycler.setLayoutManager(this.linearLayoutManager);
        this.travelrecycler.setHasFixedSize(true);
        this.fab = (FloatingActionButton) findViewById(R.id.addstory);

        this.fab.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(TravelBlog.this);
                builder.setMessage((CharSequence) "Choose your source");
                builder.setPositiveButton((CharSequence) "Type", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TravelBlog.this.startActivity(new Intent(TravelBlog.this, post_story.class));
                    }
                });
                builder.setNegativeButton((CharSequence) "Web link", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TravelBlog.this.showdialog();
                    }
                });
                builder.show();
            }
        });
        this.databaseReference = FirebaseDatabase.getInstance().getReference("TravelStory");
        this.databaseReference.keepSynced(true);
        this.databaseReference.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                TravelBlog.this.list.clear();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("onChildChanged: ");
                    sb.append(dataSnapshot2);
                    Log.d("snapshot", sb.toString());
                    TravelBlog.this.list.add((travel_pojo) dataSnapshot2.getValue(travel_pojo.class));
                }
                TravelBlog.this.adapter = new TravelAdapter(TravelBlog.this, TravelBlog.this.list, (LayoutInflater) TravelBlog.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                TravelBlog.this.travelrecycler.setAdapter(TravelBlog.this.adapter);
            }
        });
    }


    /* access modifiers changed from: private */
    public void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.bloglinkpopup, null);
        final EditText editText = (EditText) inflate.findViewById(R.id.editText4);
        final EditText editText2 = (EditText) inflate.findViewById(R.id.editText5);
        Button button = (Button) inflate.findViewById(R.id.button2);
        builder.setView(inflate);
        builder.create().show();
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TravelBlog.this.progressDialog.setMessage("Loading...");
                String trim = editText.getText().toString().trim();
                String trim2 = editText2.getText().toString().trim();
                if (trim.equals("")) {
                    editText.setError("Please add a title to the blog");
                } else if (trim2.equals("")) {
                    editText2.setError("Plase add a link to the blog");
                } else {
                    String key = TravelBlog.this.databaseReference.push().getKey();
                    DatabaseReference child = TravelBlog.this.databaseReference.child(key);
                    travel_pojo travel_pojo = new travel_pojo(trim, null, trim2, TravelBlog.currentDate(), key, 1, TravelBlog.this.user.getDisplayName(), TravelBlog.this.user.getUid());
                    child.setValue(travel_pojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            TravelBlog.this.progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public static Date currentDate() {
        return Calendar.getInstance().getTime();
    }
}
