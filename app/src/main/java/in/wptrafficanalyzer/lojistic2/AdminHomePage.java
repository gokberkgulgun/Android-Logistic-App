package in.wptrafficanalyzer.lojistic2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by Asus Computer on 22.4.2015.
 */
public class AdminHomePage extends Activity{

    Button buttonProducts,buttonPrice,buttonEmployer3,detectWorker;
    String a;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_homepage);

        buttonProducts = (Button) findViewById(R.id.viewproducts);
        buttonPrice = (Button) findViewById(R.id.price);
        buttonEmployer3 = (Button) findViewById(R.id.buttonEmployer3);
        detectWorker = (Button) findViewById(R.id.worker);
        buttonProducts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Intent myIntent = new Intent(AdminHomePage.this, AdminAllProducts.class);
                startActivity(myIntent);
            }
        });

        buttonPrice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
            }
        });

        buttonEmployer3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub

                /// Create Intent for SignUpActivity  abd Start The Activity

                Intent myIntent = new Intent(AdminHomePage.this, Login.class);
                startActivity(myIntent);
            }
        });

        detectWorker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub

                /// Create Intent for SignUpActivity  abd Start The Activity

                Intent myIntent = new Intent(AdminHomePage.this, AdminDetectWorker.class);
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
                editor.putString("key_name18", a);
                editor.commit();
                Intent myIntent = new Intent(AdminHomePage.this, AdminAddProducts.class);
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
