package com.example.road_journal.Adapters;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
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

import com.example.road_journal.Pojos.FileUploadInfo;
import com.example.road_journal.Pojos.User;
import com.bumptech.glide.Glide;
import com.example.road_journal.Activities.CommentsActivity;
import com.example.road_journal.Activities.Edit_gma;
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

public class gmaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
        public ImageView deletepost;
        public TextView disptime;
        public ImageView editpost;
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
            this.deletepost = (ImageView) view.findViewById(R.id.imgdelete);
            this.editpost = (ImageView) view.findViewById(R.id.imagedit);
            gmaAdapter.this.favoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");
            gmaAdapter.this.favoriteRef.keepSynced(true);
        }

        public void setFavBtn1(final String str) {
            gmaAdapter.this.favoriteRef.addValueEventListener(new ValueEventListener() {
                public void onCancelled(DatabaseError databaseError) {
                }

                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(str).hasChild(gmaAdapter.this.user.getUid())) {
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
        ImageView deletepost;
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
            this.deletepost = (ImageView) view.findViewById(R.id.imgdelete);
            this.town = (TextView) view.findViewById(R.id.userstown);
            gmaAdapter.this.favoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");
            gmaAdapter.this.favoriteRef.keepSynced(true);
        }

        public void setFavBtn(final String str) {
            gmaAdapter.this.favoriteRef.addValueEventListener(new ValueEventListener() {
                public void onCancelled(DatabaseError databaseError) {
                }

                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(str).hasChild(gmaAdapter.this.user.getUid())) {
                        TextTypeViewHolder.this.favorite.setImageResource(R.drawable.dark_favorite);
                    } else {
                        TextTypeViewHolder.this.favorite.setImageResource(R.drawable.favorite);
                    }
                }
            });
        }
    }

    public gmaAdapter(Context context2, List<FileUploadInfo> list, LayoutInflater layoutInflater) {
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
                if (this.user.getUid().equals(fileUploadInfo.getUserId())) {
                    textTypeViewHolder.deletepost.setVisibility(View.VISIBLE);
                    textTypeViewHolder.deletepost.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            gmaAdapter.this.textdelete(viewHolder.getAdapterPosition(), viewHolder, key);
                        }
                    });
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
                        gmaAdapter.this.textcomments(viewHolder.getAdapterPosition());
                    }
                });
                textTypeViewHolder.repost.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference("GMA").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onCancelled(DatabaseError databaseError) {
                            }

                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("original_key")) {
                                    gmaAdapter.this.textrepost2(viewHolder.getAdapterPosition());
                                } else {
                                    gmaAdapter.this.textrepost(viewHolder.getAdapterPosition());
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
                        gmaAdapter.this.context.startActivity(Intent.createChooser(intent, "Share Post"));
                    }
                });
                textTypeViewHolder.setFavBtn(key);
                textTypeViewHolder.favorite.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        gmaAdapter.this.favoritechecker = true;
                        gmaAdapter.this.favoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");
                        gmaAdapter.this.favoriteRef.keepSynced(true);
                        gmaAdapter.this.favoriteRef.addValueEventListener(new ValueEventListener() {
                            public void onCancelled(DatabaseError databaseError) {
                            }

                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!gmaAdapter.this.favoritechecker) {
                                    return;
                                }
                                if (dataSnapshot.child(key).hasChild(gmaAdapter.this.user.getUid())) {
                                    gmaAdapter.this.favoriteRef.child(key).child(gmaAdapter.this.user.getUid()).removeValue();
                                    gmaAdapter.this.favoritechecker = false;
                                    gmaAdapter.this.fav_counttxt(viewHolder, key);
                                    return;
                                }
                                gmaAdapter.this.addfavorite(viewHolder.getAdapterPosition(), viewHolder, key);
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
                        gmaAdapter.this.imagecomment(viewHolder.getAdapterPosition());
                    }
                });
                imageViewHolder.repost.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference("GMA").child(key2).addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onCancelled(DatabaseError databaseError) {
                            }

                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("original_key")) {
                                    gmaAdapter.this.imagerepost2(viewHolder.getAdapterPosition());
                                } else {
                                    gmaAdapter.this.imagerepost(viewHolder.getAdapterPosition());
                                }
                            }
                        });
                    }
                });
                if (this.user.getUid().equals(fileUploadInfo2.getUserId())) {
                    imageViewHolder.deletepost.setVisibility(View.VISIBLE);
                    imageViewHolder.deletepost.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            gmaAdapter.this.imagedelete(viewHolder.getAdapterPosition(), viewHolder, key2);
                        }
                    });
                }
                imageViewHolder.setFavBtn1(key2);
                imageViewHolder.favorite.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        gmaAdapter.this.favoritechecker = true;
                        gmaAdapter.this.favoriteRef = FirebaseDatabase.getInstance().getReference("Favorites");
                        gmaAdapter.this.favoriteRef.keepSynced(true);
                        gmaAdapter.this.favoriteRef.addValueEventListener(new ValueEventListener() {
                            public void onCancelled(DatabaseError databaseError) {
                            }

                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!gmaAdapter.this.favoritechecker) {
                                    return;
                                }
                                if (dataSnapshot.child(key2).hasChild(gmaAdapter.this.user.getUid())) {
                                    gmaAdapter.this.favoriteRef.child(key2).child(gmaAdapter.this.user.getUid()).removeValue();
                                    gmaAdapter.this.favoritechecker = false;
                                    gmaAdapter.this.fav_count(viewHolder, key2);
                                    return;
                                }
                                gmaAdapter.this.addfavorite2(viewHolder.getAdapterPosition(), viewHolder, key2);
                            }
                        });
                    }
                });
                imageViewHolder.share.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(gmaAdapter.this.context, ((ImageViewHolder) viewHolder).share);
                        popupMenu.getMenuInflater().inflate(R.menu.share_popup, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                int itemId = menuItem.getItemId();
                                if (itemId == R.id.twitter) {
                                    gmaAdapter.this.shareToTwitter(viewHolder, viewHolder.getAdapterPosition());
                                }
                                if (itemId == R.id.whatsapp) {
                                    gmaAdapter.this.shareToWhatsapp(viewHolder, viewHolder.getAdapterPosition());
                                }
                                if (itemId == R.id.telegram) {
                                    gmaAdapter.this.shareToTelegram(viewHolder, viewHolder.getAdapterPosition());
                                }
                                if (itemId == R.id.instagram) {
                                    gmaAdapter.this.shareToInstagram(viewHolder, viewHolder.getAdapterPosition());
                                }
                                if (itemId == R.id.linkedin) {
                                    gmaAdapter.this.shareToLinkedin(viewHolder, viewHolder.getAdapterPosition());
                                }
                                return true;
                            }
                        });
                        popupMenu.show();
                    }
                });
                imageViewHolder.imageView.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(gmaAdapter.this.context, FullscreenActivity.class);
                        intent.putExtra("imageurl", fileUploadInfo2.getImageURL());
                        gmaAdapter.this.context.startActivity(intent);
                    }
                });
                if (this.user.getUid().equals(fileUploadInfo2.getUserId())) {
                    imageViewHolder.editpost.setVisibility(View.VISIBLE);
                    imageViewHolder.editpost.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            long time = fileUploadInfo2.getDate().getTime();
                            Intent intent = new Intent(gmaAdapter.this.context, Edit_gma.class);
                            intent.putExtra(ImagesContract.URL, fileUploadInfo2.getImageURL());
                            intent.putExtra("updtype", fileUploadInfo2.getIncident());
                            intent.putExtra("city", fileUploadInfo2.getCity());
                            intent.putExtra("road", fileUploadInfo2.getRoad());
                            intent.putExtra("place", fileUploadInfo2.getLocation());
                            intent.putExtra("comment", fileUploadInfo2.getImageName());
                            intent.putExtra("date", time);
                            intent.putExtra("key", key2);
                            gmaAdapter.this.context.startActivity(intent);
                        }
                    });
                }
                fav_count(viewHolder, key2);
                com_count(viewHolder, key2);
                repost_count(viewHolder, fileUploadInfo2.getOriginal_key());
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void shareToLinkedin(RecyclerView.ViewHolder viewHolder, int i) {
        ImageView imageView = (ImageView) viewHolder.itemView.findViewById(R.id.imageView);
        FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
        String str = fileUploadInfo.original_key;
        String str2 = fileUploadInfo.incident;
        String imageName = fileUploadInfo.getImageName();
        Uri localBitmapUri = getLocalBitmapUri(imageView);
        if (localBitmapUri != null) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setPackage("com.linkedin.android");
            intent.putExtra("android.intent.extra.STREAM", localBitmapUri);
            StringBuilder sb = new StringBuilder();
            sb.append(imageName);
            sb.append("\n http://www.saminnoafrica.com/posts/");
            sb.append(str);
            sb.append("/");
            sb.append(str2);
            sb.append("/");
            sb.append(1);
            intent.putExtra("android.intent.extra.TEXT", sb.toString());
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                this.context.startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(this.context, "LinkedIn is not Installed", Toast.LENGTH_LONG).show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void shareToInstagram(RecyclerView.ViewHolder viewHolder, int i) {
        ImageView imageView = (ImageView) viewHolder.itemView.findViewById(R.id.imageView);
        FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
        String str = fileUploadInfo.original_key;
        String str2 = fileUploadInfo.incident;
        String imageName = fileUploadInfo.getImageName();
        Uri localBitmapUri = getLocalBitmapUri(imageView);
        if (localBitmapUri != null) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setPackage("com.instagram.android");
            intent.putExtra("android.intent.extra.STREAM", localBitmapUri);
            StringBuilder sb = new StringBuilder();
            sb.append(imageName);
            sb.append("\n http://www.saminnoafrica.com/posts/");
            sb.append(str);
            sb.append("/");
            sb.append(str2);
            sb.append("/");
            sb.append(1);
            intent.putExtra("android.intent.extra.TEXT", sb.toString());
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                this.context.startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(this.context, "Instagram is not Installed", Toast.LENGTH_LONG).show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void shareToTelegram(RecyclerView.ViewHolder viewHolder, int i) {
        ImageView imageView = (ImageView) viewHolder.itemView.findViewById(R.id.imageView);
        FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
        String str = fileUploadInfo.original_key;
        String str2 = fileUploadInfo.incident;
        String imageName = fileUploadInfo.getImageName();
        Uri localBitmapUri = getLocalBitmapUri(imageView);
        if (localBitmapUri != null) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setPackage("org.telegram.messenger");
            intent.putExtra("android.intent.extra.STREAM", localBitmapUri);
            StringBuilder sb = new StringBuilder();
            sb.append(imageName);
            sb.append("\n http://www.saminnoafrica.com/posts/");
            sb.append(str);
            sb.append("/");
            sb.append(str2);
            sb.append("/");
            sb.append(1);
            intent.putExtra("android.intent.extra.TEXT", sb.toString());
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                this.context.startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(this.context, "Telegram is not Installed", Toast.LENGTH_LONG).show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void shareToWhatsapp(RecyclerView.ViewHolder viewHolder, int i) {
        ImageView imageView = (ImageView) viewHolder.itemView.findViewById(R.id.imageView);
        FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
        String str = fileUploadInfo.original_key;
        String str2 = fileUploadInfo.incident;
        String imageName = fileUploadInfo.getImageName();
        Uri localBitmapUri = getLocalBitmapUri(imageView);
        if (localBitmapUri != null) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setPackage("com.whatsapp");
            intent.putExtra("android.intent.extra.STREAM", localBitmapUri);
            StringBuilder sb = new StringBuilder();
            sb.append(imageName);
            sb.append("\n http://www.saminnoafrica.com/posts/");
            sb.append(str);
            sb.append("/");
            sb.append(str2);
            sb.append("/");
            sb.append(1);
            intent.putExtra("android.intent.extra.TEXT", sb.toString());
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                this.context.startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(this.context, "Whatsapp is not Installed", Toast.LENGTH_LONG).show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void shareToTwitter(RecyclerView.ViewHolder viewHolder, int i) {
        ImageView imageView = (ImageView) viewHolder.itemView.findViewById(R.id.imageView);
        FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
        String str = fileUploadInfo.original_key;
        String str2 = fileUploadInfo.incident;
        String imageName = fileUploadInfo.getImageName();
        Uri localBitmapUri = getLocalBitmapUri(imageView);
        if (localBitmapUri != null) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.setPackage("com.twitter.android");
            intent.putExtra("android.intent.extra.STREAM", localBitmapUri);
            StringBuilder sb = new StringBuilder();
            sb.append(imageName);
            sb.append("\n http://www.saminnoafrica.com/posts/");
            sb.append(str);
            sb.append("/");
            sb.append(str2);
            sb.append("/");
            sb.append(1);
            intent.putExtra("android.intent.extra.TEXT", sb.toString());
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                this.context.startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(this.context, "Twitter is not Installed", Toast.LENGTH_LONG).show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void textdelete(final int i, RecyclerView.ViewHolder viewHolder, final String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setMessage((CharSequence) "Do you want to delete this post?");
        builder.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                FileUploadInfo fileUploadInfo = (FileUploadInfo) gmaAdapter.this.mainFileUploadInfoList.get(i);
                FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase.getInstance().getReference("GMA").child(str).setValue(null);
            }
        });
        builder.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    /* access modifiers changed from: private */
    public void imagedelete(final int i, RecyclerView.ViewHolder viewHolder, final String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setMessage((CharSequence) "Do you want to delete this post?");
        builder.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                FileUploadInfo fileUploadInfo = (FileUploadInfo) gmaAdapter.this.mainFileUploadInfoList.get(i);
                FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase.getInstance().getReference("GMA").child(str).setValue(null);
            }
        });
        builder.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
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

        DatabaseReference child = this.favoriteRef.child(str2).child(currentUser.getUid());
        FileUploadInfo fileUploadInfo3 = new FileUploadInfo(str4, imageName, "", imageURL, incident, email, displayName, location, null, road, usernam, 1, currentDate(), city, str3, null);
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

        DatabaseReference child = this.favoriteRef.child(str2).child(this.user.getUid());
        FileUploadInfo fileUploadInfo3 = new FileUploadInfo(str4, imageName, "", imageURL, incident, email, displayName, location, null, road, originalPostUser, Integer.valueOf(1), currentDate(), city, str3, null);
        child.setValue(fileUploadInfo3);
        this.favoritechecker = false;
        fav_counttxt(viewHolder, str);
    }

    private void repost_counttxt(final RecyclerView.ViewHolder viewHolder, String str) {
        FirebaseDatabase.getInstance().getReference("GMA").orderByChild("original_key").equalTo(str).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                int childrenCount = (int) dataSnapshot.getChildrenCount();
                String valueOf = String.valueOf(childrenCount);
                if (childrenCount == 1) {
                    ((TextTypeViewHolder) viewHolder).repostcount.setVisibility(View.GONE);
                } else {
                    ((TextTypeViewHolder) viewHolder).repostcount.setText(valueOf);
                }
            }
        });
    }

    private void repost_count(final RecyclerView.ViewHolder viewHolder, String str) {
        FirebaseDatabase.getInstance().getReference("GMA").orderByChild("original_key").equalTo(str).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                int childrenCount = (int) dataSnapshot.getChildrenCount();
                String valueOf = String.valueOf(childrenCount);
                if (childrenCount == 1) {
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
                    ((TextTypeViewHolder) viewHolder).commentcount.setVisibility(View.INVISIBLE);
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
                    ((ImageViewHolder) viewHolder).commentcount.setVisibility(View.INVISIBLE);
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
                    ((TextTypeViewHolder) viewHolder).favcount.setVisibility(View.INVISIBLE);
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
                    ((ImageViewHolder) viewHolder).favcount.setVisibility(View.INVISIBLE);
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GMA");
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
        String key = reference.push().getKey();
        String str2 = key;
        String str3 = key;
        String location = fileUploadInfo.getLocation();
        getprofilephotourl(uid);
        DatabaseReference child = reference.child(key);
        FileUploadInfo fileUploadInfo2 = new FileUploadInfo(str, null, "", null, incident, email, displayName, location, text, road, usernam, Integer.valueOf(0), currentDate(), city, str2, str3);
        child.setValue(fileUploadInfo2);
    }

    /* access modifiers changed from: private */
    public void textrepost2(int i) {
        FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GMA");
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
        String key = reference.push().getKey();
        String str2 = key;
        String location = fileUploadInfo.getLocation();
        String original_key = fileUploadInfo.getOriginal_key();
        getprofilephotourl(uid);
        DatabaseReference child = reference.child(key);
        FileUploadInfo fileUploadInfo2 = new FileUploadInfo(str, null, "", null, incident, email, displayName, location, text, road, originalPostUser, Integer.valueOf(0), currentDate(), city, str2, original_key);
        child.setValue(fileUploadInfo2);
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GMA");
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
        String key = reference.push().getKey();
        String str2 = key;
        String str3 = key;
        String location = fileUploadInfo.getLocation();
        getprofilephotourl(uid);
        DatabaseReference child = reference.child(key);
        FileUploadInfo fileUploadInfo2 = new FileUploadInfo(str, imageName, "", imageURL, incident, email, displayName, location, null, road, usernam, Integer.valueOf(1), currentDate(), city, str3, str2);
        child.setValue(fileUploadInfo2);
    }

    /* access modifiers changed from: private */
    public void imagerepost2(int i) {
        FileUploadInfo fileUploadInfo = (FileUploadInfo) this.mainFileUploadInfoList.get(i);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GMA");
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
        String key = reference.push().getKey();
        String str2 = key;
        String original_key = fileUploadInfo.getOriginal_key();
        String location = fileUploadInfo.getLocation();
        getprofilephotourl(uid);
        DatabaseReference child = reference.child(key);
        FileUploadInfo fileUploadInfo2 = new FileUploadInfo(str, imageName, "", imageURL, incident, email, displayName, location, null, road, originalPostUser, Integer.valueOf(1), currentDate(), city, str2, original_key);
        child.setValue(fileUploadInfo2);
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
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GMA");
                reference.orderByChild("userId").equalTo(str).addListenerForSingleValueEvent(new ValueEventListener() {
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
                            reference.child(substring).updateChildren(hashMap);
                            arrayList.clear();
                        }
                    }
                });
            }
        });
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
