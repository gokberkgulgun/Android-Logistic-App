package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 8.5.2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WorkerChange extends Activity {


    String tracking;
    String cargonumber;
    TextView cargostatus1;

    private ProgressDialog pDialog;


    JSONParser jsonParser = new JSONParser();



    private static final String url_update_product = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/update_cargod.php";




    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_CARGONUMBER= "cargonumber";

    private static final String TAG_TRACKING = "tracking";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_changestatus);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        cargostatus1 = (TextView) findViewById(R.id.cargostatus);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        cargonumber = pref.getString("key_name7", null);

        new SaveProductDetails().execute();

        cargostatus1.setText("Cargo ship status is updated!");

    }


    class SaveProductDetails extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(WorkerChange.this);
            pDialog.setMessage("Saving product ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {


            tracking = "1";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_CARGONUMBER, cargonumber));
            params.add(new BasicNameValuePair(TAG_TRACKING, tracking));


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