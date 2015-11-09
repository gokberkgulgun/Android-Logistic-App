package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 20.4.2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Register extends Activity implements OnClickListener{

    private EditText username1, password1,fullname1,phonenumber1;
    private Button  mRegister;
    private Spinner role1;

    private ProgressDialog pDialog;


    JSONParser jsonParser = new JSONParser();


    private static final String LOGIN_URL = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/register.php";


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        username1 = (EditText)findViewById(R.id.username);
        password1 = (EditText)findViewById(R.id.password);
        role1 = (Spinner)findViewById(R.id.role);
        fullname1=(EditText)findViewById(R.id.fullname);
        phonenumber1 =(EditText)findViewById(R.id.phone);

        mRegister = (Button)findViewById(R.id.register);
        mRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        new CreateUser().execute();

    }

    class CreateUser extends AsyncTask<String, String, String> {


        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            int success;
            String username = username1.getText().toString();
            String password = password1.getText().toString();
            String role = role1.getSelectedItem().toString();
            String fullname = fullname1.getText().toString();
            String phonenumber = phonenumber1.getText().toString();
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("role", role));
                params.add(new BasicNameValuePair("fullname", fullname));
                params.add(new BasicNameValuePair("phone", phonenumber));

                Log.d("request!", "starting");


                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);


                Log.d("Login attempt", json.toString());


                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }

}