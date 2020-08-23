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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest.Builder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements OnClickListener {
    EditText _emailText;
    TextView _loginLink;
    EditText _nameText;
    EditText _passwordText;
    Button _signupButton;
    DatabaseReference databaseReference;
    /* access modifiers changed from: private */
    public FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.sign_up);
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        _nameText = (EditText) findViewById(R.id.input_name);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _signupButton = (Button) findViewById(R.id.btn_signup);
        _loginLink = (TextView) findViewById(R.id.link_login);
        progressDialog = new ProgressDialog(this);
        _signupButton.setOnClickListener(this);
        _loginLink.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view == _signupButton) {
            registeruser();
        }
        if (view == _loginLink) {
            finish();
            startActivity(new Intent(this, login.class));
        }
    }

    private void registeruser() {
        final String trim = _emailText.getText().toString().trim();
        String trim2 = _passwordText.getText().toString().trim();
        final String trim3 = _nameText.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            Toast.makeText(this, "No field should be empty", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(trim2)) {
            Toast.makeText(this, "No field should be empty", Toast.LENGTH_LONG).show();
        } else {
            progressDialog.setMessage("Registering User...");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(trim, trim2).addOnCompleteListener((Activity) this, (OnCompleteListener<AuthResult>) new OnCompleteListener<AuthResult>() {
                public void onComplete(@NonNull Task<AuthResult> task) {
                    SignUp.this.progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = SignUp.this.firebaseAuth.getCurrentUser();
                        currentUser.updateProfile(new Builder().setDisplayName(trim3).build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "Registered Succesfully", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        FirebaseDatabase.getInstance().getReference().child("UserAccounts").push().setValue(new Acct_type(trim3, trim, currentUser.getUid(), "normal"));
                        SignUp.this.startActivity(new Intent(SignUp.this, MainActivity.class));
                        return;
                    }
                    Toast.makeText(SignUp.this, "Registration Failed.Please try again", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
