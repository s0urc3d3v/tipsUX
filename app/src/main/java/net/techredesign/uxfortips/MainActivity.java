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
import android.widget.EditText;
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

    SharedPreferences sharedPreferences;

    TextView totalAndPromptView;
    TextView UITipView;

    AlertDialog dialog;

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
        FloatingActionButton changePercentageFAB = (FloatingActionButton) findViewById(R.id.changePercentageFAB);
        FloatingActionButton calculateTipFAB = (FloatingActionButton) findViewById(R.id.calculateTipFAB);

        FloatingActionButton[] fabs = {changePercentageFAB, calculateTipFAB}; //Creates an array of the floating action buttons to make coloring theme easier

        int theme = sharedPreferences.getInt("theme", 0); //Get the users theme (appResources contains the theme values)
        /*
        for-each floating action button in fabs[] color it according to the users theme
         */
        for ( FloatingActionButton f : fabs) {
            f.setColorNormal(getResources().getColor(appResources.getPrimaryAccentColor(theme)));
            f.setColorPressed(getResources().getColor(appResources.getThemeAccentDark(theme)));
            menu1.setMenuButtonColorNormal(getResources().getColor(appResources.getPrimaryAccentColor(theme)));
            menu1.setMenuButtonColorPressed(getResources().getColor(appResources.getThemeAccentDark(theme)));
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
        double tip = getTip(userSubtotal);
        double tax = getTax(userSubtotal);
        double total = getTotal(userSubtotal, tip, tax);
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
                        dialog.dismiss();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    public void openSettings() {
        startActivity(new Intent(this, settings.class));
    }



    public void setTipSubtotalAndTotal() {
        try {
            subtotal = Double.valueOf(subtotalInputFeildET.getText().toString());
        } catch (Exception exception) {
            Toast.makeText(getApplicationContext(), getString(R.string.noSubtotalError), Toast.LENGTH_SHORT);
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

    //Start computational methods

    /**
     * Adds the wanted tip to the subtotal and returns the new subtotal
     *
     * Takes the stored tip from memory and applies it.  If it can not find the desired tip it returns -1 as
     * an error
     * @param subtotal Subtotal before tip
     * @return Subtotal with tip applied
     */
    public double getTip(double subtotal){
        int tipAsWholeNum = sharedPreferences.getInt("tip", 100);
        if (tipAsWholeNum == 100) return -1;
        else return subtotal * (tipAsWholeNum * 0.01);
    }

    /**
     * Adds total tip and tax to get
     * @param sTotal
     * @param tip
     * @param tax
     * @return Total of total tip and tax.
     */
    public double getTotal(double sTotal, double tip, double tax){
        return sTotal + tip + tax;
    }

    /**
     * Returns the total with local tax included.
     *
     * Takes the local tax as an whole number (int) from preferences storage.  Then it converts it to a decimal.
     * Lastly it applies the tax to the subtotal input.
     * a return value of -1 is an error
     * @param total subtotal without tax
     * @return subtotal with tax
     */
    public double getTax(double total){
        int localTaxAsWholeNum = sharedPreferences.getInt("tax", 0);
        if (localTaxAsWholeNum != 0){
            return (total * (localTaxAsWholeNum * 0.01));
        }
        else {
            getTaxFromUserWithDialog();
        }
        return -1;
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
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
        AlertDialog getTaxAsDialog = alertBuilder.create();
        getTaxAsDialog.show();
    }
    public void startCreditsActivity(){
        startActivity(new Intent(this, credits.class));
    }
}


