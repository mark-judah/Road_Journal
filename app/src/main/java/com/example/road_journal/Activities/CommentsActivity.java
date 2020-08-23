package com.example.road_journal.Activities;

import android.content.Context;
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

import com.example.road_journal.Pojos.Comments;
import com.example.road_journal.Adapters.CommentsAdapter;
import com.example.road_journal.R;import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CommentsActivity extends AppCompatActivity {
    RecyclerView.Adapter adapter;
    FirebaseAuth auth;
    AuthStateListener authStateListener;
    public String comment;
    EditText comments;
    RecyclerView commentsrecycler;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String key;
    LinearLayoutManager linearLayoutManager;
    List<Comments> list = new ArrayList();
    Button send;
    public String uploadusername;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_comments);
        comments = (EditText) findViewById(R.id.comment);
        commentsrecycler = (RecyclerView) findViewById(R.id.commentsrecycler);
        send = (Button) findViewById(R.id.btnSendComment);
        key = getIntent().getStringExtra("key");
        uploadusername = getIntent().getStringExtra("uploadusername");
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        commentsrecycler.setLayoutManager(linearLayoutManager);
        commentsrecycler.setHasFixedSize(true);
        setupFirebaseAuth();
        send.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String str = (String) dataSnapshot.child("profileurl").getValue();
                        StringBuilder sb = new StringBuilder();
                        sb.append("onDataChange: ");
                        sb.append(str);
                        Log.d("check", sb.toString());
                        CommentsActivity.this.addcomment(str);
                    }
                });
            }
        });
    }

    public void setupFirebaseAuth() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        authStateListener = new AuthStateListener() {
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = CommentsActivity.this.auth.getCurrentUser();
                if (currentUser != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("onAuthStateChanged: ");
                    sb.append(currentUser.getUid());
                    Log.d("users", sb.toString());
                    return;
                }
                Log.d("users", "onAuthStateChanged:signed out ");
            }
        };
        databaseReference = FirebaseDatabase.getInstance().getReference("comments");
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
                CommentsActivity.this.databaseReference.orderByChild("key").equalTo(CommentsActivity.this.key).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        CommentsActivity.this.list.clear();
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            CommentsActivity.this.list.add((Comments) value.getValue(Comments.class));
                        }
                        CommentsActivity.this.adapter = new CommentsAdapter(CommentsActivity.this, CommentsActivity.this.list, (LayoutInflater) CommentsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                        CommentsActivity.this.commentsrecycler.setAdapter(CommentsActivity.this.adapter);
                    }
                });
            }
        });
    }

    public void addcomment(String str) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String displayName = currentUser.getDisplayName();
        comment = comments.getText().toString().trim();
        databaseReference = FirebaseDatabase.getInstance().getReference("comments");
        String key2 = databaseReference.push().getKey();
        DatabaseReference child = databaseReference.child(key2);
        Comments comments2 = new Comments(displayName, uploadusername, str, comment, key, currentUser.getUid(), currentDate(), key2);
        child.setValue(comments2);
        databaseReference.keepSynced(true);
        list.clear();
        comments.setText("");
    }

    private static Date currentDate() {
        return Calendar.getInstance().getTime();
    }
}
