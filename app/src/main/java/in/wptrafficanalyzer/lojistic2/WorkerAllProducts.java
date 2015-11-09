package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 22.4.2015.
 */

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Asus Computer on 9.4.2015.
 */
public class WorkerAllProducts extends ListActivity implements LocationListener {


    protected LocationManager locationManager;
    String distance1;

    public double latitude12 = 0;
    public double latitude121 ;
    public double longitude13 = 0;
    public double longitude131;
    public String deststateKey;
    private ProgressDialog pDialog;
    Geocoder coder;


    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;




    private static String url_all_products = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/get_track_details.php";




    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "pid";
    private static final String TAG_CARGONUMBER = "cargonumber";
    private static final String TAG_NAME = "name";
    private static final String TAG_DESTNAME = "destname";
    private static final String TAG_PHONENUMBER = "phonenumber";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_STATE = "state";
    private static final String TAG_DESTADDRESS = "destadress";
    private static final String TAG_DESTSTATE= "deststate";
    private static final String TAG_TYPE= "type";
    private static final String TAG_DISTANCE = "distance";

    JSONArray products = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_allproducts);



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


            Toast.makeText(getApplicationContext(), "Enable Location", Toast.LENGTH_LONG).show();
        }

        coder = new Geocoder(this);

        productsList = new ArrayList<HashMap<String, String>>();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        deststateKey = pref.getString("key_name8", null);



        new LoadAllProducts().execute();


        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();


                Intent in = new Intent(getApplicationContext(),
                        WorkerEditProducts.class);

                in.putExtra(TAG_PID, pid);


                startActivityForResult(in, 100);
            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {

            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }


    class LoadAllProducts extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(WorkerAllProducts.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("deststate",deststateKey));

            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);


            Log.d("All Products: ", json.toString());

            try {

                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    products = json.getJSONArray(TAG_PRODUCTS);


                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);


                        String id = c.getString(TAG_PID);
                        String cargonumber = c.getString(TAG_CARGONUMBER);
                        String name = c.getString(TAG_NAME);
                        String destname = c.getString(TAG_DESTNAME);
                        String phonenumber = c.getString(TAG_PHONENUMBER);
                        String address = c.getString(TAG_ADDRESS);
                        String state1 = c.getString(TAG_STATE);
                        String destaddress = c.getString(TAG_DESTADDRESS);
                        String deststat1 = c.getString(TAG_DESTSTATE);
                        String type = c.getString(TAG_TYPE);
                        getLocationFromAddress(destaddress);
                        String latitude12135 = Double.toString(latitude12);
                        Log.v(latitude12135, "address lat");
                        String longitude13131 = Double.toString(longitude13);
                        Log.v(longitude13131, "address long");
                        String latitude1213 = Double.toString(latitude121);
                        Log.v(latitude1213, "current lat");
                        String longitude1313 = Double.toString(longitude131);
                        Log.v(longitude1313, "current long");
                        latitude121=41.108152;
                        longitude131=29.020767;
                        double Radius = 6371;
                        double latDiff = Math.toRadians(latitude121 - latitude12);
                        double longDiff =  Math.toRadians(longitude131 - longitude13);

                        double distance = Math.sin(latDiff/2) * Math.sin(latDiff/2) +
                                Math.cos(Math.toRadians(latitude12)) * Math.cos(Math.toRadians(latitude121)) *
                                        Math.sin(longDiff/2) * Math.sin(longDiff/2);
                        double ca = 2 * Math.asin(Math.sqrt(distance));
                        double ka = Radius * ca;
                        String distance1 = Double.toString(ka);
                        Log.v(distance1, "Message here");

                        HashMap<String, String> map = new HashMap<String, String>();


                        map.put(TAG_PID, id);
                        map.put(TAG_CARGONUMBER ,"Cargo Number : " +cargonumber);
                        map.put(TAG_NAME,"Sender : "+ name);
                        map.put(TAG_DESTNAME,"Receiver: " +destname);
                        map.put(TAG_PHONENUMBER,"Phone : " +phonenumber);
                        map.put(TAG_ADDRESS,"Address : " + address);
                        map.put(TAG_STATE,"State : " + state1);
                        map.put(TAG_DESTADDRESS,"Destination Address: " + destaddress);
                        map.put(TAG_DESTSTATE,"Destination State:" + deststat1);
                        map.put(TAG_TYPE,"Product Type: " + type);
                        map.put(TAG_DISTANCE ,"Current Location to Destination(km) : " + distance1);
                        // adding HashList to ArrayList
                        productsList.add(map);
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    //Intent i = new Intent(getApplicationContext(),
                    //NewProductActivity.class);
                    // Closing all previous activities
                    //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String file_url) {



            pDialog.dismiss();

            runOnUiThread(new Runnable() {
                public void run() {

                     ListAdapter adapter = new SimpleAdapter(
                            WorkerAllProducts.this, productsList,
                            R.layout.list_itemworker, new String[]{TAG_PID,TAG_CARGONUMBER,
                            TAG_NAME,TAG_DESTNAME, TAG_PHONENUMBER, TAG_ADDRESS,TAG_STATE,TAG_DESTADDRESS,TAG_DESTSTATE,TAG_TYPE,TAG_DISTANCE},
                            new int[]{R.id.pid,R.id.cargonumber2, R.id.name,R.id.destname, R.id.phonenumber, R.id.address,R.id.state2,R.id.destaddress,R.id.deststate2,R.id.type2,R.id.distance});

                    setListAdapter(adapter);
                }
            });

        }


    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
           latitude12= location.getLatitude();

            longitude13= location.getLongitude();


            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

        latitude121 = location.getLatitude();

        longitude131 =location.getLongitude();



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
