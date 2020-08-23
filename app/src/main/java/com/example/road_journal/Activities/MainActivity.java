package com.example.road_journal.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.FileUploadInfo;
import com.example.road_journal.Adapters.Recycler_Adapter;
import com.example.road_journal.R;import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String INTRO_CARD = "material_intro";
    private static final String INTRO_CARD2 = "recyclerView_material_intro";
    private static final String MENU_SEARCH_ID_TAG = "menuSearchIdTag";
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 200;
    Uri FilePathUri;
    int Image_Request_Code = 7;
    List<Acct_type> acct_typeList = new ArrayList();
    Recycler_Adapter adapter;
    /* access modifiers changed from: private */
    public FirebaseAuth auth;
    Bitmap bitmap = null;

    DatabaseReference databaseReference;
    DatabaseReference dbref;
    private ActionBarDrawerToggle drawerToggle;
    private Editor editor;
    FloatingActionButton fab;
    Intent in1;
    Intent in2;
    Intent intent = new Intent();
    LinearLayoutManager linearLayoutManager;
    List<FileUploadInfo> list = new ArrayList();
    ImageView logout;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.Map /*2131296262*/:
                    startActivity(new Intent(getApplicationContext(), live_map.class));
                    break;
                case R.id.home /*2131296405*/:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    break;
                case R.id.incidents /*2131296425*/:
                    startActivity(new Intent(getApplicationContext(), IncidentsActivity.class));
                    break;
                case R.id.region /*2131296583*/:
                    startActivity(new Intent(getApplicationContext(), RegionsList.class));
                    break;
                case R.id.sacco /*2131296601*/:
                    startActivity(new Intent(getApplicationContext(), Sacco_list.class));
                    break;
            }
            return false;
        }
    };
    NavigationView navigationView;
    private SharedPreferences prefs;
    ImageView profilepic;
    ProgressBar progressBar;
    ImageView speed;
    Toolbar toolbar;
    private int totalCount;
    RecyclerView updatesrecyler;

    public static class BottomNavigationViewHelper {
        @SuppressLint({"RestrictedApi"})
        public static void disableShiftMode(BottomNavigationView bottomNavigationView) {
            BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
            try {
                Field declaredField = bottomNavigationMenuView.getClass().getDeclaredField("mShiftingMode");
                declaredField.setAccessible(true);
                declaredField.setBoolean(bottomNavigationMenuView, false);
                declaredField.setAccessible(false);
                for (int i = 0; i < bottomNavigationMenuView.getChildCount(); i++) {
                    BottomNavigationItemView bottomNavigationItemView = (BottomNavigationItemView) bottomNavigationMenuView.getChildAt(i);
                    bottomNavigationItemView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
                    bottomNavigationItemView.setChecked(bottomNavigationItemView.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e2) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e2);
            }
        }
    }

    /* access modifiers changed from: protected */
    @TargetApi(23)
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        prefs = getPreferences(0);
        editor = prefs.edit();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, login.class));
            finish();
        }
        checkaccounttype();
        checkpermission();
        setContentView((int) R.layout.activity_main);
        totalCount = prefs.getInt("counter", 0);
        totalCount++;
        editor.putInt("counter", totalCount);
        editor.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        logout = (ImageView) findViewById(R.id.logout);
        speed = (ImageView) findViewById(R.id.speed);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        hide();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser.getUid().equals("DZ9Cazk0aBhp8CzD1XzlqrxXGyM2")) {
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.speedreports).setVisible(true);
        }
        String displayName = currentUser.getDisplayName();
        String email = currentUser.getEmail();
        View inflateHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        TextView textView = (TextView) inflateHeaderView.findViewById(R.id.user_name);
        TextView textView2 = (TextView) inflateHeaderView.findViewById(R.id.user_email);
        StringBuilder sb = new StringBuilder();
        sb.append("Username: ");
        sb.append(displayName);
        textView.setText(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Email: ");
        sb2.append(email);
        textView2.setText(sb2.toString());
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        in1 = new Intent(this, upload.class);
        in2 = new Intent(this, Uploadtext.class);
        updatesrecyler = (RecyclerView) findViewById(R.id.updatesrecycler);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        updatesrecyler.setLayoutManager(linearLayoutManager);
        updatesrecyler.setHasFixedSize(true);

        logout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                logout();
            }
        });
        speed.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ReportSpeed.class));
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fabselect);

        fab.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                    checkpermission();
                    PopupMenu popupMenu = new PopupMenu(MainActivity.this, fab);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int itemId = menuItem.getItemId();
                            if (itemId == R.id.traffic) {
                                String str = "Traffic";
                                in1.putExtra("updtype", str);
                                in2.putExtra("updtype", str);
                                launchbuilder();
                            }
                            if (itemId == R.id.weather) {
                                String str2 = "Weather";
                                in1.putExtra("updtype", str2);
                                in2.putExtra("updtype", str2);
                                launchbuilder();
                            }
                            if (itemId == R.id.accident) {
                                String str3 = "Accident";
                                in1.putExtra("updtype", str3);
                                in2.putExtra("updtype", str3);
                                launchbuilder();
                            }
                            if (itemId == R.id.security) {
                                String str4 = "Security";
                                in1.putExtra("updtype", str4);
                                in2.putExtra("updtype", str4);
                                launchbuilder();
                            }
                            if (itemId == R.id.road_divertion) {
                                String str5 = "Road divertion";
                                in1.putExtra("updtype", str5);
                                in2.putExtra("updtype", str5);
                                launchbuilder();
                            }
                            if (itemId == R.id.dangerous_driving) {
                                String str6 = "Dangerous driving";
                                in1.putExtra("updtype", str6);
                                in2.putExtra("updtype", str6);
                                launchbuilder();
                            }
                            if (itemId == R.id.construction_works) {
                                String str7 = "Construction Works";
                                in1.putExtra("updtype", str7);
                                in2.putExtra("updtype", str7);
                                launchbuilder();
                            }
                            if (itemId == R.id.fire) {
                                String str8 = "Fire";
                                in1.putExtra("updtype", str8);
                                in2.putExtra("updtype", str8);
                                launchbuilder();
                            }
                            return true;
                        }
                    });
                    popupMenu.show();

            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Data").child("Incidents");
        databaseReference.keepSynced(true);
        databaseReference.addChildEventListener(new ChildEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String str) {
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            public void onChildAdded(DataSnapshot dataSnapshot, String str) {
                databaseReference.keepSynced(true);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("onChildChanged: ");
                            sb.append(dataSnapshot2);
                            Log.d("snapshot", sb.toString());
                            list.add((FileUploadInfo) dataSnapshot2.getValue(FileUploadInfo.class));
                        }
                        adapter = new Recycler_Adapter(MainActivity.this, list, (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                        updatesrecyler.setAdapter(adapter);
                    }
                });
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String str) {
                databaseReference.keepSynced(true);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("onChildChanged: ");
                            sb.append(dataSnapshot2);
                            Log.d("snapshot", sb.toString());
                            list.add((FileUploadInfo) dataSnapshot2.getValue(FileUploadInfo.class));
                        }
                        adapter = new Recycler_Adapter(MainActivity.this, list, (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                        updatesrecyler.setAdapter(adapter);
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public void checkpermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 200);
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (iArr.length <= 0 || iArr[0] != 0) {
            Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
        } else {
            int checkSelfPermission = ContextCompat.checkSelfPermission(this, "android.permission.CAMERA");
        }
    }



    private void hide() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.sacco_portal).setVisible(false);
        menu.findItem(R.id.speedreports).setVisible(false);
    }

    /* access modifiers changed from: private */
    public void operations(String str) {
        if (str.equals("sacco")) {
            ((NavigationView) findViewById(R.id.nav_view)).getMenu().findItem(R.id.sacco_portal).setVisible(true);
        } else {
            ((NavigationView) findViewById(R.id.nav_view)).getMenu().findItem(R.id.sacco_portal).setVisible(false);
        }
    }

    private void checkaccounttype() {
        dbref = FirebaseDatabase.getInstance().getReference("UserAccounts");
        dbref.orderByChild("uid").equalTo(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("onDataChange: ");
                    sb.append(dataSnapshot2);
                    Log.d("values", sb.toString());
                    String type = ((Acct_type) dataSnapshot2.getValue(Acct_type.class)).getType();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onDataChange: ");
                    sb2.append(type);
                    Log.d("account_type", sb2.toString());
                    operations(type);
                }
            }
        });
    }

    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen((int) GravityCompat.START)) {
            drawerLayout.closeDrawer((int) GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        drawerToggle.onConfigurationChanged(configuration);
    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.account_settings /*2131296270*/:
                startActivity(new Intent(this, Account_Settings.class));
                break;
            case R.id.fav /*2131296386*/:
                startActivity(new Intent(this, FetchFavorite.class));
                break;
            case R.id.nav_got_my_attention /*2131296530*/:
                startActivity(new Intent(this, GMA.class));
                break;
            case R.id.nav_home /*2131296531*/:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.nav_incident /*2131296532*/:
                startActivity(new Intent(this, IncidentsActivity.class));
                break;
            case R.id.nav_maps /*2131296533*/:
                startActivity(new Intent(this, live_map.class));
                break;
            case R.id.regions /*2131296585*/:
                startActivity(new Intent(this, RegionsList.class));
                break;
            case R.id.sacco_portal /*2131296603*/:
                startActivity(new Intent(this, Sacco_portal.class));
                break;
            case R.id.speed /*2131296645*/:
                startActivity(new Intent(this, ReportSpeed.class));
                break;
            case R.id.speedreports /*2131296647*/:
                startActivity(new Intent(this, SpeedReports.class));
                break;
            case R.id.travel_story /*2131296710*/:
                startActivity(new Intent(this, TravelBlog.class));
                break;
        }
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer((int) GravityCompat.START);
        return true;
    }

    /* access modifiers changed from: private */
    public void launchbuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Choose an upload source");
        builder.setPositiveButton((CharSequence) "Device", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                pickimage();
            }
        });
        builder.setNegativeButton((CharSequence) "Camera", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                captureimage();
            }
        });
        builder.setNeutralButton((CharSequence) "Text", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(in2);
            }
        });
        builder.show();
    }

    /* access modifiers changed from: private */
    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Do you want to logout");
        builder.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this, login.class));
                finishAffinity();
            }
        });
        builder.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
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
            in1.putExtra("uri", FilePathUri.toString());
            in1.putExtra("ext", MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().toString()));
            startActivity(in1);
        } else if (i == 3 && i2 == -1 && intent2 != null && intent2.getData() != null) {
            FilePathUri = intent2.getData();
            in1.putExtra("uri", FilePathUri.toString());
            in1.putExtra("ext", MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().toString()));
            startActivity(in1);
        }
    }
}
