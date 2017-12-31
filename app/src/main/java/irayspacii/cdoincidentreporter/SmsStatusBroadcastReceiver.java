package irayspacii.cdoincidentreporter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by ivanray on 1/1/18.
 */

public class SmsStatusBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Notify about the reporting.
        switch(getResultCode()) {
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
            case SmsManager.RESULT_ERROR_NO_SERVICE:
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                Toast.makeText(context, "Unable to report!", Toast.LENGTH_SHORT).show();
                break;
            case Activity.RESULT_OK:
                Toast.makeText(context, "The report has been submitted!", Toast.LENGTH_SHORT).show();
                break;
        }

    }

}