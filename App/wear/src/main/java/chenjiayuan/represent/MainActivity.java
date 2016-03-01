package chenjiayuan.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
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
        String mode = intent.getStringExtra("mode");
        Log.d("T", "mode is: " + mode);

        //GridViewPager
//        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
//        pager.setAdapter(new SampleGridPagerAdapter(this, getFragmentManager(), mode));


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
                startService(phoneIntent);
            }
        });
    }

    public void buttonClickHandler(View view) {
        if (view.getId() == R.id.button) {
            Intent intent = new Intent(getBaseContext(), WatchToPhoneService.class);
            intent.putExtra("mode", "shake");
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
