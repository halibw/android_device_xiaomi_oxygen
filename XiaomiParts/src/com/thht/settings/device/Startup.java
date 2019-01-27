/*
 * Copyright (C) 2013 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.thht.settings.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.os.Handler;
import java.lang.Runnable;
import android.util.Log;


public class Startup extends BroadcastReceiver {

  private static final String TAG = "XiaomiParts";
  
  @Override
    public void onReceive(final Context context, final Intent bootintent) {
     
        Boolean shouldRestore = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(DeviceSettings.KEY_RESTORE_ON_BOOT, false); 
        Boolean shouldRestorePreset = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(DeviceSettings.KEY_KCAL_PRESETS, false); 
        Log.e(TAG, Boolean.toString(shouldRestore));
        if(bootintent.getAction().equals("android.intent.action.BOOT_COMPLETED") && shouldRestore) {
            new Handler().postDelayed(new Runnable() {
            @Override
              public void run() {
                Intent in = new Intent(context, RestoreService.class);
                in.putExtra(DeviceSettings.KEY_KCAL_PRESETS, shouldRestorePreset);
                context.startService(in);
              }
           }, 0);
        } 
    }
}
