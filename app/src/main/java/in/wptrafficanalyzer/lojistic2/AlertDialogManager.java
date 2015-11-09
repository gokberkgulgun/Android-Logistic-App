package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 5.5.2015.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager {

    public void showAlertDialog(Context context, String title, String message,
                                Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();


        alertDialog.setTitle(title);


        alertDialog.setMessage(message);

        if(status != null)


        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });


        alertDialog.show();
    }
}