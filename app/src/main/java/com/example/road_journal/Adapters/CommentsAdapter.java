package com.example.road_journal.Adapters;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.Comments;
import com.bumptech.glide.Glide;
import com.example.road_journal.R;import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CommentsAdapter  extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>  {
    private static final int DAY_MILLIS = 86400000;
    private static final int HOUR_MILLIS = 3600000;
    private static final int MINUTE_MILLIS = 60000;
    private static final int SECOND_MILLIS = 1000;
    List<Comments> commentslist;
    Context context;
    private LayoutInflater inflater;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView comments;
        public ImageView delete;
        public ImageView profilepic;
        public TextView time;
        public TextView username;

        public ViewHolder(View view) {
            super(view);
            this.comments = (TextView) view.findViewById(R.id.tvComment);
            this.profilepic = (ImageView) view.findViewById(R.id.profilepicture);
            this.username = (TextView) view.findViewById(R.id.profileusername);
            this.time = (TextView) view.findViewById(R.id.time);
            this.delete = (ImageView) view.findViewById(R.id.imgdelete);
        }
    }

    public CommentsAdapter(Context context2, List<Comments> list, LayoutInflater layoutInflater) {
        this.commentslist = list;
        this.context = context2;
        this.inflater = layoutInflater;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.inflater = LayoutInflater.from(viewGroup.getContext());
        return new ViewHolder(this.inflater.inflate(R.layout.comments_recycler_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Comments comments = (Comments) this.commentslist.get(i);
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        viewHolder2.comments.setText(comments.getComment());
        TextView textView = viewHolder2.username;
        StringBuilder sb = new StringBuilder();
        sb.append(comments.getReplyusername());
        sb.append("  ");
        textView.setText(sb.toString());
        Glide.with(this.context).load(comments.getReplyprofilepic()).into(viewHolder2.profilepic);
        viewHolder2.time.setText(getTimeAgo(comments.getDate()));
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(comments.getUid())) {
            viewHolder2.delete.setVisibility(View.VISIBLE);
            viewHolder2.delete.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Builder builder = new Builder(CommentsAdapter.this.context);
                    builder.setMessage("Delete this comment?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference("comments").child(comments.getCommentkey()).setValue(null);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                }
            });
        }
    }

    private static Date currentDate() {
        return Calendar.getInstance().getTime();
    }

    public static String getTimeAgo(Date date) {
        long time = date.getTime();
        if (time < 1000000000000L) {
            time *= 1000;
        }
        long time2 = currentDate().getTime();
        if (time > time2 || time <= 0) {
            return "In the future";
        }
        long j = time2 - time;
        if (j < 60000) {
            return "MOMENTS AGO";
        }
        if (j < 120000) {
            return "1 MINUTE";
        }
        if (j < 3600000) {
            StringBuilder sb = new StringBuilder();
            sb.append(j / 60000);
            sb.append(" MINUTES");
            return sb.toString();
        } else if (j < 7200000) {
            return "1 HOUR";
        } else {
            if (j < 86400000) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(j / 3600000);
                sb2.append(" HOURS");
                return sb2.toString();
            } else if (j < 172800000) {
                return "YESTERDAY";
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(j / 86400000);
                sb3.append(" DAYS");
                return sb3.toString();
            }
        }
    }

    public int getItemCount() {
        return this.commentslist.size();
    }
}
