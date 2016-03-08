package chenjiayuan.represent;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    TextView location;
    EditText zipcode;
    ImageView icon;

    //default mode: zipcode
    String mode = "zipcode";

    //default current location: Emeryville
    String latitude = "37.8312983";
    String longitude = "-122.2849983";
    String location_county = "Franklin County";
    String location_state = "AR";

    //Google API
    String site = "https://maps.googleapis.com/maps/api/geocode/json?";
    String api = "&key=AIzaSyAWGQa5PmTMdLylcOKGOn2XbKMI9DaXoik";
    //zipcode: https://maps.googleapis.com/maps/api/geocode/json?address=30332&sensor=false&key=AIzaSyAWGQa5PmTMdLylcOKGOn2XbKMI9DaXoik

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        Intent randomIntent = getIntent();
//        if (randomIntent.getStringExtra("mode").equals("shakeIt")) {
//            latitude = randomIntent.getStringExtra("latitude");
//            longitude = randomIntent.getStringExtra("longitude");
//
//            //directly fetch api
//            String url = site + "latlng=" + latitude + "," + longitude + api;
//            Log.d("T", "api sent for random la/lo: " + url);
//            JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject jLocation) {
//                            JSONArray addr_1 = jLocation.optJSONArray("results");
//                            try{
//                                JSONObject address_components = addr_1.getJSONObject(0);
//                                JSONArray addr_2 = address_components.optJSONArray("address_components");
//                                for (int k=0; k<addr_2.length(); k++) {
//                                    JSONObject obj = addr_2.getJSONObject(k);
//                                    if (obj.getString("long_name").contains("County")) {
//                                        location_county = obj.getString("long_name");
//                                        location_state = addr_2.getJSONObject(k+1).getString("short_name");
//                                    }
//                                }
//                                mode = "currentLocation";
//                                createIntent();
//                                Log.d("T", "location found by random la/lo: " + location_county + ", " + location_state);
//                            } catch (JSONException e) {e.printStackTrace();}
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            // TODO Auto-generated method stub
//                        }
//                    });
//        } else {
            //prevent keyboard appear automatically
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            super.onCreate(savedInstanceState);

            //initialize Google api
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addApi(Wearable.API)  // used for data layer API
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            //get the buttons and textboxes
            setContentView(R.layout.activity_main);
            location = (TextView) findViewById(R.id.location_option);
            zipcode = (EditText) findViewById(R.id.zip_option);
            zipcode.setVisibility(View.VISIBLE);
            location.setVisibility(View.GONE);
//        }
    } //onCreate

    //click search button
    public void searchClickHandler(View view) {
        if (view.getId() == R.id.searchButton) {

            //first get location for zipcode
            if (mode.equals("zipcode")) {
                String url = site + "address=" + zipcode.getText().toString() + "&sensor=false" + api;
                Log.d("T", "api sent for zipcode to location: " + url);
                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jLocation) {
                                JSONArray addr_1 = jLocation.optJSONArray("results");
                                try{
                                    JSONObject address_components = addr_1.getJSONObject(0);
                                    JSONArray addr_2 = address_components.optJSONArray("address_components");
                                    for (int k=0; k<addr_2.length(); k++) {
                                        JSONObject obj = addr_2.getJSONObject(k);
                                        if (obj.getString("long_name").contains("County")) {
                                            location_county = obj.getString("long_name");
                                            location_state = addr_2.getJSONObject(k+1).getString("short_name");
                                        }
                                    }
                                    Log.d("T", "location found by zipcode: " + location_county + ", " + location_state);
                                    createIntent();
                                    //TODO: Chicago issue
                                } catch (JSONException e) {e.printStackTrace();}
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                // Access the RequestQueue through your singleton class.
                MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
            } else {
                createIntent();
            }

        }
    }

    private void createIntent() {
        //TODO: location fetched from above for zipcode is not passed to intent
        //create intent
        Intent intent;
        intent = new Intent(this, CongressionalActivity.class);
        intent.putExtra("mode", mode);
        intent.putExtra("location", location_county + ", " + location_state);
        Log.d("T", "location passed to congressional view: " + location_county + ", " + location_state);
        intent.putExtra("zipcode", zipcode.getText().toString());
        intent.putExtra("lalo", latitude+"/"+longitude);
        Log.d("T", "lalo: " + latitude+"/"+longitude);
        startActivity(intent);
    }

    //clicked location option, will update location_state and location_county
    public void locationOptionClicked(View view) {
        if (view.getId() == R.id.use_location) {
            location = (TextView) findViewById(R.id.location_option);
            zipcode = (EditText) findViewById(R.id.zip_option);
            icon = (ImageView) findViewById(R.id.imageView);
            location.setVisibility(View.VISIBLE);
            zipcode.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.ic_location_on_black_48dp);
            mode = "currentLocation";

            //fetch api
            String url = site + "latlng=" + latitude + "," + longitude + api;
            Log.d("T", "api sent for lat/long to location: " + url);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jLocation) {
                        JSONArray addr_1 = jLocation.optJSONArray("results");
                        try{
                            JSONObject address_components = addr_1.getJSONObject(0);
                            JSONArray addr_2 = address_components.optJSONArray("address_components");
                            for (int k=0; k<addr_2.length(); k++) {
                                JSONObject obj = addr_2.getJSONObject(k);
                                if (obj.getString("long_name").contains("County")) {
                                    location_county = obj.getString("long_name");
                                    location_state = addr_2.getJSONObject(k+1).getString("short_name");
                                }
                            }
                            //set text of current location
                            location.setText("Current Location:\n" + location_county + ", "
                                    + location_state);
                            Log.d("T", "location found by la/lo: " + location_county + ", " + location_state);
                        } catch (JSONException e) {e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                });

            // Access the RequestQueue through your singleton class.
            MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        }
    }

    //clicked zipcode button
    public void zipcodeOptionClicked(View view) {
        if (view.getId() == R.id.use_zipcode) {
            location = (TextView) findViewById(R.id.location_option);
            zipcode = (EditText) findViewById(R.id.zip_option);
            icon = (ImageView) findViewById(R.id.imageView);
            location.setVisibility(View.GONE);
            zipcode.setVisibility(View.VISIBLE);
            icon.setImageResource(R.drawable.ic_location_city_black_48dp);
            mode = "zipcode";
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        Log.d("T", "mGoogleApiClient connected..");
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        Log.d("T", "mGoogleApiClient disconnected..");
        super.onStop();
    }

    //handle option selection
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.home).setIcon(R.drawable.home);
        //menu.findItem(R.id.info).setIcon(R.drawable.info);
        return true;
    }

    //handle option selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
//            case R.id.info:
//                intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d("T", "latitude = " + String.valueOf(mLastLocation.getLatitude()));
            latitude = String.valueOf(mLastLocation.getLatitude());
            Log.d("T", "longitude = " + String.valueOf(mLastLocation.getLongitude()));
            longitude = String.valueOf(mLastLocation.getLongitude());
        } else {
            Log.d("T", "null latitude and longitude");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {}
}
