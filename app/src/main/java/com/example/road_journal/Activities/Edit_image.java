package com.example.road_journal.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.road_journal.Pojos.FileUploadInfo;
import com.example.road_journal.Pojos.User;
import com.bumptech.glide.Glide;
import com.example.road_journal.R;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class Edit_image extends AppCompatActivity implements OnClickListener, OnMenuItemClickListener {
    public Button btnupload;
    public String city;
    public String comment;
    public String commenttext;
    DatabaseReference databaseReference;
    Date date;
    DatabaseReference dbref;
    EditText editText;
    String extension;
    ImageView imageView2;
    public String imgurl;
    String incidentlocation;
    String incidentroad;
    public String key;
    EditText location;
    public String place;
    ProgressDialog progressDialog;
    RelativeLayout relativeLayout;
    EditText road;
    public String roadname;
    StorageReference storageReference;
    EditText town;
    String updatetype;
    long val;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.upload);
        imgurl = getIntent().getStringExtra(ImagesContract.URL);
        updatetype = getIntent().getStringExtra("updtype");
        city = getIntent().getStringExtra("city");
        roadname = getIntent().getStringExtra("road");
        place = getIntent().getStringExtra("place");
        commenttext = getIntent().getStringExtra("comment");
        extension = getIntent().getStringExtra("ext");
        key = getIntent().getStringExtra("key");
        date = new Date();
        date.setTime(getIntent().getLongExtra("date", val));
        road = (EditText) findViewById(R.id.road);
        town = (EditText) findViewById(R.id.town);
        editText = (EditText) findViewById(R.id.editText);
        location = (EditText) findViewById(R.id.location);
        StringBuilder sb = new StringBuilder();
        sb.append("Reporting on: ");
        sb.append(updatetype);
        Log.d("Incident", sb.toString());
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        Glide.with((FragmentActivity) this).load(imgurl).into(imageView2);
        editText.setText(commenttext);
        location.setText(place);
        road.setText(roadname);
        town.setText(city);
        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Data").child("Incidents");
        btnupload = (Button) findViewById(R.id.upload);
        relativeLayout = (RelativeLayout) findViewById(R.id.snackbarhere);
        btnupload.setOnClickListener(this);
        town.setInputType(0);
        town.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Edit_image.this.getWindow().setSoftInputMode(2);
                PopupMenu popupMenu = new PopupMenu(Edit_image.this, view);
                popupMenu.setOnMenuItemClickListener(Edit_image.this);
                popupMenu.inflate(R.menu.regions);
                popupMenu.show();
            }
        });
    }

    public boolean onMenuItemClick(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        switch (itemId) {
            case R.id.bungoma /*2131296316*/:
                town.setText("Bungoma");
                break;
            case R.id.busia /*2131296317*/:
                town.setText("Baringo");
                break;
            default:
                switch (itemId) {
                    case R.id.kajiado /*2131296440*/:
                        town.setText("Kajiado");
                        break;
                    case R.id.kakamega /*2131296441*/:
                        town.setText("Kakamega");
                        break;
                    case R.id.kericho /*2131296442*/:
                        town.setText("Kericho");
                        break;
                    case R.id.kiambu /*2131296443*/:
                        town.setText("Kiambu");
                        break;
                    case R.id.kilifi /*2131296444*/:
                        town.setText("Kiifi");
                        break;
                    case R.id.kirinyaga /*2131296445*/:
                        town.setText("Kirinyaga");
                        break;
                    case R.id.kisii /*2131296446*/:
                        town.setText("Kisii");
                        break;
                    case R.id.kisumu /*2131296447*/:
                        town.setText("Kisumu");
                        break;
                    case R.id.kitui /*2131296448*/:
                        town.setText("Kitui");
                        break;
                    case R.id.kwale /*2131296449*/:
                        town.setText("Kwale");
                        break;
                    case R.id.laikipia /*2131296450*/:
                        town.setText("Laikipia");
                        break;
                    case R.id.lamu /*2131296451*/:
                        town.setText("Lamu");
                        break;
                    default:
                        switch (itemId) {
                            case R.id.machakos /*2131296471*/:
                                town.setText("Machakos");
                                break;
                            case R.id.makueni /*2131296472*/:
                                town.setText("Makueni");
                                break;
                            case R.id.mandera /*2131296473*/:
                                town.setText("Mandera");
                                break;
                            default:
                                switch (itemId) {
                                    case R.id.muranga /*2131296524*/:
                                        town.setText("Muranga");
                                        break;
                                    case R.id.nairobi /*2131296525*/:
                                        town.setText("Nairobi");
                                        break;
                                    case R.id.nakuru /*2131296526*/:
                                        town.setText("Nakuru");
                                        break;
                                    default:
                                        switch (itemId) {
                                            case R.id.nandi /*2131296528*/:
                                                town.setText("Nandi");
                                                break;
                                            case R.id.narok /*2131296529*/:
                                                town.setText("Nakuru");
                                                break;
                                            default:
                                                switch (itemId) {
                                                    case R.id.baringo /*2131296306*/:
                                                        town.setText("Baringo");
                                                        break;
                                                    case R.id.bomet /*2131296311*/:
                                                        town.setText("Bomet");
                                                        break;
                                                    case R.id.elgeyo /*2131296369*/:
                                                        town.setText("Elgeyo Marakwet");
                                                        break;
                                                    case R.id.embu /*2131296372*/:
                                                        town.setText("Embu");
                                                        break;
                                                    case R.id.garissa /*2131296399*/:
                                                        town.setText("Garissa");
                                                        break;
                                                    case R.id.homabay /*2131296404*/:
                                                        town.setText("Homa Bay");
                                                        break;
                                                    case R.id.isiolo /*2131296434*/:
                                                        town.setText("Isiolo");
                                                        break;
                                                    case R.id.marsabit /*2131296477*/:
                                                        town.setText("Marsabit");
                                                        break;
                                                    case R.id.meru /*2131296513*/:
                                                        town.setText("Meru");
                                                        break;
                                                    case R.id.migori /*2131296516*/:
                                                        town.setText("Migori");
                                                        break;
                                                    case R.id.mombasa /*2131296518*/:
                                                        town.setText("Mombasa");
                                                        break;
                                                    case R.id.nyamira /*2131296547*/:
                                                        town.setText("Nyamira");
                                                        break;
                                                    case R.id.nyeri /*2131296549*/:
                                                        town.setText("Nyeri");
                                                        break;
                                                    case R.id.samburu /*2131296608*/:
                                                        town.setText("Samburu");
                                                        break;
                                                    case R.id.siaya /*2131296636*/:
                                                        town.setText("Siaya");
                                                        break;
                                                    case R.id.taita /*2131296666*/:
                                                        town.setText("Taita Taveta");
                                                        break;
                                                    case R.id.tharaka /*2131296692*/:
                                                        town.setText("Tharaka Nithi");
                                                        break;
                                                    case R.id.transnzoia /*2131296708*/:
                                                        town.setText("Trans Nzoia");
                                                        break;
                                                    case R.id.turkana /*2131296711*/:
                                                        town.setText("Turkana");
                                                        break;
                                                    case R.id.uasingishu /*2131296717*/:
                                                        town.setText("Uasin Gishu");
                                                        break;
                                                    case R.id.vihiga /*2131296734*/:
                                                        town.setText("Vihiga");
                                                        break;
                                                    case R.id.wajir /*2131296736*/:
                                                        town.setText("Wajir");
                                                        break;
                                                    case R.id.westpokot /*2131296738*/:
                                                        town.setText("West Pokot");
                                                        break;
                                                }
                                        }
                                }
                        }
                }
        }
        return false;
    }

    public void onClick(View view) {
        if (view == btnupload) {
            FirebaseDatabase.getInstance().getReference("Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(DatabaseError databaseError) {
                }

                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = (User) dataSnapshot.getValue(User.class);
                    String str = "";
                    if (user != null) {
                        Edit_image.this.uploadtofirebase(user.getProfileurl());
                        return;
                    }
                    Edit_image.this.uploadtofirebase(str);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void uploadtofirebase(String str) {
        progressDialog.show();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = currentUser.getUid();
        final String displayName = currentUser.getDisplayName();
        final String email = currentUser.getEmail();
        if (editText != null) {
            comment = editText.getText().toString().trim();
        }
        if (town != null) {
            city = town.getText().toString().trim();
        }
        if (location != null) {
            incidentlocation = location.getText().toString().trim();
        }
        if (road != null) {
            incidentroad = road.getText().toString().trim();
        }
        if (editText == null && town == null && location == null && road == null) {
            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_LONG).show();
            return;
        }
        Task value = databaseReference.child(key).setValue(null);
        final String str2 = str;
        OnCompleteListener r1 = new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                DatabaseReference child = Edit_image.this.databaseReference.child(Edit_image.this.key);

                FileUploadInfo fileUploadInfo2 = new FileUploadInfo(uid, Edit_image.this.comment, str2, Edit_image.this.imgurl, Edit_image.this.updatetype, email, displayName, Edit_image.this.incidentlocation, null, Edit_image.this.incidentroad, displayName, Integer.valueOf(1), Edit_image.this.date, Edit_image.this.city, Edit_image.this.key, Edit_image.this.key);
                child.setValue(fileUploadInfo2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    public void onSuccess(Void voidR) {
                        Snackbar.make((View) Edit_image.this.relativeLayout, (CharSequence) "Post Uploaded", Snackbar.LENGTH_LONG).setAction((CharSequence) "DONE", (OnClickListener) new OnClickListener() {
                            public void onClick(View view) {
                                Edit_image.this.startActivity(new Intent(Edit_image.this, MainActivity.class));
                            }
                        }).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    public void onFailure(@NonNull Exception exc) {
                        Edit_image.this.progressDialog.dismiss();
                        Toast.makeText(Edit_image.this, exc.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Edit_image.this.progressDialog.dismiss();
                    }
                });
            }
        };
        value.addOnCompleteListener(r1);
    }
}
