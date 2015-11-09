package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 22.4.2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserAddProduct extends Activity {


    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputPhoneNumber;
    EditText inputAddress;
    EditText destAddress;
    EditText state1;
    EditText deststate1;
    Integer cargonumber;
    int randomNumber;


    private static String url_create_product = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/create_user.php";


    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_addproduct);
        Random random = new Random();
        randomNumber = random.nextInt(60000 - 10000) + 10000;

        inputName = (EditText) findViewById(R.id.inputName);
        inputPhoneNumber = (EditText) findViewById(R.id.inputPhonenumber);
        inputAddress = (EditText) findViewById(R.id.inputAddress);
        destAddress = (EditText) findViewById(R.id.destAddress);
        state1 = (EditText) findViewById(R.id.state);
        deststate1 = (EditText) findViewById(R.id.deststate);


        Button btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);


        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                new CreateNewProduct().execute();
            }
        });
    }


    class CreateNewProduct extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserAddProduct.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            String name = inputName.getText().toString();
            String destname = "Meltem Kal";
            String phonenumber = inputPhoneNumber.getText().toString();
            String address = inputAddress.getText().toString();
            String destaddress = destAddress.getText().toString();
            String state = state1.getText().toString();
            String deststate = deststate1.getText().toString();
            String type1 = "Cargo";
            String cargonumber1 =Integer.toString(randomNumber);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("cargonumber", cargonumber1));
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("destname", destname));
            params.add(new BasicNameValuePair("phonenumber", phonenumber));
            params.add(new BasicNameValuePair("address", address));
            params.add(new BasicNameValuePair("destadress", destaddress));
            params.add(new BasicNameValuePair("state", state));
            params.add(new BasicNameValuePair("deststate",deststate));
            params.add(new BasicNameValuePair("type", type1));


            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);


            Log.d("Create Response", json.toString());


            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    Intent i = new Intent(UserAddProduct.this, UserHomePage.class);
                    startActivity(i);


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