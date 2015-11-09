package in.wptrafficanalyzer.lojistic2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Asus Computer on 30.4.2015.
 */
public class SignPage extends Activity {
    Button btnSignIn,btnSignUp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        btnSignIn=(Button)findViewById(R.id.buttonSignIN);
        btnSignUp=(Button)findViewById(R.id.buttonSignUP);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                Intent intentSignUP=new Intent(getApplicationContext(),Register.class);
                startActivity(intentSignUP);
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intentSignUP=new Intent(getApplicationContext(),Login.class);
                startActivity(intentSignUP);
            }
        });

    }
}
