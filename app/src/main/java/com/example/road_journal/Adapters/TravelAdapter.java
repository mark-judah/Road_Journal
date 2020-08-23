package com.example.road_journal.Adapters;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.Favorite;
import com.example.road_journal.Pojos.travel_pojo;
import com.example.road_journal.Activities.CommentsActivity;
import com.example.road_journal.Activities.StoryView;
import com.example.road_journal.R;import com.google.android.gms.common.internal.ImagesContract;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.StoryViewHolder> {
    private static final int DAY_MILLIS = 86400000;
    private static final int HOUR_MILLIS = 3600000;
    private static final int MINUTE_MILLIS = 60000;
    private static final int SECOND_MILLIS = 1000;
    Context context;
    DatabaseReference databaseReference;
    DatabaseReference favoriteRef;
    boolean favoritechecker = false;
    private LayoutInflater inflater;
    List<travel_pojo> travel_pojoList;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public class StoryViewHolder extends RecyclerView.ViewHolder {
        public TextView author;
        CardView card;
        public ImageView comment;
        public TextView commentcount;
        public ImageView delete;
        public TextView favcount;
        public ImageView favorite;
        public ImageView share;
        TextView story;
        TextView time;
        TextView title;

        public StoryViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.story_title);
            this.story = (TextView) view.findViewById(R.id.story_intro);
            this.time = (TextView) view.findViewById(R.id.time);
            this.comment = (ImageView) view.findViewById(R.id.imgComments);
            this.card = (CardView) view.findViewById(R.id.card_view);
            this.favorite = (ImageView) view.findViewById(R.id.blankfavorite);
            this.favcount = (TextView) view.findViewById(R.id.favcount);
            this.commentcount = (TextView) view.findViewById(R.id.commentcount);
            this.author = (TextView) view.findViewById(R.id.author);
            this.delete = (ImageView) view.findViewById(R.id.delete);
            this.share = (ImageView) view.findViewById(R.id.imgshare);
            TravelAdapter.this.favoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");
            TravelAdapter.this.favoriteRef.keepSynced(true);
        }

        public void setFavBtn1(final String str) {
            TravelAdapter.this.favoriteRef.addValueEventListener(new ValueEventListener() {
                public void onCancelled(DatabaseError databaseError) {
                }

                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(str).hasChild(TravelAdapter.this.user.getUid())) {
                        StoryViewHolder.this.favorite.setImageResource(R.drawable.dark_favorite);
                    } else {
                        StoryViewHolder.this.favorite.setImageResource(R.drawable.favorite);
                    }
                }
            });
        }
    }

    public TravelAdapter(Context context2, List<travel_pojo> list, LayoutInflater layoutInflater) {
        this.travel_pojoList = list;
        this.inflater = layoutInflater;
        this.context = context2;
    }

    public StoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.inflater = LayoutInflater.from(viewGroup.getContext());
        return new StoryViewHolder(this.inflater.inflate(R.layout.story_card, viewGroup, false));
    }

    public void onBindViewHolder(final StoryViewHolder viewHolder, int i) {
        final travel_pojo travel_pojo = (travel_pojo) this.travel_pojoList.get(i);
        StoryViewHolder storyViewHolder = (StoryViewHolder) viewHolder;
        storyViewHolder.title.setText(travel_pojo.getTitle());
        storyViewHolder.story.setText(travel_pojo.getStory());
        if (this.user.getUid().equals(travel_pojo.getUid())) {
            storyViewHolder.delete.setVisibility(0);
            storyViewHolder.delete.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TravelAdapter.this.context);
                    builder.setMessage((CharSequence) "Do you want to delete this post?");
                    builder.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseAuth.getInstance().getCurrentUser();
                            FirebaseDatabase.getInstance().getReference("TravelStory").child(travel_pojo.getKey()).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                                public void onSuccess(Void voidR) {
                                    Toast.makeText(TravelAdapter.this.context, "Deleted Successfully", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    builder.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                }
            });
        }
        TextView textView = storyViewHolder.author;
        StringBuilder sb = new StringBuilder();
        sb.append("Posted By: ");
        sb.append(travel_pojo.getUsername());
        textView.setText(sb.toString());
        final String key = travel_pojo.getKey();
        storyViewHolder.setFavBtn1(key);
        storyViewHolder.comment.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TravelAdapter.this.textcomments(viewHolder.getAdapterPosition());
            }
        });
        storyViewHolder.favorite.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TravelAdapter.this.favoritechecker = true;
                TravelAdapter.this.favoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");
                TravelAdapter.this.favoriteRef.keepSynced(true);
                TravelAdapter.this.favoriteRef.addValueEventListener(new ValueEventListener() {
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (TravelAdapter.this.favoritechecker) {
                            if (dataSnapshot.child(key).hasChild(TravelAdapter.this.user.getUid())) {
                                TravelAdapter.this.favoriteRef.child(key).child(TravelAdapter.this.user.getUid()).removeValue();
                                TravelAdapter.this.favoritechecker = false;
                                TravelAdapter.this.fav_counttxt(viewHolder, key);
                                return;
                            }

                            DatabaseReference child = TravelAdapter.this.favoriteRef.child(key).child(TravelAdapter.this.user.getUid());
                            Favorite favorite2 = new Favorite("", "", "", "", TravelAdapter.this.user.getUid(), TravelAdapter.this.user.getEmail(), "", "", "", "", "", "", null, null, "", travel_pojo.getKey());
                            child.setValue(favorite2);
                            TravelAdapter.this.favoritechecker = false;
                            TravelAdapter.this.fav_counttxt(viewHolder, key);
                        }
                    }
                });
            }
        });
        fav_counttxt(viewHolder, key);
        com_counttxt(viewHolder, key);
        storyViewHolder.time.setText(getTimeAgo(travel_pojo.getTime()));
        storyViewHolder.card.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (travel_pojo.getType() == 0) {
                    long time = travel_pojo.getTime().getTime();
                    Intent intent = new Intent(TravelAdapter.this.context, StoryView.class);
                    intent.putExtra("imageurl", travel_pojo.getUrl());
                    intent.putExtra("title", travel_pojo.getTitle());
                    intent.putExtra("story", travel_pojo.getStory());
                    intent.putExtra("time", time);
                    intent.putExtra("postuserid", travel_pojo.getUid());
                    intent.putExtra("key", travel_pojo.getKey());
                    TravelAdapter.this.context.startActivity(intent);
                } else if (travel_pojo.getType() == 1) {
                    TravelAdapter.this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(travel_pojo.getUrl())));
                }
            }
        });
        storyViewHolder.share.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(TravelAdapter.this.context, ((StoryViewHolder) viewHolder).share);
                popupMenu.getMenuInflater().inflate(R.menu.share_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if (itemId == R.id.twitter) {
                            TravelAdapter.this.shareToTwitter(viewHolder, viewHolder.getAdapterPosition());
                        }
                        if (itemId == R.id.whatsapp) {
                            TravelAdapter.this.shareToWhatsapp(viewHolder, viewHolder.getAdapterPosition());
                        }
                        if (itemId == R.id.telegram) {
                            TravelAdapter.this.shareToTelegram(viewHolder, viewHolder.getAdapterPosition());
                        }
                        if (itemId == R.id.instagram) {
                            TravelAdapter.this.shareToInstagram(viewHolder, viewHolder.getAdapterPosition());
                        }
                        if (itemId == R.id.linkedin) {
                            TravelAdapter.this.shareToLinkedIn(viewHolder, viewHolder.getAdapterPosition());
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    /* access modifiers changed from: private */
    public void shareToLinkedIn(RecyclerView.ViewHolder viewHolder, int i) {
        travel_pojo travel_pojo = (travel_pojo) this.travel_pojoList.get(i);
        String title = travel_pojo.getTitle();
        String key = travel_pojo.getKey();
        travel_pojo.getUsername();
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        sb.append("\n http://www.saminnoafrica.com/posts/");
        sb.append(key);
        sb.append("/blog/");
        sb.append(2);
        intent.putExtra("android.intent.extra.TEXT", sb.toString());
        intent.setPackage("com.linkedin.android");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            this.context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this.context, "LinkedIn is not Installed", Toast.LENGTH_LONG).show();
        }
    }

    /* access modifiers changed from: private */
    public void shareToInstagram(RecyclerView.ViewHolder viewHolder, int i) {
        travel_pojo travel_pojo = (travel_pojo) this.travel_pojoList.get(i);
        String title = travel_pojo.getTitle();
        String key = travel_pojo.getKey();
        travel_pojo.getUsername();
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        sb.append("\n http://www.saminnoafrica.com/posts/");
        sb.append(key);
        sb.append("/blog/");
        sb.append(2);
        intent.putExtra("android.intent.extra.TEXT", sb.toString());
        intent.setPackage("com.instagram.android");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            this.context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this.context, "Instagram is not Installed", Toast.LENGTH_LONG).show();
        }
    }

    /* access modifiers changed from: private */
    public void shareToTelegram(RecyclerView.ViewHolder viewHolder, int i) {
        travel_pojo travel_pojo = (travel_pojo) this.travel_pojoList.get(i);
        String title = travel_pojo.getTitle();
        String key = travel_pojo.getKey();
        travel_pojo.getUsername();
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        sb.append("\n http://www.saminnoafrica.com/posts/");
        sb.append(key);
        sb.append("/blog/");
        sb.append(2);
        intent.putExtra("android.intent.extra.TEXT", sb.toString());
        intent.setPackage("org.telegram.messenger");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            this.context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this.context, "Whatsapp is not Installed", Toast.LENGTH_LONG).show();
        }
    }

    /* access modifiers changed from: private */
    public void shareToWhatsapp(RecyclerView.ViewHolder viewHolder, int i) {
        travel_pojo travel_pojo = (travel_pojo) this.travel_pojoList.get(i);
        String title = travel_pojo.getTitle();
        String key = travel_pojo.getKey();
        travel_pojo.getUsername();
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        sb.append("\n http://www.saminnoafrica.com/posts/");
        sb.append(key);
        sb.append("/blog/");
        sb.append(2);
        intent.putExtra("android.intent.extra.TEXT", sb.toString());
        intent.setPackage("com.whatsapp");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            this.context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this.context, "Whatsapp is not Installed", Toast.LENGTH_LONG).show();
        }
    }

    /* access modifiers changed from: private */
    public void shareToTwitter(RecyclerView.ViewHolder viewHolder, int i) {
        travel_pojo travel_pojo = (travel_pojo) this.travel_pojoList.get(i);
        String title = travel_pojo.getTitle();
        String key = travel_pojo.getKey();
        travel_pojo.getUsername();
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        sb.append("\n http://www.saminnoafrica.com/posts/");
        sb.append(key);
        sb.append("/blog/");
        sb.append(2);
        intent.putExtra("android.intent.extra.TEXT", sb.toString());
        intent.setPackage("com.twitter.android");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            this.context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this.context, "Twitter is not Installed", Toast.LENGTH_LONG).show();
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
            return "in the future";
        }
        long j = time2 - time;
        if (j < 60000) {
            return "moments ago";
        }
        if (j < 120000) {
            return "a minute ago";
        }
        if (j < 3600000) {
            StringBuilder sb = new StringBuilder();
            sb.append(j / 60000);
            sb.append(" minutes ago");
            return sb.toString();
        } else if (j < 7200000) {
            return "an hour ago";
        } else {
            if (j < 86400000) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(j / 3600000);
                sb2.append(" hours ago");
                return sb2.toString();
            } else if (j < 172800000) {
                return "yesterday";
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(j / 86400000);
                sb3.append(" days ago");
                return sb3.toString();
            }
        }
    }

    /* access modifiers changed from: private */
    public void fav_counttxt(final RecyclerView.ViewHolder viewHolder, String str) {
        FirebaseDatabase.getInstance().getReference("Favorites").child(str).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                int childrenCount = (int) dataSnapshot.getChildrenCount();
                String valueOf = String.valueOf(childrenCount);
                if (childrenCount == 0) {
                    ((StoryViewHolder) viewHolder).favcount.setVisibility(4);
                } else {
                    ((StoryViewHolder) viewHolder).favcount.setText(valueOf);
                }
            }
        });
    }

    private void com_counttxt(final RecyclerView.ViewHolder viewHolder, String str) {
        FirebaseDatabase.getInstance().getReference("comments").orderByChild("key").equalTo(str).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                int childrenCount = (int) dataSnapshot.getChildrenCount();
                String valueOf = String.valueOf(childrenCount);
                if (childrenCount == 0) {
                    ((StoryViewHolder) viewHolder).commentcount.setVisibility(4);
                } else {
                    ((StoryViewHolder) viewHolder).commentcount.setText(valueOf);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void textcomments(int i) {
        String key = ((travel_pojo) this.travel_pojoList.get(i)).getKey();
        Intent intent = new Intent(this.context, CommentsActivity.class);
        intent.putExtra("key", key);
        intent.putExtra("uploadusername", "");
        String str = ImagesContract.URL;
        StringBuilder sb = new StringBuilder();
        sb.append("is");
        sb.append(key);
        Log.d(str, sb.toString());
        this.context.startActivity(intent);
    }

    public int getItemCount() {
        return this.travel_pojoList.size();
    }
}
