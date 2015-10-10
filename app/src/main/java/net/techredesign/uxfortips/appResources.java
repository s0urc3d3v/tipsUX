package net.techredesign.uxfortips;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Matthewelbing on 10/10/15.
 */
public class appResources extends Activity{

    /**
     * STATIC
     * @param theme
     * @return themeValue
     * You can pass in a shared preferance referance
     * IN CALLING CLASS
     *setTheme(appResources.getUserTheme([YOU SHAREDPREFERANCES HERE));
     */
    public static int getUserTheme(int theme){
        if (theme == 1){
            return R.style.orangeTheme;
        }
        else if (theme == 2){
            return R.style.orangeThemeDark;
        }
        else if (theme == 3){
            return R.style.tealTheme;
        }
        else if (theme == 4){
            return R.style.tealThemeDark;
        }
        else if (theme == 5){
            return R.style.greenTheme;
        }
        else if (theme == 6){
            return R.style.greenThemeDark;
        }
        else if (theme == 7){
            return R.style.redTheme;
        }
        else if (theme == 8){
            return R.style.redThemeDark;
        }
        else{
            return R.style.orangeTheme;
        }
    }

}
