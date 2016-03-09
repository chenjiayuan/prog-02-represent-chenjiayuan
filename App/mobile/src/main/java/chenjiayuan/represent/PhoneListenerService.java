package chenjiayuan.represent;

import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Random;

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

            //generate a set of random coordinates within US
            Random rand = new Random();
            int lat1 = rand.nextInt((41 - 33) + 1) + 33;
            int lat2 = rand.nextInt(1000000);
            int lon1 = rand.nextInt((116 - 83) + 1) + 83;
            int lon2 = rand.nextInt(1000000);
            final String latitude = Integer.toString(lat1) + "." + Integer.toString(lat2);
            final String longitude = "-" + Integer.toString(lon1) + "." + Integer.toString(lon2);
            Log.d("T", "rand location is " + latitude + " " + longitude);

            //call api
            String site = "https://maps.googleapis.com/maps/api/geocode/json?";
            String api = "&key=AIzaSyAWGQa5PmTMdLylcOKGOn2XbKMI9DaXoik";
            String url = site + "latlng=" + latitude + "," + longitude + api;

            Log.d("T", "api sent for random la/lo: " + url);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                 (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jLocation) {
                        JSONArray addr_1 = jLocation.optJSONArray("results");
                        try{
                            JSONObject address_components = addr_1.getJSONObject(0);
                            JSONArray addr_2 = address_components.optJSONArray("address_components");
                            String location_county  = null;
                            String location_state = null;
                            for (int k=0; k<addr_2.length(); k++) {
                                JSONObject obj = addr_2.getJSONObject(k);
                                if (obj.getString("long_name").contains("County")) {
                                    location_county = obj.getString("long_name");
                                    location_state = addr_2.getJSONObject(k+1).getString("short_name");
                                }
                            }
                            Log.d("T", "location found by random la/lo: " + location_county + ", " + location_state);

                            Intent intent;
                            intent = new Intent(PhoneListenerService.this, CongressionalActivity.class);
                            intent.putExtra("mode", "currentLocation");
                            intent.putExtra("location", location_county + ", " + location_state);
                            Log.d("T", "location passed to congressional view: " + location_county + ", " + location_state);
                            intent.putExtra("zipcode", "");
                            intent.putExtra("lalo", latitude+"/"+longitude);
                            Log.d("T", "lalo: " + latitude + "/" + longitude);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });
            MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
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
            indexActivity.putExtra("picID", r.getPicURL());
            indexActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(indexActivity);
        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}