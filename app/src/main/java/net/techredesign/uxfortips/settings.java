package net.techredesign.uxfortips;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

public class settings extends Activity {
    EditText localTax;
    EditText defaultTipET;
    SharedPreferences sharedPreferences;
    private static final String PreferancesName = "net.techredesign.uxForTips";
    int tempValue = 0;
    static int theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(PreferancesName, MODE_PRIVATE);
        setTheme(appResources.getUserTheme(sharedPreferences.getInt("theme", 0)));
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
            tax = Integer.valueOf(localTax.getText().toString());
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), R.string.failedToParseTax, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        tempValue = 0;
        if (sharedPreferences.getInt("tax", 0)!= 0){
            tempValue = sharedPreferences.getInt("tax", 0);
        }
        editor.putInt("tax", tax);
        editor.apply();
        SnackbarManager.show(
                Snackbar.with(getApplicationContext())
                        .text(getString(R.string.taxSet))
                        .actionLabel(getString(R.string.undoTax))
                        .colorResource(R.color.accentOrange) //TODO implement theming
                        .actionColor(getResources().getColor(R.color.primaryOrange)) //TODO implement theming
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                editor.putInt("tax", tempValue);
                                editor.apply();

                            }
                        }), this
        );
    }
    public void setDefaultTip(View view) {
        int tip = 0;
        try {
            Log.w("output", String.valueOf(defaultTipET.getText().toString()));
            tip = Integer.valueOf(defaultTipET.getText().toString());
        } catch (Exception couldNotParseInteger) {
            Toast.makeText(getApplicationContext(), R.string.faildToParseInt, Toast.LENGTH_SHORT).show();
            couldNotParseInteger.printStackTrace();
        }
        tempValue = 0;
        if (sharedPreferences.getInt("tip", 0) != 0) {
            tempValue = sharedPreferences.getInt("tip", 0);
        }
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("tip", tip);
        editor.apply();
        SnackbarManager.show(
                Snackbar.with(getApplicationContext())
                        .text(getString(R.string.taxSet))
                        .actionLabel(getString(R.string.undoTax))
                        .colorResource(R.color.accentOrange) //TODO implement theming
                        .actionColor(getResources().getColor(R.color.accentOrange)) //TODO implement theming
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                editor.putInt("tip", tempValue);
                                editor.apply();

                            }
                        }), this
        );
    }


    }

