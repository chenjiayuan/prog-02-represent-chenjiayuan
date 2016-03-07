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
//            String[] names = value.split("/")[0].split("-");
//            String[] parties = value.split("/")[1].split("-");
//            System.out.println(names);
//            System.out.println(parties);
            intent.putExtra("mode", "zipcode");
            startActivity(intent);
        } else {
            super.onMessageReceived( messageEvent );
            Log.d("T", "mode 0 is: shit");
        }

    }
}