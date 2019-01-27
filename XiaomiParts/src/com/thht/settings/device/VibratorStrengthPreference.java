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

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.database.ContentObserver;
import android.preference.SeekBarDialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Button;
import android.os.Bundle;
import android.util.Log;
import android.os.Vibrator;

import java.util.List;

import com.thht.settings.device.R;

public class VibratorStrengthPreference extends SeekBarDialogPreference implements
        SeekBar.OnSeekBarChangeListener {

    private SeekBar mSeekBar;
    private int mOldStrength;
    private int mMinValue;
    private int mMaxValue;
    private float offset;
    private Vibrator mVibrator;
    private TextView mValueText;
    private Button mPlusOneButton;
    private Button mMinusOneButton;
    private Button mRestoreDefaultButton;

    private static final String FILE_LEVEL = "/sys/devices/virtual/timed_output/vibrator/vtg_level";
    private static final long testVibrationPattern[] = {0,250};
    private static final int DEFAULT_VALUE = 2873;

    public VibratorStrengthPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        // from drivers/platform/msm/qpnp-haptic.c
        // #define QPNP_HAP_VMAX_MIN_MV		116
        // #define QPNP_HAP_VMAX_MAX_MV		3596
        mMinValue = 116;
        mMaxValue = 3596;
        offset = mMaxValue / 100f;

        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        setDialogLayoutResource(R.layout.preference_dialog_vibrator_strength);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mOldStrength = Integer.parseInt(getValue(getContext()));
        mSeekBar = (SeekBar) view.findViewById(R.id.vibratorSeekBar);
        mSeekBar.setMax(mMaxValue - mMinValue);
        mSeekBar.setProgress(mOldStrength - mMinValue);
        mValueText = (TextView) view.findViewById(R.id.current_value);
        mValueText.setText(Integer.toString(Math.round(mOldStrength / offset)) + "%");
        mSeekBar.setOnSeekBarChangeListener(this);
        mPlusOneButton = (Button) view.findViewById(R.id.plus_one);
        mPlusOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.plus_one) {
                    singleStepPlus();
                }
            }
        });
        mMinusOneButton = (Button) view.findViewById(R.id.minus_one);
        mMinusOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.minus_one) {
                    singleStepMinus();
                }
            }
        });
        mRestoreDefaultButton = (Button) view.findViewById(R.id.restore_default);
        mRestoreDefaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.restore_default) {
                    restoreDefault();
                }
            }
        });
    }

    public static boolean isSupported() {
        return Utils.fileWritable(FILE_LEVEL);
    }

    public static String getValue(Context context) {
        return Utils.getFileValue(FILE_LEVEL, String.valueOf(DEFAULT_VALUE));
    }

    public static void setValue(String newValue) {
        Utils.writeValue(FILE_LEVEL, newValue);
    }

    public static void restore(Context context) {
        if (!isSupported()) {
            return;
        }

        String storedValue = PreferenceManager.getDefaultSharedPreferences(context).getString(DeviceSettings.KEY_VIBSTRENGTH, String.valueOf(DEFAULT_VALUE)); 
        Utils.writeValue(FILE_LEVEL, storedValue);
    }

    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromTouch) {
        setValue(String.valueOf(progress + mMinValue));
        mValueText.setText(Integer.toString(Math.round((progress + mMinValue) / offset)) + "%");
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        // NA
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mVibrator.hasVibrator())
            mVibrator.vibrate(testVibrationPattern, -1);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            final int value = mSeekBar.getProgress() + mMinValue;
            setValue(String.valueOf(value));
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
            editor.putString(DeviceSettings.KEY_VIBSTRENGTH, String.valueOf(value));
            editor.commit();
        } else {
            restoreOldState();
        }
        mVibrator.cancel();
    }

    private void restoreOldState() {
        setValue(String.valueOf(mOldStrength));
    }

    private void singleStepPlus() {
        int currentValue = mSeekBar.getProgress();
        if (currentValue < mMaxValue) {
            mSeekBar.setProgress(currentValue + Math.round(offset));        
        }
    }

    private void singleStepMinus() {
        int currentValue = mSeekBar.getProgress();
        if (currentValue > mMinValue) {
            mSeekBar.setProgress(currentValue - Math.round(offset));
        }
    }

    private void restoreDefault() {
        mSeekBar.setProgress(DEFAULT_VALUE);
    }
}

