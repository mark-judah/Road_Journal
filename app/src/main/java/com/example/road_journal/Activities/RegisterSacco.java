package com.example.road_journal.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.road_journal.Pojos.register_sacco;
import com.example.road_journal.R;import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterSacco extends AppCompatActivity {
    CardView cardView;
    DatabaseReference databaseReference;
    String e_mail;
    EditText email;
    String p_assword;
    EditText password;
    TextView pendingmessage;
    String phone_number;
    EditText phonenumber;
    Button register;
    String sacco_name;
    EditText sacconame;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_register_sacco);
        sacconame = (EditText) findViewById(R.id.input_name);
        email = (EditText) findViewById(R.id.input_email);
        phonenumber = (EditText) findViewById(R.id.input_phone);
        password = (EditText) findViewById(R.id.input_password);
        pendingmessage = (TextView) findViewById(R.id.pending_message);
        cardView = (CardView) findViewById(R.id.cardView);
        register = (Button) findViewById(R.id.btn_register);
        register.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                RegisterSacco.this.launchbuilder();
            }
        });
    }

    /* access modifiers changed from: private */
    public void register() {
        databaseReference = FirebaseDatabase.getInstance().getReference("pending saccos");
        if (sacconame != null) {
            sacco_name = sacconame.getText().toString().trim();
        }
        if (email != null) {
            e_mail = email.getText().toString().trim();
        }
        if (phonenumber != null) {
            phone_number = phonenumber.getText().toString().trim();
        }
        if (password != null) {
            p_assword = password.getText().toString().trim();
        }
        databaseReference.push().setValue((Object) new register_sacco(sacco_name, e_mail, phone_number, p_assword), (CompletionListener) new CompletionListener() {
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                RegisterSacco.this.success();
            }
        });
    }

    public void launchbuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Submit Account creation request?");
        builder.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                RegisterSacco.this.register();
            }
        });
        builder.setNegativeButton((CharSequence) "Edit", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    public void success() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage((CharSequence) "Your account request will be processed within 48hrs");
        builder.setPositiveButton((CharSequence) "Done", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                RegisterSacco.this.startActivity(new Intent(RegisterSacco.this, MainActivity.class));
            }
        });
        builder.show();
    }
}
