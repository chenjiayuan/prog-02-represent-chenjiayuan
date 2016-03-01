package chenjiayuan.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
