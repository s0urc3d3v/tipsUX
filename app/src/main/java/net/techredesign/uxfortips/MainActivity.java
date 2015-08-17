package net.techredesign.uxfortips;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
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
import java.util.Locale;

public class MainActivity extends Activity {

    EditText e;
    TextView t;
    AlertDialog dialog;
    AlertDialog pathDialog;
    int percent = 20;
    String path = "";
    double tip;
    double subtotal;
    double total;

    private List<FloatingActionMenu> menus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e = (EditText) findViewById(R.id.editText);
        t = (TextView) findViewById(R.id.textView);

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void calc(View view) {
        subtotal = 0;
        tip = calculateTip();
        if (tip != -1) {
            try {
                subtotal = Double.valueOf(e.getText().toString());
            } catch (Exception conversionError) {
                conversionError.printStackTrace();
                Toast.makeText(getApplicationContext(), R.string.invalidSubtotalWarning, Toast.LENGTH_LONG).show();
            }
            setValues(tip, calculateTotal(subtotal, tip));
        } else {
            Log.w("input invalid", "NOT A NUMBER [INT]");

        }
    }


    public double calculateTip() {  //will return -1 if try failed
        try {
            final Double subtotal = Double.valueOf(e.getText().toString());
            return subtotal * (percent * 0.01);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), R.string.invalidStringWarning, Toast.LENGTH_SHORT).show();
            return -1;
        }

    }

    public double calculateTotal(double subtotal, double tip) {
        return subtotal + tip;
    }

    public void getPercentage() {
        final EditText percentage = new EditText(this);
        percentage.setHint("%");
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_attach_money)
                .setView(percentage)
                .setPositiveButton(R.string.percentagePositiveButtonPrompt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /**
                         * Get new percentage or fall back to a Toast prompt for the user
                         */
                        try {
                            percent = Integer.valueOf(percentage.getText().toString());
                        } catch (Exception invalidNumberException) {
                            invalidNumberException.printStackTrace();
                            Toast.makeText(getApplicationContext(), R.string.pleaseEnterValidPercent, Toast.LENGTH_SHORT).show();
                        }
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

    public void setValues(double tip, double total) {
        DecimalFormat decimalFormatter = new DecimalFormat("#.00");
        t.setText(getString(R.string.tipView) + decimalFormatter.format(tip) + "\n" + getString(R.string.totalView) + decimalFormatter.format(total));
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
                            Toast.makeText(getApplicationContext(), R.string.noSubtotalError, Toast.LENGTH_SHORT).show();;
                        }


                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismissPathAlertDialogView();
                    }
                });
        pathDialog = alertBuilder.create();
        pathDialog.show();
    }

    public void dismissPathAlertDialogView() {
        pathDialog.dismiss();
    }

    public void setTipSubtotalAndTotal() {
        try {
            subtotal = Double.valueOf(e.getText().toString());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        tip = subtotal * 0.2;
        total = subtotal + tip;


    }
    public void writeTipAndTotal(){ //There is a check in the alertDialog so additional checks are not necessary
        String filename = getString(R.string.filename);
        String newLine = "\n";
        FileOutputStream output;
        File f = new File("/storage/emulated/Documents/spendings.txt");
        if (f.exists()){
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


        }
        else {
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
    public boolean isExternalStorageWritable(){
        String stateOfExteneralWriteEnviorment = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(stateOfExteneralWriteEnviorment)){
            return true;
        }
        return false;
    }
    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            return true;
        }
        return false;
    }

    public File getSaveDirectory(String dirName){
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), dirName);
        if (!f.mkdirs()){
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

    public long getStorageAviable(){
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long blockSize = statFs.getBlockSizeLong();
        long availableBlocks = statFs.getAvailableBlocksLong();
        return availableBlocks * blockSize;
    }

    public void saveSpendingsToFile(View view){
        saveMySpendings();
    }
    public void changePercentage(View view){
        getPercentage();
    }
    public void showCreditsView(){
        startActivity(new Intent(this, licenses.class));
    }


}


