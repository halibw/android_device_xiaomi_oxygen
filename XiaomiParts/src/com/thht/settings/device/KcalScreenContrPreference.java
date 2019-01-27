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

import java.util.List;

public class KcalScreenContrPreference extends SeekBarDialogPreference implements
        SeekBar.OnSeekBarChangeListener {

    private SeekBar mSeekBar;
    private int mOldStrength;
    private int mMinValue;
    private int mMaxValue;
    private TextView mValueText;
    private Button mPlusOneButton;
    private Button mMinusOneButton;
    private Button mRestoreDefaultButton;
    
    private static final int OFFSET = 128;
    private static final String FILE_LEVEL = "/sys/devices/platform/kcal_ctrl.0/kcal_cont";
    private static final String DEFAULT_VALUE = "127";

    public KcalScreenContrPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMinValue = 0;
        mMaxValue = 255;
        setDialogLayoutResource(R.layout.preference_dialog_kcal);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mOldStrength = Integer.parseInt(getValue(getContext()));
        mSeekBar = (SeekBar) view.findViewById(R.id.kcalSeekBar);
        mSeekBar.setMax(mMaxValue - mMinValue);
        mSeekBar.setProgress(mOldStrength - mMinValue);
        mValueText = (TextView) view.findViewById(R.id.current_value);
        mValueText.setText(String.valueOf(mOldStrength));
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
        int value = Integer.parseInt(Utils.getFileValue(FILE_LEVEL, DEFAULT_VALUE));
        return String.valueOf(translate(value, true));
    }

    public static void setValue(String newValue) {
        String value = String.valueOf(translate(Integer.parseInt(newValue), false));
        Utils.writeValue(FILE_LEVEL, value);
    }

    public static void restore(Context context) {
        if (!isSupported()) {
            return;
        }

        String storedValue = PreferenceManager.getDefaultSharedPreferences(context).getString(DeviceSettings.KEY_KCAL_SCR_CONTR, DEFAULT_VALUE);
        String value = String.valueOf(translate(Integer.parseInt(storedValue), false));
        Utils.writeValue(FILE_LEVEL, value);
    }

    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromTouch) {
        String value = String.valueOf(progress + mMinValue);
        setValue(value);
        mValueText.setText(value);
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        // NA
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        // NA
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            final int value = mSeekBar.getProgress() + mMinValue;
            setValue(String.valueOf(value));
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
            editor.putString(DeviceSettings.KEY_KCAL_SCR_CONTR, String.valueOf(value));
            editor.commit();
        } else {
            restoreOldState();
        }
    }

    private void restoreOldState() {
        setValue(String.valueOf(mOldStrength));
    }

    private static int translate(int value, boolean read) {
        if (!read)
            return value + OFFSET;
        else
            return value - OFFSET;
    }

    private void singleStepPlus() {
        int currentValue = mSeekBar.getProgress();
        if (currentValue < mMaxValue) {
            mSeekBar.setProgress(currentValue + 1);        
        }
    }

    private void singleStepMinus() {
        int currentValue = mSeekBar.getProgress();
        if (currentValue > mMinValue) {
            mSeekBar.setProgress(currentValue - 1);
        }
    }

    private void restoreDefault() {
        int defaultValue = Integer.parseInt(DEFAULT_VALUE);
        mSeekBar.setProgress(defaultValue);
    }
}

