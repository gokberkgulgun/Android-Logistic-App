package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 2.5.2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WorkerEditProducts extends Activity {


    private CheckBox track1;
    private CheckBox track2;
    private CheckBox track3;
    Button btnSave;
    Button btnChat;
    Button btnTracker;

    String a,b,c,d;
    String pid;


    private ProgressDialog pDialog;


    JSONParser jsonParser = new JSONParser();



    private static final String url_update_product = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/update_deliver.php";

    private static final String url_product_detials = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/get_worker_details.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_CARGONUMBER = "cargonumber";
    private static final String TAG_TAKEN = "taken";
    private static final String TAG_WORKERDELIVER = "workerdel";
    private static final String TAG_PRIO = "prioworker";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_editproduct);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnChat = (Button) findViewById(R.id.btnChat);
        btnTracker = (Button) findViewById(R.id.btnTracker);
        track1 = (CheckBox) findViewById(R.id.chcTrack);
        track2 = (CheckBox) findViewById(R.id.chcTrack1);
        track3 = (CheckBox) findViewById(R.id.chcTrack2);


        Intent i = getIntent();


        pid = i.getStringExtra(TAG_PID);

        new GetProductDetails().execute();



        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                new SaveProductDetails().execute();
            }
        });
        btnChat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent my3Intent = new Intent(WorkerEditProducts.this,LoginParse.class);
                startActivity(my3Intent);
            }
        });

        btnTracker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent my3Intent = new Intent(WorkerEditProducts.this,WorkerTrackWorker.class);
                startActivity(my3Intent);
            }
        });




    }




    class GetProductDetails extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(WorkerEditProducts.this);
            pDialog.setMessage("Loading product details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... params) {


            runOnUiThread(new Runnable() {
                public void run() {

                    int success;
                    try {

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("pid", pid));


                        JSONObject json = jsonParser.makeHttpRequest(
                                url_product_detials, "GET", params);


                        Log.d("Single Product Details", json.toString());


                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {

                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT);


                            JSONObject product = productObj.getJSONObject(0);



                            a = product.getString(TAG_TAKEN);
                            b = product.getString(TAG_WORKERDELIVER);
                            c = product.getString(TAG_PRIO);
                            d = product.getString(TAG_CARGONUMBER);
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("key_name1991", d);
                            editor.commit();
                            if(a.equals("1")){
                                track1.setChecked(true);
                            }
                        }else{

                        }

                        if(b.equals("1")){
                            track2.setChecked(true);

                        }
                        if(c.equals("1")) {
                           track3.setChecked(true);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }


        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
        }
    }

    class SaveProductDetails extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(WorkerEditProducts.this);
            pDialog.setMessage("Saving product ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {


            String taken;
            String workerdel1;
            String prio;


            if (track1.isChecked()) {
                taken = "1";

            } else {
                taken = "0";

            }
            if(track2.isChecked()){
                workerdel1 = "1";
            }
            else {
                workerdel1 = "0";
            }

            if(track3.isChecked()){
                prio = "1";
            }
            else {
                prio = "0";
            }

            Log.i("Taken : ", taken);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PID, pid));
            params.add(new BasicNameValuePair(TAG_TAKEN, taken));
            params.add(new BasicNameValuePair(TAG_WORKERDELIVER,workerdel1));
            params.add(new BasicNameValuePair(TAG_PRIO,prio));

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

}