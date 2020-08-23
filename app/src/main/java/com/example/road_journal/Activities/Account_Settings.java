package com.example.road_journal.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.road_journal.R;import java.util.ArrayList;

public class Account_Settings extends AppCompatActivity {
    ListView listView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_account__settings);
        listView = (ListView) findViewById(R.id.listview);
        ArrayList arrayList = new ArrayList();
        arrayList.add("Edit Profile");
        arrayList.add("Change password");
        arrayList.add("My Posts");
        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList));
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                switch (i) {
                    case 0:
                        Account_Settings.this.startActivity(new Intent(Account_Settings.this, Edit_profile.class));
                        return;
                    case 1:
                        Account_Settings.this.startActivity(new Intent(Account_Settings.this, Change_password.class));
                        return;
                    case 2:
                        Account_Settings.this.startActivity(new Intent(Account_Settings.this, posts.class));
                        return;
                    default:
                        return;
                }
            }
        });
    }
}
