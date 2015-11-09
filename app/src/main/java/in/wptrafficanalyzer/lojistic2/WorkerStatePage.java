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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WorkerStatePage extends Activity implements OnClickListener{

    private EditText state, pass ;
    private Button mSubmit;


    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();


    private static final String LOGIN_URL = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/loginstate.php";


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginstate);


        state = (EditText)findViewById(R.id.state);
        pass = (EditText)findViewById(R.id.password);


        mSubmit = (Button)findViewById(R.id.login);



        mSubmit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login:
                new AttemptLogin().execute();
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
            pDialog = new ProgressDialog(WorkerStatePage.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            int success;
            String state1 = state.getText().toString();
            String password = pass.getText().toString();

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("state", state1));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);


                Log.d("Login attempt", json.toString());


                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());



                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("key_name8", state1);
                    editor.commit();
                    Intent i = new Intent (WorkerStatePage.this,WorkerHomePage.class);
                    startActivity(i);
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
                Toast.makeText(WorkerStatePage.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }

}