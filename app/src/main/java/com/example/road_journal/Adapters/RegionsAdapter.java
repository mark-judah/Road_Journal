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
import com.example.road_journal.Activities.CommentsActivity;
import com.example.road_journal.Activities.FullscreenActivity;
import com.example.road_journal.R;import com.google.android.gms.common.internal.ImagesContract;
import com.google.firebase.auth.FirebaseAuth;
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

public class RegionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ANIMATED_ITEMS_COUNT = 2;
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
    private int itemsCount = 0;
    private int lastAnimatedPosition = -1;
    List<FileUploadInfo> mainFileUploadInfoList;
    List<User> profile;
    int size;
    int total_types;
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
            RegionsAdapter.this.favoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");
            RegionsAdapter.this.favoriteRef.keepSynced(true);
        }

        public void setFavBtn1(final String str) {
            RegionsAdapter.this.favoriteRef.addValueEventListener(new ValueEventListener() {
                public void onCancelled(DatabaseError databaseError) {
                }

                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(RegionsAdapter.this.user.getUid()).hasChild(str)) {
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
            RegionsAdapter.this.favoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");
            RegionsAdapter.this.favoriteRef.keepSynced(true);
        }

        public void setFavBtn(final String str) {
            RegionsAdapter.this.favoriteRef.addValueEventListener(new ValueEventListener() {
                public void onCancelled(DatabaseError databaseError) {
                }

                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(RegionsAdapter.this.user.getUid()).hasChild(str)) {
                        TextTypeViewHolder.this.favorite.setImageResource(R.drawable.dark_favorite);
                    } else {
                        TextTypeViewHolder.this.favorite.setImageResource(R.drawable.favorite);
                    }
                }
            });
        }
    }

    public RegionsAdapter(Context context2, List<FileUploadInfo> list, LayoutInflater layoutInflater) {
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
                final FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
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
                textTypeViewHolder.comment.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        RegionsAdapter.this.textcomments(viewHolder.getAdapterPosition());
                    }
                });
                textTypeViewHolder.repost.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference("Data").child("Incidents").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onCancelled(DatabaseError databaseError) {
                            }

                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("original_key")) {
                                    RegionsAdapter.this.textrepost2(viewHolder.getAdapterPosition());
                                } else {
                                    RegionsAdapter.this.textrepost(viewHolder.getAdapterPosition());
                                }
                            }
                        });
                    }
                });
                textTypeViewHolder.share.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        String text = fileUploadInfo.getText();
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.SEND");
                        intent.putExtra("android.intent.extra.SUBJECT", "@Road Journal");
                        intent.putExtra("android.intent.extra.TEXT", text);
                        intent.setType("text/plain");
                        RegionsAdapter.this.context.startActivity(Intent.createChooser(intent, "Share Post"));
                    }
                });
                textTypeViewHolder.setFavBtn(key);
                textTypeViewHolder.favorite.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        RegionsAdapter.this.favoritechecker = true;
                        RegionsAdapter.this.favoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");
                        RegionsAdapter.this.favoriteRef.keepSynced(true);
                        RegionsAdapter.this.favoriteRef.addValueEventListener(new ValueEventListener() {
                            public void onCancelled(DatabaseError databaseError) {
                            }

                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!RegionsAdapter.this.favoritechecker) {
                                    return;
                                }
                                if (dataSnapshot.child(key).hasChild(RegionsAdapter.this.user.getUid())) {
                                    RegionsAdapter.this.favoriteRef.child(RegionsAdapter.this.user.getUid()).child(key).removeValue();
                                    RegionsAdapter.this.favoritechecker = false;
                                    RegionsAdapter.this.fav_counttxt(viewHolder, key);
                                    return;
                                }
                                RegionsAdapter.this.addfavorite(viewHolder.getAdapterPosition(), viewHolder, key);
                            }
                        });
                    }
                });
                fav_counttxt(viewHolder, key);
                com_counttxt(viewHolder, key);
                repost_counttxt(viewHolder, fileUploadInfo.getOriginal_key());
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
                imageViewHolder.comment.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        RegionsAdapter.this.imagecomment(viewHolder.getAdapterPosition());
                    }
                });
                imageViewHolder.repost.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference("Data").child("Incidents").child(key2).addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onCancelled(DatabaseError databaseError) {
                            }

                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("original_key")) {
                                    RegionsAdapter.this.imagerepost2(viewHolder.getAdapterPosition());
                                } else {
                                    RegionsAdapter.this.imagerepost(viewHolder.getAdapterPosition());
                                }
                            }
                        });
                    }
                });
                imageViewHolder.setFavBtn1(key2);
                imageViewHolder.favorite.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        RegionsAdapter.this.favoritechecker = true;
                        RegionsAdapter.this.favoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");
                        RegionsAdapter.this.favoriteRef.keepSynced(true);
                        RegionsAdapter.this.favoriteRef.addValueEventListener(new ValueEventListener() {
                            public void onCancelled(DatabaseError databaseError) {
                            }

                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!RegionsAdapter.this.favoritechecker) {
                                    return;
                                }
                                if (dataSnapshot.child(key2).hasChild(RegionsAdapter.this.user.getUid())) {
                                    RegionsAdapter.this.favoriteRef.child(RegionsAdapter.this.user.getUid()).child(key2).removeValue();
                                    RegionsAdapter.this.favoritechecker = false;
                                    RegionsAdapter.this.fav_count(viewHolder, key2);
                                    return;
                                }
                                RegionsAdapter.this.addfavorite2(viewHolder.getAdapterPosition(), viewHolder, key2);
                            }
                        });
                    }
                });
                imageViewHolder.share.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        RegionsAdapter.this.shareImage(fileUploadInfo2.getImageName(), viewHolder);
                    }
                });
                imageViewHolder.imageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(RegionsAdapter.this.context, FullscreenActivity.class);
                        intent.putExtra("imageurl", fileUploadInfo2.getImageURL());
                        RegionsAdapter.this.context.startActivity(intent);
                    }
                });
                fav_count(viewHolder, key2);
                com_count(viewHolder, key2);
                repost_count(viewHolder, fileUploadInfo2.getOriginal_key());
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
        DatabaseReference child = this.favoriteRef.child(currentUser.getUid()).child(str2);
        FileUploadInfo fileUploadInfo3 = new FileUploadInfo(str4, imageName, "", imageURL, incident, email, displayName, location, null, road, usernam, Integer.valueOf(1), currentDate(), city, str3, null);
        child.setValue(fileUploadInfo3);
        this.favoritechecker = false;
        fav_count(viewHolder, str);
    }

    /* access modifiers changed from: private */
    public void addfavorite(int i, RecyclerView.ViewHolder viewHolder, String str) {
        String str2 = str;
        String str3 = str;
        FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
        String uid = this.user.getUid();
        String str4 = uid;
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

        DatabaseReference child = this.favoriteRef.child(this.user.getUid()).child(str2);
        FileUploadInfo fileUploadInfo3 = new FileUploadInfo(str4, imageName, "", imageURL, incident, email, displayName, location, null, road, originalPostUser, Integer.valueOf(1), currentDate(), city, str3, null);
        child.setValue(fileUploadInfo3);
        this.favoritechecker = false;
        fav_counttxt(viewHolder, str);
    }

    private void repost_counttxt(final RecyclerView.ViewHolder viewHolder, String str) {
        FirebaseDatabase.getInstance().getReference("Data").child("Incidents").orderByChild("original_key").equalTo(str).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                int childrenCount = ((int) dataSnapshot.getChildrenCount()) - 1;
                String valueOf = String.valueOf(childrenCount);
                if (childrenCount == 0) {
                    ((TextTypeViewHolder) viewHolder).repostcount.setVisibility(View.GONE);
                } else {
                    ((TextTypeViewHolder) viewHolder).repostcount.setText(valueOf);
                }
            }
        });
    }

    private void repost_count(final RecyclerView.ViewHolder viewHolder, String str) {
        FirebaseDatabase.getInstance().getReference("Data").child("Incidents").orderByChild("original_key").equalTo(str).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                int childrenCount = ((int) dataSnapshot.getChildrenCount()) - 1;
                String valueOf = String.valueOf(childrenCount);
                if (childrenCount == 0) {
                    ((ImageViewHolder) viewHolder).repostcount.setVisibility(View.INVISIBLE);
                } else {
                    ((ImageViewHolder) viewHolder).repostcount.setText(valueOf);
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
                    ((TextTypeViewHolder) viewHolder).commentcount.setVisibility(View.GONE);
                } else {
                    ((TextTypeViewHolder) viewHolder).commentcount.setText(valueOf);
                }
            }
        });
    }

    private void com_count(final RecyclerView.ViewHolder viewHolder, String str) {
        FirebaseDatabase.getInstance().getReference("comments").orderByChild("key").equalTo(str).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                int childrenCount = (int) dataSnapshot.getChildrenCount();
                String valueOf = String.valueOf(childrenCount);
                if (childrenCount == 0) {
                    ((ImageViewHolder) viewHolder).commentcount.setVisibility(View.GONE);
                } else {
                    ((ImageViewHolder) viewHolder).commentcount.setText(valueOf);
                }
            }
        });
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

    /* access modifiers changed from: private */
    public void textcomments(int i) {
        FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
        String key = fileUploadInfo.getKey();
        String usernam = fileUploadInfo.getUsernam();
        Intent intent = new Intent(this.context, CommentsActivity.class);
        intent.putExtra("key", key);
        intent.putExtra("uploadusername", usernam);
        String str = ImagesContract.URL;
        StringBuilder sb = new StringBuilder();
        sb.append("is");
        sb.append(key);
        Log.d(str, sb.toString());
        this.context.startActivity(intent);
    }

    /* access modifiers changed from: private */
    public void textrepost(int i) {
        FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
        DatabaseReference child = FirebaseDatabase.getInstance().getReference("Data").child("Incidents");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        String str = uid;
        String displayName = currentUser.getDisplayName();
        String usernam = fileUploadInfo.getUsernam();
        String road = fileUploadInfo.getRoad();
        String text = fileUploadInfo.getText();
        String incident = fileUploadInfo.getIncident();
        String email = currentUser.getEmail();
        String city = fileUploadInfo.getCity();
        String key = child.push().getKey();
        String str2 = key;
        String str3 = key;
        String location = fileUploadInfo.getLocation();
        getprofilephotourl(uid);
        DatabaseReference child2 = child.child(key);
        FileUploadInfo fileUploadInfo2 = new FileUploadInfo(str, null, "", null, incident, email, displayName, location, text, road, usernam, Integer.valueOf(0), currentDate(), city, str2, str3);
        child2.setValue(fileUploadInfo2);
    }

    /* access modifiers changed from: private */
    public void textrepost2(int i) {
        FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
        DatabaseReference child = FirebaseDatabase.getInstance().getReference("Data").child("Incidents");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        String str = uid;
        String displayName = currentUser.getDisplayName();
        String originalPostUser = fileUploadInfo.getOriginalPostUser();
        String road = fileUploadInfo.getRoad();
        String text = fileUploadInfo.getText();
        String incident = fileUploadInfo.getIncident();
        String email = currentUser.getEmail();
        String city = fileUploadInfo.getCity();
        String key = child.push().getKey();
        String str2 = key;
        String location = fileUploadInfo.getLocation();
        String original_key = fileUploadInfo.getOriginal_key();
        getprofilephotourl(uid);
        DatabaseReference child2 = child.child(key);
        FileUploadInfo fileUploadInfo2 = new FileUploadInfo(str, null, "", null, incident, email, displayName, location, text, road, originalPostUser, Integer.valueOf(0), currentDate(), city, str2, original_key);
        child2.setValue(fileUploadInfo2);
    }

    /* access modifiers changed from: private */
    public void imagecomment(int i) {
        FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
        String key = fileUploadInfo.getKey();
        String usernam = fileUploadInfo.getUsernam();
        Intent intent = new Intent(this.context, CommentsActivity.class);
        intent.putExtra("key", key);
        intent.putExtra("uploadusername", usernam);
        String str = ImagesContract.URL;
        StringBuilder sb = new StringBuilder();
        sb.append("is");
        sb.append(key);
        Log.d(str, sb.toString());
        this.context.startActivity(intent);
    }

    /* access modifiers changed from: private */
    public void imagerepost(int i) {
        FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference child = FirebaseDatabase.getInstance().getReference("Data").child("Incidents");
        String uid = currentUser.getUid();
        String str = uid;
        String imageURL = fileUploadInfo.getImageURL();
        String displayName = currentUser.getDisplayName();
        String usernam = fileUploadInfo.getUsernam();
        String road = fileUploadInfo.getRoad();
        String imageName = fileUploadInfo.getImageName();
        String incident = fileUploadInfo.getIncident();
        String email = currentUser.getEmail();
        String city = fileUploadInfo.getCity();
        String key = child.push().getKey();
        String str2 = key;
        String str3 = key;
        String location = fileUploadInfo.getLocation();
        getprofilephotourl(uid);
        DatabaseReference child2 = child.child(key);
        FileUploadInfo fileUploadInfo2 = new FileUploadInfo(str, imageName, "", imageURL, incident, email, displayName, location, null, road, usernam, Integer.valueOf(1), currentDate(), city, str3, str2);
        child2.setValue(fileUploadInfo2);
    }

    /* access modifiers changed from: private */
    public void imagerepost2(int i) {
        FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference child = FirebaseDatabase.getInstance().getReference("Data").child("Incidents");
        String uid = currentUser.getUid();
        String str = uid;
        String imageURL = fileUploadInfo.getImageURL();
        String displayName = currentUser.getDisplayName();
        String originalPostUser = fileUploadInfo.getOriginalPostUser();
        String road = fileUploadInfo.getRoad();
        String imageName = fileUploadInfo.getImageName();
        String incident = fileUploadInfo.getIncident();
        String email = currentUser.getEmail();
        String city = fileUploadInfo.getCity();
        String key = child.push().getKey();
        String str2 = key;
        String original_key = fileUploadInfo.getOriginal_key();
        String location = fileUploadInfo.getLocation();
        getprofilephotourl(uid);
        DatabaseReference child2 = child.child(key);
        FileUploadInfo fileUploadInfo2 = new FileUploadInfo(str, imageName, "", imageURL, incident, email, displayName, location, null, road, originalPostUser, Integer.valueOf(1), currentDate(), city, str2, original_key);
        child2.setValue(fileUploadInfo2);
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
