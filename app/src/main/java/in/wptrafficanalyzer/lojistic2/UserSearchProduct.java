package in.wptrafficanalyzer.lojistic2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class UserSearchProduct extends Activity implements OnClickListener {
    private EditText txtkeyword;
    private Button btnsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_searchproduct);


        txtkeyword=(EditText)findViewById(R.id.txtkeyword);
        btnsearch=(Button)findViewById(R.id.btnsearch);
        btnsearch.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnsearch){
            Intent searchIntent = new Intent(this, UserSearch.class);

            searchIntent.putExtra("key",txtkeyword.getText().toString());

            startActivity(searchIntent);
        }

    }


}
