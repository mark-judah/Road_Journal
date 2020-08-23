package com.example.road_journal.Activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.road_journal.R;import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Change_password extends AppCompatActivity {
    Button button;
    EditText editText;
    EditText editText1;
    EditText editText2;
    EditText editText3;
    String email;
    /* access modifiers changed from: private */
    public FirebaseAuth firebaseAuth;
    LinearLayout linearLayout;
    String password1;
    String password2;
    String password3;
    ProgressDialog progressDialog;
    /* access modifiers changed from: private */
    public FirebaseUser user;
    Context context;
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_change_password);
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        editText = (EditText) findViewById(R.id.originalpass);
        editText1 = (EditText) findViewById(R.id.newpass);
        editText2 = (EditText) findViewById(R.id.confirmnewpass);
        editText3 = (EditText) findViewById(R.id.emai_l);
        linearLayout = (LinearLayout) findViewById(R.id.rel_layout);
        button = (Button) findViewById(R.id.passwordbutton);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Change_password.this.password1 = Change_password.this.editText.getText().toString().trim();
                Change_password.this.password2 = Change_password.this.editText2.getText().toString().trim();
                Change_password.this.password3 = Change_password.this.editText1.getText().toString().trim();
                Change_password.this.email = Change_password.this.editText3.getText().toString().trim();
                if (Change_password.this.editText3.getText().toString().equalsIgnoreCase("")) {
                    Change_password.this.editText3.setError("Required field");
                } else if (Change_password.this.editText2.getText().toString().equalsIgnoreCase("")) {
                    Change_password.this.editText2.setError("Required field");
                } else if (Change_password.this.editText1.getText().toString().equalsIgnoreCase("")) {
                    Change_password.this.editText1.setError("Required field");
                } else if (Change_password.this.editText.getText().toString().equalsIgnoreCase("")) {
                    Change_password.this.editText.setError("Required field");
                } else if (Change_password.this.password3.equals(Change_password.this.password2)) {
                    Change_password.this.editText2.setError("Passwords do not match");
                } else if (Change_password.this.password2.length() < 6) {
                    Change_password.this.progressDialog.dismiss();
                    Change_password.this.editText2.setError("Password too short");
                } else {
                    Change_password.this.progressDialog.show();
                    Change_password.this.user = Change_password.this.firebaseAuth.getCurrentUser();
                    Change_password.this.user.reauthenticate(EmailAuthProvider.getCredential(Change_password.this.email, Change_password.this.password1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Change_password.this.user.updatePassword(Change_password.this.password2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            Change_password.this.progressDialog.dismiss();
                                            Snackbar.make((View) Change_password.this.linearLayout, (CharSequence) "An error occurred.Try again later", BaseTransientBottomBar.LENGTH_LONG).show();
                                            return;
                                        }
                                        Change_password.this.progressDialog.dismiss();
                                        Change_password.this.success();
                                    }
                                });
                                return;
                            }
                            Change_password.this.progressDialog.dismiss();
                            Snackbar.make((View) Change_password.this.linearLayout, (CharSequence) "Authentication Failed", BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void success() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Password changed successfully");
        builder.setPositiveButton((CharSequence) "Done", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Change_password.this.startActivity(new Intent(Change_password.this, MainActivity.class));
            }
        });
        builder.show();
    }
}
