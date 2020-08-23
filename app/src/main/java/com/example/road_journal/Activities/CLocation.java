package com.example.road_journal.Activities;

import android.location.Location;

public class CLocation extends Location {
    public CLocation(Location location) {
        super(location);
    }

    public float distanceTo(Location location) {
        return super.distanceTo(location);
    }

    public float getAccuracy() {
        return super.getAccuracy();
    }

    public double getAltitude() {
        return super.getAltitude();
    }

    public float getSpeed() {
        return super.getSpeed();
    }
}
