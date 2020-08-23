package com.example.road_journal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.SaccoPortalPojo;
import com.bumptech.glide.Glide;
import com.example.road_journal.Activities.Sacco_issues;
import com.example.road_journal.R;import java.util.List;


public class SaaccoPortalAdapter extends RecyclerView.Adapter<SaaccoPortalAdapter.PortalViewHolder> {
    /* access modifiers changed from: private */
    public Context context;
    private List<SaccoPortalPojo> itemList;

    public class PortalViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView value;

        public PortalViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.issuephoto);
            this.title = (TextView) view.findViewById(R.id.issuetitle);
        }
    }

    public SaaccoPortalAdapter(Context context2, List<SaccoPortalPojo> list) {
        this.itemList = list;
        this.context = context2;
    }

    public PortalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new PortalViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sacco_portal_pojo, null));
    }

    public void onBindViewHolder(PortalViewHolder viewHolder, int i) {
        final SaccoPortalPojo saccoPortalPojo = (SaccoPortalPojo) this.itemList.get(i);
        PortalViewHolder portalViewHolder = (PortalViewHolder) viewHolder;
        Glide.with(this.context).load(Integer.valueOf(saccoPortalPojo.getImage())).into(portalViewHolder.imageView);
        portalViewHolder.title.setText(saccoPortalPojo.getTitle());
        portalViewHolder.imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String title = saccoPortalPojo.getTitle();
                Intent intent = new Intent(SaaccoPortalAdapter.this.context, Sacco_issues.class);
                intent.putExtra("issue", title);
                SaaccoPortalAdapter.this.context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.itemList.size();
    }
}
