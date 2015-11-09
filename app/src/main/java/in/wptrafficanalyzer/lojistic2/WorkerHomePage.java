package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 22.4.2015.
 */

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
public class WorkerHomePage extends Activity  {

    Button buttonProducts, buttonHowtoGo , buttonPut , buttonWorker3;
    String a;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_homepage);

        buttonProducts = (Button) findViewById(R.id.viewproducts);
        buttonHowtoGo = (Button) findViewById(R.id.howtogo);
        buttonWorker3 = (Button) findViewById(R.id.buttonWorker3);

        buttonPut = (Button) findViewById(R.id.buttonPut1);

        buttonProducts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Intent myIntent = new Intent(WorkerHomePage.this, WorkerAllProducts.class);
                startActivity(myIntent);
            }
        });

        buttonHowtoGo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub


                Intent myIntent = new Intent(WorkerHomePage.this, WorkerDrawRoute.class);
                startActivity(myIntent);
            }
        });


        buttonPut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);	//Barcode Scanner to scan for us
            }
        });

        buttonWorker3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub

                /// Create Intent for SignUpActivity  abd Start The Activity

                Intent myIntent = new Intent(WorkerHomePage.this, Login.class);
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
                editor.putString("key_name7", a);
                editor.commit();
                Intent myIntent = new Intent(WorkerHomePage.this, WorkerChange.class);
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


