package com.example.road_journal.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.road_journal.Pojos.SaccoResponsePojo;
import com.example.road_journal.R;import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;

public class SaccoResponse extends AppCompatActivity {
    Button btn;
    String complaint_uid;
    DatabaseReference databaseReference;
    EditText editText;
    String key;
    String response;
    String sacconame;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_sacco_response);
        editText = (EditText) findViewById(R.id.editText3);
        btn = (Button) findViewById(R.id.button);
        complaint_uid = getIntent().getStringExtra("uid");
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Sacco Response");
        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SaccoResponse.this.sacconame = currentUser.getDisplayName();
                SaccoResponse.this.response = SaccoResponse.this.editText.getText().toString().trim();
                SaccoResponse.this.key = SaccoResponse.this.databaseReference.push().getKey();
                SaccoResponse.this.databaseReference.child(SaccoResponse.this.key).setValue((Object) new SaccoResponsePojo(SaccoResponse.this.sacconame, SaccoResponse.this.complaint_uid, SaccoResponse.this.response, SaccoResponse.this.key), (CompletionListener) new CompletionListener() {
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Toast.makeText(SaccoResponse.this, "Done", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
