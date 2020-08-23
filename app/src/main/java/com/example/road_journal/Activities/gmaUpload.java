package com.example.road_journal.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
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

import com.example.road_journal.Pojos.FileUploadInfo;
import com.example.road_journal.Pojos.User;
import com.example.road_journal.R;import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class gmaUpload extends AppCompatActivity implements OnClickListener, OnMenuItemClickListener {
    Uri FileUri;
    String Storage_Path = "Got my attention/";
    Bitmap bmp;
    public Button btnupload;
    String city;
    public String comment;
    DatabaseReference databaseReference;
    EditText editText;
    private ExifInterface exifObject;
    String extension;
    String getpathfromuri;
    ImageView imageView2;
    String incidentlocation;
    String incidentroad;
    EditText location;
    double progress;
    ProgressDialog progressDialog;
    RelativeLayout relativeLayout;
    EditText road;
    StorageReference storageReference;
    EditText town;
    String updatetype;
    UploadTask uploadTask;
    String uripath;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.upload);
        bmp = null;
        uripath = getIntent().getStringExtra("uri");
        FileUri = Uri.parse(uripath);
        getpathfromuri = getFileName(FileUri);
        extension = getIntent().getStringExtra("ext");
        editText = (EditText) findViewById(R.id.editText);
        location = (EditText) findViewById(R.id.location);
        road = (EditText) findViewById(R.id.road);
        town = (EditText) findViewById(R.id.town);
        try {
            bmp = Media.getBitmap(getContentResolver(), Uri.parse(uripath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            exifObject = new ExifInterface(getpathfromuri);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        Bitmap rotateBitmap = rotateBitmap(bmp, exifObject.getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 0));
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView2.setImageBitmap(rotateBitmap);
        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("GMA");
        btnupload = (Button) findViewById(R.id.upload);
        relativeLayout = (RelativeLayout) findViewById(R.id.snackbarhere);
        btnupload.setOnClickListener(this);
        town.setInputType(0);
        town.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                gmaUpload.this.getWindow().setSoftInputMode(2);
                PopupMenu popupMenu = new PopupMenu(gmaUpload.this, view);
                popupMenu.setOnMenuItemClickListener(gmaUpload.this);
                popupMenu.inflate(R.menu.regions);
                popupMenu.show();
            }
        });
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        switch (i) {
            case 2:
                matrix.setScale(-1.0f, 1.0f);
                break;
            case 3:
                matrix.setRotate(180.0f);
                break;
            case 4:
                matrix.setRotate(180.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 5:
                matrix.setRotate(90.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 6:
                matrix.setRotate(90.0f);
                break;
            case 7:
                matrix.setRotate(-90.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 8:
                matrix.setRotate(-90.0f);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return createBitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFileName(Uri uri) {
        String str = null;
        if (uri.getScheme().equals(Param.CONTENT)) {
            Cursor query = getContentResolver().query(uri, null, null, null, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        str = query.getString(query.getColumnIndex("_display_name"));
                    }
                } catch (Throwable th) {
                    query.close();
                    throw th;
                }
            }
            query.close();
        }
        if (str != null) {
            return str;
        }
        String path = uri.getPath();
        int lastIndexOf = path.lastIndexOf(47);
        return lastIndexOf != -1 ? path.substring(lastIndexOf + 1) : path;
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
                        gmaUpload.this.uploadtofirebase(user.getProfileurl());
                        return;
                    }
                    gmaUpload.this.uploadtofirebase(str);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void uploadtofirebase(String str) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = currentUser.getUid();
        final String displayName = currentUser.getDisplayName();
        final String email = currentUser.getEmail();
        if (editText != null) {
            comment = editText.getText().toString().trim();
        }
        if (location != null) {
            incidentlocation = location.getText().toString().trim();
        }
        if (road != null) {
            incidentroad = road.getText().toString().trim();
        }
        if (town != null) {
            city = town.getText().toString().trim();
        }
        if (FileUri != null) {
            progressDialog.setMessage("Posting");
            progressDialog.show();
            StorageReference reference = FirebaseStorage.getInstance().getReference();
            StringBuilder sb = new StringBuilder();
            sb.append(Storage_Path);
            sb.append(System.currentTimeMillis());
            storageReference = reference.child(sb.toString());
            uploadTask = storageReference.putFile(FileUri);
            Task continueWithTask = uploadTask.continueWithTask(new Continuation<TaskSnapshot, Task<Uri>>() {
                public Task<Uri> then(@NonNull Task<TaskSnapshot> task) throws Exception {
                    if (task.isSuccessful()) {
                        return gmaUpload.this.storageReference.getDownloadUrl();
                    }
                    throw task.getException();
                }
            });
            final String str2 = str;
            OnCompleteListener r1 = new OnCompleteListener<Uri>() {
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        String valueOf = String.valueOf((Uri) task.getResult());
                        String key = gmaUpload.this.databaseReference.push().getKey();
                        String str = key;
                        String str2 = key;
                        DatabaseReference child = gmaUpload.this.databaseReference.child(key);

                        FileUploadInfo fileUploadInfo2 = new FileUploadInfo(uid, gmaUpload.this.comment, str2, valueOf, "", email, displayName, gmaUpload.this.incidentlocation, gmaUpload.this.comment, gmaUpload.this.incidentroad, displayName, Integer.valueOf(1), gmaUpload.this.currentDate(), gmaUpload.this.city, str, str2);
                        child.setValue(fileUploadInfo2).addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                gmaUpload.this.progressDialog.dismiss();
                                Snackbar.make((View) gmaUpload.this.relativeLayout, (CharSequence) "Post Uploaded", Snackbar.LENGTH_LONG).setAction((CharSequence) "DONE", (OnClickListener) new OnClickListener() {
                                    public void onClick(View view) {
                                        gmaUpload.this.startActivity(new Intent(gmaUpload.this, GMA.class));
                                    }
                                }).show();
                            }
                        });
                    }
                }
            };
            continueWithTask.addOnCompleteListener(r1);
            return;
        }
        Toast.makeText(this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();
    }

    /* access modifiers changed from: private */
    public static Date currentDate() {
        return Calendar.getInstance().getTime();
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
}
