package chenjiayuan.represent;

import android.content.Intent;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CongressionalActivity extends AppCompatActivity {
    private List<Representative> reps = new ArrayList<Representative>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);

        TextView location = (TextView) findViewById(R.id.loc);
        Intent intent = getIntent();
        if (intent.getStringExtra("mode").equals("zipcode")) { //zipcode
            location.setText("Zipcode " + intent.getStringExtra("zipcode"));
        } else { //current location
            location.setText(intent.getStringExtra("location"));
        }
        populateRepList();
        populateListView();
        registerClickCallback();
    }

    //fake data populator
    private void populateRepList() {
        reps.add(new Representative("Stephen Curry", "Senator", "Republican", "jiayuan.chen@berkeley.edu", "chenjiayuan.com", "lastTweet", "9/1/2017"));
        reps.add(new Representative("Klay Thompson", "Senator", "Democrat", "jiayuan.chen@berkeley.edu", "chenjiayuan.com", "lastTweet", "9/2/2017"));
        reps.add(new Representative("Harrison Barnes", "Representative", "Republican", "jiayuan.chen@berkeley.edu", "chenjiayuan.com", "lastTweet", "9/3/2017"));
        reps.add(new Representative("Andrew Bogut", "Senator", "Democrat", "jiayuan.chen@berkeley.edu", "chenjiayuan.com", "lastTweet", "9/4/2017"));
        reps.add(new Representative("Draymond Green", "Representative", "Republican", "jiayuan.chen@berkeley.edu", "chenjiayuan.com", "lastTweet", "9/5/2017"));
    }

    //populate the list view
    private void populateListView() {
        ArrayAdapter<Representative> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.repListView);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.repListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long id) {
            }
        });
    }

    //adapter that manage the content of the list view
    private class MyListAdapter extends ArrayAdapter<Representative> {
        public MyListAdapter() {
            super(CongressionalActivity.this, R.layout.item_view, reps);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }
            Button btn = (Button) itemView.findViewById(R.id.moreInfoButton);
            final Representative r = reps.get(position);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("T", "in onconnected");
                    Intent intent = new Intent(CongressionalActivity.this, DetailActivity.class);
                    intent.putExtra("name", r.getName());
                    intent.putExtra("party", r.getParty());
                    intent.putExtra("term", r.getTerm());
                    intent.putExtra("role", r.getRole());
                    startActivity(intent);
                }
            });

            // Fill the view
            //ImageView imageView = (ImageView)itemView.findViewById(R.id.item_icon);
            //imageView.setImageResource(currentCar.getIconID());
            TextView nameText = (TextView) itemView.findViewById(R.id.name);
            nameText.setText(r.getName());
            TextView roleText = (TextView) itemView.findViewById(R.id.role);
            roleText.setText(r.getRole());
            TextView partyText = (TextView) itemView.findViewById(R.id.party);
            partyText.setText(r.getParty());
            TextView emailText = (TextView) itemView.findViewById(R.id.email);
            emailText.setText(r.getEmail());
            TextView webText = (TextView) itemView.findViewById(R.id.website);
            webText.setText(r.getWebsite());
            //TextView tweetText = (TextView) itemView.findViewById(R.id.tweet);
            //tweetText.setText(r.getLastTweet());
            return itemView;
        }
    }
}
