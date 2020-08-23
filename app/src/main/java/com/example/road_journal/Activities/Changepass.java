package com.example.road_journal.Activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.road_journal.R;import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;


public class Changepass extends AppCompatActivity {
    EditText email;
    FirebaseAuth firebaseAuth;
    Button next;
    ProgressDialog progressDialog;

    /* access modifiers changed from: protected */
    @TargetApi(23)
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.changepass);
        firebaseAuth = FirebaseAuth.getInstance();
        next = (Button) findViewById(R.id.next);
        email = (EditText) findViewById(R.id.email);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing");
        next.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Changepass.this.progressDialog.show();
                /*Changepass.this.firebaseAuth.fetchProvidersForEmail(Changepass.this.email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                    public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                        if (!(!((ProviderQueryResult) task.getResult()).getProviders().isEmpty())) {
                            Changepass.this.progressDialog.dismiss();
                            Changepass.this.email.setError("Email not found");
                            return;
                        }
                        Changepass.this.progressDialog.dismiss();
                        Changepass.this.check();
                    }
                });*/
                firebaseAuth.fetchSignInMethodsForEmail(Changepass.this.email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            Changepass.this.progressDialog.dismiss();
                            Changepass.this.email.setError("Email not found");
                            return;
                        }
                        Changepass.this.progressDialog.dismiss();
                        Changepass.this.check();
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public void check() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Account found. Send email with reset instructions?");
        builder.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Changepass.this.progressDialog.show();
                Changepass.this.sendemail();
            }
        });
        builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    public void sendemail() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Changepass.this.progressDialog.dismiss();
                    Changepass.this.success();
                    return;
                }
                Changepass.this.progressDialog.dismiss();
                Toast.makeText(Changepass.this, "An error occured", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void success() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "You will receive an email with reset instructions");
        builder.setPositiveButton((CharSequence) "Done", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Changepass.this.startActivity(new Intent(Changepass.this, MainActivity.class));
            }
        });
        builder.show();
    }
}
