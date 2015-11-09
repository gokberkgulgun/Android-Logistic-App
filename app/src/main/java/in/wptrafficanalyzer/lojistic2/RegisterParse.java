package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 10.5.2015.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import in.wptrafficanalyzer.lojistic2.custom.CustomActivity;
import in.wptrafficanalyzer.lojistic2.utils.Utils;


public class RegisterParse extends CustomActivity
{

    private EditText user;

    private EditText pwd;

    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerparse);
        Parse.initialize(this, "VuAAncnuZtiEZWPFXgRdh8BmA1nbyye5Ezmg4Bl4",
                "qBFGVEg6qfyRs3kMGlsBf4xD9L6okVZNmp9mxkLZ");
        setTouchNClick(R.id.btnReg);

        user = (EditText) findViewById(R.id.user);
        pwd = (EditText) findViewById(R.id.pwd);
        email = (EditText) findViewById(R.id.email);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        String u = user.getText().toString();
        String p = pwd.getText().toString();
        String e = email.getText().toString();
        if (u.length() == 0 || p.length() == 0 || e.length() == 0)
        {
            Utils.showDialog(this, R.string.err_fields_empty);
            return;
        }
        final ProgressDialog dia = ProgressDialog.show(this, null,
                getString(R.string.alert_wait));

        final ParseUser pu = new ParseUser();
        pu.setEmail(e);
        pu.setPassword(p);
        pu.setUsername(u);
        pu.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(ParseException e)
            {
                dia.dismiss();
                if (e == null)
                {
                    UserList.user = pu;
                    startActivity(new Intent(RegisterParse.this, UserList.class));
                    setResult(RESULT_OK);
                    finish();
                }
                else
                {
                    Utils.showDialog(
                            RegisterParse.this,
                            getString(R.string.err_singup) + " "
                                    + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

    }
}
