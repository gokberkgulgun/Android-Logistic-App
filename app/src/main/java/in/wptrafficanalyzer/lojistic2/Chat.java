package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 10.5.2015.
 */

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.wptrafficanalyzer.lojistic2.custom.CustomActivity;
import in.wptrafficanalyzer.lojistic2.model.Conversation;
import in.wptrafficanalyzer.lojistic2.utils.Const;

/**
 * The Class Chat is the Activity class that holds main chat screen. It shows
 * all the conversation messages between two users and also allows the user to
 * send and receive messages.
 */
public class Chat extends CustomActivity
{

    private ArrayList<Conversation> convList;

    /** The chat adapter. */
    private ChatAdapter adp;

    private EditText txt;

    private String buddy;

    private Date lastMsgDate;

    private boolean isRunning;


    private static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        Parse.initialize(this, "VuAAncnuZtiEZWPFXgRdh8BmA1nbyye5Ezmg4Bl4",
                "qBFGVEg6qfyRs3kMGlsBf4xD9L6okVZNmp9mxkLZ");

        convList = new ArrayList<Conversation>();
        ListView list = (ListView) findViewById(R.id.list);
        adp = new ChatAdapter();
        list.setAdapter(adp);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);

        txt = (EditText) findViewById(R.id.txt);
        txt.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        setTouchNClick(R.id.btnSend);

        buddy = getIntent().getStringExtra(Const.EXTRA_DATA);


        handler = new Handler();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        isRunning = true;
        loadConversationList();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        isRunning = false;
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v.getId() == R.id.btnSend)
        {
            sendMessage();
        }

    }

    private void sendMessage()
    {
        if (txt.length() == 0)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txt.getWindowToken(), 0);

        String s = txt.getText().toString();
        final Conversation c = new Conversation(s, new Date(),
                UserList.user.getUsername());
        c.setStatus(Conversation.STATUS_SENDING);
        convList.add(c);
        adp.notifyDataSetChanged();
        txt.setText(null);

        ParseObject po = new ParseObject("Chat");
        po.put("sender", UserList.user.getUsername());
        po.put("receiver", buddy);
        // po.put("createdAt", "");
        po.put("message", s);
        po.saveEventually(new SaveCallback() {

            @Override
            public void done(ParseException e)
            {
                if (e == null)
                    c.setStatus(Conversation.STATUS_SENT);
                else
                    c.setStatus(Conversation.STATUS_FAILED);
                adp.notifyDataSetChanged();
            }
        });
    }

    private void loadConversationList()
    {
        ParseQuery<ParseObject> q = ParseQuery.getQuery("Chat");
        if (convList.size() == 0)
        {
            // load all messages...
            ArrayList<String> al = new ArrayList<String>();
            al.add(buddy);
            al.add(UserList.user.getUsername());
            q.whereContainedIn("sender", al);
            q.whereContainedIn("receiver", al);
        }
        else
        {
            // load only newly received message..
            if (lastMsgDate != null)
                q.whereGreaterThan("createdAt", lastMsgDate);
            q.whereEqualTo("sender", buddy);
            q.whereEqualTo("receiver", UserList.user.getUsername());
        }
        q.orderByDescending("createdAt");
        q.setLimit(30);
        q.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> li, ParseException e)
            {
                if (li != null && li.size() > 0)
                {
                    for (int i = li.size() - 1; i >= 0; i--)
                    {
                        ParseObject po = li.get(i);
                        Conversation c = new Conversation(po
                                .getString("message"), po.getCreatedAt(), po
                                .getString("sender"));
                        convList.add(c);
                        if (lastMsgDate == null
                                || lastMsgDate.before(c.getDate()))
                            lastMsgDate = c.getDate();
                        adp.notifyDataSetChanged();
                    }
                }
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run()
                    {
                        if (isRunning)
                            loadConversationList();
                    }
                }, 1000);
            }
        });

    }

    /**
     * The Class ChatAdapter is the adapter class for Chat ListView. This
     * adapter shows the Sent or Receieved Chat message in each list item.
     */
    private class ChatAdapter extends BaseAdapter
    {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount()
        {
            return convList.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Conversation getItem(int arg0)
        {
            return convList.get(arg0);
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }


        @Override
        public View getView(int pos, View v, ViewGroup arg2)
        {
            Conversation c = getItem(pos);
            if (c.isSent())
                v = getLayoutInflater().inflate(R.layout.chat_item_sent, null);
            else
                v = getLayoutInflater().inflate(R.layout.chat_item_rcv, null);

            TextView lbl = (TextView) v.findViewById(R.id.lbl1);
            lbl.setText(DateUtils.getRelativeDateTimeString(Chat.this, c
                            .getDate().getTime(), DateUtils.SECOND_IN_MILLIS,
                    DateUtils.DAY_IN_MILLIS, 0));

            lbl = (TextView) v.findViewById(R.id.lbl2);
            lbl.setText(c.getMsg());

            lbl = (TextView) v.findViewById(R.id.lbl3);
            if (c.isSent())
            {
                if (c.getStatus() == Conversation.STATUS_SENT)
                    lbl.setText("Delivered");
                else if (c.getStatus() == Conversation.STATUS_SENDING)
                    lbl.setText("Sending...");
                else
                    lbl.setText("Failed");
            }
            else
                lbl.setText("");

            return v;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
