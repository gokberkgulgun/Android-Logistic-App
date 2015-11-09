package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 22.4.2015.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WorkerTrackWorker extends Activity implements LocationListener{
    protected LocationManager locationManager;
    protected Context context;
    private double latitude = 0;
    private double longitude = 0;
    public String lat1;
    public String lat2;
    TextView lat,lng;
    Button refresh;
    ProgressDialog dialog;
    private ProgressDialog pDialog;
    String name1;



    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TRACKER = "tracker";
    private static final String TAG_ID = "id";
    private static final String TAG_CARGONUMBER = "name";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LNG = "lng";


    JSONParser jsonParser = new JSONParser();
    private static final String url_update_product = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/update_track.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workertrack);
        lat = (TextView)findViewById(R.id.lat);
        lng = (TextView)findViewById(R.id.lng);



        refresh = (Button)findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                refresh();
            }
        });

        dialog = new ProgressDialog(WorkerTrackWorker.this);
        dialog.show();
        dialog.setMessage("Getting Coordinates");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 10000,
                    1, this);
        } else if (locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 10000,
                    1, this);
        }
        else {
            dialog.dismiss();

            Toast.makeText(getApplicationContext(), "Enable Location", Toast.LENGTH_LONG).show();
        }
    }
    private class SaveProductDetails extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(WorkerTrackWorker.this);
            pDialog.setMessage("Saving product ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {


            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            name1 = pref.getString("key_name1991", null);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_CARGONUMBER, name1));
            params.add(new BasicNameValuePair(TAG_LAT, lat1));
            params.add(new BasicNameValuePair(TAG_LNG, lat2));


            JSONObject json = jsonParser.makeHttpRequest(url_update_product,
                    "POST", params);


            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    Intent i = getIntent();

                    setResult(100, i);
                    finish();
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
        }
    }
    protected void refresh() {

        super.onResume();
        this.onCreate(null);

    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

        latitude = location.getLatitude();
        longitude =location.getLongitude();
        if (latitude != 0 && longitude != 0){

            lat.setText("Latitude is :" +location.getLatitude());
            lng.setText("Longitude is :" +location.getLongitude());

            dialog.dismiss();
        }
        lat1 = Double.toString(latitude);

        lat2 = Double.toString(longitude);

        new SaveProductDetails().execute();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}