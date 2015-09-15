package net.techredesign.uxfortips;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.github.clans.fab.FloatingActionMenu;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
        sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
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
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        subtotalInputFeildET = (EditText) findViewById(R.id.subTotalInputFieldET);
        UITipView = (TextView) findViewById(R.id.tipViewUIwidgetTV);
        totalAndPromptView = (TextView) findViewById(R.id.PromptAndTotalTV);
        FloatingActionMenu menu1 = (FloatingActionMenu) findViewById(R.id.FABMenu);
        menus.add(menu1);
        menu1.setClosedOnTouchOutside(true);
        menu1.setIconAnimated(false);

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
            case R.id.creditsItem:
                showCreditsView();
                return true;
            case R.id.ActionBarThemeActivity:
                invokeThemeActivity();
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

    public void saveMySpendings() {
        final EditText editText = new EditText(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                .setTitle(R.string.givePathPrompt)
                .setIcon(R.drawable.ic_attach_file_black_24dp)
                .setPositiveButton(R.string.saveToFile, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setTipSubtotalAndTotal();
                        if (tip != 0 && subtotal != 0 && total != 0) {
                            path = editText.getText().toString();
                            if (isExternalStorageWritable()) {
                                if (shouldWriteBasedOnStorage()) {
                                    writeTipAndTotal();
                                    Toast.makeText(getApplicationContext(), R.string.writeFinished, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                if (isExternalStorageReadable()) {
                                    Toast.makeText(getApplicationContext(), R.string.externalStorageNotWritableError, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.externalStorageNotAccessable, Toast.LENGTH_LONG).show();

                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.noSubtotalError, Toast.LENGTH_SHORT).show();
                            ;
                        }


                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismissView();
                    }
                });
        pathDialog = alertBuilder.create();
        pathDialog.show();
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

    public void writeTipAndTotal() { //There is a check in the alertDialog so additional checks are not necessary
        String filename = getString(R.string.filename);
        String newLine = "\n";
        FileOutputStream output;
        File f = new File("/storage/emulated/Documents/spendings.txt");
        if (f.exists()) {
            try {
                PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("/storage/emulated/0/Documents/spendings.txt", true)));
                writer.println(newLine);
                writer.println(getString(R.string.tipPromptForFile));
                writer.println(getString(R.string.tab));
                writer.println(String.valueOf(tip));
                writer.println(newLine);
                writer.println(getString(R.string.totalPromptForFile));
                writer.println(R.string.tab);
                writer.println(String.valueOf(subtotal));
                writer.println(newLine);
                writer.println(getString(R.string.lineFormatting));
                writer.close();

            } catch (IOException IOExceptionCreatingWriter) {
                IOExceptionCreatingWriter.printStackTrace();
            }


        } else {
            try {
                output = openFileOutput(filename, Context.MODE_WORLD_WRITEABLE);
                output.write(getString(R.string.spendingHeader).getBytes());
                output.write(getString(R.string.tipPromptForFile).getBytes());
                output.write(getString(R.string.tab).getBytes());
                output.write(String.valueOf(tip).getBytes());
                output.write(newLine.getBytes());
                output.write(getString(R.string.tab).getBytes());
                output.write(newLine.getBytes());
                output.write(getString(R.string.lineFormatting).getBytes());
                output.close();

            } catch (IOException IOException) {
                IOException.printStackTrace();
                Toast.makeText(getApplicationContext(), R.string.writeError, Toast.LENGTH_LONG).show();
            }
        }


    }

    public boolean isExternalStorageWritable() {
        String stateOfExteneralWriteEnviorment = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(stateOfExteneralWriteEnviorment)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getSaveDirectory(String dirName) {
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), dirName);
        if (!f.mkdirs()) {
            Toast.makeText(getApplicationContext(), R.string.errorGettingDirectory, Toast.LENGTH_SHORT).show();
        }
        return f;

    }

    public boolean shouldWriteBasedOnStorage() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long blockSize = statFs.getBlockSizeLong();
        long availableBlocks = statFs.getAvailableBlocksLong();
        long freeSpaceInBytes = availableBlocks * blockSize;
        return freeSpaceInBytes >= 20000000;
    }

    public long getStorageAviable() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long blockSize = statFs.getBlockSizeLong();
        long availableBlocks = statFs.getAvailableBlocksLong();
        return availableBlocks * blockSize;
    }

    public void saveSpendingsToFile(View view) {
        saveMySpendings();
    }

    public void changePercentage(View view) {
        getPercentage();
    }

    public void showCreditsView() {
        startActivity(new Intent(this, licenses.class));
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
        try{
            totalAsString = String.valueOf(total);
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
        try{
            tipAsString = String.valueOf(tip);
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
}


