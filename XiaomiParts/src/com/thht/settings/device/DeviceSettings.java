/*
* Copyright (C) 2016 The OmniROM Project
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.preference.ListPreference;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.util.Log;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;

public class DeviceSettings extends PreferenceActivity implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "thhtKCAL";

    public static final String KEY_VIBSTRENGTH = "vib_strength";
    public static final String KEY_YELLOW_TORCH_BRIGHTNESS = "yellow_torch_brightness";
    public static final String KEY_WHITE_TORCH_BRIGHTNESS = "white_torch_brightness";
    public static final String KEY_KCAL_RGB_RED = "kcal_rgb_red";
    public static final String KEY_KCAL_RGB_BLUE = "kcal_rgb_blue";
    public static final String KEY_KCAL_RGB_GREEN = "kcal_rgb_green";
    public static final String KEY_KCAL_RGB_MIN = "kcal_rgb_min";
    public static final String KEY_KCAL_SAT_INTENSITY = "kcal_sat_intensity";
    public static final String KEY_KCAL_SCR_CONTR = "key_kcal_scr_contr";
    public static final String KEY_KCAL_SCR_VAL = "key_kcal_scr_val";
    public static final String KEY_KCAL_SCR_HUE = "key_kcal_scr_hue";
    public static final String KEY_RESTORE_ON_BOOT = "restore_on_boot";
    public static final String KEY_KCAL_PRESETS = "kcal_presets";
    public static final String KEY_KCAL_PRESETS_LIST = "presets_list";
    public static final String KEY_SCREEN_COLOR = "key_screen_color";
    public static final String KEY_KCAL_EXTRAS = "key_kcal_extras";


    private VibratorStrengthPreference mVibratorStrength;
    private YellowTorchBrightnessPreference mYellowTorchBrightness;
    private WhiteTorchBrightnessPreference mWhiteTorchBrightness;
    private SwitchPreference restoreOnBootPreference;
    private SwitchPreference kcalPresetsPreference;
    private ListPreference kcalPresetsListPreference;
    private PreferenceCategory screenColorCategory;
    private PreferenceCategory kcalExtrasCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        addPreferencesFromResource(R.xml.main);

        getListView().setDivider(new ColorDrawable(Color.TRANSPARENT));
        getListView().setDividerHeight(0);

        mVibratorStrength = (VibratorStrengthPreference) findPreference(KEY_VIBSTRENGTH);
        if (mVibratorStrength != null) {
            mVibratorStrength.setEnabled(VibratorStrengthPreference.isSupported());
        }

        mYellowTorchBrightness = (YellowTorchBrightnessPreference) findPreference(KEY_YELLOW_TORCH_BRIGHTNESS);
        if (mYellowTorchBrightness != null) {
            mYellowTorchBrightness.setEnabled(YellowTorchBrightnessPreference.isSupported());
        }
        
        mWhiteTorchBrightness = (WhiteTorchBrightnessPreference) findPreference(KEY_WHITE_TORCH_BRIGHTNESS);
        if (mWhiteTorchBrightness != null) {
            mWhiteTorchBrightness.setEnabled(WhiteTorchBrightnessPreference.isSupported());
        }

        restoreOnBootPreference = (SwitchPreference) findPreference(KEY_RESTORE_ON_BOOT);
        Boolean shouldRestore = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(DeviceSettings.KEY_RESTORE_ON_BOOT, false); 
        restoreOnBootPreference.setChecked(shouldRestore);
        restoreOnBootPreference.setOnPreferenceChangeListener(this);

        kcalPresetsListPreference = (ListPreference) findPreference(KEY_KCAL_PRESETS_LIST);
        screenColorCategory = (PreferenceCategory) findPreference(KEY_SCREEN_COLOR);
        kcalExtrasCategory = (PreferenceCategory)findPreference(KEY_KCAL_EXTRAS);
        
        kcalPresetsPreference = (SwitchPreference) findPreference(KEY_KCAL_PRESETS);
        Boolean shouldRestorePreset = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(DeviceSettings.KEY_KCAL_PRESETS, false); 
        kcalPresetsPreference.setChecked(shouldRestorePreset);
        setKcalPresetsDependents(shouldRestorePreset);
        kcalPresetsPreference.setOnPreferenceChangeListener(this);

        String kcalPresetsValue = shouldRestore && shouldRestorePreset ? PreferenceManager.getDefaultSharedPreferences(this).getString(DeviceSettings.KEY_KCAL_PRESETS_LIST, "0") : "0" ;
        kcalPresetsListPreference.setValue(kcalPresetsValue);
        kcalPresetsPreference.setOnPreferenceChangeListener(this);

        kcalPresetsListPreference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        
        if (preference == restoreOnBootPreference ||
            preference == kcalPresetsPreference) {
            boolean value = (Boolean) newValue;
            if (preference == restoreOnBootPreference)
                editor.putBoolean(DeviceSettings.KEY_RESTORE_ON_BOOT, value);
            else if (preference == kcalPresetsPreference) {
                editor.putBoolean(DeviceSettings.KEY_KCAL_PRESETS, value);
                setKcalPresetsDependents(value);                
            }
            editor.commit();
        } 
        
        else if (preference == kcalPresetsListPreference) {
            String currValue = (String) newValue;
            editor.putString(DeviceSettings.KEY_KCAL_PRESETS_LIST, currValue);
            KcalPresets.setValue(currValue);
            editor.commit();
        }

        return true;
    }

    private void setKcalPresetsDependents(boolean value) {
        kcalPresetsListPreference.setEnabled(value);
        screenColorCategory.setEnabled(!value);
        kcalExtrasCategory.setEnabled(!value);
    }
}
