package in.wptrafficanalyzer.lojistic2;

/**
 * Created by Asus Computer on 22.4.2015.
 */
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Asus Computer on 9.4.2015.
 */
public class AdminAllProducts extends ListActivity{


    private ProgressDialog pDialog;

    public String namelist1;
    public String address1;
    public String destaddress1;
    public String phonenumber1;
    public String state1;
    public String taken1;
    public String stateKey;

    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;


    private static String url_all_products = "http://ec2-52-17-144-69.eu-west-1.compute.amazonaws.com/get_all_user_details.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_DESTNAME ="destname";
    private static final String TAG_ADDRESS= "address";
    private static final String TAG_DESTADRESS= "destadress";
    private static final String TAG_CARGONUMBER = "cargonumber";
    private static final String TAG_STATE= "state";
    private static final String TAG_DESTSTATE= "deststate";
    private static final String TAG_TYPE = "type";
    private static final String TAG_PHONENUMBER= "phonenumber";
    private static final String TAG_PRIOWORKER= "prioworker";
    private static final String TAG_TAKEN = "taken";
    private static final String TAG_CREATEDAT= "created_at";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_allproducts);


        productsList = new ArrayList<HashMap<String, String>>();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        stateKey = pref.getString("key_name6", null);


        new LoadAllProducts().execute();


        ListView lv = getListView();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();


                Intent in = new Intent(getApplicationContext(),
                        AdminEditProducts.class);

                in.putExtra(TAG_PID, pid);


                startActivityForResult(in, 100);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {

            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }


    class LoadAllProducts extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdminAllProducts.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("state",stateKey));
            params.add(new BasicNameValuePair("deststate",stateKey));
            Log.v(stateKey, "String");

            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);


            Log.d("All Products: ", json.toString());

            try {

                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    JSONArray products = json.getJSONArray(TAG_PRODUCTS);


                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);


                        String id = c.getString(TAG_PID);
                        String cargonumber = c.getString(TAG_CARGONUMBER);
                        String name = c.getString(TAG_NAME);
                        String destname = c.getString(TAG_DESTNAME);
                        String address = c.getString(TAG_ADDRESS);
                        String destadress = c.getString(TAG_DESTADRESS);
                        String state = c.getString(TAG_STATE);
                        String deststate = c.getString(TAG_DESTSTATE);
                        String phonenumber = c.getString(TAG_PHONENUMBER);
                        String prioworker = c.getString(TAG_PRIOWORKER);
                        String taken = c.getString(TAG_TAKEN);
                        String type = c.getString(TAG_TYPE);
                        String created_at = c.getString(TAG_CREATEDAT);

                        HashMap<String, String> map = new HashMap<String, String>();




                        map.put(TAG_PID,id);
                        map.put(TAG_CARGONUMBER,"Cargo Number : " +cargonumber);
                        map.put(TAG_NAME,"Sender : " + name);
                        map.put(TAG_DESTNAME,"Receiver: "+destname);
                        map.put(TAG_ADDRESS,"Address : " + address);
                        map.put(TAG_DESTADRESS,"Destination Address : " + destadress);
                        map.put(TAG_STATE,"The main state : " + state);
                        map.put(TAG_DESTSTATE,"The destination state : " + deststate);
                        map.put(TAG_PHONENUMBER,"Phone Number : " + phonenumber);
                        map.put(TAG_TYPE,"Type : " + type);
                        map.put(TAG_PRIOWORKER,"Priority(Default NULL) : " + prioworker);
                        map.put(TAG_CREATEDAT,"Created at : " + created_at);

                        productsList.add(map);
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    //Intent i = new Intent(getApplicationContext(),
                            //NewProductActivity.class);
                    // Closing all previous activities
                    //i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String file_url) {

            pDialog.dismiss();

            runOnUiThread(new Runnable() {
                public void run() {

                    ListAdapter adapter = new SimpleAdapter(
                            AdminAllProducts.this, productsList,
                            R.layout.list_item, new String[] {TAG_PID, TAG_CARGONUMBER,
                            TAG_NAME,TAG_DESTNAME,TAG_ADDRESS,TAG_DESTADRESS,TAG_STATE,TAG_DESTSTATE,TAG_PHONENUMBER,TAG_TYPE,TAG_PRIOWORKER,TAG_CREATEDAT},
                            new int[] {R.id.pid ,R.id.cargonumber, R.id.name ,R.id.destname,R.id.address,R.id.destadress,R.id.state,R.id.deststate,R.id.phonenumber,R.id.type,R.id.prioworker,R.id.createdat});

                    setListAdapter(adapter);
                }
            });

        }

    }
}
