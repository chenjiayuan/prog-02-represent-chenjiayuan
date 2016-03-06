package chenjiayuan.represent;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    String location_county = "Emeryville";
    String location_state = "CA";

    //Google API
    String site = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
    String api = "&key=AIzaSyAWGQa5PmTMdLylcOKGOn2XbKMI9DaXoik";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    } //onCreate

    public void searchClickHandler(View view) {
        if (view.getId() == R.id.searchButton) {
            Intent intent;
            intent = new Intent(this, CongressionalActivity.class);
            //TODO: use bundle instead
            intent.putExtra("mode", mode);
            intent.putExtra("location", location_county + ", " + location_state);
            intent.putExtra("zipcode", zipcode.getText().toString());
            intent.putExtra("lalo", latitude+"/"+longitude);
            startActivity(intent);
        }
    }

    public void locationOptionClicked(View view) {
        if (view.getId() == R.id.use_location) {
            location = (TextView) findViewById(R.id.location_option);
            zipcode = (EditText) findViewById(R.id.zip_option);
            icon = (ImageView) findViewById(R.id.imageView);
            location.setVisibility(View.VISIBLE);
            zipcode.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.ic_location_on_black_48dp);
            mode = "currentLocation";
            final JSONObject jLocation;

            //fetch api
            String url = site + latitude + "," + longitude + api;
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jLocation) {
                        System.out.println(jLocation);
                        JSONArray addr_1 = jLocation.optJSONArray("results");
                        try{
                            JSONObject address_components = addr_1.getJSONObject(0);
                            JSONArray addr_2 = address_components.optJSONArray("address_components");
                            JSONObject jCounty  = addr_2.getJSONObject(3);
                            JSONObject jState = addr_2.getJSONObject(4);

                            //set text
                            location.setText("Current Location:\n" + jCounty.getString("long_name") + ", "
                                    + jState.getString("short_name"));
                            location_county = jCounty.getString("long_name");
                            location_state = jState.getString("short_name");
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
        System.out.println("mGoogleApiClient connected..");
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        System.out.println("mGoogleApiClient disconnected..");
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.home).setIcon(R.drawable.home);
        //menu.findItem(R.id.info).setIcon(R.drawable.info);
        return true;
    }

    //handle option select
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
            System.out.println(String.valueOf(mLastLocation.getLatitude()));
            latitude = String.valueOf(mLastLocation.getLatitude());
            System.out.println(String.valueOf(mLastLocation.getLongitude()));
            longitude = String.valueOf(mLastLocation.getLongitude());
            //getAddressFromLocation(mLastLocation, this, new GeocoderHandler());
        } else {
            System.out.println("null location");
        }
    }

//    //print the received geo location
//    private class GeocoderHandler extends Handler {
//        @Override
//        public void handleMessage(Message message) {
//            String result;
//            switch (message.what) {
//                case 1:
//                    Bundle bundle = message.getData();
//                    result = bundle.getString("address");
//                    break;
//                default:
//                    result = null;
//            }
//            // replace by what you need to do
//            //System.out.println(result);
//        }
//    }

//    //transform coordinate to location
//    public static void getAddressFromLocation(
//            final  Location location, final Context context, final Handler handler) {
//        Thread thread = new Thread() {
//            @Override public void run() {
//                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//                String result = null;
//                try {
//                    List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                    if (list != null && list.size() > 0) {
//                        Address address = list.get(0);
//                        System.out.println(address);
//                        // sending back first address line and locality
//                        result = address.getAddressLine(0) + ", " + address.getLocality() +
//                                "\n"+ address.getSubAdminArea() + "/" + address.getAdminArea();
//                    }
//                } catch (IOException e) {
//                    Log.e("T", "Impossible to connect to Geocoder", e);
//                } finally {
//                    Message msg = Message.obtain();
//                    msg.setTarget(handler);
//                    if (result != null) {
//                        msg.what = 1;
//                        Bundle bundle = new Bundle();
//                        bundle.putString("address", result);
//                        msg.setData(bundle);
//                    } else
//                        msg.what = 0;
//                    msg.sendToTarget();
//                }
//            }
//        };
//        thread.start();
//    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {}
}
