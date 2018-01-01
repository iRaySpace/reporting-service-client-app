package irayspacii.cdoincidentreporter;

import android.Manifest;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Handler for GPS
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the handler from service
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Notify about GPS status
        if(getGpsStatus())
            Toast.makeText(this, "GPS is enabled", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "GPS is disabled", Toast.LENGTH_SHORT).show();

        // Permission checker
        if(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Get the location listener
        locationListener = new SenderLocationListener(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));

        // Start requesting for location
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        // Locate button configuration
        Button locateButton = (Button) findViewById(R.id.LocateButton);

        locateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SenderLocationListener senderListener = (SenderLocationListener) locationListener;

                if(senderListener.isLocationAvailable()) {
                    Toast.makeText(getBaseContext(), "Longitude: " + senderListener.getLongitude(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getBaseContext(), "Latitude: " + senderListener.getLatitude(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Location not available", Toast.LENGTH_SHORT).show();
                }

            }
        });

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

        // SMS Handler of Android
        SmsManager sender = SmsManager.getDefault();

        // BroadcastReceiver Intent
        Intent sentIntent = new Intent("irayspacii.cdoincidentreporter.BroadcastReceiver");

        // Send PI
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent, 0);

        // Send the SMS (the number is from AFREESMS PH) (don't spam lol.)
        sender.sendTextMessage("+639177140579", null, incidentType, sentPI, null);

    }

}