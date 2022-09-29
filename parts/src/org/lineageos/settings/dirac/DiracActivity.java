/*
 * Copyright (C) 2018 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings.dirac;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.DialogInterface;

import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity;
import com.android.settingslib.collapsingtoolbar.R;

public class DiracActivity extends CollapsingToolbarBaseActivity {
    private boolean isOn;

    private static final String TAG_DIRAC = "dirac";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        isOn = sharedPreferences.getBoolean("saveT", false);
        if (!isOn) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setTitle("Warring")
		    .setMessage("This function may affect the audio effect in some scenarios. Are you sure you want to turn it on?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isOn = true;
                            SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("saveT", isOn);
                            editor.apply();
                        }
                    });
            alert.show();
        } else {
	    getFragmentManager()
		    .beginTransaction()
		    .replace(R.id.content_frame, new DiracSettingsFragment(), TAG_DIRAC)
		    .commit();
	}
    }
}
