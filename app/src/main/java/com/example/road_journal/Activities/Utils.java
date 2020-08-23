package com.example.road_journal.Activities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.view.Display;
import android.view.WindowManager;

import com.google.firebase.database.FirebaseDatabase;

public class Utils {
    private static FirebaseDatabase mDatabase;
    private static int screenHeight;
    private static int screenWidth;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    public static int dpToPx(int i) {
        return (int) (((float) i) * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context context) {
        if (screenHeight == 0) {
            Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            screenHeight = point.y;
        }
        return screenHeight;
    }

    public static int getScreenWidth(Context context) {
        if (screenWidth == 0) {
            Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            screenWidth = point.x;
        }
        return screenWidth;
    }

    public static boolean isAndroid5() {
        return VERSION.SDK_INT >= 21;
    }
}
