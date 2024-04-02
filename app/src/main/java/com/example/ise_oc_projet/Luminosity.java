package com.example.ise_oc_projet;

import android.content.ContentResolver;
import android.provider.Settings;
import android.widget.CheckBox;
import android.widget.SeekBar;

public class Luminosity {

    //*** VARIABLE ***//
    // Management of luminosity
    int brightnessLevel;

    public Luminosity(){

        brightnessLevel = getScreenBrightness();

    }

    private int getScreenBrightness() {
        int brightness = 0;
        /*ContentResolver contentResolver = getContentResolver();
        try {
            // Récupérer le niveau de luminosité actuel depuis les paramètres système
            brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return brightness;*/
        return 0;
    }


}
