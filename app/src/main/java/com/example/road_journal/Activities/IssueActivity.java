package com.example.road_journal.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.road_journal.Pojos.IssuePojo;
import com.example.road_journal.R;import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;
import com.google.firebase.database.FirebaseDatabase;

public class IssueActivity extends AppCompatActivity {
    public String Email;
    public String Message;
    public String Name;
    public String Phone;
    EditText email;
    Intent intent = new Intent();
    String issue;
    EditText message;
    EditText name;
    EditText phone;
    String photo;
    DatabaseReference reference;
    String sacconame;
    Button submit;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_issue);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        message = (EditText) findViewById(R.id.message);
        submit = (Button) findViewById(R.id.send);
        sacconame = getIntent().getStringExtra("sacconame");
        issue = getIntent().getStringExtra("issue");
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        submit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (IssueActivity.this.name != null) {
                    IssueActivity.this.Name = IssueActivity.this.name.getText().toString().trim();
                }
                if (IssueActivity.this.email != null) {
                    IssueActivity.this.Email = IssueActivity.this.email.getText().toString().trim();
                }
                if (IssueActivity.this.phone != null) {
                    IssueActivity.this.Phone = IssueActivity.this.phone.getText().toString().trim();
                }
                if (IssueActivity.this.message != null) {
                    IssueActivity.this.Message = IssueActivity.this.message.getText().toString().trim();
                    IssueActivity.this.reference = FirebaseDatabase.getInstance().getReference("All_Issues");
                    DatabaseReference push = IssueActivity.this.reference.push();
                    IssuePojo issuePojo = new IssuePojo(IssueActivity.this.Name, IssueActivity.this.Email, IssueActivity.this.Phone, IssueActivity.this.Message, IssueActivity.this.sacconame, IssueActivity.this.issue, currentUser.getUid());
                    push.setValue((Object) issuePojo, (CompletionListener) new CompletionListener() {
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            Toast.makeText(IssueActivity.this, "Done", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
