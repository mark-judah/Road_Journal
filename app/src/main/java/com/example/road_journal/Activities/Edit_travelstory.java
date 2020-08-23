package com.example.road_journal.Activities;


import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.road_journal.Pojos.travel_pojo;
import com.bumptech.glide.Glide;
import com.example.road_journal.R;import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

import java.io.IOException;
import java.util.Date;

public class Edit_travelstory extends AppCompatActivity {
    Uri FilePathUri;
    Uri FileUri;
    int Image_Request_Code = 7;
    int PERMISSION_ALL = 1;
    String Storage_Path = "All_Image_Uploads/";
    Bitmap bmp;
    DatabaseReference databaseReference;
    Date date;
    EditText editText;
    EditText editText1;
    private ExifInterface exifObject;
    String getpathfromuri;
    ImageView imageview;
    Intent intent = new Intent();
    TextView post;
    double progress;
    ProgressDialog progressDialog;
    RelativeLayout relativeLayout;
    StorageReference storageReference;
    String story;
    String title;
    TextView tvtitle;
    UploadTask uploadTask;
    String url;
    FirebaseUser user;
    long val;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_post_story);
        String[] strArr = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"};
        if (!hasPermissions(this, strArr)) {
            ActivityCompat.requestPermissions(this, strArr, PERMISSION_ALL);
        }
        imageview = (ImageView) findViewById(R.id.post_image);
        editText = (EditText) findViewById(R.id.storytitle);
        editText1 = (EditText) findViewById(R.id.storytext);
        relativeLayout = (RelativeLayout) findViewById(R.id.snackbar_here);
        tvtitle = (TextView) findViewById(R.id.tvttile);
        post = (TextView) findViewById(R.id.tvpost);
        tvtitle.setVisibility(0);
        post.setVisibility(0);
        imageview.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Edit_travelstory.this.launchbuilder();
            }
        });
        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("TravelStory");
        user = FirebaseAuth.getInstance().getCurrentUser();
        date = new Date();
        date.setTime(getIntent().getLongExtra("time", val));
        StringBuilder sb = new StringBuilder();
        sb.append("onCreate: ");
        sb.append(date);
        Log.d("dated", sb.toString());
        url = getIntent().getStringExtra("imageurl");
        Glide.with((FragmentActivity) this).load(url).into(imageview);
        title = getIntent().getStringExtra("title");
        editText.setText(title);
        story = getIntent().getStringExtra("story");
        editText1.setText(story);
    }

    public static boolean hasPermissions(Context context, String... strArr) {
        if (!(context == null || strArr == null)) {
            for (String checkSelfPermission : strArr) {
                if (ContextCompat.checkSelfPermission(context, checkSelfPermission) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void launchbuilder() {
        Builder builder = new Builder(this);
        builder.setMessage((CharSequence) "Choose an upload source");
        builder.setPositiveButton((CharSequence) "Device", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Edit_travelstory.this.pickimage();
            }
        });
        builder.setNegativeButton((CharSequence) "Camera", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Edit_travelstory.this.captureimage();
            }
        });
        builder.show();
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

    /* access modifiers changed from: private */
    public void captureimage() {
        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 3);
    }

    public void onActivityResult(int i, int i2, Intent intent2) {
        super.onActivityResult(i, i2, intent2);
        if (i == Image_Request_Code && i2 == -1 && intent2 != null && intent2.getData() != null) {
            FilePathUri = intent2.getData();
            FileUri = Uri.parse(String.valueOf(FilePathUri));
            getpathfromuri = getFileName(FileUri);
            try {
                bmp = Media.getBitmap(getContentResolver(), Uri.parse(String.valueOf(FilePathUri)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                exifObject = new ExifInterface(getpathfromuri);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            imageview.setImageBitmap(rotateBitmap(bmp, exifObject.getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 0)));
        } else if (i == 3 && i2 == -1 && intent2 != null && intent2.getData() != null) {
            FilePathUri = intent2.getData();
            FileUri = Uri.parse(String.valueOf(FilePathUri));
            getpathfromuri = getFileName(FileUri);
            try {
                bmp = Media.getBitmap(getContentResolver(), Uri.parse(String.valueOf(FilePathUri)));
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            try {
                exifObject = new ExifInterface(getpathfromuri);
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            imageview.setImageBitmap(rotateBitmap(bmp, exifObject.getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 0)));
        }
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_done) {
            Builder builder = new Builder(this);
            builder.setMessage((CharSequence) "Edit Blog");
            builder.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (Edit_travelstory.this.FileUri != null) {
                        Edit_travelstory.this.progressDialog.setMessage("Editing");
                        Edit_travelstory.this.progressDialog.show();
                        Edit_travelstory edit_travelstory = Edit_travelstory.this;
                        StorageReference reference = FirebaseStorage.getInstance().getReference();
                        StringBuilder sb = new StringBuilder();
                        sb.append(Edit_travelstory.this.Storage_Path);
                        sb.append(System.currentTimeMillis());
                        edit_travelstory.storageReference = reference.child(sb.toString());
                        Edit_travelstory.this.uploadTask = Edit_travelstory.this.storageReference.putFile(Edit_travelstory.this.FileUri);
                        Edit_travelstory.this.uploadTask.continueWithTask(new Continuation<TaskSnapshot, Task<Uri>>() {
                            public Task<Uri> then(@NonNull Task<TaskSnapshot> task) throws Exception {
                                if (task.isSuccessful()) {
                                    return Edit_travelstory.this.storageReference.getDownloadUrl();
                                }
                                throw task.getException();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    String valueOf = String.valueOf((Uri) task.getResult());
                                    String key = Edit_travelstory.this.databaseReference.push().getKey();
                                    if (Edit_travelstory.this.editText != null) {
                                        Edit_travelstory.this.title = Edit_travelstory.this.editText.getText().toString().trim();
                                    }
                                    if (Edit_travelstory.this.editText1 != null) {
                                        Edit_travelstory.this.story = Edit_travelstory.this.editText1.getText().toString().trim();
                                    }
                                    DatabaseReference child = Edit_travelstory.this.databaseReference.child(key);
                                    travel_pojo travel_pojo = new travel_pojo(Edit_travelstory.this.title, Edit_travelstory.this.story, valueOf, Edit_travelstory.this.date, key, 0, Edit_travelstory.this.user.getDisplayName(), Edit_travelstory.this.user.getUid());
                                    child.setValue(travel_pojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Edit_travelstory.this.progressDialog.dismiss();
                                            Snackbar.make((View) Edit_travelstory.this.relativeLayout, (CharSequence) "Post Edited", Snackbar.LENGTH_LONG).setAction((CharSequence) "DONE", (OnClickListener) new OnClickListener() {
                                                public void onClick(View view) {
                                                    Edit_travelstory.this.startActivity(new Intent(Edit_travelstory.this, TravelBlog.class));
                                                }
                                            }).show();
                                        }
                                    });
                                }
                            }
                        });
                        return;
                    }
                    String stringExtra = Edit_travelstory.this.getIntent().getStringExtra("key");
                    String stringExtra2 = Edit_travelstory.this.getIntent().getStringExtra("imageurl");
                    if (Edit_travelstory.this.editText != null) {
                        Edit_travelstory.this.title = Edit_travelstory.this.editText.getText().toString().trim();
                    }
                    if (Edit_travelstory.this.editText1 != null) {
                        Edit_travelstory.this.story = Edit_travelstory.this.editText1.getText().toString().trim();
                    }
                    DatabaseReference child = Edit_travelstory.this.databaseReference.child(stringExtra);
                    travel_pojo travel_pojo = new travel_pojo(Edit_travelstory.this.title, Edit_travelstory.this.story, stringExtra2, Edit_travelstory.this.date, stringExtra, 0, Edit_travelstory.this.user.getDisplayName(), Edit_travelstory.this.user.getUid());
                    child.setValue(travel_pojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Edit_travelstory.this, "Edited Successfully", Toast.LENGTH_LONG).show();
                            Edit_travelstory.this.startActivity(new Intent(Edit_travelstory.this, TravelBlog.class));
                        }
                    });
                }
            });

            builder.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(menuItem);
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
}
