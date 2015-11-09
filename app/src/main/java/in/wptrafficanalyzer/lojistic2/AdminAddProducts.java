package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 9.5.2015.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdminAddProducts extends Activity {


    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    String inputName;
    String inputDestName;
    String inputPhoneNumber;
    String inputAddress;
    String destAddress;
    String state1;
    String deststate1;
    String typeproduct;
    Integer cargonumber;
    int randomNumber;
    String addKey;


    private static String url_create_product = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/create_user.php";


    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_addproduct);
        Random random = new Random();
        randomNumber = random.nextInt(60000 - 10000) + 10000;


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        addKey = pref.getString("key_name18", null);


        String[] tokens = addKey.split("\n");

        for (int i = 0; i < tokens.length; i++) {
            System.out.println(" " + tokens[i]);

            if (tokens[i].startsWith("NAME:")) {
                inputName = tokens[i].substring(5);
            }else if (tokens[i].startsWith("DESTNAME:")) {
                inputDestName = tokens[i].substring(9);
            }
            else if (tokens[i].startsWith("PHONE:")) {
                inputPhoneNumber = tokens[i].substring(6);
            } else if (tokens[i].startsWith("ADDRESS:")) {
                inputAddress = tokens[i].substring(8);
            } else if (tokens[i].startsWith("DEST:")) {
                destAddress = tokens[i].substring(5);
            } else if (tokens[i].startsWith("STATE:")) {
                state1 = tokens[i].substring(6);
            }else if (tokens[i].startsWith("DESTSTATE:")) {
                deststate1 = tokens[i].substring(10);
            }
            else if (tokens[i].startsWith("TYPE:")) {
                typeproduct = tokens[i].substring(5);
            }




        }

        new CreateNewProduct().execute();
    }

    class CreateNewProduct extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdminAddProducts.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            String name = inputName;
            String destname = inputDestName;
            String phonenumber = inputPhoneNumber;
            String address = inputAddress;
            String destaddress = destAddress;
            String state = state1;
            String deststate = deststate1;
            String type1 = typeproduct;
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

                    Intent i = new Intent(AdminAddProducts.this, AdminHomePage.class);
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