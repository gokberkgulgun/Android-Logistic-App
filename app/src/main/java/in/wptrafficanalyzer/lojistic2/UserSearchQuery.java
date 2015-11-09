package in.wptrafficanalyzer.lojistic2;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserSearchQuery extends ListActivity {


    private ProgressDialog pDialog;
    public String cargoship;
    Button btnTracker,button1;
    TextView newone;
    public String al;
    public String ed;


    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> productsList;


    private static String url_search = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/get_user_track.php";


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "product";
    private static final String TAG_PID ="pid";
    private static final String TAG_CARGONUMBER = "cargonumber";
    private static final String TAG_NAME = "name";
    private static final String TAG_PHONENUMBER = "phonenumber";
    private static final String TAG_STATE = "state";
    private static final String TAG_DESTSTATE = "deststate";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_DESTADDRESS = "destadress";
    private static final String TAG_TYPE = "type";
    private static final String TAG_TRACK = "tracking";
    private static final String TAG_TAKEN = "taken";
    private static final String TAG_WORKDELIVER = "workerdel";
    private static final String TAG_PRIOWORKER = "prioworker";


    JSONArray products = null;

    public String searchKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_result);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        newone = (TextView) findViewById(R.id.cargowhere);
        btnTracker = (Button) findViewById(R.id.btnTracker);
        button1 = (Button) findViewById(R.id.btnMessage);


        Intent myIntent = getIntent();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        searchKey = pref.getString("key_name5", null);


        productsList = new ArrayList<HashMap<String, String>>();


        new LoadIdioms().execute();

        btnTracker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent my3Intent = new Intent(UserSearchQuery.this,UserTrackWorker.class);
                startActivity(my3Intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent my3Intent = new Intent(UserSearchQuery.this,LoginParse.class);
                startActivity(my3Intent);
            }
        });


    }


    class LoadIdioms extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserSearchQuery.this);
            pDialog.setMessage("Loading Products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("cargonumber", searchKey));

            JSONObject json = jParser.makeHttpRequest(url_search, "GET", params);
            Log.v(searchKey,"string to log");

            Log.d("Search idioms: ", json.toString());



            try {

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                    products = json.getJSONArray(TAG_PRODUCTS);
                    Log.v(TAG_NAME,"dsag");

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);


                        String pid = c.getString(TAG_PID);
                        String cargonumber = c.getString(TAG_CARGONUMBER);
                        String name = c.getString(TAG_NAME);
                        String phonenumber = c.getString(TAG_PHONENUMBER);
                        String address = c.getString(TAG_ADDRESS);
                        String destaddress = c.getString(TAG_DESTADDRESS);
                        String type = c.getString(TAG_TYPE);
                        String tracking = c.getString(TAG_TRACK);
                        String state = c.getString(TAG_STATE);
                        String deststate = c.getString(TAG_DESTSTATE);
                        String taken = c.getString(TAG_TAKEN);
                        String workdel = c.getString(TAG_WORKDELIVER);
                        String prioworker = c.getString(TAG_PRIOWORKER);
                        al = c.getString(TAG_PRIOWORKER);
                        ed = c.getString(TAG_CARGONUMBER);
                        Log.v(ed, "String");
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("dev245", ed);
                        editor.commit();

                        if(tracking.equals("1") && workdel.equals("1")){


                            cargoship = "Cargo is on way from  "+deststate+ ". Worker will deliver your product to destination address";
                            Log.v(cargoship, "String");


                        }
                        if(tracking.equals("0") && workdel.equals("1")){

                            cargoship = "Your cargo is at " + deststate + " branch office . It will soon hit the road.";
                            Log.v(cargoship, "String");



                        }

                        if(tracking.equals("1") && workdel.equals("0")){

                            cargoship = "Cargo is on way from  "+state+ ". Worker will deliver your product to " + deststate + " branch office";
                            Log.v(cargoship, "String");



                        }

                        if(tracking.equals("0") && workdel.equals("0")){

                            cargoship = "Your cargo is at " + state + "  branch office . It will soon hit  to " + deststate + " branch office";
                            Log.v(cargoship, "String");



                        }





                        HashMap<String, String> map = new HashMap<String, String>();


                        map.put(TAG_PID,"Pid : "+pid);
                        map.put(TAG_CARGONUMBER,"Cargo Track Number : " + cargonumber);
                        map.put(TAG_NAME,"Name : " + name);
                        map.put(TAG_PHONENUMBER,"Phone Number :   " + phonenumber);
                        map.put(TAG_ADDRESS,"Address :  " + address);
                        map.put(TAG_DESTADDRESS,"Destination address:   "+destaddress);
                        map.put(TAG_TYPE,"Type : " + type);
                        map.put(TAG_TRACK, tracking);



                        productsList.add(map);
                    }
                } else {

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

                    newone.setText(cargoship);
                    if(al.equals("1")) {
                        button1.setVisibility(View.VISIBLE);

                    }

                    ListAdapter adapter = new SimpleAdapter(
                            UserSearchQuery.this, productsList,
                            R.layout.list_view, new String[] {TAG_PID, TAG_NAME, TAG_PHONENUMBER, TAG_ADDRESS,TAG_DESTADDRESS,TAG_TYPE},
                            new int[] { R.id.pid,R.id.name, R.id.phonenumber, R.id.address,R.id.destadress,R.id.type});


                    setListAdapter(adapter);
                }
            });

        }

    }
}
