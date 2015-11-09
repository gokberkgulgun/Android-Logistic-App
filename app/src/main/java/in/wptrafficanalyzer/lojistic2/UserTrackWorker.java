package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 30.4.2015.
 */
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserTrackWorker extends FragmentActivity{

    private ProgressDialog pDialog;
    public double lat1,lat2;

    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> productsList;


    private static String url_search = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/get_track_worker.php";


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TRACKER = "tracker";
    private static final String TAG_ID ="id";
    private static final String TAG_NAME = "name";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LNG = "lng";

    JSONArray tracker = null;

    public String searchKey ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usertrackworker);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        searchKey = pref.getString("dev245", null);
        Log.v(searchKey, "String");

        productsList = new ArrayList<HashMap<String, String>>();


        new LoadIdioms().execute();


    }

    class LoadIdioms extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserTrackWorker.this);
            pDialog.setMessage("Loading Products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("name", searchKey));

            JSONObject json = jParser.makeHttpRequest(url_search, "GET", params);


            Log.v(searchKey, "string to log");

            Log.d("Search idioms: ", json.toString());



            try {

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                    tracker = json.getJSONArray(TAG_TRACKER);
                    Log.v(TAG_NAME,"dsag");

                    for (int i = 0; i < tracker.length(); i++) {
                        JSONObject c = tracker.getJSONObject(i);



                        String lat = c.getString(TAG_LAT);
                        String lng = c.getString(TAG_LNG);
                        Log.v(TAG_LAT,"ben");
                        lat1 = Double.parseDouble(lat);
                        String ab = Double.toString(lat1);
                        Log.v(ab,"ben");
                        lat2 = Double.parseDouble(lng);



                    }
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }


        protected void onPostExecute(String file_url) {

            LatLng position = new LatLng(lat1, lat2);


            MarkerOptions options = new MarkerOptions();


            options.position(position);


            options.title("Worker Position");

            options.snippet("Latitude:"+lat1+",Longitude:"+lat2);


            SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);


            GoogleMap googleMap = fm.getMap();


            googleMap.addMarker(options);

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {


                }
            });

        }


}

}