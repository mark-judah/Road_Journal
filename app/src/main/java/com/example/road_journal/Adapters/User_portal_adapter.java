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

import com.example.road_journal.Pojos.UserPortalPojo;
import com.bumptech.glide.Glide;
import com.example.road_journal.Activities.IssueActivity;
import com.example.road_journal.Activities.UserInbox;
import com.example.road_journal.R;import java.util.List;

public class User_portal_adapter extends RecyclerView.Adapter<User_portal_adapter.PortalViewHolder> {
    /* access modifiers changed from: private */
    public Context context;
    private List<UserPortalPojo> itemList;

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

    public User_portal_adapter(Context context2, List<UserPortalPojo> list) {
        this.itemList = list;
        this.context = context2;
    }

    public PortalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new PortalViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_portal_recycler, null));
    }

    public void onBindViewHolder(final PortalViewHolder viewHolder, int i) {
        UserPortalPojo userPortalPojo = (UserPortalPojo) this.itemList.get(i);
        PortalViewHolder portalViewHolder = (PortalViewHolder) viewHolder;
        Glide.with(this.context).load(Integer.valueOf(userPortalPojo.getImage())).into(portalViewHolder.imageView);
        portalViewHolder.title.setText(userPortalPojo.getTitle());
        portalViewHolder.imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                User_portal_adapter.this.issueactivity(viewHolder.getAdapterPosition());
            }
        });
        if (i == 4) {
            portalViewHolder.imageView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    User_portal_adapter.this.context.startActivity(new Intent(User_portal_adapter.this.context, UserInbox.class));
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void issueactivity(int i) {
        UserPortalPojo userPortalPojo = (UserPortalPojo) this.itemList.get(i);
        String saconame = userPortalPojo.getSaconame();
        String title = userPortalPojo.getTitle();
        Intent intent = new Intent(this.context, IssueActivity.class);
        intent.putExtra("sacconame", saconame);
        intent.putExtra("issue", title);
        this.context.startActivity(intent);
    }

    public int getItemCount() {
        return this.itemList.size();
    }
}
