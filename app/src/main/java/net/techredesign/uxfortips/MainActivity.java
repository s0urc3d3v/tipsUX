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

    double percent = 0.2;
    String tipMod = "";
    double n = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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


        switch (item.getItemId()) {
            case (R.id.ammount)
                    : {

                final int in = 0;

                //use a
                final EditText input = new EditText(this);
                input.setHint("%__");
                input.setInputType(InputType.TYPE_CLASS_NUMBER);

                new AlertDialog.Builder(this)

                        .setTitle("Enter ammount")
                        .setView(input)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("use", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                tipMod = input.getText().toString();
                                percent = Double.valueOf(tipMod)
                                        * 0.01
                                ;

                                Toast.makeText(getApplicationContext(), "Using " + tipMod + "%", Toast.LENGTH_LONG).show();
                            }

                        }).show();
                break;
            }

            case(R.id.total):
                getTotal();
                new AlertDialog.Builder(this)
                        .setTitle(getTotal().toString())
                .show();

        }
        return super.onOptionsItemSelected(item);


    }




    public void calc(View view)
    {
        Log.w("Ran", "Func");
        getTip(percent);
    }

    public double getTip(double percent)
    {
        EditText e = (EditText) findViewById(R.id.editText);
        TextView t = (TextView) findViewById(R.id.textView);


        if (e != null )
        {
            Log.w("Not", "null");
            try {
                n = Double.valueOf(e.getText().toString())
                        ;
                DecimalFormat df = new DecimalFormat("#.##");
                df.format(n);
            }
            catch (Exception e5)
            {
                Toast.makeText(getApplicationContext(), "That is not a number", Toast.LENGTH_SHORT)
                        .show();
            }
            double tip = findTip(n, percent);

            t.setText("$" + String.valueOf(tip));
        }
        else
        {
            Toast.makeText(getApplicationContext(), "You need to enter an amount", Toast.LENGTH_LONG).show();

        }





    return  0;

    }

    public double findTip(double in, double percent)
    {

        DecimalFormat df = new DecimalFormat("#.##")
                ;
        return Double.parseDouble(df.format(in * percent)); //This line might break all the things

    }



    public void change(int i)
    {
        Toast.makeText(getApplicationContext(), "COMING S O O N ™", Toast.LENGTH_LONG).show();
    }

    public Double getTotal(){
        Double n = getTip(percent); EditText e = (EditText) findViewById(R.id.editText);
        return n = n * Double.parseDouble(e.getText().toString());




    }
}
