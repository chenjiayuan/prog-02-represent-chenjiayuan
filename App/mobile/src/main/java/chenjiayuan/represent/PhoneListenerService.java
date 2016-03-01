package chenjiayuan.represent;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by chenjiayuan on 2/27/16.
 */
public class PhoneListenerService extends WearableListenerService {

    private static final String SHAKE = "/shake";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(SHAKE) ) {
            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            Intent randomIntent = new Intent(this, CongressionalActivity.class);
            //TODO: use bundle instead
            randomIntent.putExtra("mode", "currentLocation");
            randomIntent.putExtra("location", "Random location: Atlanta, GA");
            startActivity(randomIntent);
        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}