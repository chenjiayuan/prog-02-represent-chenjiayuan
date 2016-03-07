package chenjiayuan.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        populateCongressBill(intent.getStringExtra("id"));


    }

    private void populateCongressBill(String id) {
        String site = "http://congress.api.sunlightfoundation.com/";
        String api = "&apikey=d1ff26dd5fb04253940eae90e286b0ea";
        //congress.api.sunlightfoundation.com/committees?member_ids=I000055&apikey=d1ff26dd5fb04253940eae90e286b0ea
        //congress.api.sunlightfoundation.com/bills?sponsor_id=I000055&apikey=d1ff26dd5fb04253940eae90e286b0ea
        String request1 = site + "committees?member_ids=" + id + api;
        String request2 = site + "bills?sponsor_id=" + id + api;
        final TextView comList = (TextView) findViewById(R.id.committes);
        final TextView billList = (TextView) findViewById(R.id.bills);

        //fetch committees data
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, request1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jComList) {
                try{
                    //update number of representatives received
                    int committeCount = jComList.getInt("count");
                    JSONArray results = jComList.optJSONArray("results");
                    StringBuilder sb = new StringBuilder();
                    for (int i=0; i<committeCount; i++) {
                        JSONObject committe = results.getJSONObject(i);
                        System.out.println(committe);
                        sb.append("►" + committe.getString("name")+"\n");
                    }
                    comList.setText(sb.toString());
                } catch (JSONException e) {e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

        //fetch bills data
        JsonObjectRequest jsObjRequest2 = new JsonObjectRequest(Request.Method.GET, request2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jBillList) {
                try{
                    //update number of representatives received
                    int BillCount = jBillList.getInt("count");
                    if (BillCount > 20) BillCount = 20; //TODO: deal with this 20 per page issue?
                    JSONArray results = jBillList.optJSONArray("results");
                    StringBuilder sb = new StringBuilder();
                    for (int i=0; i<BillCount; i++) {
                        if (results.getJSONObject(i) != null) {
                            JSONObject bill = results.getJSONObject(i);
                            System.out.println(bill);
                            sb.append("►" + bill.getString("official_title")+"\n");
                        }
                    }
                    billList.setText(sb.toString());
                } catch (JSONException e) {e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest2);
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
