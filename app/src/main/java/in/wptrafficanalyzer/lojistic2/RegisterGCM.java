package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 5.5.2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static in.wptrafficanalyzer.lojistic2.CommonUtilities.SENDER_ID;
import static in.wptrafficanalyzer.lojistic2.CommonUtilities.SERVER_URL;

public class RegisterGCM extends Activity {

    AlertDialogManager alert = new AlertDialogManager();


    ConnectionDetector cd;


    EditText txtName;
    EditText txtEmail;


    Button btnRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_gcm);

        cd = new ConnectionDetector(getApplicationContext());


        if (!cd.isConnectingToInternet()) {

            alert.showAlertDialog(RegisterGCM.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);

            return;
        }


        if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
                || SENDER_ID.length() == 0) {

            alert.showAlertDialog(RegisterGCM.this, "Configuration Error!",
                    "Please set your Server URL and GCM Sender ID", false);

            return;
        }

        txtName = (EditText) findViewById(R.id.txtName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        btnRegister = (Button) findViewById(R.id.btnRegister);


        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String name = txtName.getText().toString();
                String email = txtEmail.getText().toString();


                if(name.trim().length() > 0 && email.trim().length() > 0){

                    Intent i = new Intent(getApplicationContext(), ActivityGCM.class);


                    i.putExtra("name", name);
                    i.putExtra("email", email);
                    startActivity(i);
                    finish();
                }else{

                    alert.showAlertDialog(RegisterGCM.this, "Registration Error!", "Please enter your details", false);
                }
            }
        });
    }

}