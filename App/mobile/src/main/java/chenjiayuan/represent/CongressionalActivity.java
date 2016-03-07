package chenjiayuan.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CongressionalActivity extends AppCompatActivity {
    //api for google maps
    private String site = "http://congress.api.sunlightfoundation.com/legislators/locate?";
    private String api = "&apikey=d1ff26dd5fb04253940eae90e286b0ea";

    //total num of people, and numb of senators, subtract to get num of reps
    private int totalNum = 0;
    private int sNum = 0;

    //value to send to watch
    private String mNames = "defaultNames";
    private String mParties = "defaultParties";
    private String county = "Mohave";
    private String state = "AZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);
        TextView location = (TextView) findViewById(R.id.loc);

        //display location on top, and populate rep list
        //TODO: data not saved fast enough
        Intent intent = getIntent();
        if (intent.getStringExtra("mode").equals("zipcode")) {
            location.setText("Zipcode " + intent.getStringExtra("zipcode"));
            populateRepList("zipcode", intent.getStringExtra("zipcode"));
        } else {
            location.setText(intent.getStringExtra("location"));
            populateRepList("location", intent.getStringExtra("lalo"));
        }

        //save county and state data
        county = intent.getStringExtra("location").split(", ")[0].split(" ")[0]; //Alameda County
        state = intent.getStringExtra("location").split(", ")[1]; //CA

        //display view
        populateListView();
        registerClickCallback();

        //send info to watch
        startWatch();
    }

    private void startWatch() {
        //construct strings of names and parties
        StringBuilder namesb = new StringBuilder();
        StringBuilder partysb = new StringBuilder();
        for (int i=0; i<PeopleData.people.size(); i++) {
            namesb.append(PeopleData.people.get(i).getName() + "-");
            partysb.append(PeopleData.people.get(i).getParty() + "-");
        }
        mNames = namesb.toString();
        mParties = partysb.toString();

        //construct string of 2012vote
        String m2012vote = get2012Vote(county, state);

        //create intent
        Intent watchIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        watchIntent.putExtra("names", mNames);
        watchIntent.putExtra("parties", mParties);
        watchIntent.putExtra("2012votes", m2012vote);

        Log.d("T", "==data to send to watch======");
        Log.d("T", mNames);
        Log.d("T", mParties);
        Log.d("T", m2012vote);
        startService(watchIntent);
    }

    //search for 2012 vote data based on County + State
    private String get2012Vote(String county, String state) {
        String voteString = loadJSONFromAsset();
        String concatVoteData = null;

        try {
            JSONArray jVote = new JSONArray(voteString);
            int jVoteLength = jVote.length();

            //search through match attributes
            Log.d("T", "Location to search in election json: " + county + " " + state);
            for(int i=0; i < jVoteLength; i++) {
                JSONObject loc = jVote.getJSONObject(i);
                if(loc.getString("county-name").equals(county) && loc.getString("state-postal").equals(state)) {
                    //location match!
                    String obamaVote = Double.toString(loc.getDouble("obama-percentage"));
                    String romneyVote = Double.toString(loc.getDouble("romney-percentage"));

                    //concatenate 2012 vote data here
                    concatVoteData = county + "-" + state + "-" + obamaVote + "-" + romneyVote + "-";
                    Log.d("T", "string constructed: " + concatVoteData);
                    return concatVoteData;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return concatVoteData;
    }

    //load Json from asset directory
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("election-county-2012.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //save list of API fetched reps to data structure
    private void populateRepList(String mode, String data) {
        String request;
        //create api link
        if (mode.equals("zipcode")) {
            request= site + "zip=" + data + api;
        } else {
            String[] lalo = data.split("/");
            request = site + "latitude=" + lalo[0] + "&longitude=" + lalo[1] + api;
        }

        //send api request
        final TextView stat = (TextView) findViewById(R.id.ppl);

        PeopleData.people = new ArrayList<Representative>();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
            (Request.Method.GET, request, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jRepList) {
                    try{
                        //update number of representatives received
                        totalNum = jRepList.getInt("count");
                        JSONArray results = jRepList.optJSONArray("results");

                        //add people to PeopleData
                        for (int i=0; i<totalNum; i++) {
                            JSONObject person = results.getJSONObject(i);
                            String name = person.getString("first_name")+" "+person.getString("last_name");
                            String title;
                            if (person.getString("title").equals("Sen")){
                                title = "Senator";
                                sNum++;
                            } else { title = "Representative"; }
                            String party;
                            if (person.getString("party").equals("R")){
                                party = "Republican";
                            } else if (person.getString("party").equals("D")) {
                                party = "Democrat";
                            } else { party = "Independent"; }
                            String email = person.getString("oc_email");
                            String website = person.getString("website");
                            String twitter = person.getString("twitter_id");
                            String term = person.getString("term_end");
                            String id = person.getString("bioguide_id");
                            PeopleData.people.add(new Representative(id, name, title, party, email,
                                    website, twitter, term, R.drawable.curry));
                        }
                        System.out.println("====peopleDATA.people====");
                        System.out.println(PeopleData.people);

                        //set stat text
                        stat.setText(Integer.toString(sNum)+" senators and " + Integer.toString(totalNum-sNum)
                        + " representatives found!");
                    } catch (JSONException e) {e.printStackTrace();}
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                }
            });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    //populate the list view
    private void populateListView() {
        ArrayAdapter<Representative> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.repListView);
        list.setAdapter(adapter);
    }

    //??
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
            super(CongressionalActivity.this, R.layout.item_view, PeopleData.people);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }
            Button btn = (Button) itemView.findViewById(R.id.moreInfoButton);
            final Representative r = PeopleData.people.get(position);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(CongressionalActivity.this, DetailActivity.class);
                    intent.putExtra("id", r.getId());
                    intent.putExtra("name", r.getName());
                    intent.putExtra("party", r.getParty());
                    intent.putExtra("term", r.getTerm());
                    intent.putExtra("role", r.getRole());
                    intent.putExtra("picID", Integer.toString(r.getPic()));
                    startActivity(intent);
                }
            });

            // Fill the view
            ImageView imageView = (ImageView)itemView.findViewById(R.id.profile_pic);
            imageView.setImageResource(r.getPic());
            TextView nameText = (TextView) itemView.findViewById(R.id.name);
            nameText.setText(r.getName());
            TextView roleText = (TextView) itemView.findViewById(R.id.role);
            roleText.setText(r.getRole());
            TextView partyText = (TextView) itemView.findViewById(R.id.party);
            //TODO: email and website not clickable
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
}

