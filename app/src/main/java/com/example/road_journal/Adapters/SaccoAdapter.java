package com.example.road_journal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.SaccoPOJO;
import com.example.road_journal.Activities.user_portal;
import com.example.road_journal.R;import java.util.List;

public class SaccoAdapter extends RecyclerView.Adapter<SaccoAdapter.MyViewHolder> {
    Context context;
    List<SaccoPOJO> saccoPOJOList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public RelativeLayout relativeLayout;
        /* access modifiers changed from: private */
        public TextView sacconame;

        public MyViewHolder(View view) {
            super(view);
            this.sacconame = (TextView) view.findViewById(R.id.sacconame);
            this.relativeLayout = (RelativeLayout) view.findViewById(R.id.rellayout);
        }
    }

    public SaccoAdapter(Context context2, List<SaccoPOJO> list) {
        this.context = context2;
        this.saccoPOJOList = list;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sacco_recycler, viewGroup, false));
    }

    public void onBindViewHolder(final MyViewHolder viewHolder, int i) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        myViewHolder.sacconame.setText(((SaccoPOJO) this.saccoPOJOList.get(i)).getSacco_name());
        myViewHolder.relativeLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SaccoAdapter.this.userportal(viewHolder.getAdapterPosition());
            }
        });
    }

    /* access modifiers changed from: private */
    public void userportal(int i) {
        String sacco_name = ((SaccoPOJO) this.saccoPOJOList.get(i)).getSacco_name();
        Intent intent = new Intent(this.context, user_portal.class);
        intent.putExtra("sacco name", sacco_name);
        this.context.startActivity(intent);
    }

    public int getItemCount() {
        return this.saccoPOJOList.size();
    }
}
