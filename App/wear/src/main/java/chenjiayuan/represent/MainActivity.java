package chenjiayuan.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "xDCRIJ1Qe9W6vxz8XCdvkvVK4";
    private static final String TWITTER_SECRET = "thwaz8A9XkfmSk1kKAjftbdcjhOL4J0FsWTB0XPKI0hqeUrggX";


    private TextView mTextView;
    private Button mFeedBtn;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String voteStat = new StringBuilder().append(intent.getStringExtra("voteCounty"))
                .append(", ").append(intent.getStringExtra("voteState")).append("\nObama: ")
                .append(intent.getStringExtra("obama")).append("%\nRomney ")
                .append(intent.getStringExtra("romney")).append("%").toString();

        //create p
        if (intent.getStringExtra("name1") == null) { //no reps
            PageData.p = new Page[][] {
                {new Page("Represent!", "start with your phone...", 0),}
            };
        } else if (intent.getStringExtra("name4") != null) { //4 person
            PageData.p = new Page[][] {
                {
                    new Page(intent.getStringExtra("name1"), intent.getStringExtra("party1"), 0),
                    new Page(intent.getStringExtra("name2"), intent.getStringExtra("party2"), 0),
                    new Page(intent.getStringExtra("name3"), intent.getStringExtra("party3"), 0),
                    new Page(intent.getStringExtra("name4"), intent.getStringExtra("party4"), 0),
                },
                {
                    new Page("2012 Vote", voteStat, 0)
                }
            };
        } else { //3 person
            PageData.p = new Page[][] {
                {
                    new Page(intent.getStringExtra("name1"), intent.getStringExtra("party1"), 0),
                    new Page(intent.getStringExtra("name2"), intent.getStringExtra("party2"), 0),
                    new Page(intent.getStringExtra("name3"), intent.getStringExtra("party3"), 0),
                },
                {
                    new Page("2012 Vote", voteStat, 0),
                }
            };
        }

        //GridViewPager
        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new SampleGridPagerAdapter(this, getFragmentManager()));

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                Log.d("T", "shake detected!!!!");
                Intent phoneIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                phoneIntent.putExtra("mode", "shake");
                phoneIntent.putExtra("index", "-1");
                startService(phoneIntent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}
