package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 20.4.2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class Login extends Activity implements OnClickListener{

    private EditText user, pass ;
    private Button mSubmit, mRegister;
    private Spinner rol;

    private ProgressDialog pDialog;


    JSONParser jsonParser = new JSONParser();


    private static final String LOGIN_URL = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/login.php";


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        user = (EditText)findViewById(R.id.username);
        pass = (EditText)findViewById(R.id.password);
        rol = (Spinner) findViewById(R.id.role);

        mSubmit = (Button)findViewById(R.id.login);
        mRegister = (Button)findViewById(R.id.register);

        mSubmit.setOnClickListener(this);
        mRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login:
                new AttemptLogin().execute();
                break;
            case R.id.register:
                Intent i = new Intent(this, Register.class);
                startActivity(i);
                break;

            default:
                break;
        }
    }

    class AttemptLogin extends AsyncTask<String, String, String> {


        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            int success;
            String username = user.getText().toString();
            String password = pass.getText().toString();
            String role = rol.getSelectedItem().toString();
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("role",role));
                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);


                Log.d("Login attempt", json.toString());


                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());
                    if(role.equals("Customer")){

                        Intent i = new Intent(Login.this, UserHomePage.class);

                        startActivity(i);
                        finish();

                    }
                    else if(role.equals("Employer")){

                        Intent i = new Intent (Login.this,AdminStatePage.class);
                        startActivity(i);
                        finish();

                    }
                    else if(role.equals("Worker")){

                        Intent i = new Intent (Login.this,WorkerStatePage.class);
                        startActivity(i);
                        finish();

                    }
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
                Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }

}