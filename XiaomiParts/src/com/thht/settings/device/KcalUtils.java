package com.thht.settings.device;

import android.preference.PreferenceManager;
import android.content.Context;
import java.util.List;

public class KcalUtils {
    
    public static final String FILE_LEVEL = "/sys/devices/platform/kcal_ctrl.0/kcal";
    public static final String DEFAULT_VALUE = "237 237 237";
    
    public static String[] getIndividualRGB(String rgb) {
        return rgb.split(" ", 3);
    }

    public static String combineIndividualRGB(String[] rgb) {
        return String.join(" ", rgb);
    }

    public static boolean isSupported() {
        return Utils.fileWritable(FILE_LEVEL);
    }

    public static void restoreRGBAfterBoot(Context context) {
        
      if(!isSupported()) { return; }

        String[] redStoredValue = getIndividualRGB(PreferenceManager.getDefaultSharedPreferences(context).getString(DeviceSettings.KEY_KCAL_RGB_RED, DEFAULT_VALUE)); 
        String[] greenStoredValue = getIndividualRGB(PreferenceManager.getDefaultSharedPreferences(context).getString(DeviceSettings.KEY_KCAL_RGB_GREEN, DEFAULT_VALUE)); 
        String[] blueStoredValue = getIndividualRGB(PreferenceManager.getDefaultSharedPreferences(context).getString(DeviceSettings.KEY_KCAL_RGB_BLUE, DEFAULT_VALUE));
      
        String[] combinedValue = {redStoredValue[0], greenStoredValue[1], blueStoredValue[2]};
        String finalValue = combineIndividualRGB(combinedValue);
        
        Utils.writeValue(FILE_LEVEL, finalValue);
    
    }
}
