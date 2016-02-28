package chenjiayuan.represent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CongressionalActivity extends AppCompatActivity {
    private List<Representative> reps = new ArrayList<Representative>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);

        populateCarList();
        populateListView();
        registerClickCallback();
    }

    private void populateCarList() {
        reps.add(new Representative("1", "party", "email", "website", "lastTweet"));
        reps.add(new Representative("2", "party", "email", "website", "lastTweet"));
        reps.add(new Representative("3", "party", "email", "website", "lastTweet"));
        reps.add(new Representative("4", "party", "email", "website", "lastTweet"));
        reps.add(new Representative("5", "party", "email", "website", "lastTweet"));
        reps.add(new Representative("6", "party", "email", "website", "lastTweet"));
        reps.add(new Representative("7", "party", "email", "website", "lastTweet"));
    }

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

                Representative selectedRep = reps.get(position);
                String message = "You clicked position " + position
                        + " Whose name is " + selectedRep.getName();
                Toast.makeText(CongressionalActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

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

            Representative r = reps.get(position);

            // Fill the view
            //ImageView imageView = (ImageView)itemView.findViewById(R.id.item_icon);
            //imageView.setImageResource(currentCar.getIconID());

            // Make:
            TextView makeText = (TextView) itemView.findViewById(R.id.item_txtMake);
            makeText.setText(r.getName());

            // Year:
            TextView yearText = (TextView) itemView.findViewById(R.id.item_txtYear);
            yearText.setText("" + r.getEmail());

            // Condition:
            TextView condionText = (TextView) itemView.findViewById(R.id.item_txtCondition);
            condionText.setText(r.getWebsite());

            return itemView;
        }
    }
}
