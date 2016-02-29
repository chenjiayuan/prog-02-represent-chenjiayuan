package chenjiayuan.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //update the values for the person
        Intent intent = getIntent();
        TextView nameText = (TextView) findViewById(R.id.name);
        nameText.setText(intent.getStringExtra("name"));
        TextView roleText = (TextView) findViewById(R.id.role);
        roleText.setText(intent.getStringExtra("role"));
        TextView partyText = (TextView) findViewById(R.id.party);
        partyText.setText(intent.getStringExtra("party"));
        TextView termText = (TextView) findViewById(R.id.term);
        termText.setText("Term End Date: " + intent.getStringExtra("term"));
        ImageView profilePic = (ImageView) findViewById(R.id.profile_pic);
        profilePic.setImageResource(Integer.parseInt(intent.getStringExtra("picID")));
    }
}
