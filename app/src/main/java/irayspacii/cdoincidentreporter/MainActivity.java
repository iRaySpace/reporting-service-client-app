package irayspacii.cdoincidentreporter;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Target;

public class MainActivity extends AppCompatActivity implements LocationSubscriber {

    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    // Handler for GPS
    private LocationManager locationManager;
    private LocationListener locationListener;

    // Location variable
    private Location location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Disable the report button
        Button reportButton = (Button) findViewById(R.id.ReportButton);
        reportButton.setEnabled(false);

        // Get the handler from service
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//        // Notify about GPS status
//        if(getGpsStatus())
//            Toast.makeText(this, "GPS is enabled", Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(this, "GPS is disabled", Toast.LENGTH_SHORT).show();

        // Get the GPS location
        if(Build.VERSION.SDK_INT >= 23)
            getGpsLocationWrapper();
        else
            getGpsLocation();

    }

    public void onClick(View v) {

        RadioGroup incident = (RadioGroup) findViewById(R.id.IncidentRadios);

        // Get the selected incident type
        int selectedIncidentId = incident.getCheckedRadioButtonId();

        // Get the corresponding radio button
        RadioButton selectedIncident = (RadioButton) findViewById(selectedIncidentId);

        // Send SMS about the incident
        sendSMS(selectedIncident.getText().toString());

    }

    private boolean getGpsStatus() {

        // Provides access to content providers
        ContentResolver contentResolver = getBaseContext().getContentResolver();

        return Settings.Secure.isLocationProviderEnabled(contentResolver, LocationManager.GPS_PROVIDER);

    }

    private void sendSMS(String incidentType) {

        // Notify the user
        Toast.makeText(MainActivity.this, incidentType, Toast.LENGTH_SHORT).show();

        // Text Message
        String textMessage = "lat:" + location.getLatitude() + "\n"
                + "long:" + location.getLongitude() + "\n"
                + "type:" + incidentType;

        // SMS Handler of Android
        SmsManager sender = SmsManager.getDefault();

        // BroadcastReceiver Intent
        Intent sentIntent = new Intent("irayspacii.cdoincidentreporter.BroadcastReceiver");

        // Send PI
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent, 0);

        // Send the SMS (the number is from AFREESMS PH) (don't spam lol.)
        sender.sendTextMessage("09177140579", null, textMessage, sentPI, null);

    }

    private void getGpsLocation() {

        // Initialize the listener for GPS
        locationListener = new SenderLocationListener(this);

        // Explicit permission for SDK 23 and above
        if(Build.VERSION.SDK_INT >= 23) {

            int hasGpsPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

            if(hasGpsPermission != PackageManager.PERMISSION_GRANTED)
                return;

        }

        // Start requesting for location
        // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    /**
     * Apparently, the SDK version 23 and above needs Runtime Permission handled.
     */
    @TargetApi(23)
    private void getGpsLocationWrapper() {

        int hasGpsPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        // Request for runtime permission
        if(hasGpsPermission != PackageManager.PERMISSION_GRANTED) {

            // Only ask for fine location.
            // If granted, permissions to coarse location will be also granted
            String[] permission = { Manifest.permission.ACCESS_FINE_LOCATION };

            requestPermissions(permission, REQUEST_CODE_ASK_PERMISSIONS);

            return;

        }

        // Get the GPS location
        getGpsLocation();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Execute getGpsLocation() if granted.
        switch(requestCode) {

            case REQUEST_CODE_ASK_PERMISSIONS:

                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) { getGpsLocation(); }
                else { Toast.makeText(this, "Please allow GPS!", Toast.LENGTH_SHORT).show(); }

                break;

        }

    }

    @Override
    public void locationUpdate(Location location) {

        Toast.makeText(getBaseContext(), "Position updated!", Toast.LENGTH_SHORT).show();

        // Update location
        this.location = location;

        // Hide progress bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.ProgressCircle);
        progressBar.setVisibility(View.INVISIBLE);

        // Set the progress text
        TextView progressText = (TextView) findViewById(R.id.ProgressText);
        progressText.setText("Your position is identified\nAccuracy: " + location.getAccuracy());

        // Enable the report button
        Button reportButton = (Button) findViewById(R.id.ReportButton);
        reportButton.setEnabled(true);

    }

}