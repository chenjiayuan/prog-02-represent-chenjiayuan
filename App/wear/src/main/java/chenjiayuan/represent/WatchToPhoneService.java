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


public class WatchToPhoneService extends Service implements GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mWatchApiClient;
    private List<Node> nodes = new ArrayList<>();
    private String mode = "";

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mWatchApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(this)
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
        Log.d("T", "received mode: " + mode);
        //and actually connect it
        mWatchApiClient.connect();
        Log.d("T", "mWatchApiClient.connect() called");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override //alternate method to connecting: no longer create this in a new thread, but as a callback
    public void onConnected(Bundle bundle) {
        Log.d("T", "in onconnected");
        Wearable.NodeApi.getConnectedNodes(mWatchApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                        nodes = getConnectedNodesResult.getNodes();
                        Log.d("T", "found nodes");
                        //when we find a connected node, we populate the list declared above
                        //finally, we can send a message
                        sendMessage("/" + mode, mode); // ('/shake', 'shake')
                        Log.d("T", "sent");
                    }
                });
    }

    @Override //we need this to implement GoogleApiClient.ConnectionsCallback
    public void onConnectionSuspended(int i) {}

    private void sendMessage(final String path, final String text ) {
        for (Node node : nodes) {
            Wearable.MessageApi.sendMessage(
                    mWatchApiClient, node.getId(), path, text.getBytes());
        }
    }

}