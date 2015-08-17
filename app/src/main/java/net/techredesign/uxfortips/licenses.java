package net.techredesign.uxfortips;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

public class licenses extends Activity {
    TextView floatingActionButtonLicenseView;
    TextView apacheLicense2View;
    TextView apacheLicense2View2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licenses);
        floatingActionButtonLicenseView  = (TextView) findViewById(R.id.floatingActionButonLibraryLicenseTextView);
        apacheLicense2View = (TextView) findViewById(R.id.apacheLicense2View);
        apacheLicense2View.setMovementMethod(new ScrollingMovementMethod());
        apacheLicense2View2 = (TextView) findViewById(R.id.apacheLicense2View2);
        apacheLicense2View2.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_licenses, menu);
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
}
