package irayspacii.cdoincidentreporter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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