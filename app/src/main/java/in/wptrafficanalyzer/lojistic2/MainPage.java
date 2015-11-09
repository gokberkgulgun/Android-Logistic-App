package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 30.4.2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;


public class MainPage extends Activity  {


    private int progressStatus = 0;
    public TextView textView;
    private Handler handler = new Handler();
    private GestureDetector gestureDetector;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();
        if(nf != null && nf.isConnected()==true )
        {
            Toast.makeText(this, "Network Available!", Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(this, "Network Not Available!", Toast.LENGTH_LONG).show();

        }

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Intent my3Intent = new Intent(MainPage.this,SignPage.class);
                startActivity(my3Intent);
                return true;
            }
        });


       /* Thread logoTimer = new Thread() {
            public void run(){
                try{
                    int logoTimer = 0;
                    while(logoTimer < 5000){
                        sleep(100);
                        logoTimer = logoTimer +100;
                        int a = 0;
                    };
                    startActivity(new Intent("com.tutorial.CLEARSCREEN"));
                }

                catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                finally{
                    finish();
                }
            }
        };


        logoTimer.start();*/
    }
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
            return true;
        return super.onTouchEvent(event);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {


        }

        return false;

    }
}