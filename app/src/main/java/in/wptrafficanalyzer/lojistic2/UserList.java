package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 10.5.2015.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import in.wptrafficanalyzer.lojistic2.custom.CustomActivity;
import in.wptrafficanalyzer.lojistic2.utils.Const;
import in.wptrafficanalyzer.lojistic2.utils.Utils;


public class UserList extends CustomActivity
{


    private ArrayList<ParseUser> uList;


    public static ParseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        Parse.initialize(this, "VuAAncnuZtiEZWPFXgRdh8BmA1nbyye5Ezmg4Bl4",
                "qBFGVEg6qfyRs3kMGlsBf4xD9L6okVZNmp9mxkLZ");

        updateUserStatus(true);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        updateUserStatus(false);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadUserList();

    }


    private void updateUserStatus(boolean online)
    {
        user.put("online", online);
        user.saveEventually();
    }


    private void loadUserList()
    {
        final ProgressDialog dia = ProgressDialog.show(this, null,
                getString(R.string.alert_loading));
        ParseUser.getQuery().whereNotEqualTo("username", user.getUsername())
                .findInBackground(new FindCallback<ParseUser>() {

                    @Override
                    public void done(List<ParseUser> li, ParseException e)
                    {
                        dia.dismiss();
                        if (li != null)
                        {
                            if (li.size() == 0)
                                Toast.makeText(UserList.this,
                                        R.string.msg_no_user_found,
                                        Toast.LENGTH_SHORT).show();

                            uList = new ArrayList<ParseUser>(li);
                            ListView list = (ListView) findViewById(R.id.list);
                            list.setAdapter(new UserAdapter());
                            list.setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> arg0,
                                                        View arg1, int pos, long arg3)
                                {
                                    startActivity(new Intent(UserList.this,
                                            Chat.class).putExtra(
                                            Const.EXTRA_DATA, uList.get(pos)
                                                    .getUsername()));
                                }
                            });
                        }
                        else
                        {
                            Utils.showDialog(
                                    UserList.this,
                                    getString(R.string.err_users) + " "
                                            + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
    }

    private class UserAdapter extends BaseAdapter
    {


        @Override
        public int getCount()
        {
            return uList.size();
        }

        @Override
        public ParseUser getItem(int arg0)
        {
            return uList.get(arg0);
        }

        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }


        @Override
        public View getView(int pos, View v, ViewGroup arg2)
        {
            if (v == null)
                v = getLayoutInflater().inflate(R.layout.chat_item, null);

            ParseUser c = getItem(pos);
            TextView lbl = (TextView) v;
            lbl.setText(c.getUsername());
            lbl.setCompoundDrawablesWithIntrinsicBounds(
                    c.getBoolean("online") ? R.drawable.ic_online
                            : R.drawable.ic_offline, 0, R.drawable.arrow, 0);

            return v;
        }

    }
}
