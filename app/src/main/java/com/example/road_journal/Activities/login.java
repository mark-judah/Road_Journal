package com.example.road_journal.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.road_journal.R;import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity implements OnClickListener {
    private Button btnlogin;
    private FirebaseAuth firebaseAuth;
    public TextView forgotpassword;
    private EditText loginemail;
    private EditText loginpass;
    ProgressDialog progressDialog;
    public TextView textviewsignup;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.login);
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        firebaseAuth.getCurrentUser();
        loginemail = (EditText) findViewById(R.id.loginemail);
        loginpass = (EditText) findViewById(R.id.loginpassword);
        btnlogin = (Button) findViewById(R.id.login);
        textviewsignup = (TextView) findViewById(R.id.textviewlogin);
        forgotpassword = (TextView) findViewById(R.id.forgot_password);
        progressDialog = new ProgressDialog(this);
        btnlogin.setOnClickListener(this);
        textviewsignup.setOnClickListener(this);
        forgotpassword.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view == btnlogin) {
            userlogin();
        }
        if (view == textviewsignup) {
            finish();
            startActivity(new Intent(this, SignUp.class));
        }
        if (view == forgotpassword) {
            finish();
            startActivity(new Intent(this, Changepass.class));
        }
    }

    private void userlogin() {
        String trim = loginemail.getText().toString().trim();
        String trim2 = loginpass.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            Toast.makeText(this, "No field should be empty", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(trim2)) {
            Toast.makeText(this, "No field should be empty", Toast.LENGTH_LONG).show();
        } else {
            progressDialog.setMessage("Signing in...");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(trim, trim2).addOnCompleteListener((Activity) this, (OnCompleteListener<AuthResult>) new OnCompleteListener<AuthResult>() {
                public void onComplete(@NonNull Task<AuthResult> task) {
                    login.this.progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        login.this.startActivity(new Intent(login.this, MainActivity.class));
                    } else {
                        Toast.makeText(login.this, "Login Failed.Please try again", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
