package com.example.road_journal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.SaccoResponsePojo;
import com.example.road_journal.R;import java.util.List;

public class Sacco_Response_Adapter extends RecyclerView.Adapter<Sacco_Response_Adapter.Myviewholder> {
    private Context context;
    private List<SaccoResponsePojo> saccoResponsePojoList;

    public class Myviewholder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public TextView response;
        /* access modifiers changed from: private */
        public TextView sacconame;

        public Myviewholder(View view) {
            super(view);
            this.sacconame = (TextView) view.findViewById(R.id.sacconame);
            this.response = (TextView) view.findViewById(R.id.saccoresponse);
        }
    }

    public Sacco_Response_Adapter(Context context2, List<SaccoResponsePojo> list, LayoutInflater layoutInflater) {
        this.context = context2;
        this.saccoResponsePojoList = list;
    }

    public Myviewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Myviewholder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sacco_response_card, viewGroup, false));
    }

    public void onBindViewHolder(Myviewholder viewHolder, int i) {
        SaccoResponsePojo saccoResponsePojo = (SaccoResponsePojo) this.saccoResponsePojoList.get(i);
        Myviewholder myviewholder = (Myviewholder) viewHolder;
        TextView access$000 = myviewholder.sacconame;
        StringBuilder sb = new StringBuilder();
        sb.append("Sacco name: ");
        sb.append(saccoResponsePojo.getSacconame());
        access$000.setText(sb.toString());
        TextView access$100 = myviewholder.response;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Message: ");
        sb2.append(saccoResponsePojo.getResponse());
        access$100.setText(sb2.toString());
    }

    public int getItemCount() {
        return this.saccoResponsePojoList.size();
    }
}
