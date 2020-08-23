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
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.road_journal.Pojos.FileUploadInfo;
import com.example.road_journal.Pojos.User;
import com.example.road_journal.R;import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class Uploadtext extends AppCompatActivity implements OnMenuItemClickListener {
    Button button;
    String city;
    DatabaseReference databaseReference;
    DatabaseReference dbref;
    EditText incident;
    String incidentlocation;
    String incidentroad;
    String incidenttext;
    EditText place;
    ProgressDialog progressDialog;
    RelativeLayout relativeLayout;
    EditText road;
    EditText town;
    String updatetype;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_uploadtext);
        updatetype = getIntent().getStringExtra("updtype");
        town = (EditText) findViewById(R.id.town);
        road = (EditText) findViewById(R.id.road);
        place = (EditText) findViewById(R.id.location);
        incident = (EditText) findViewById(R.id.incident);
        button = (Button) findViewById(R.id.uploadtext);
        relativeLayout = (RelativeLayout) findViewById(R.id.snkbarhere);
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);
        town.setInputType(0);
        town.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Uploadtext.this.getWindow().setSoftInputMode(2);
                PopupMenu popupMenu = new PopupMenu(Uploadtext.this, view);
                popupMenu.setOnMenuItemClickListener(Uploadtext.this);
                popupMenu.inflate(R.menu.regions);
                popupMenu.show();
            }
        });
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Profile").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = (User) dataSnapshot.getValue(User.class);
                        String str = "";
                        if (user != null) {
                            Uploadtext.this.uploadtofirebase(user.getProfileurl());
                            Uploadtext.this.progressDialog.setMessage("Uploading...");
                            Uploadtext.this.progressDialog.show();
                            return;
                        }
                        Uploadtext.this.uploadtofirebase(str);
                        Uploadtext.this.progressDialog.setMessage("Uploading...");
                        Uploadtext.this.progressDialog.show();
                    }
                });
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

    /* access modifiers changed from: private */
    public void uploadtofirebase(String str) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Data").child("Incidents");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currentUser.getEmail();
        String uid = currentUser.getUid();
        String displayName = currentUser.getDisplayName();
        if (incident != null) {
            incidenttext = incident.getText().toString().trim();
        }
        if (town != null) {
            city = town.getText().toString().trim();
        }
        if (place != null) {
            incidentlocation = place.getText().toString().trim();
        }
        if (road != null) {
            incidentroad = road.getText().toString().trim();
            String key = databaseReference.push().getKey();
            String str2 = key;
            String str3 = key;
            DatabaseReference child = databaseReference.child(key);
            FileUploadInfo fileUploadInfo2 = new FileUploadInfo(uid, null, str, null, updatetype, email, displayName, incidentlocation, incidenttext, incidentroad, displayName, Integer.valueOf(0), currentDate(), city, str2, str3);
            child.setValue((Object) fileUploadInfo2, (CompletionListener) new CompletionListener() {
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    Uploadtext.this.progressDialog.dismiss();
                    Snackbar.make((View) Uploadtext.this.relativeLayout, (CharSequence) "Post Uploaded", Snackbar.LENGTH_LONG).setAction((CharSequence) "DONE", (OnClickListener) new OnClickListener() {
                        public void onClick(View view) {
                            Uploadtext.this.startActivity(new Intent(Uploadtext.this, MainActivity.class));
                        }
                    }).show();
                    StringBuilder sb = new StringBuilder();
                    sb.append("onComplete: ");
                    sb.append(Uploadtext.this.currentDate());
                    Log.d("Date", sb.toString());
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public static Date currentDate() {
        return Calendar.getInstance().getTime();
    }
}
