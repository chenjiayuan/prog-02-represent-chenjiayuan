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
    private static final String SELECT = "/select";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(SHAKE) ) {
            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            Intent randomIntent = new Intent(this, CongressionalActivity.class);
            //TODO: use bundle instead
            randomIntent.putExtra("mode", "currentLocation");
            double longitude = Math.random() * Math.PI * 2;
            double latitude = Math.acos(Math.random() * 2 - 1);
            randomIntent.putExtra("location", "Random location:\n" + String.valueOf(longitude) + ", " + String.valueOf(latitude));
            randomIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(randomIntent);
        } else if (messageEvent.getPath().equalsIgnoreCase(SELECT)) {
            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            System.out.println(value);
            Intent indexActivity = new Intent(this, DetailActivity.class);
            //TODO: demo purpose
            Representative r = PeopleData.people.get(Integer.parseInt(value));
            indexActivity.putExtra("id", r.getId());
            indexActivity.putExtra("name", r.getName());
            indexActivity.putExtra("party", r.getParty());
            indexActivity.putExtra("term", r.getTerm());
            indexActivity.putExtra("role", r.getRole());
            indexActivity.putExtra("picID", Integer.toString(r.getPic()));
            indexActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(indexActivity);
        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}