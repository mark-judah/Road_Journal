package com.example.road_journal.Activities;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.road_journal.Pojos.User;
import com.bumptech.glide.Glide;
import com.example.road_journal.R;import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class Edit_profile extends AppCompatActivity {
    private static final int PICKFILE_REQUEST_CODE = 8778;
    Uri ProfilepciUri;
    Bitmap bitmap;
    Bitmap bmp;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    DatabaseReference databaseReference3;
    DatabaseReference databaseReference4;
    DatabaseReference databaseReference5;
    ImageView editname;
    Intent intent = new Intent();
    ImageView profilepic;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    Button submit;
    UploadTask uploadTask;
    EditText useremail;
    EditText username;
    TextView usernametxt;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_edit_profile);
        profilepic = (ImageView) findViewById(R.id.profilepic);
        editname = (ImageView) findViewById(R.id.imageView4);
        username = (EditText) findViewById(R.id.editText2);
        usernametxt = (TextView) findViewById(R.id.textView4);
        storageReference = FirebaseStorage.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Data").child("Incidents");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Profile").child(currentUser.getUid());
        databaseReference3 = FirebaseDatabase.getInstance().getReference("GMA");
        databaseReference4 = FirebaseDatabase.getInstance().getReference("UserAccounts");
        databaseReference5 = FirebaseDatabase.getInstance().getReference("comments");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing");
        progressDialog.setCancelable(false);
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                String str = (String) dataSnapshot.child("profileurl").getValue();
                String str2 = (String) dataSnapshot.child("user_name").getValue();
                StringBuilder sb = new StringBuilder();
                sb.append("onDataChange: ");
                sb.append(str);
                sb.append(str2);
                sb.append(dataSnapshot);
                Log.d("VALS", sb.toString());
                TextView textView = usernametxt;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Username: ");
                sb2.append(str2);
                textView.setText(sb2.toString());
                if (str != null) {
                    Glide.with(getApplicationContext()).load(str).into(profilepic);
                } else {
                    Glide.with(getApplicationContext()).load(Integer.valueOf(R.drawable.profileavatar)).into(profilepic);
                }
            }
        });
        profilepic.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                profilebuilder();
            }
        });
        editname.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                editusername();
            }
        });
    }

    /* access modifiers changed from: private */
    public void profilebuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Change Profile picture?");
        builder.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (VERSION.SDK_INT < 19) {
                    intent.setAction("android.intent.action.GET_CONTENT");
                    intent.setType("*/*");
                    startActivityForResult(Intent.createChooser(intent, "Please Select Image"), PICKFILE_REQUEST_CODE);
                    return;
                }
                intent = new Intent("android.intent.action.OPEN_DOCUMENT");
                intent.addCategory("android.intent.category.OPENABLE");
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), PICKFILE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    public void onActivityResult(int i, int i2, Intent intent2) {
        super.onActivityResult(i, i2, intent2);
        if (i == PICKFILE_REQUEST_CODE && i2 == -1 && intent2 != null && intent2.getData() != null) {
            ProfilepciUri = intent2.getData();
            updateprofilepic(ProfilepciUri);
        }
    }

    public void updateprofilepic(Uri uri) {
        updatemain(uri);
        updatecomments(uri);
    }

    private void updatemain(final Uri uri) {
        progressDialog.show();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser.updateProfile(new UserProfileChangeRequest.Builder().setPhotoUri(uri).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.setMessage("Posting");
                    progressDialog.show();

                    StorageReference reference = FirebaseStorage.getInstance().getReference();
                    StringBuilder sb = new StringBuilder();
                    sb.append("profile pics");
                    sb.append(System.currentTimeMillis());
                    Edit_profile.this.storageReference = reference.child(sb.toString());
                    uploadTask = storageReference.putFile(uri);
                    uploadTask.continueWithTask(new Continuation<TaskSnapshot, Task<Uri>>() {
                        public Task<Uri> then(@NonNull Task<TaskSnapshot> task) throws Exception {
                            if (task.isSuccessful()) {
                                return storageReference.getDownloadUrl();
                            }
                            throw task.getException();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                final String valueOf = String.valueOf((Uri) task.getResult());
                                Glide.with(getApplicationContext()).load(valueOf).into(profilepic);
                                databaseReference = FirebaseDatabase.getInstance().getReference("Data").child("Incidents");
                                databaseReference.orderByChild("userId").equalTo(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    public void onCancelled(DatabaseError databaseError) {
                                    }

                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        ArrayList arrayList = new ArrayList();
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                            arrayList.add(dataSnapshot2.getKey());
                                            String valueOf = String.valueOf(arrayList);
                                            String substring = valueOf.substring(1, valueOf.length() - 1);
                                            HashMap hashMap = new HashMap();
                                            hashMap.put("profileurl", valueOf);
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("onDataChange: ");
                                            sb.append(substring);
                                            Log.d("arraykey", sb.toString());
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append("onDataChange: ");
                                            sb2.append(dataSnapshot2.getKey());
                                            Log.d("normalgetkey", sb2.toString());
                                            databaseReference.child(substring).updateChildren(hashMap);
                                            arrayList.clear();
                                        }
                                        databaseReference1.child(currentUser.getUid()).setValue(new User(currentUser.getDisplayName(), currentUser.getEmail(), currentUser.getUid(), valueOf));
                                    }
                                });
                                progressDialog.dismiss();
                                Toast.makeText(Edit_profile.this, "Updated Succesfully", Toast.LENGTH_LONG).show();
                                return;
                            }
                            Toast.makeText(Edit_profile.this, "An error occured.Try again later", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void updatecomments(final Uri uri) {
        progressDialog.show();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser.updateProfile(new UserProfileChangeRequest.Builder().setPhotoUri(uri).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.setMessage("Posting");
                    progressDialog.show();
                    StorageReference reference = FirebaseStorage.getInstance().getReference();
                    StringBuilder sb = new StringBuilder();
                    sb.append("profile pics");
                    sb.append(System.currentTimeMillis());
                    Edit_profile.this.storageReference = reference.child(sb.toString());
                    uploadTask = storageReference.putFile(uri);
                    uploadTask.continueWithTask(new Continuation<TaskSnapshot, Task<Uri>>() {
                        public Task<Uri> then(@NonNull Task<TaskSnapshot> task) throws Exception {
                            if (task.isSuccessful()) {
                                return storageReference.getDownloadUrl();
                            }
                            throw task.getException();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                final String valueOf = String.valueOf((Uri) task.getResult());
                                Glide.with(getApplicationContext()).load(valueOf).into(profilepic);
                                databaseReference = FirebaseDatabase.getInstance().getReference("comments");
                                databaseReference.orderByChild("uid").equalTo(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    public void onCancelled(DatabaseError databaseError) {
                                    }

                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        ArrayList arrayList = new ArrayList();
                                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                            arrayList.add(dataSnapshot2.getKey());
                                            String valueOf = String.valueOf(arrayList);
                                            String substring = valueOf.substring(1, valueOf.length() - 1);
                                            HashMap hashMap = new HashMap();
                                            hashMap.put("replyprofilepic", valueOf);
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("onDataChange: ");
                                            sb.append(substring);
                                            Log.d("arraykey", sb.toString());
                                            StringBuilder sb2 = new StringBuilder();
                                            sb2.append("onDataChange: ");
                                            sb2.append(dataSnapshot2.getKey());
                                            Log.d("normalgetkey", sb2.toString());
                                            databaseReference.child(substring).updateChildren(hashMap);
                                            arrayList.clear();
                                        }
                                        databaseReference1.child(currentUser.getUid()).setValue(new User(currentUser.getDisplayName(), currentUser.getEmail(), currentUser.getUid(), valueOf));
                                    }
                                });
                                progressDialog.dismiss();
                                Toast.makeText(Edit_profile.this, "Updated Succesfully", Toast.LENGTH_LONG).show();
                                return;
                            }
                            Toast.makeText(Edit_profile.this, "An error occured.Try again later", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void editusername() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Change username?");
        builder.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                edit();
            }
        });
        builder.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    /* access modifiers changed from: private */
    public void edit() {
        progressDialog.show();
        FirebaseAuth.getInstance().getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(username.getText().toString().trim()).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                update1();
                update2();
                update3();
                update4();
                update5();
                progressDialog.dismiss();
                success();
            }
        });
    }

    public void update1() {
        databaseReference.orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList arrayList = new ArrayList();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    arrayList.add(dataSnapshot2.getKey());
                    String valueOf = String.valueOf(arrayList);
                    String substring = valueOf.substring(1, valueOf.length() - 1);
                    HashMap hashMap = new HashMap();
                    hashMap.put("usernam", username.getText().toString().trim());
                    StringBuilder sb = new StringBuilder();
                    sb.append("onDataChange: ");
                    sb.append(substring);
                    Log.d("arraykey", sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onDataChange: ");
                    sb2.append(dataSnapshot2.getKey());
                    Log.d("normalgetkey", sb2.toString());
                    databaseReference.child(substring).updateChildren(hashMap);
                    arrayList.clear();
                }
            }
        });
    }

    public void update2() {
        databaseReference1.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList arrayList = new ArrayList();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    arrayList.add(dataSnapshot2.getKey());
                    String valueOf = String.valueOf(arrayList);
                    String substring = valueOf.substring(1, valueOf.length() - 1);
                    HashMap hashMap = new HashMap();
                    hashMap.put("user_name", username.getText().toString().trim());
                    StringBuilder sb = new StringBuilder();
                    sb.append("onDataChange: ");
                    sb.append(substring);
                    Log.d("arraykey", sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onDataChange: ");
                    sb2.append(dataSnapshot2.getKey());
                    Log.d("normalgetkey", sb2.toString());
                    databaseReference1.child(substring).updateChildren(hashMap);
                    arrayList.clear();
                }
            }
        });
    }

    public void update3() {
        databaseReference3.orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList arrayList = new ArrayList();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    arrayList.add(dataSnapshot2.getKey());
                    String valueOf = String.valueOf(arrayList);
                    String substring = valueOf.substring(1, valueOf.length() - 1);
                    HashMap hashMap = new HashMap();
                    hashMap.put("usernam", username.getText().toString().trim());
                    StringBuilder sb = new StringBuilder();
                    sb.append("onDataChange: ");
                    sb.append(substring);
                    Log.d("arraykey", sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onDataChange: ");
                    sb2.append(dataSnapshot2.getKey());
                    Log.d("normalgetkey", sb2.toString());
                    databaseReference3.child(substring).updateChildren(hashMap);
                    arrayList.clear();
                }
            }
        });
    }

    public void update4() {
        databaseReference4.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList arrayList = new ArrayList();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    arrayList.add(dataSnapshot2.getKey());
                    String valueOf = String.valueOf(arrayList);
                    String substring = valueOf.substring(1, valueOf.length() - 1);
                    HashMap hashMap = new HashMap();
                    hashMap.put("user_name", username.getText().toString().trim());
                    StringBuilder sb = new StringBuilder();
                    sb.append("onDataChange: ");
                    sb.append(substring);
                    Log.d("arraykey", sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onDataChange: ");
                    sb2.append(dataSnapshot2.getKey());
                    Log.d("normalgetkey", sb2.toString());
                    databaseReference4.child(substring).updateChildren(hashMap);
                    arrayList.clear();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void update5() {
        databaseReference5.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList arrayList = new ArrayList();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    arrayList.add(dataSnapshot2.getKey());
                    String valueOf = String.valueOf(arrayList);
                    String substring = valueOf.substring(1, valueOf.length() - 1);
                    HashMap hashMap = new HashMap();
                    hashMap.put("replyusername", username.getText().toString().trim());
                    StringBuilder sb = new StringBuilder();
                    sb.append("onDataChange: ");
                    sb.append(substring);
                    Log.d("arraykey", sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onDataChange: ");
                    sb2.append(dataSnapshot2.getKey());
                    Log.d("normalgetkey", sb2.toString());
                    databaseReference5.child(substring).updateChildren(hashMap);
                    arrayList.clear();
                }
            }
        });
    }

    public void success() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Uername changed succesfuly");
        builder.setPositiveButton((CharSequence) "Done", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Edit_profile.this, MainActivity.class));
            }
        });
        builder.show();
    }
}
