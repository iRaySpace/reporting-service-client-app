package irayspacii.cdoincidentreporter;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by ivanray on 1/2/18.
 */

public class SenderLocationListener implements LocationListener {

    private LocationSubscriber subscriber;

    /**
     * The listener not only listens but notifies any subscriber for location change.
     * @param subscriber
     */
    public SenderLocationListener(LocationSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void onLocationChanged(Location location) {
        subscriber.locationUpdate(location);
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