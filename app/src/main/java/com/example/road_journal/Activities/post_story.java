package com.example.road_journal.Activities;

import android.app.AlertDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.road_journal.Pojos.travel_pojo;
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
import java.util.Calendar;
import java.util.Date;

public class post_story extends AppCompatActivity {
    Uri FilePathUri;
    Uri FileUri;
    int Image_Request_Code = 7;
    int PERMISSION_ALL = 1;
    String Storage_Path = "All_Image_Uploads/";
    Bitmap bmp;
    DatabaseReference databaseReference;
    EditText editText;
    EditText editText1;
    private ExifInterface exifObject;
    String getpathfromuri;
    ImageView imageview;
    Intent intent = new Intent();
    double progress;
    ProgressDialog progressDialog;
    RelativeLayout relativeLayout;
    StorageReference storageReference;
    String story;
    String title;
    UploadTask uploadTask;
    FirebaseUser user;

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
        imageview.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                post_story.this.launchbuilder();
            }
        });
        progressDialog = new ProgressDialog(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("TravelStory");
        user = FirebaseAuth.getInstance().getCurrentUser();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Choose an upload source");
        builder.setPositiveButton((CharSequence) "Device", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                post_story.this.pickimage();
            }
        });
        builder.setNegativeButton((CharSequence) "Camera", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                post_story.this.captureimage();
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
        if (menuItem.getItemId() != R.id.action_done) {
            Toast.makeText(this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();
        } else if (FileUri != null) {
            progressDialog.setMessage("Posting");
            progressDialog.show();
            StorageReference reference = FirebaseStorage.getInstance().getReference();
            StringBuilder sb = new StringBuilder();
            sb.append(Storage_Path);
            sb.append(System.currentTimeMillis());
            storageReference = reference.child(sb.toString());
            uploadTask = storageReference.putFile(FileUri);
            uploadTask.continueWithTask(new Continuation<TaskSnapshot, Task<Uri>>() {
                public Task<Uri> then(@NonNull Task<TaskSnapshot> task) throws Exception {
                    if (task.isSuccessful()) {
                        return post_story.this.storageReference.getDownloadUrl();
                    }
                    throw task.getException();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        String valueOf = String.valueOf((Uri) task.getResult());
                        String key = post_story.this.databaseReference.push().getKey();
                        if (post_story.this.editText != null) {
                            post_story.this.title = post_story.this.editText.getText().toString().trim();
                        }
                        if (post_story.this.editText1 != null) {
                            post_story.this.story = post_story.this.editText1.getText().toString().trim();
                        }
                        DatabaseReference child = post_story.this.databaseReference.child(key);
                        travel_pojo travel_pojo = new travel_pojo(post_story.this.title, post_story.this.story, valueOf, post_story.this.currentDate(), key, 0, post_story.this.user.getDisplayName(), post_story.this.user.getUid());
                        child.setValue(travel_pojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Snackbar.make((View) post_story.this.relativeLayout, (CharSequence) "Post Uploaded", Snackbar.LENGTH_LONG).setAction((CharSequence) "DONE", (OnClickListener) new OnClickListener() {
                                    public void onClick(View view) {
                                        post_story.this.startActivity(new Intent(post_story.this, TravelBlog.class));
                                    }
                                }).show();
                            }
                        });
                    }
                }
            });
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* access modifiers changed from: private */
    public static Date currentDate() {
        return Calendar.getInstance().getTime();
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
