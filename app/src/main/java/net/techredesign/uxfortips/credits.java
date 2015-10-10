package net.techredesign.uxfortips;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class credits extends Activity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(getString(R.string.packageName), MODE_PRIVATE);
        setTheme(appResources.getUserTheme(sharedPreferences.getInt("theme", 0)));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        TextView creditFABLibrary = (TextView) findViewById(R.id.creditFAB);
        TextView creditGoogleForIcons = (TextView) findViewById(R.id.creditGoogleForIcons);
        TextView creditSnackbar = (TextView) findViewById(R.id.snackBarLibrary);
        TextView textViews[] = {creditFABLibrary, creditGoogleForIcons, creditSnackbar};
        for (TextView t : textViews){
            t.setMovementMethod(new ScrollingMovementMethod());
            t.setMovementMethod(new LinkMovementMethod());
        }
        creditFABLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFabLibraryLicense();
            }
        });
        creditGoogleForIcons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIconLicense();
            }
        });
        creditSnackbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackBarLibraryLicense();
            }
        });

    }
    public void showSnackBarLibraryLicense(){
        SpannableString msg = new SpannableString(getString(R.string.snackBarLicenseText));
        Linkify.addLinks(msg, Linkify.WEB_URLS);
        final TextView msgView = new TextView(this);
        msgView.setText(msg);
        msgView.setMovementMethod(new LinkMovementMethod());

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.snackBarLicenseTextTitle))
                .setView(msgView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void showFabLibraryLicense(){
        SpannableString msg = new SpannableString(getString(R.string.FABLicenseTextForDialog));
        Linkify.addLinks(msg, Linkify.WEB_URLS);
        final TextView textView = new TextView(this);
        textView.setText(msg);
        textView.setMovementMethod(new LinkMovementMethod());
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.FABdialogTitle))
                .setView(textView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showIconLicense(){
        SpannableString msg = new SpannableString(getString(R.string.GoogleIconLicenseText));
        Linkify.addLinks(msg, Linkify.WEB_URLS);
        final EditText editText = new EditText(this);
        editText.setText(msg);
        editText.setMovementMethod(new LinkMovementMethod());
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.GoogleIconLicenseTextTitle))
                .setView(editText);
        AlertDialog dialog = builder.create();
        dialog.show();
    }



}
