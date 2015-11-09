package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 22.4.2015.
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
import android.widget.CheckBox;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminEditProducts extends Activity {

    EditText txtName;
    EditText txtPhonenumber;
    EditText txtAddress;
    EditText txtTracking;
    EditText txtCreatedAt;
    private CheckBox track1;
    Button btnSave;
    Button btnDelete;
    String tracking , under;
    String a ;
    String pid;


    private ProgressDialog pDialog;


    JSONParser jsonParser = new JSONParser();


    private static final String url_product_detials = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/get_user_details.php";


    private static final String url_update_product = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/update_user.php";


    private static final String url_delete_product = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/delete_user.php";


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PHONENUMBER = "phonenumber";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_TRACKING ="tracking";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_editproduct);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        track1 = (CheckBox) findViewById(R.id.chcTrack);


        Intent i = getIntent();


        pid = i.getStringExtra(TAG_PID);


        new GetProductDetails().execute();


        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                new SaveProductDetails().execute();
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                new DeleteProduct().execute();
            }
        });

    }


    class GetProductDetails extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdminEditProducts.this);
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


                            txtName = (EditText) findViewById(R.id.inputName);
                            txtPhonenumber = (EditText) findViewById(R.id.inputPhoneNumber);
                            txtAddress = (EditText) findViewById(R.id.inputAddress);



                            txtName.setText(product.getString(TAG_NAME));
                            txtPhonenumber.setText(product.getString(TAG_PHONENUMBER));
                            txtAddress.setText(product.getString(TAG_ADDRESS));


                            a = product.getString(TAG_TRACKING);

                            if(a.equals("1")){
                                track1.setChecked(true);
                            }
                        }else{

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
            pDialog = new ProgressDialog(AdminEditProducts.this);
            pDialog.setMessage("Saving product ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {


            String name = txtName.getText().toString();
            String phonenumber = txtPhonenumber.getText().toString();
            String address = txtAddress.getText().toString();
            String tracking;

            track1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (((CheckBox) v).isChecked()) {
                       under = "1";

                    }

                }
            });

            if(track1.isChecked()){
                tracking = "1";

            }
            else {
                tracking = "0";

            }

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PID, pid));
            params.add(new BasicNameValuePair(TAG_NAME, name));
            params.add(new BasicNameValuePair(TAG_PHONENUMBER, phonenumber));
            params.add(new BasicNameValuePair(TAG_ADDRESS, address));
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


    class DeleteProduct extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdminEditProducts.this);
            pDialog.setMessage("Deleting Product...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {


            int success;
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("pid", pid));


                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_product, "POST", params);


                Log.d("Delete Product", json.toString());


                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {

                    Intent i = getIntent();

                    setResult(100, i);
                    finish();
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