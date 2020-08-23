package com.example.road_journal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.road_journal.Pojos.FileUploadInfo;
import com.example.road_journal.Pojos.User;
import com.bumptech.glide.Glide;
import com.example.road_journal.Activities.FullscreenActivity;
import com.example.road_journal.R;import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FetchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int DAY_MILLIS = 86400000;
    private static final int HOUR_MILLIS = 3600000;
    private static final int MINUTE_MILLIS = 60000;
    private static final int SECOND_MILLIS = 1000;
    Bitmap bitmap;
    Context context;
    DatabaseReference databaseReference;
    DatabaseReference favoriteRef;
    boolean favoritechecker = false;
    private LayoutInflater inflater;
    List<FileUploadInfo> mainFileUploadInfoList;
    List<User> profile;
    int size;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView Profilepic;
        public ImageView comment;
        public TextView commentcount;
        public TextView disptime;
        public TextView favcount;
        public ImageView favorite;
        public TextView imageNameTextView;
        public ImageView imageView;
        public TextView incident;
        public TextView place;
        public ImageView repost;
        public TextView repostcount;
        public TextView reposttext;
        public TextView road;
        public ImageView share;
        public TextView town;
        public TextView username;

        public ImageViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.imageView);
            this.Profilepic = (ImageView) view.findViewById(R.id.profilepic);
            this.imageNameTextView = (TextView) view.findViewById(R.id.ImageNameTextView);
            this.username = (TextView) view.findViewById(R.id.userprofilename);
            this.comment = (ImageView) view.findViewById(R.id.imgComments);
            this.share = (ImageView) view.findViewById(R.id.imgShare);
            this.favorite = (ImageView) view.findViewById(R.id.blankfavorite);
            this.repost = (ImageView) view.findViewById(R.id.imgrepost);
            this.reposttext = (TextView) view.findViewById(R.id.userrepostname);
            this.disptime = (TextView) view.findViewById(R.id.time);
            this.road = (TextView) view.findViewById(R.id.road);
            this.town = (TextView) view.findViewById(R.id.usertown);
            this.incident = (TextView) view.findViewById(R.id.incident);
            this.place = (TextView) view.findViewById(R.id.place);
            this.favcount = (TextView) view.findViewById(R.id.favcount);
            this.commentcount = (TextView) view.findViewById(R.id.commentcount);
            this.repostcount = (TextView) view.findViewById(R.id.repostcount);
            FetchAdapter.this.favoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");
            FetchAdapter.this.favoriteRef.keepSynced(true);
        }

        public void setFavBtn1(final String str) {
            FetchAdapter.this.favoriteRef.addValueEventListener(new ValueEventListener() {
                public void onCancelled(DatabaseError databaseError) {
                }

                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(str).hasChild(FetchAdapter.this.user.getUid())) {
                        ImageViewHolder.this.favorite.setImageResource(R.drawable.dark_favorite);
                    } else {
                        ImageViewHolder.this.favorite.setImageResource(R.drawable.favorite);
                    }
                }
            });
        }
    }

    public class TextTypeViewHolder extends RecyclerView.ViewHolder {
        ImageView Profilepic;
        CardView cardView;
        ImageView comment;
        public TextView commentcount;
        public TextView disptime;
        public TextView favcount;
        ImageView favorite;
        public TextView incident;
        public TextView place;
        ImageView repost;
        public TextView repostcount;
        public TextView reposttext;
        public TextView road;
        ImageView share;
        TextView textcomment;
        public TextView town;
        TextView username;

        public TextTypeViewHolder(View view) {
            super(view);
            this.textcomment = (TextView) view.findViewById(R.id.textcomment);
            this.Profilepic = (ImageView) view.findViewById(R.id.profilepic);
            this.cardView = (CardView) view.findViewById(R.id.card_view);
            this.username = (TextView) view.findViewById(R.id.userprofilename);
            this.comment = (ImageView) view.findViewById(R.id.imgComments);
            this.share = (ImageView) view.findViewById(R.id.imgShare);
            this.repost = (ImageView) view.findViewById(R.id.imgrepost);
            this.reposttext = (TextView) view.findViewById(R.id.userrepostname);
            this.favorite = (ImageView) view.findViewById(R.id.blankfavorite);
            this.disptime = (TextView) view.findViewById(R.id.time);
            this.road = (TextView) view.findViewById(R.id.road);
            this.incident = (TextView) view.findViewById(R.id.incident);
            this.place = (TextView) view.findViewById(R.id.place);
            this.favcount = (TextView) view.findViewById(R.id.favcount);
            this.commentcount = (TextView) view.findViewById(R.id.commentcount);
            this.repostcount = (TextView) view.findViewById(R.id.repostcount);
            this.town = (TextView) view.findViewById(R.id.userstown);
            FetchAdapter.this.favoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");
            FetchAdapter.this.favoriteRef.keepSynced(true);
        }

        public void setFavBtn(final String str) {
            FetchAdapter.this.favoriteRef.addValueEventListener(new ValueEventListener() {
                public void onCancelled(DatabaseError databaseError) {
                }

                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(str).hasChild(FetchAdapter.this.user.getUid())) {
                        TextTypeViewHolder.this.favorite.setImageResource(R.drawable.dark_favorite);
                    } else {
                        TextTypeViewHolder.this.favorite.setImageResource(R.drawable.favorite);
                    }
                }
            });
        }
    }

    public FetchAdapter(Context context2, List<FileUploadInfo> list, LayoutInflater layoutInflater) {
        this.mainFileUploadInfoList = list;
        this.inflater = layoutInflater;
        this.context = context2;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.inflater = LayoutInflater.from(viewGroup.getContext());
        switch (i) {
            case 0:
                return new TextTypeViewHolder(this.inflater.inflate(R.layout.recycler_text_item, viewGroup, false));
            case 1:
                return new ImageViewHolder(this.inflater.inflate(R.layout.recycler_image_item, viewGroup, false));
            default:
                return null;
        }
    }

    public int getItemViewType(int i) {
        switch (((FileUploadInfo) this.mainFileUploadInfoList.get(i)).type.intValue()) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return -1;
        }
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i) {
        switch (getItemViewType(i)) {
            case 0:
                FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
                final String key = fileUploadInfo.getKey();
                TextTypeViewHolder textTypeViewHolder = (TextTypeViewHolder) viewHolder;
                textTypeViewHolder.textcomment.setText(fileUploadInfo.getText());
                if (fileUploadInfo.getProfileurl() != null) {
                    Glide.with(this.context).load(fileUploadInfo.getProfileurl()).into(textTypeViewHolder.Profilepic);
                }
                textTypeViewHolder.disptime.setText(getTimeAgo(fileUploadInfo.getDate()));
                textTypeViewHolder.road.setText(fileUploadInfo.getRoad());
                textTypeViewHolder.place.setText(fileUploadInfo.getLocation());
                textTypeViewHolder.incident.setText(fileUploadInfo.getIncident());
                textTypeViewHolder.username.setText(fileUploadInfo.getUsernam());
                textTypeViewHolder.town.setText(fileUploadInfo.getCity());
                if (fileUploadInfo.getOriginalPostUser().equals(fileUploadInfo.getUsernam())) {
                    textTypeViewHolder.reposttext.setVisibility(View.VISIBLE);
                } else {
                    TextView textView = textTypeViewHolder.reposttext;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Posted by ");
                    sb.append(fileUploadInfo.getOriginalPostUser());
                    textView.setText(sb.toString());
                }
                textTypeViewHolder.comment.setVisibility(View.INVISIBLE);
                textTypeViewHolder.repost.setVisibility(View.INVISIBLE);
                textTypeViewHolder.share.setVisibility(View.INVISIBLE);
                textTypeViewHolder.setFavBtn(key);
                textTypeViewHolder.favorite.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        FetchAdapter.this.favoritechecker = true;
                        FetchAdapter.this.favoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");
                        FetchAdapter.this.favoriteRef.keepSynced(true);
                        FetchAdapter.this.favoriteRef.addValueEventListener(new ValueEventListener() {
                            public void onCancelled(DatabaseError databaseError) {
                            }

                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!FetchAdapter.this.favoritechecker) {
                                    return;
                                }
                                if (dataSnapshot.child(key).hasChild(FetchAdapter.this.user.getUid())) {
                                    FetchAdapter.this.favoriteRef.child(key).child(FetchAdapter.this.user.getUid()).removeValue();
                                    FetchAdapter.this.favoritechecker = false;
                                    FetchAdapter.this.fav_counttxt(viewHolder, key);
                                    return;
                                }
                                FetchAdapter.this.addfavorite(viewHolder.getAdapterPosition(), viewHolder, key);
                            }
                        });
                    }
                });
                fav_counttxt(viewHolder, key);
                return;
            case 1:
                final FileUploadInfo fileUploadInfo2 = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
                if (fileUploadInfo2.getProfileurl() != null) {
                    Glide.with(this.context).load(fileUploadInfo2.getProfileurl()).into(((ImageViewHolder) viewHolder).Profilepic);
                }
                final String key2 = fileUploadInfo2.getKey();
                ImageViewHolder imageViewHolder = (ImageViewHolder) viewHolder;
                imageViewHolder.disptime.setText(getTimeAgo(fileUploadInfo2.getDate()));
                imageViewHolder.town.setText(fileUploadInfo2.getCity());
                imageViewHolder.imageNameTextView.setText(fileUploadInfo2.getImageName());
                imageViewHolder.road.setText(fileUploadInfo2.getRoad());
                imageViewHolder.incident.setText(fileUploadInfo2.getIncident());
                imageViewHolder.place.setText(fileUploadInfo2.getLocation());
                Glide.with(this.context).load(fileUploadInfo2.getImageURL()).into(imageViewHolder.imageView);
                imageViewHolder.username.setText(fileUploadInfo2.getUsernam());
                if (fileUploadInfo2.getOriginalPostUser().equals(fileUploadInfo2.getUsernam())) {
                    imageViewHolder.reposttext.setVisibility(View.VISIBLE);
                } else {
                    TextView textView2 = imageViewHolder.reposttext;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Posted by ");
                    sb2.append(fileUploadInfo2.getOriginalPostUser());
                    textView2.setText(sb2.toString());
                }
                imageViewHolder.comment.setVisibility(View.INVISIBLE);
                imageViewHolder.repost.setVisibility(8);
                imageViewHolder.setFavBtn1(key2);
                imageViewHolder.favorite.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        FetchAdapter.this.favoritechecker = true;
                        FetchAdapter.this.favoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");
                        FetchAdapter.this.favoriteRef.keepSynced(true);
                        FetchAdapter.this.favoriteRef.addValueEventListener(new ValueEventListener() {
                            public void onCancelled(DatabaseError databaseError) {
                            }

                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!FetchAdapter.this.favoritechecker) {
                                    return;
                                }
                                if (dataSnapshot.child(key2).hasChild(FetchAdapter.this.user.getUid())) {
                                    FetchAdapter.this.favoriteRef.child(key2).child(FetchAdapter.this.user.getUid()).removeValue();
                                    FetchAdapter.this.favoritechecker = false;
                                    FetchAdapter.this.fav_count(viewHolder, key2);
                                    return;
                                }
                                FetchAdapter.this.addfavorite2(viewHolder.getAdapterPosition(), viewHolder, key2);
                            }
                        });
                    }
                });
                imageViewHolder.share.setVisibility(View.INVISIBLE);
                imageViewHolder.imageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(FetchAdapter.this.context, FullscreenActivity.class);
                        intent.putExtra("imageurl", fileUploadInfo2.getImageURL());
                        FetchAdapter.this.context.startActivity(intent);
                    }
                });
                fav_count(viewHolder, key2);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void addfavorite2(int i, RecyclerView.ViewHolder viewHolder, String str) {
        String str2 = str;
        String str3 = str;
        FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        String str4 = uid;
        String imageURL = fileUploadInfo.getImageURL();
        String displayName = currentUser.getDisplayName();
        String usernam = fileUploadInfo.getUsernam();
        String road = fileUploadInfo.getRoad();
        String imageName = fileUploadInfo.getImageName();
        String incident = fileUploadInfo.getIncident();
        String email = currentUser.getEmail();
        String city = fileUploadInfo.getCity();
        String location = fileUploadInfo.getLocation();
        getprofilephotourl(uid);
        DatabaseReference child = this.favoriteRef.child(str2);
        FileUploadInfo fileUploadInfo3 = new FileUploadInfo(str4, imageName, "", imageURL, incident, email, displayName, location, null, road, usernam, Integer.valueOf(1), currentDate(), city, str3, null);
        child.setValue(fileUploadInfo3);
        this.favoritechecker = false;
        fav_count(viewHolder, str);
    }

    /* access modifiers changed from: private */
    public void addfavorite(int i, RecyclerView.ViewHolder viewHolder, String str) {
        String str2 = str;
        FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
        String uid = this.user.getUid();
        String str3 = uid;
        String imageURL = fileUploadInfo.getImageURL();
        String displayName = this.user.getDisplayName();
        String originalPostUser = fileUploadInfo.getOriginalPostUser();
        String road = fileUploadInfo.getRoad();
        String imageName = fileUploadInfo.getImageName();
        String incident = fileUploadInfo.getIncident();
        String email = this.user.getEmail();
        String city = fileUploadInfo.getCity();
        String location = fileUploadInfo.getLocation();
        getprofilephotourl(uid);
        DatabaseReference child = this.favoriteRef.child(str).child(this.user.getUid());
        FileUploadInfo fileUploadInfo3 = new FileUploadInfo(str3, imageName, "", imageURL, incident, email, displayName, location, null, road, originalPostUser, Integer.valueOf(1), currentDate(), city, str2, null);
        child.setValue(fileUploadInfo3);
        this.favoritechecker = false;
        fav_counttxt(viewHolder, this.user.getUid());
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
                    ((TextTypeViewHolder) viewHolder).favcount.setVisibility(View.GONE);
                } else {
                    ((TextTypeViewHolder) viewHolder).favcount.setText(valueOf);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void fav_count(final RecyclerView.ViewHolder viewHolder, String str) {
        FirebaseDatabase.getInstance().getReference("Favorites").child(str).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                int childrenCount = (int) dataSnapshot.getChildrenCount();
                String valueOf = String.valueOf(childrenCount);
                if (childrenCount == 0) {
                    ((ImageViewHolder) viewHolder).favcount.setVisibility(View.GONE);
                } else {
                    ((ImageViewHolder) viewHolder).favcount.setText(valueOf);
                }
            }
        });
    }

    public void getprofilephotourl(final String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("getprofilephotourl: ");
        sb.append(str);
        Log.d("TAG", sb.toString());
        FirebaseDatabase.getInstance().getReference("Profile").child(str).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                final String str = (String) dataSnapshot.child("profileurl").getValue(String.class);
                final DatabaseReference child = FirebaseDatabase.getInstance().getReference("Data").child("Incidents");
                child.orderByChild("userId").equalTo(str).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList arrayList = new ArrayList();
                        for (DataSnapshot key : dataSnapshot.getChildren()) {
                            arrayList.add(key.getKey());
                            String valueOf = String.valueOf(arrayList);
                            String substring = valueOf.substring(1, valueOf.length() - 1);
                            HashMap hashMap = new HashMap();
                            hashMap.put("profileurl", str);
                            child.child(substring).updateChildren(hashMap);
                            arrayList.clear();
                        }
                    }
                });
            }
        });
    }

    public void shareImage(String str, RecyclerView.ViewHolder viewHolder) {
        Uri localBitmapUri = getLocalBitmapUri((ImageView) viewHolder.itemView.findViewById(R.id.imageView));
        if (localBitmapUri != null) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.SUBJECT", "@Road Journal");
            intent.putExtra("android.intent.extra.STREAM", localBitmapUri);
            intent.putExtra("android.intent.extra.TEXT", str);
            intent.setType("image/*");
            this.context.startActivity(Intent.createChooser(intent, "Share Image"));
        }
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        Uri uri;
        if (!(imageView.getDrawable() instanceof BitmapDrawable)) {
            return null;
        }
        Bitmap bitmap2 = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        try {
            File externalFilesDir = this.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            StringBuilder sb = new StringBuilder();
            sb.append("share_image_");
            sb.append(System.currentTimeMillis());
            sb.append(".png");
            File file = new File(externalFilesDir, sb.toString());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap2.compress(CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.close();
            uri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            uri = null;
        }
        return uri;
    }

    public int getItemCount() {
        return this.mainFileUploadInfoList.size();
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
}
