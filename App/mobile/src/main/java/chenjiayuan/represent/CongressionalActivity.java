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

import java.util.ArrayList;

public class CongressionalActivity extends AppCompatActivity {
    private String site = "http://congress.api.sunlightfoundation.com/legislators/locate?";
    private String api = "&apikey=d1ff26dd5fb04253940eae90e286b0ea";
    int repNum = 0;
    int sNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);
        TextView location = (TextView) findViewById(R.id.loc);
        TextView stat = (TextView) findViewById(R.id.ppl);

        //display location
        Intent intent = getIntent();
        if (intent.getStringExtra("mode").equals("zipcode")) { //zipcode
            location.setText("Zipcode " + intent.getStringExtra("zipcode"));
            populateRepList("zipcode", intent.getStringExtra("zipcode"));
        } else { //current location
            location.setText(intent.getStringExtra("location"));
            populateRepList("location", intent.getStringExtra("lalo"));
        }

        //start watch
        Intent watchIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        watchIntent.putExtra("mode", intent.getStringExtra("mode")); //mode = "zipcode" or "currentLocation"
        startService(watchIntent);

        //populate representatives list
        populateListView();
        registerClickCallback();
    }

    //fake data populator
    private void populateRepList(String mode, String data) {
        String request;
//        final TextView stat = (TextView) findViewById(R.id.ppl);

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
                System.out.println(jRepList);
                try{
                    //update number of representatives received
                    repNum = jRepList.getInt("count");
                    JSONArray results = jRepList.optJSONArray("results");
                    for (int i=0; i<repNum; i++) {
                        JSONObject person = results.getJSONObject(i);
                        System.out.println(person);
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
                        } else { party = "Representative"; }
                        String email = person.getString("oc_email");
                        String website = person.getString("website");
                        String twitter = person.getString("twitter_id");
                        String term = person.getString("term_end");
                        String id = person.getString("bioguide_id");
                        PeopleData.people.add(new Representative(id, name, title, party, email,
                                website, twitter, term, R.drawable.curry));
                    }

                    //set stat text
                    stat.setText(Integer.toString(sNum)+" senators and " + Integer.toString(repNum-sNum)
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
                    Log.d("T", "in onconnected");
                    Intent intent = new Intent(CongressionalActivity.this, DetailActivity.class);
                    //TODO: use bundle instead
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
}
