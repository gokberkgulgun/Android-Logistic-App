package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 5.5.2015.
 */
import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {


    static final String SERVER_URL = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/gcmregister.php";


    static final String SENDER_ID = "1072900572734";


    static final String TAG = "Logistic Notification Service";

    static final String DISPLAY_MESSAGE_ACTION =
            "in.wptrafficanalyzer.lojistic2.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";


    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}