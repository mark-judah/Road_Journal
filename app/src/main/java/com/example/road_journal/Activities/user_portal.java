package com.example.road_journal.Activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.UserPortalPojo;
import com.example.road_journal.Adapters.User_portal_adapter;
import com.example.road_journal.R;import java.util.ArrayList;
import java.util.List;

public class user_portal extends AppCompatActivity {
    private GridLayoutManager lLayout;

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private boolean includeEdge;
        private int spacing;
        private int spanCount;

        public GridSpacingItemDecoration(int i, int i2, boolean z) {
            spanCount = i;
            spacing = i2;
            includeEdge = z;
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
            int i = childAdapterPosition % spanCount;
            if (includeEdge) {
                rect.left = spacing - ((spacing * i) / spanCount);
                rect.right = ((i + 1) * spacing) / spanCount;
                if (childAdapterPosition < spanCount) {
                    rect.top = spacing;
                }
                rect.bottom = spacing;
                return;
            }
            rect.left = (spacing * i) / spanCount;
            rect.right = spacing - (((i + 1) * spacing) / spanCount);
            if (childAdapterPosition >= spanCount) {
                rect.top = spacing;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_user_portal);
        getSupportActionBar().setTitle((CharSequence) getIntent().getStringExtra("sacco name"));
        List allItemList = getAllItemList();
        lLayout = new GridLayoutManager(this, 2);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.portalrecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(1), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(lLayout);
        recyclerView.setAdapter(new User_portal_adapter(this, allItemList));
    }

    private List<UserPortalPojo> getAllItemList() {
        String stringExtra = getIntent().getStringExtra("sacco name");
        ArrayList arrayList = new ArrayList();
        arrayList.add(new UserPortalPojo("", R.drawable.complaints, "COMPLAINTS", stringExtra));
        arrayList.add(new UserPortalPojo("", R.drawable.compliments, "COMPLIMENTS", stringExtra));
        arrayList.add(new UserPortalPojo("", R.drawable.lostitem, "LOST ITEMS", stringExtra));
        arrayList.add(new UserPortalPojo("", R.drawable.other, "OTHER", stringExtra));
        arrayList.add(new UserPortalPojo("", R.drawable.inbox, "INBOX", stringExtra));
        return arrayList;
    }

    private int dpToPx(int i) {
        return Math.round(TypedValue.applyDimension(1, (float) i, getResources().getDisplayMetrics()));
    }
}
