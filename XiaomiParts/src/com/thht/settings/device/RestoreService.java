package com.thht.settings.device;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.preference.PreferenceManager;

public class RestoreService extends IntentService {

		private static final String TAG = "RestoreService";

		public RestoreService() {
      super(RestoreService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        
        VibratorStrengthPreference.restore(this);
        WhiteTorchBrightnessPreference.restore(this);
        YellowTorchBrightnessPreference.restore(this);

        Boolean shouldRestorePreset = intent.getExtras().getBoolean(DeviceSettings.KEY_KCAL_PRESETS, false);

        if (shouldRestorePreset) {
            String kcalPresetsValue = PreferenceManager.getDefaultSharedPreferences(this).getString(DeviceSettings.KEY_KCAL_PRESETS_LIST, "0");
            KcalPresets.setValue(kcalPresetsValue);
        } else {
            KcalRGBMinPreference.restore(this);
            KcalSatIntensityPreference.restore(this);
            KcalScreenHuePreference.restore(this);
            KcalScreenValuePreference.restore(this);
            KcalScreenContrPreference.restore(this);
            KcalUtils.restoreRGBAfterBoot(this);
        }
 
    }
}
