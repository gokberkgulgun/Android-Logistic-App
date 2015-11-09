package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 21.4.2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class UserHomePage extends Activity {
    Button buttonPlace, buttonPrice, buttonTrack, buttonSearch, buttonQR,buttonUser3;
    Button buttonGCM;
    String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userhomepage);
        buttonPlace = (Button) findViewById(R.id.cargo);
        buttonPrice = (Button) findViewById(R.id.price);
        buttonTrack = (Button) findViewById(R.id.track);
        buttonUser3 = (Button) findViewById(R.id.buttonUser3);
        buttonSearch = (Button) findViewById(R.id.serach);
        buttonGCM  = (Button) findViewById(R.id.gcm);
        buttonQR = (Button) findViewById(R.id.qr);

        buttonPlace.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Intent myIntent = new Intent(UserHomePage.this, UserPlace.class);
                startActivity(myIntent);
            }
        });

        buttonPrice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Intent myIntent = new Intent(UserHomePage.this, UserPrice.class);
                startActivity(myIntent);
            }
        });
        buttonTrack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Intent myIntent = new Intent(UserHomePage.this, UserAddProduct.class);
                startActivity(myIntent);
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Intent myIntent = new Intent(UserHomePage.this, UserSearchProduct.class);
                startActivity(myIntent);
            }
        });

        buttonGCM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Intent myIntent = new Intent(UserHomePage.this, RegisterGCM.class);
                startActivity(myIntent);
            }
        });
        buttonQR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
            }
        });

        buttonUser3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub

                /// Create Intent for SignUpActivity  abd Start The Activity

                Intent myIntent = new Intent(UserHomePage.this, Login.class);
                startActivity(myIntent);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                a = intent.getStringExtra("SCAN_RESULT");
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("key_name5", a);
                editor.commit();
                Intent myIntent = new Intent(UserHomePage.this, UserSearchQuery.class);
                startActivity(myIntent);
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {


        }

        return false;

    }
}