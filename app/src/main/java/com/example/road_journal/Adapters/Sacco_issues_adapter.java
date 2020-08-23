package com.example.road_journal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.Sacco_issues_pojo;
import com.example.road_journal.Activities.SaccoResponse;
import com.example.road_journal.R;import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class Sacco_issues_adapter extends RecyclerView.Adapter<Sacco_issues_adapter.Myviewholder> {
    /* access modifiers changed from: private */
    public Context context;
    private List<Sacco_issues_pojo> sacco_issues_pojoList;

    public class Myviewholder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public TextView email;
        FloatingActionButton floatingActionButton;
        /* access modifiers changed from: private */
        public TextView message;
        /* access modifiers changed from: private */
        public TextView phonenumber;
        /* access modifiers changed from: private */
        public TextView username;

        public Myviewholder(View view) {
            super(view);
            this.username = (TextView) view.findViewById(R.id.reporters_username);
            this.email = (TextView) view.findViewById(R.id.reporters_email);
            this.phonenumber = (TextView) view.findViewById(R.id.reporters_phonenumber);
            this.message = (TextView) view.findViewById(R.id.reporters_message);
            this.floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabreply);
        }
    }

    public Sacco_issues_adapter(Context context2, List<Sacco_issues_pojo> list, LayoutInflater layoutInflater) {
        this.context = context2;
        this.sacco_issues_pojoList = list;
    }

    public Myviewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Myviewholder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sacco_issues_item, viewGroup, false));
    }

    public void onBindViewHolder(Myviewholder viewHolder, int i) {
        final Sacco_issues_pojo sacco_issues_pojo = (Sacco_issues_pojo) this.sacco_issues_pojoList.get(i);
        Myviewholder myviewholder = (Myviewholder) viewHolder;
        TextView access$000 = myviewholder.username;
        StringBuilder sb = new StringBuilder();
        sb.append("Username: ");
        sb.append(sacco_issues_pojo.getName());
        access$000.setText(sb.toString());
        TextView access$100 = myviewholder.email;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Email: ");
        sb2.append(sacco_issues_pojo.getEmail());
        access$100.setText(sb2.toString());
        TextView access$200 = myviewholder.phonenumber;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Contact: ");
        sb3.append(sacco_issues_pojo.getPhone());
        access$200.setText(sb3.toString());
        TextView access$300 = myviewholder.message;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("Message: ");
        sb4.append(sacco_issues_pojo.getMessage());
        access$300.setText(sb4.toString());
        myviewholder.floatingActionButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Sacco_issues_adapter.this.context, SaccoResponse.class);
                intent.putExtra("uid", sacco_issues_pojo.getUid());
                Sacco_issues_adapter.this.context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.sacco_issues_pojoList.size();
    }
}
