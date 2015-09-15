package net.techredesign.uxfortips;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

public class settings extends Activity {
    EditText localTax;
    EditText defaultTipET;
    SharedPreferences sharedPreferences;
    private static final String PreferancesName = "net.techredesign.uxForTips";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.orangeTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        localTax = (EditText) findViewById(R.id.taxInput);
        defaultTipET = (EditText) findViewById(R.id.defaultTipValueWidgetET);
        sharedPreferences = getSharedPreferences(PreferancesName, Context.MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setTax(View view){
        int tax = 0;
        try {
            Log.w("TaxFeild: ", String.valueOf(localTax));
            tax = Integer.valueOf(localTax.getText().toString());
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), R.string.failedToParseTax, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("tax", tax);
        editor.apply();
    }
    public void setDefaultTip(View view){
        int tip = 0;
        try{
            Log.w("output", String.valueOf(defaultTipET.getText().toString()));
            tip = Integer.valueOf(defaultTipET.getText().toString());
        }
        catch (Exception couldNotParseInteger){
            Toast.makeText(getApplicationContext(), R.string.faildToParseInt, Toast.LENGTH_SHORT).show();
            couldNotParseInteger.printStackTrace();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("tip", tip);
        editor.apply();
    }
}
