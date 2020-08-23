package com.example.road_journal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.SpeedPojo;
import com.example.road_journal.R;import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

public class SpeedReportAdapter extends RecyclerView.Adapter<SpeedReportAdapter.Myviewholder> {
    /* access modifiers changed from: private */
    public Context context;
    private List<SpeedPojo> speedpojo;

    public class Myviewholder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public TextView comments;
        /* access modifiers changed from: private */
        public TextView coordinates;
        FloatingActionButton fabmap;
        /* access modifiers changed from: private */
        public TextView numberplate;
        /* access modifiers changed from: private */
        public TextView roadname;
        /* access modifiers changed from: private */
        public TextView speed;

        public Myviewholder(View view) {
            super(view);
            this.numberplate = (TextView) view.findViewById(R.id.numberplate);
            this.roadname = (TextView) view.findViewById(R.id.roadname);
            this.speed = (TextView) view.findViewById(R.id.speed);
            this.coordinates = (TextView) view.findViewById(R.id.coordinates);
            this.comments = (TextView) view.findViewById(R.id.comments);
            this.fabmap = (FloatingActionButton) view.findViewById(R.id.mapview);
        }
    }

    public SpeedReportAdapter(Context context2, List<SpeedPojo> list, LayoutInflater layoutInflater) {
        this.context = context2;
        this.speedpojo = list;
    }

    public Myviewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Myviewholder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.speed_reports_card, viewGroup, false));
    }

    public void onBindViewHolder(Myviewholder viewHolder, int i) {
        final SpeedPojo speedPojo = (SpeedPojo) this.speedpojo.get(i);
        Myviewholder myviewholder = (Myviewholder) viewHolder;
        TextView access$000 = myviewholder.numberplate;
        StringBuilder sb = new StringBuilder();
        sb.append("Number Plate: ");
        sb.append(speedPojo.getNumberplate());
        access$000.setText(sb.toString());
        TextView access$100 = myviewholder.roadname;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Road: ");
        sb2.append(speedPojo.getRoadname());
        access$100.setText(sb2.toString());
        TextView access$200 = myviewholder.speed;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Speed: ");
        sb3.append(speedPojo.getSpeed());
        access$200.setText(sb3.toString());
        String valueOf = String.valueOf(speedPojo.getCordinates());
        TextView access$300 = myviewholder.coordinates;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("Coordinates: ");
        sb4.append(valueOf);
        access$300.setText(sb4.toString());
        TextView access$400 = myviewholder.comments;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("Comments: ");
        sb5.append(speedPojo.getComment());
        access$400.setText(sb5.toString());
        myviewholder.fabmap.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String[] split = speedPojo.getCordinates().split(",");
                SpeedReportAdapter.this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(String.format(Locale.ENGLISH, "geo:%s,%s", new Object[]{split[0].trim(), split[1].trim()}))));
            }
        });
    }

    public int getItemCount() {
        return this.speedpojo.size();
    }
}
