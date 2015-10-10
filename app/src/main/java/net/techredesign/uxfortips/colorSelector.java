package net.techredesign.uxfortips;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class colorSelector extends Activity {
    private static final String preferencesName = "net.techredesign.uxForTips";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor preferenceEditor;
    boolean switchState = false;

    /**
     * 1 = orange light
     * 2 = orange dark
     * 3 = teal light
     * 4 = teal dark
     * 5 = green light
     * 6 = green dark
     * 7 = red light
     * 8 = red dark
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        int theme = sharedPreferences.getInt("theme", 0);
        if (theme == 1){
            setTheme(R.style.orangeTheme);
        }
        else if (theme == 2){
            setTheme(R.style.orangeThemeDark);
        }
        else if (theme == 3){
            setTheme(R.style.tealTheme);

        }
        else if (theme == 4){
            setTheme(R.style.tealThemeDark);
        }
        else if (theme == 5){
            setTheme(R.style.greenTheme);
        }
        else if (theme == 6){
            setTheme(R.style.greenThemeDark);
        }
        else if (theme == 7){
            setTheme(R.style.redTheme);
        }
        else if (theme == 8){
            setTheme(R.style.redThemeDark);
        }
        else{
            setTheme(R.style.orangeTheme);
        }
        preferenceEditor = sharedPreferences.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_selector);

        // Get Fabs from XML
        FloatingActionButton FABS[]= {((FloatingActionButton) findViewById(R.id.orangeFAB)), ((FloatingActionButton) findViewById(R.id.redFAB)), ((FloatingActionButton) findViewById(R.id.greenFAB)), ((FloatingActionButton) findViewById(R.id.tealFAB))};
        int[] pararallelColorArray = {getResources().getColor(R.color.primaryOrange), getResources().getColor(R.color.primaryRed), getResources().getColor(R.color.primaryGreen), getResources().getColor(R.color.primaryTeal)};
        FloatingActionButton darkThemeToggle = (FloatingActionButton) findViewById(R.id.DarkThemeFAB);
        String[] colorNames = getResources().getStringArray(R.array.Colors).clone();
        int i = 0;

        for (FloatingActionButton f : FABS){
            f.setColorNormal(pararallelColorArray[i]);
            f.setLabelText(colorNames[i]);
            i++;
        }
        darkThemeToggle.setColorNormal(getResources().getColor(R.color.black));
        darkThemeToggle.setLabelText(getResources().getString(R.string.toggleDarkThem));
        FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.colorSelectorMenu);
        menu.setAnimated(true);



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

    /**
     *
     * @param view
     *Replace with a FAB menu
     * */
    public void orange(View view){
        if (sharedPreferences.getBoolean("dark", false)){
            preferenceEditor.putInt("theme", 2);
            preferenceEditor.apply();
        }
        else {
            preferenceEditor.putInt("theme", 1);
            preferenceEditor.apply();
        }
    }
    public void red(View view){
        if (sharedPreferences.getBoolean("dark", false)){
            preferenceEditor.putInt("theme", 6);
            preferenceEditor.apply();
        }
        else {
            preferenceEditor.putInt("theme", 7);
            preferenceEditor.apply();
        }
    }
    public void teal(View view){
        if (sharedPreferences.getBoolean("dark", false)){
            preferenceEditor.putInt("theme", 2);
            preferenceEditor.apply();
        }
        else {
            preferenceEditor.putInt("theme", 3);
            preferenceEditor.apply();
        }
    }
    public void green(View view){
        if (sharedPreferences.getBoolean("dark", false)){
            preferenceEditor.putInt("theme", 4);
            preferenceEditor.apply();
        }
        else {
            preferenceEditor.putInt("theme", 5);
            preferenceEditor.apply();
        }
    }
    public void toggleDarkTheme(View view){
        if (!sharedPreferences.getBoolean("dark", false)){
            preferenceEditor.putBoolean("dark", true);
        }
        else {
            preferenceEditor.putBoolean("dark", false);
        }
    }


}