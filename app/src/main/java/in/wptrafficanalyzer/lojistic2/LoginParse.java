package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 10.5.2015.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import in.wptrafficanalyzer.lojistic2.custom.CustomActivity;
import in.wptrafficanalyzer.lojistic2.utils.Utils;

/**
 * The Class Login is an Activity class that shows the login screen to users.
 * The current implementation simply includes the options for Login and button
 * for Register. On login button click, it sends the Login details to Parse
 * server to verify user.
 */
public class LoginParse extends CustomActivity
{

    /** The username edittext. */
    private EditText user;

    /** The password edittext. */
    private EditText pwd;

    /* (non-Javadoc)
     * @see com.chatt.custom.CustomActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginparse);

        Parse.initialize(this, "VuAAncnuZtiEZWPFXgRdh8BmA1nbyye5Ezmg4Bl4",
                "qBFGVEg6qfyRs3kMGlsBf4xD9L6okVZNmp9mxkLZ");

        setTouchNClick(R.id.btnLogin);
        setTouchNClick(R.id.btnReg);

        user = (EditText) findViewById(R.id.user);
        pwd = (EditText) findViewById(R.id.pwd);
    }

    /* (non-Javadoc)
     * @see com.chatt.custom.CustomActivity#onClick(android.view.View)
     */
    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v.getId() == R.id.btnReg)
        {
            startActivityForResult(new Intent(this, RegisterParse.class), 10);
        }
        else
        {

            String u = user.getText().toString();
            String p = pwd.getText().toString();
            if (u.length() == 0 || p.length() == 0)
            {
                Utils.showDialog(this, R.string.err_fields_empty);
                return;
            }
            final ProgressDialog dia = ProgressDialog.show(this, null,
                    getString(R.string.alert_wait));
            ParseUser.logInInBackground(u, p, new LogInCallback() {

                @Override
                public void done(ParseUser pu, ParseException e)
                {
                    dia.dismiss();
                    if (pu != null)
                    {
                        UserList.user = pu;
                        startActivity(new Intent(LoginParse.this, UserList.class));
                        finish();
                    }
                    else
                    {
                        Utils.showDialog(
                                LoginParse.this,
                                getString(R.string.err_login) + " "
                                        + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK)
            finish();

    }
}
