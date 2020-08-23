package com.example.road_journal.Activities;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.road_journal.Pojos.SpeedPojo;
import com.example.road_journal.R;import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ReportSpeed extends AppCompatActivity implements OnClickListener {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 200;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    static EditText comment;
    static ProgressDialog dialog;
    static TextView dist;
    static long endTime;
    static ProgressDialog locate;
    static EditText numberplate;

    /* renamed from: p */
    static int f46p;
    static EditText road;
    static TextView speed;
    static long startTime;
    static boolean status;
    static TextView time;
    boolean GPSstatus = false;
    Button button;
    Button button2;
    DatabaseReference databaseReference;
    double lat;
    LocationManager locationManager;
    double lon;
    LocationService myService;
    Button pause;
    ImageView proofimage;

    /* renamed from: sc */
    private ServiceConnection f47sc = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LocationService.LocalBinder localBinder = (LocationService.LocalBinder) iBinder;
            ReportSpeed.this.myService = localBinder.getService();
            ReportSpeed.this.status = true;
        }

        public void onServiceDisconnected(ComponentName componentName) {
            ReportSpeed.this.status = false;
        }
    };
    Button start;
    Button stop;
    FirebaseUser user;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_report_speed);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        button = (Button) findViewById(R.id.getspeed);
        checkLocationPermission();
        statuscheck();
        button.setOnClickListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        dialog = new ProgressDialog(this);
        speed = (TextView) findViewById(R.id.speedtext);
        numberplate = (EditText) findViewById(R.id.numplate);
        road = (EditText) findViewById(R.id.road);
        comment = (EditText) findViewById(R.id.comment);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_done) {
            update();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void statuscheck() {
        if (!((LocationManager) getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("gps")) {
            showGPSDisabledAlertToUser();
        } else {
            GPSstatus = true;
        }
    }

    public void onClick(View view) {
        if (view == button) {
            Log.d("more", "onClick: ");
            if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                checkGps();
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled("gps")) {
                    if (!status) {
                        bindService();
                    }
                    locate = new ProgressDialog(this);
                    locate.setIndeterminate(true);
                    locate.setCancelable(false);
                    locate.setMessage("Locating...");
                    locate.show();
                }
            } else {
                checkLocationPermission();
            }
        }
    }

    public void update() {
        if (numberplate.getText().toString().trim().equals("")) {
            Toast.makeText(this, "No field should be empty", Toast.LENGTH_LONG).show();
        } else if (road.getText().toString().trim().equals("")) {
            Toast.makeText(this, "No field should be empty", Toast.LENGTH_LONG).show();
        } else if (comment.getText().toString().trim().equals("")) {
            Toast.makeText(this, "No field should be empty", Toast.LENGTH_LONG).show();
        } else {
            if (LocationService.mCurrentLocation != null) {
                lat = LocationService.mCurrentLocation.getLatitude();
                lon = LocationService.mCurrentLocation.getLongitude();
                String valueOf = String.valueOf(new LatLng(lat, lon));
                databaseReference = FirebaseDatabase.getInstance().getReference("NTSA");
                dialog.setMessage("Reporting");
                dialog.show();
                String key = databaseReference.push().getKey();
                StringBuilder sb = new StringBuilder();
                sb.append("updateSpeed: ");
                sb.append(key);
                Log.d("key", sb.toString());
                SpeedPojo speedPojo = new SpeedPojo(user.getDisplayName(), user.getEmail(), numberplate.getText().toString().trim(), road.getText().toString().trim(), comment.getText().toString().trim(), speed.getText().toString().trim(), valueOf, key, getTime());
                databaseReference.child(key).setValue(speedPojo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    public void onSuccess(Void voidR) {
                        ReportSpeed.this.dialog.dismiss();
                        Toast.makeText(ReportSpeed.this, "Done", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(this, "Get speed first!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getTime() {
        Calendar instance = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(instance.get(3));
        sb.append(":");
        sb.append(instance.get(5));
        sb.append(":");
        sb.append(instance.get(11));
        sb.append(":");
        sb.append(instance.get(12));
        return sb.toString();
    }

    /* access modifiers changed from: 0000 */
    public void checkGps() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled("gps")) {
            showGPSDisabledAlertToUser();
        }
    }

    private void showGPSDisabledAlertToUser() {
        Builder builder = new Builder(this);
        builder.setMessage("Enable GPS to use application").setCancelable(false).setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ReportSpeed.this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.ACCESS_FINE_LOCATION")) {
                new Builder(this).setTitle("Location Permission Needed").setMessage("This app needs the Location permission, please accept to use location functionality").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(ReportSpeed.this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 99);
                    }
                }).create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 99);
            }
        }
        if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 200);
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 99) {
            if (iArr.length <= 0 || iArr[0] != 0) {
                Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
            } else {
                int checkSelfPermission = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION");
            }
        } else if (i == 200) {
            if (iArr.length <= 0 || iArr[0] != 0) {
                Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
            } else {
                int checkSelfPermission2 = ContextCompat.checkSelfPermission(this, "android.permission.CAMERA");
            }
        }
    }

    public void finish() {
        super.finish();
        System.exit(0);
    }

    /* access modifiers changed from: 0000 */
    public void bindService() {
        if (!status) {
            bindService(new Intent(getApplicationContext(), LocationService.class), f47sc, 1);
            status = true;
            startTime = System.currentTimeMillis();
        }
    }

    /* access modifiers changed from: 0000 */
    public void unbindService() {
        if (status) {
            new Intent(getApplicationContext(), LocationService.class);
            unbindService(f47sc);
            status = false;
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
    }
}
