package net.techredesign.uxfortips;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends Activity {

    EditText e;
    TextView t;
    AlertDialog dialog;
    int percent = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e = (EditText) findViewById(R.id.editText);
        t = (TextView) findViewById(R.id.textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.tipPercentageMenuItem:
                getPercentage();
                return true;
            case R.id.mainActivitySettings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void calc(View view) {
        double subtotal = 0;
        double tip = calculateTip();
        if (tip != -1) {
            try {
                subtotal = Double.valueOf(e.getText().toString());
            } catch (Exception conversionError) {
                conversionError.printStackTrace();
                Toast.makeText(getApplicationContext(), "invalid subtotal", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "That is not a number :)", Toast.LENGTH_SHORT).show();
                return -1;
            }

    }

    public double calculateTotal(double subtotal, double tip){return subtotal + tip;}

    public void getPercentage()
    {
        final EditText percentage = new EditText(this);
        percentage.setHint("%");
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setIcon(getDrawable(R.drawable.ic_attach_money_black_18dp))
                .setView(percentage)
                .setPositiveButton(R.string.percentagePositiveButtonPrompt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /**
                         * Get new percentage or fall back to a Toast prompt for the user
                         */
                        try {percent = Integer.valueOf(percentage.getText().toString());}
                        catch (Exception invalidNumberException) {
                            invalidNumberException.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Please enter a valid percentage", Toast.LENGTH_SHORT).show();
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

    public void dismissView(){ //workaround :(
        dialog.dismiss();
    }
    public void setValues(double tip, double total){
        DecimalFormat decimalFormatter = new DecimalFormat("#.00");
        t.setText("tip: $" + decimalFormatter.format(tip) +  "    total: $" + decimalFormatter.format(total));
    }

    public void openSettings(){
        startActivity(new Intent(this, settings.class));
    }

}
