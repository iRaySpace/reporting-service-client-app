package irayspacii.cdoincidentreporter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by ivanray on 1/2/18.
 */

public class SenderLocationListener implements LocationListener {

    private Location location;

    public SenderLocationListener(Location location) {
        this.location = location;
    }

    public double getLongitude() {
        return location.getLongitude();
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public boolean isLocationAvailable() {
        if(location == null)
            return false;
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}