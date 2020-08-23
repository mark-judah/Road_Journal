package com.example.road_journal.Activities;

import android.util.Log;

import com.example.road_journal.Pojos.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUtils {
    private static String condition = null;
    private static DatabaseReference dbref = null;
    private static boolean like = false;
    private static int one = 1;
    /* access modifiers changed from: private */
    public static String username;

    public static String getcurrentuser() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static String getusername() {
        if (one == 1) {
            String str = getcurrentuser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            reference.keepSynced(true);
            reference.orderByChild("uid").equalTo(str).addListenerForSingleValueEvent(new ValueEventListener() {
                public void onCancelled(DatabaseError databaseError) {
                }

                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot value : dataSnapshot.getChildren()) {
                        FirebaseUtils.username = ((User) value.getValue(User.class)).getUser_name();
                        StringBuilder sb = new StringBuilder();
                        sb.append("we are here");
                        sb.append(FirebaseUtils.username);
                        Log.d("comment1", sb.toString());
                    }
                }
            });
        }
        return username;
    }

    public static String fetchfavorites(String str, String str2) {
        int i = one;
        return null;
    }
}
