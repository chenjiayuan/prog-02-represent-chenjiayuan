package chenjiayuan.represent;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by chenjiayuan on 2/27/16
 */
public class WatchListenerService extends WearableListenerService {
    private static final String MSG = "/msg";
    private static final String LOC = "/currentLocation";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());

        if( messageEvent.getPath().equalsIgnoreCase( MSG ) ) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d("T", "received value: " + value);
            Intent intent = new Intent(this, MainActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //split the messages
            //value = "name-name-name-name-/party-party-party-party-/County-state-obama-romney-";
            String[] names = value.split("/")[0].split("-");
            String[] parties = value.split("/")[1].split("-");
            String[] votes = value.split("/")[2].split("-");
            intent.putExtra("name1", names[0]);
            intent.putExtra("name2", names[1]);
            intent.putExtra("name3", names[2]);
            intent.putExtra("party1", parties[0]);
            intent.putExtra("party2", parties[1]);
            intent.putExtra("party3", parties[2]);
            intent.putExtra("voteCounty", votes[0]);
            intent.putExtra("voteState", votes[1]);
            intent.putExtra("obama", votes[2]);
            intent.putExtra("romney", votes[3]);
            if (names.length == 4) {
                intent.putExtra("name4", names[3]);
                intent.putExtra("party4", parties[3]);
            }
            startActivity(intent);
        } else {
            super.onMessageReceived( messageEvent );
            Log.d("T", "mode 0 is: shit");
        }

    }
}