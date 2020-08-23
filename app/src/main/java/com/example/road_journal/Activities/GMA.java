package com.example.road_journal.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.FileUploadInfo;
import com.example.road_journal.Adapters.gmaAdapter;
import com.example.road_journal.R;import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GMA extends AppCompatActivity {

    Uri FilePathUri;
    int Image_Request_Code = 7;
    RecyclerView.Adapter adapter;
    private FirebaseAuth auth;

    DatabaseReference databaseReference;
    private Editor editor;
    FloatingActionButton fab;
    Intent in1;
    Intent intent = new Intent();
    Intent[] intentArray;
    LinearLayoutManager linearLayoutManager;
    List<FileUploadInfo> list = new ArrayList();
    private SharedPreferences prefs;
    ImageView profilepic;
    private int totalCount;
    RecyclerView updatesrecyler;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        prefs = getPreferences(0);
        editor = prefs.edit();
        setContentView((int) R.layout.activity_got);
        totalCount = prefs.getInt("counter2", 0);
        totalCount++;
        editor.putInt("counter2", totalCount);
        editor.commit();

        in1 = new Intent(this, gmaUpload.class);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        updatesrecyler = (RecyclerView) findViewById(R.id.updatesrecycler);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        updatesrecyler.setLayoutManager(linearLayoutManager);
        updatesrecyler.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("GMA");
        databaseReference.keepSynced(true);
        databaseReference.addChildEventListener(new ChildEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String str) {
            }

            public void onChildAdded(DataSnapshot dataSnapshot, String str) {
                GMA.this.databaseReference.keepSynced(true);
                GMA.this.databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GMA.this.list.clear();
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            GMA.this.list.add((FileUploadInfo) value.getValue(FileUploadInfo.class));
                        }
                        GMA.this.adapter = new gmaAdapter(GMA.this, GMA.this.list, (LayoutInflater) GMA.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                        GMA.this.updatesrecyler.setAdapter(GMA.this.adapter);
                    }
                });
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String str) {
                GMA.this.databaseReference.keepSynced(true);
                GMA.this.databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GMA.this.list.clear();
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            GMA.this.list.add((FileUploadInfo) value.getValue(FileUploadInfo.class));
                        }
                        GMA.this.adapter = new gmaAdapter(GMA.this, GMA.this.list, (LayoutInflater) GMA.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                        GMA.this.updatesrecyler.setAdapter(GMA.this.adapter);
                    }
                });
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                GMA.this.databaseReference.keepSynced(true);
                GMA.this.databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GMA.this.list.clear();
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            GMA.this.list.add((FileUploadInfo) value.getValue(FileUploadInfo.class));
                        }
                        GMA.this.adapter = new gmaAdapter(GMA.this, GMA.this.list, (LayoutInflater) GMA.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                        GMA.this.updatesrecyler.setAdapter(GMA.this.adapter);
                    }
                });
            }
        });

        fab.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                GMA.this.launchbuilder();
            }
        });
    }

    /* access modifiers changed from: private */
    public void launchbuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Choose an upload source");
        builder.setPositiveButton((CharSequence) "Device", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                GMA.this.pickimage();
            }
        });
        builder.setNegativeButton((CharSequence) "Camera", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                GMA.this.captureimage();
            }
        });
        builder.show();
    }

    /* access modifiers changed from: private */
    public void captureimage() {
        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 3);
    }

    /* access modifiers changed from: private */
    public void pickimage() {
        if (VERSION.SDK_INT < 19) {
            intent.setAction("android.intent.action.GET_CONTENT");
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
            return;
        }
        intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
    }


    public void onActivityResult(int i, int i2, Intent intent2) {
        super.onActivityResult(i, i2, intent2);
        if (i == Image_Request_Code && i2 == -1 && intent2 != null && intent2.getData() != null) {
            FilePathUri = intent2.getData();
            in1.putExtra("uri", FilePathUri.toString());
            in1.putExtra(Param.SOURCE, "GMA");
            in1.putExtra("ext", MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().toString()));
            startActivity(in1);
        } else if (i == 3 && i2 == -1 && intent2 != null && intent2.getData() != null) {
            FilePathUri = intent2.getData();
            in1.putExtra("uri", FilePathUri.toString());
            in1.putExtra(Param.SOURCE, "GMA");
            in1.putExtra("ext", MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().toString()));
            startActivity(in1);
        }
    }
}
