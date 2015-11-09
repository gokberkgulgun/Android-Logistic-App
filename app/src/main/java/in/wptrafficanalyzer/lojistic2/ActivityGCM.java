package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 5.5.2015.
 */
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import static in.wptrafficanalyzer.lojistic2.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static in.wptrafficanalyzer.lojistic2.CommonUtilities.EXTRA_MESSAGE;
import static in.wptrafficanalyzer.lojistic2.CommonUtilities.SENDER_ID;

public class ActivityGCM extends Activity {

    TextView lblMessage;


    AsyncTask<Void, Void, Void> mRegisterTask;


    AlertDialogManager alert = new AlertDialogManager();


    ConnectionDetector cd;

    public static String name;
    public static String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcm);

        cd = new ConnectionDetector(getApplicationContext());


        if (!cd.isConnectingToInternet()) {

            alert.showAlertDialog(ActivityGCM.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);

            return;
        }


        Intent i = getIntent();

        name = i.getStringExtra("name");
        email = i.getStringExtra("email");


        GCMRegistrar.checkDevice(this);


        GCMRegistrar.checkManifest(this);

        lblMessage = (TextView) findViewById(R.id.lblMessage);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));


        final String regId = GCMRegistrar.getRegistrationId(this);


        if (regId.equals("")) {

            GCMRegistrar.register(this, SENDER_ID);
        } else {

            if (GCMRegistrar.isRegisteredOnServer(this)) {

                Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {

                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {

                        ServerUtilities.register(context, name, email, regId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }


    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);

            WakeLocker.acquire(getApplicationContext());




            lblMessage.append(newMessage + "\n");
            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();


            WakeLocker.release();
        }
    };

    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

}