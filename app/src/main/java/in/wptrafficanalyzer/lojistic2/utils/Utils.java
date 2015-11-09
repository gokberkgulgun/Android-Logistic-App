package in.wptrafficanalyzer.lojistic2.utils;

/**
 * Created by Asus Computer on 10.5.2015.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class Utils
{


    public static AlertDialog showDialog(Context ctx, String msg, String btn1,
                                         String btn2, DialogInterface.OnClickListener listener1,
                                         DialogInterface.OnClickListener listener2)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        // builder.setTitle(R.string.app_name);
        builder.setMessage(msg).setCancelable(false)
                .setPositiveButton(btn1, listener1);
        if (btn2 != null && listener2 != null)
            builder.setNegativeButton(btn2, listener2);

        AlertDialog alert = builder.create();
        alert.show();
        return alert;

    }


    public static AlertDialog showDialog(Context ctx, int msg, int btn1,
                                         int btn2, DialogInterface.OnClickListener listener1,
                                         DialogInterface.OnClickListener listener2)
    {

        return showDialog(ctx, ctx.getString(msg), ctx.getString(btn1),
                ctx.getString(btn2), listener1, listener2);

    }


    public static AlertDialog showDialog(Context ctx, String msg, String btn1,
                                         String btn2, DialogInterface.OnClickListener listener)
    {

        return showDialog(ctx, msg, btn1, btn2, listener,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {

                        dialog.dismiss();
                    }
                });

    }


    public static AlertDialog showDialog(Context ctx, int msg, int btn1,
                                         int btn2, DialogInterface.OnClickListener listener)
    {

        return showDialog(ctx, ctx.getString(msg), ctx.getString(btn1),
                ctx.getString(btn2), listener);

    }


    public static AlertDialog showDialog(Context ctx, String msg,
                                         DialogInterface.OnClickListener listener)
    {

        return showDialog(ctx, msg, ctx.getString(android.R.string.ok), null,
                listener, null);
    }


    public static AlertDialog showDialog(Context ctx, int msg,
                                         DialogInterface.OnClickListener listener)
    {

        return showDialog(ctx, ctx.getString(msg),
                ctx.getString(android.R.string.ok), null, listener, null);
    }


    public static AlertDialog showDialog(Context ctx, String msg)
    {

        return showDialog(ctx, msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {

                dialog.dismiss();
            }
        });

    }

    public static AlertDialog showDialog(Context ctx, int msg)
    {

        return showDialog(ctx, ctx.getString(msg));

    }


    public static void showDialog(Context ctx, int title, int msg,
                                  DialogInterface.OnClickListener listener)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(msg).setCancelable(false)
                .setPositiveButton(android.R.string.ok, listener);
        builder.setTitle(title);
        AlertDialog alert = builder.create();
        alert.show();
    }


    public static final void hideKeyboard(Activity ctx)
    {

        if (ctx.getCurrentFocus() != null)
        {
            InputMethodManager imm = (InputMethodManager) ctx
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ctx.getCurrentFocus().getWindowToken(),
                    0);
        }
    }


    public static final void hideKeyboard(Activity ctx, View v)
    {

        try
        {
            InputMethodManager imm = (InputMethodManager) ctx
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
