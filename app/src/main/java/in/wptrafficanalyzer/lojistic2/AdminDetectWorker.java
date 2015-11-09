package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 2.5.2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminDetectWorker extends Activity {


    Button btnSave,btnGet;
    EditText fullName1, fullName2;
    EditText txtName;
    EditText txtState;
    EditText state1;


    String fullName12;


    private ProgressDialog pDialog;


    JSONParser jsonParser = new JSONParser();

    private static final String url_product_detials = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/get_detect_details.php";

    private static final String url_update_user = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/update_detect.php";


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_STATE = "state";
    private static final String TAG_USERNAME = "username";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_detectworker);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnGet = (Button) findViewById(R.id.submit12);
        btnSave = (Button) findViewById(R.id.submit1);
        txtState = (EditText) findViewById(R.id.state1);
        txtName = (EditText) findViewById(R.id.fullname2);

        fullName1 = (EditText) findViewById(R.id.fullname1);



        btnGet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                fullName12 = fullName1.getText().toString();
                new GetProductDetails().execute();
            }
        });





        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                new SaveProductDetails().execute();
            }
        });


    }


    class GetProductDetails extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdminDetectWorker.this);
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
                        params.add(new BasicNameValuePair("username", fullName12));
                        Log.v(fullName12,"ewq");

                        JSONObject json = jsonParser.makeHttpRequest(
                                url_product_detials, "GET", params);


                        Log.d("Single Product Details", json.toString());


                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {

                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT);


                            JSONObject product = productObj.getJSONObject(0);






                            txtName.setText(product.getString(TAG_USERNAME));
                            txtState.setText(product.getString(TAG_STATE));


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
            pDialog = new ProgressDialog(AdminDetectWorker.this);
            pDialog.setMessage("Saving product ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {


            String name = txtName.getText().toString();
            String state = txtState.getText().toString();


            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair(TAG_USERNAME, name));
            params.add(new BasicNameValuePair(TAG_STATE, state));


            JSONObject json = jsonParser.makeHttpRequest(url_update_user,
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
