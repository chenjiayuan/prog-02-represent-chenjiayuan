package chenjiayuan.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTextView;
    private Button mFeedBtn;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String voteStat = new StringBuilder().append(intent.getStringExtra("voteCounty"))
                .append(", ").append(intent.getStringExtra("voteState")).append("\nObama: ")
                .append(intent.getStringExtra("obama")).append("% of vote \nRomney ")
                .append(intent.getStringExtra("romney")).append("% of vote").toString();

        //create p
        if (intent.getStringExtra("name1") == null) { //no reps
            PageData.p = new Page[][] {
                {new Page("", "", 0),}
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
                    new Page("2012 Presidential Vote", voteStat, 0)
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
                    new Page("2012 Presidential Vote", voteStat, 0),
                            //Emeryville, CA Obama: 67% of vote Romney 20% of vote
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

    public void buttonClickHandler(View view) {
        if (view.getId() == R.id.button) {
            final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
            System.out.println(pager.getCurrentItem().x);

            Intent intent = new Intent(getBaseContext(), WatchToPhoneService.class);
            intent.putExtra("mode", "select");
            intent.putExtra("index", Integer.toString(pager.getCurrentItem().x));
            startService(intent);
        }
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
