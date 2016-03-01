package chenjiayuan.represent;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenjiayuan on 2/27/16.
**/


public class WatchToPhoneService extends Service {

    private GoogleApiClient mWatchApiClient;
    private List<Node> nodes = new ArrayList<>();
    private String mode = "";
    private String index = "";

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mWatchApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                    }
                })
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWatchApiClient.disconnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("T", "onStartCommand");
        Bundle extras = intent.getExtras();
        mode = extras.getString("mode");
        index = extras.getString("index");
        Log.d("T", "received mode: " + mode);
        mWatchApiClient.connect();
        sendMessage("/" + mode, index); // ('/shake', 'shake')
        Log.d("T", "mWatchApiClient.connect() called");
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendMessage(final String path, final String text ) {
        Wearable.NodeApi.getConnectedNodes(mWatchApiClient).
                setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>()
        {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                nodes = getConnectedNodesResult.getNodes();
                for (Node node : nodes)
                    Wearable.MessageApi.sendMessage(mWatchApiClient, node.getId(), path, text.getBytes());
            }
        });
    }
}

