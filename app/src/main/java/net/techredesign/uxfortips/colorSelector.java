package net.techredesign.uxfortips;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class colorSelector extends Activity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor preferenceEditor;
    private RelativeLayout layout;
    private ActionBar actionBar;
    private ColorDrawable whiteColorDrawable;
    private ColorDrawable blackColorDrawable;
    private ColorDrawable orangeColorDrawable;
    private ColorDrawable redColorDrawable;
    private ColorDrawable greenColorDrawable;
    private ColorDrawable tealColorDrawable;
    private ColorDrawable orangeDarkColorDrawable;
    private ColorDrawable redDarkColorDrawable;
    private ColorDrawable tealDarkColorDrawable;
    private ColorDrawable greenDarkColorDrawable;
    private ColorDrawable actionBarPrimaryColorColorDrawable;
    private Window window;
    private FloatingActionMenu menu;
    private Animation animation;
    private FloatingActionButton FABS[] = new FloatingActionButton[4];
    private int theme = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.packageName), Context.MODE_PRIVATE);

        setTheme(appResources.getUserTheme(sharedPreferences.getInt("theme", 0)));
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_color_selector);

        menu = (FloatingActionMenu) findViewById(R.id.colorSelectorMenu);
        menu.setAnimated(true);

        layout = (RelativeLayout) findViewById(R.id.colorSelectorLayout);
        layout.setBackgroundColor(getResources().getColor(appResources.getPrimaryThemeColor(sharedPreferences.getInt("theme", 0))));

        whiteColorDrawable = new ColorDrawable(getResources().getColor(R.color.white));
        blackColorDrawable = new ColorDrawable(getResources().getColor(R.color.black));
        orangeColorDrawable = new ColorDrawable(getResources().getColor(R.color.primaryOrange));
        redColorDrawable = new ColorDrawable(getResources().getColor(R.color.primaryRed));
        tealColorDrawable = new ColorDrawable(getResources().getColor(R.color.primaryTeal));
        greenColorDrawable = new ColorDrawable(getResources().getColor(R.color.primaryGreen));
        orangeDarkColorDrawable = new ColorDrawable(getResources().getColor(R.color.primaryDarkOrange));
        redDarkColorDrawable = new ColorDrawable(getResources().getColor(R.color.primaryDarkRed));
        tealDarkColorDrawable = new ColorDrawable(getResources().getColor(R.color.primaryDarkTeal));
        greenDarkColorDrawable = new ColorDrawable(getResources().getColor(R.color.primaryDarkGreen));

        //TODO allow older API versions with conditional execution (if (androidVersion is >= 21){})
        window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.color_ripple);

        FABS[0] = ((FloatingActionButton) findViewById(R.id.orangeFAB));
        FABS[1] = ((FloatingActionButton) findViewById(R.id.redFAB));
        FABS[2] = ((FloatingActionButton) findViewById(R.id.greenFAB));
        FABS[3] = ((FloatingActionButton) findViewById(R.id.tealFAB));

        preferenceEditor = sharedPreferences.edit();
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
        switch (item.getItemId()){
            case (R.id.applyMenuItem):
                restartApp();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param view
     *Replace with a FAB menu
     * TODO Changed color of menu with background so they do not blend
     *
     * TODO Add material animation on background with color change
     *
     * TODO implement action bar theming with button press and onCreate();
     * */
    public void orange(View view){
        layout.setBackgroundColor(getResources().getColor(R.color.primaryOrange));
        if (!sharedPreferences.getBoolean("dark", false)){
            preferenceEditor.putInt("theme", 1);
            preferenceEditor.commit();
            applyThemePreview(1, false);
            theme = 1;
        }
        else {
            preferenceEditor.putInt("theme", 2);
            preferenceEditor.commit();
            applyThemePreview(2, true);
            theme = 2;

        }
    }
    public void teal(View view) {
        layout.setBackgroundColor(getResources().getColor(R.color.primaryTeal));
        if (!sharedPreferences.getBoolean("dark", false)) {
            preferenceEditor.putInt("theme", 3);
            preferenceEditor.commit();
            applyThemePreview(3, false);
            theme = 3;

        } else {
            preferenceEditor.putInt("theme", 4);
            preferenceEditor.commit();
            applyThemePreview(4, true);
            theme = 4;

        }
    }

    public void green(View view){
        layout.setBackgroundColor(getResources().getColor(R.color.primaryGreen));
        if (!sharedPreferences.getBoolean("dark", false)){
            preferenceEditor.putInt("theme", 5);
            preferenceEditor.commit();
            applyThemePreview(5, false);
            theme = 5;

        }
        else {
            preferenceEditor.putInt("theme", 6);
            preferenceEditor.commit();
            applyThemePreview(6, true);
            theme = 6;

        }
    }
    public void red(View view) {
        layout.setBackgroundColor(getResources().getColor(R.color.primaryRed));
        if (!sharedPreferences.getBoolean("dark", false)) {
            preferenceEditor.putInt("theme", 6);
            preferenceEditor.commit();
            applyThemePreview(7, false);
            theme = 7;

        } else {
            preferenceEditor.putInt("theme", 7);
            preferenceEditor.commit();
            actionBar = getActionBar();
            applyThemePreview(8, true);
            theme = 8;
        }
    }




    public void toggleDarkTheme(View view){
        if (!sharedPreferences.getBoolean("dark", false)){
            preferenceEditor.putBoolean("dark", true);
            preferenceEditor.commit();
        }
        else {
            preferenceEditor.putBoolean("dark", false);
            preferenceEditor.commit();
        }
    }

    public void restartApp(){
        Intent restartIntent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(restartIntent);

    }

    public void applyThemePreview(int theme, boolean isDark){
        //TODO make colors different for dark theme and therefore use "isDark" parameter
        //TODO check boolean when this method is run
        actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(appResources.getPrimaryThemeColor(theme))));
        }
        else{
            Log.w("BUG", "ACTIONBAR");
        }
        if (window != null) {
            window.setStatusBarColor(getResources().getColor(appResources.getThemePrimaryDarkColor(theme)));
        }
        else {
            Log.w("BUG", "WINDOW");
        }
        if (menu != null){
            menu.setMenuButtonColorNormal(getResources().getColor(appResources.getPrimaryAccentColor(theme)));
        }
        else {
            Log.w("THEME", "BUG");
        }
        }
    }


