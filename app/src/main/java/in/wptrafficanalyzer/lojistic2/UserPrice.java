package in.wptrafficanalyzer.lojistic2;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Asus Computer on 22.4.2015.
 */
public class UserPrice extends Activity {

    private int a;
    private int b;
    String price11,price22;
    public double latitudeprice1;
    public Location locationA,locationB;
    public double latitudeprice2;
    public double longitudeprice1;
    public double longitudeprice2;
    public double totalprice19;
    public double mm;
    String total;
    public double distance;
    private Spinner price1,price2;

    private TextView totalOutput;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_price);

        price1 = (Spinner) findViewById(R.id.price1991);
        price2 = (Spinner) findViewById(R.id.price1992);
        EditText inputA = (EditText) findViewById(R.id.weight);

        totalOutput = (TextView) findViewById(R.id.totalprice);
        locationA = new Location("PointA") ;
        locationB = new Location("PointB");

        price1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                price11 = price1.getSelectedItem().toString();
                getLocationFromAddress(price11);
                locationB.setLatitude(latitudeprice1);
                String ac = Double.toString(latitudeprice1);
                Log.v(ac,"Location A latitude changed");
                locationB.setLongitude(longitudeprice1);
                calculateDistance();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        price2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                price22 = price2.getSelectedItem().toString();
                getLocationFromAddress1(price22);
                locationA.setLatitude(latitudeprice2);
                String ac = Double.toString(latitudeprice2);
                Log.v(ac,"Location B changed");
                locationA.setLongitude(longitudeprice2);
                calculateDistance();

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });








        inputA.addTextChangedListener(new TextChangedListener()
        {

            @Override
            public void numberEntered(int number)
            {

                a = number;
                mm = a * totalprice19;
                total = Double.toString(mm);
                totalOutput.setText("Price : " + total + " TL "); // need to do that otherwise int will
            }
        });

    }

    public void calculateDistance(){
        double distance = locationA.distanceTo(locationB)/1000;
        String distance1 = Double.toString(distance);
        Log.v(distance1,"new");
        if(distance<1) {
            totalprice19=4.41;
        }
        else if(distance>1 && distance<200 ){
            totalprice19 = 7.36;
        }

        else if(distance>201 && distance <600){
            totalprice19=8.21;
        }
        else if (distance >601 && distance <1000){
            totalprice19 = 9.12;

        }
        else {
            totalprice19=9.96;
        }

    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            latitudeprice1= location.getLatitude();
            String latitude12135 = Double.toString(latitudeprice1);
            Log.v(latitude12135, "address lat");
            longitudeprice1= location.getLongitude();
            String longitude13131 = Double.toString(longitudeprice1);
            Log.v(longitude13131, "address long");

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public LatLng getLocationFromAddress1(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            latitudeprice2= location.getLatitude();
            String latitude12135 = Double.toString(latitudeprice2);
            Log.v(latitude12135, "address lat");
            longitudeprice2= location.getLongitude();
            String longitude13131 = Double.toString(longitudeprice2);
            Log.v(longitude13131, "address long");

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private abstract class TextChangedListener implements TextWatcher
    {

        public abstract void numberEntered(int number);

        @Override
        public void afterTextChanged(Editable s)
        {
            String text = s.toString();
            try
            {
                int parsedInt = Integer.parseInt(text);
                numberEntered(parsedInt);
            } catch (NumberFormatException e)
            {
                Log.w(getPackageName(), "Could not parse '" + text + "' as a number", e);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
        }
    }

}
