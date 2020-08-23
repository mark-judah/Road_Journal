package com.example.road_journal.Activities;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class LocationService extends Service implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener {
    private static final long FASTEST_INTERVAL = 1000;
    private static final long INTERVAL = 2000;
    static double distance;
    static Location lEnd;
    static Location lStart;
    static Location mCurrentLocation;
    private final IBinder mBinder = new LocalBinder();
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    LocationService myService;
    double speed;

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public LocationService getService() {
            return LocationService.this;
        }
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public void onConnectionSuspended(int i) {
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        createLocationRequest();
        mGoogleApiClient = new Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        return mBinder;
    }

    /* access modifiers changed from: protected */
    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(100);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }

    public void onConnected(Bundle bundle) {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
        } catch (SecurityException unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
        distance = 0.0d;
    }

    public void onLocationChanged(Location location) {
        ReportSpeed.locate.dismiss();
        mCurrentLocation = location;
        if (lStart == null) {
            lStart = mCurrentLocation;
            lEnd = mCurrentLocation;
        } else {
            lEnd = mCurrentLocation;
        }
        updateUI();
        speed = (double) ((location.getSpeed() * 18.0f) / 5.0f);
    }

    private void updateUI() {
        if (ReportSpeed.f46p == 0) {
            double d = distance;
            double distanceTo = (double) lStart.distanceTo(lEnd);
            Double.isNaN(distanceTo);
            distance = d + (distanceTo / 1000.0d);
            ReportSpeed.endTime = System.currentTimeMillis();
            TimeUnit.MILLISECONDS.toMinutes(ReportSpeed.endTime - ReportSpeed.startTime);
            if (speed > 0.0d) {
                TextView textView = ReportSpeed.speed;
                StringBuilder sb = new StringBuilder();
                sb.append(new DecimalFormat("#.##").format(speed));
                sb.append(" km/hr");
                textView.setText(sb.toString());
            } else {
                ReportSpeed.speed.setText("0 km/hr");
            }
            lStart = lEnd;
        }
    }

    public boolean onUnbind(Intent intent) {
        stopLocationUpdates();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        lStart = null;
        lEnd = null;
        distance = 0.0d;
        return super.onUnbind(intent);
    }
}
