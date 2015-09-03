package net.techredesign.uxfortips;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class colorSelector extends Activity {
    Switch darkThemeSwitch;
    public static int themeValue = 0;
    SharedPreferences.Editor prefEditor;
    private static final String preferancesName = "tipsUX_preferances";



    /**
     * 0 = orange light
     * 1 = orange dark
     * 2 = teal light
     * 3 = teal dark
     * 4 = green light
     * 5 = green dark
     * 6 = red light
     * 7 = red dark
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(getApplicationContext(), R.string.comingSoon, Toast.LENGTH_LONG).show();
        setTheme(R.style.orangeTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_selector);
        darkThemeSwitch = (Switch) findViewById(R.id.darkThemeToggle);
        prefEditor = getSharedPreferences(preferancesName, MODE_PRIVATE).edit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_selector, menu);
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
    public void orange(View view){
        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) themeValue = 1;
            }
        });
        if (themeValue != 1){
            themeValue = 2;
        }
        prefEditor.putInt("theme", themeValue);
    }
    public void teal(View view){
        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) themeValue = 2;
            }
        });

        if(themeValue != 2){
            themeValue = 3;
        }
        prefEditor.putInt("theme", themeValue);

    }
    public void green(View view){
        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) themeValue = 5;

            }
        });
        if (themeValue != 5){
            themeValue = 4;
        }
        prefEditor.putInt("theme", themeValue);

    }
    public void red(View view){
        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) themeValue = 7;
            }
        });
        if (themeValue != 7){
            themeValue = 6;
        }
        prefEditor.putInt("theme", themeValue);

    }

}
