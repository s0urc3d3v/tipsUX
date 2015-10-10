package net.techredesign.uxfortips;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;Adde
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    EditText subtotalInputFeildET;
    String myPreferences = "net.techredesign.uxForTips";
    SharedPreferences sharedPreferences;
    TextView totalAndPromptView;
    TextView UITipView;
    AlertDialog dialog;
    AlertDialog pathDialog;
    int percent = 20;
    String path = "";
    private double tip = 0;
    private double subtotal = 0;
    private double total = 0;
    Spinner serviceQualitySpinner;
    colorSelector colorSelector = new colorSelector();
    private static int themeValue = 0;
    static long localTaxPercent = 0;
    static long localTax;

    private List<FloatingActionMenu> menus = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.packageName), MODE_PRIVATE);
        setTheme(appResources.getUserTheme(sharedPreferences.getInt("theme", 0)));
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        subtotalInputFeildET = (EditText) findViewById(R.id.subTotalInputFieldET);
        UITipView = (TextView) findViewById(R.id.tipViewUIwidgetTV);
        totalAndPromptView = (TextView) findViewById(R.id.PromptAndTotalTV);
        FloatingActionMenu menu1 = (FloatingActionMenu) findViewById(R.id.FABMenu);
        FloatingActionButton saveFAB = (FloatingActionButton) findViewById(R.id.saveFAB);
        FloatingActionButton changePercentageFAB = (FloatingActionButton) findViewById(R.id.changePercentageFAB);
        FloatingActionButton calculateTipFAB = (FloatingActionButton) findViewById(R.id.calculateTipFAB);
        FloatingActionButton[] fabs = {saveFAB, changePercentageFAB, calculateTipFAB};
        int theme = sharedPreferences.getInt("theme", 0);
        for ( FloatingActionButton f : fabs){
            if (theme == 1){ //TODO implement Unified Fab Theming
                f.setColorNormal(getResources().getColor(R.color.accentOrange));
                f.setColorPressed(getResources().getColor(R.color.accentDarkOrange));
                menu1.setMenuButtonColorNormal(getResources().getColor(R.color.accentOrange));
                menu1.setMenuButtonColorPressed(getResources().getColor(R.color.accentDarkOrange));
            }

            else if (theme == 3){
                f.setColorNormal(getResources().getColor(R.color.accentTeal));
                f.setColorPressed(getResources().getColor(R.color.accentDarkTeal));
                menu1.setMenuButtonColorNormal(getResources().getColor(R.color.accentTeal));
                menu1.setMenuButtonColorPressed(getResources().getColor(R.color.accentDarkTeal));
            }

            else if (theme == 5){
                f.setColorNormal(getResources().getColor(R.color.accentGreen));
                f.setColorPressed(getResources().getColor(R.color.accentDarkGreen));
                menu1.setMenuButtonColorNormal(getResources().getColor(R.color.accentGreen));
                menu1.setMenuButtonColorPressed(getResources().getColor(R.color.accentDarkGreen));
            }

            else if (theme == 7){
                f.setColorNormal(getResources().getColor(R.color.accentRed));
                f.setColorPressed(getResources().getColor(R.color.accentDarkRed));
                menu1.setMenuButtonColorNormal(getResources().getColor(R.color.accentRed));
                menu1.setMenuButtonColorPressed(getResources().getColor(R.color.accentDarkRed));
            }

            else {
                f.setColorNormal(getResources().getColor(R.color.accentOrange));
                f.setColorPressed(getResources().getColor(R.color.accentDarkOrange));
                menu1.setMenuButtonColorNormal(getResources().getColor(R.color.accentOrange));
                menu1.setMenuButtonColorPressed(getResources().getColor(R.color.accentDarkOrange));
            }
        }
        menus.add(menu1);
        menu1.setClosedOnTouchOutside(true);
        menu1.setIconAnimated(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.mainActivitySettings:
                openSettings();
                return true;
            case R.id.ActionBarThemeActivity:
                invokeThemeActivity();
                return true;
            case R.id.credits:
                startCreditsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void calc(View view) {
        double userSubtotal = getUserSubtotalInput();
        //Log.w("USER SUBTOTAL: ", String.valueOf(userSubtotal));
        double tip = getTip(userSubtotal);
        //Log.w("TIP: ", String.valueOf(tip));
        double tax = getTax(userSubtotal);
        double total = getTotal(userSubtotal, tip, tax);
        //Log.w("TOTAL: ", String.valueOf(total));
        setTotalOnUI(total);
        setTipOnUI(tip);
        //Log.w("Tax", String.valueOf(tax));
    }

    public void getPercentage() {
        final EditText percentageInput = new EditText(this);
        percentageInput.setHint("%");
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.serviceQualityPrompt))
                        //.setIcon(R.drawable.ic_attach_money)
                .setView(percentageInput)
                .setPositiveButton(R.string.percentagePositiveButtonPrompt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismissView();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    public void dismissView() { //workaround :(
        dialog.dismiss();
    }

    public void openSettings() {
        startActivity(new Intent(this, settings.class));
    }



    public void setTipSubtotalAndTotal() {
        try {
            subtotal = Double.valueOf(subtotalInputFeildET.getText().toString());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        tip = subtotal * 0.2;
        total = subtotal + tip;
    }

    public void changePercentage(View view) {
        getPercentage();
    }


    public void invokeThemeActivity() {
        startActivity(new Intent(this, colorSelector.class));
    }

    //computational methods
    public double getTip(double subtotal){ //BROKEN
        int tipAsWholeNum = sharedPreferences.getInt("tip", 20);
        return subtotal * (tipAsWholeNum * 0.01);
    }
    public double getTotal(double sTotal, double tip, double tax){
        Log.w("subtotal: ", String.valueOf(sTotal));
        Log.w("tip: ", String.valueOf(tip));
        Log.w("Tax: ", String.valueOf(tax));
        return sTotal + tip + tax;
    }
    public double getTax(double total){ //treat 0 as an exception
        int localTaxAsWholeNum = sharedPreferences.getInt("tax", 0);
        if (localTaxAsWholeNum != 0){
            return (total * (localTaxAsWholeNum * 0.01));
        }
        else {
            getTaxFromUserWithDialog();
        }
        return 0;
    }
    //end computational methods
    
    //parse methods
    public double getUserSubtotalInput(){
        double userSubtotal = 0;
        try {
            userSubtotal = Double.valueOf(subtotalInputFeildET.getText().toString());
        }
        catch (Exception cantParseFloat){
            Toast.makeText(getApplicationContext(), R.string.invalidSubtotalWarning, Toast.LENGTH_SHORT).show();
            cantParseFloat.printStackTrace();
        }
        if (userSubtotal != 0){
            return userSubtotal;
        }
        else{
            return 0;
        }
    }
    //end parse method
    
    //apply to GUI methods
    public void setTotalOnUI(double total){
        String totalAsString = "";
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        try{
            totalAsString = String.valueOf(decimalFormat.format(total));
        }
        catch (Exception stringConversionFailed){
            Toast.makeText(getApplicationContext(), R.string.genericError, Toast.LENGTH_SHORT).show();
            stringConversionFailed.printStackTrace();
        }
        String concatenatedStringWithTotalForUI = "Total: $" + totalAsString;
        totalAndPromptView.setText(concatenatedStringWithTotalForUI);

    }
    public void setTipOnUI(double tip){
        String tipAsString = "";
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        try{
            tipAsString = String.valueOf(decimalFormat.format(tip));
        }
        catch (Exception failedStringConversion){
            Toast.makeText(getApplicationContext(), R.string.genericError, Toast.LENGTH_SHORT).show();
            failedStringConversion.printStackTrace();
        }
        String concatenatedStringWithTipForUI = "Tip: $" + tipAsString;
        UITipView.setText(concatenatedStringWithTipForUI);
    }
    //end apply to GUI methods

    //get Tax is not already available
    public void getTaxFromUserWithDialog() {
        final EditText taxInputFieldForAlertDialog = new EditText(this);
        taxInputFieldForAlertDialog.setHint("%");
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                .setTitle("Please enter your local tax")
                .setIcon(R.drawable.ic_attach_money)
                .setView(taxInputFieldForAlertDialog)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userTaxInputViaAlert = taxInputFieldForAlertDialog.getText().toString();
                        int userTaxInputViaAlertAsDouble = 0;
                        try {
                            userTaxInputViaAlertAsDouble = Integer.valueOf(userTaxInputViaAlert);
                        }
                        catch (Exception failedToParseUserTaxViaAlertToFloat){
                            Toast.makeText(getApplicationContext(), R.string.invalidNumer, Toast.LENGTH_SHORT).show();
                            failedToParseUserTaxViaAlertToFloat.printStackTrace();
                            return;
                        }

                        if (userTaxInputViaAlertAsDouble != 0 && userTaxInputViaAlertAsDouble <= 100){
                            SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
                            preferencesEditor.putInt("tax", userTaxInputViaAlertAsDouble);
                            preferencesEditor.apply();
                            return;
                        }
                        else {
                            Toast.makeText(getApplicationContext(), R.string.invalidTax, Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismissView();
                    }
                });
        AlertDialog getTaxAsDialog = alertBuilder.create();
        getTaxAsDialog.show();
    }
    public void startCreditsActivity(){
        startActivity(new Intent(this, credits.class));
    }
}


