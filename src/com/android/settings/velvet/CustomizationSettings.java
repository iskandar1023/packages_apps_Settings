package com.android.settings.velvet;

import android.os.Bundle;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;

import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;

import com.android.internal.logging.MetricsProto.MetricsEvent;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.preference.SystemSettingSwitchPreference;


public class CustomizationSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String MISC_CAT = "misc";

    private static final String FINGERPRINT_VIB = "fingerprint_success_vib";
    private static final String QS_COLUMNS = "qs_columns";

    private FingerprintManager mFingerprintManager;
    private ListPreference mQsColumns;
    private SystemSettingSwitchPreference mFingerprintVib;

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.APPLICATION;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.velvet_customization_settings);

        PreferenceCategory miscCategory = (PreferenceCategory) findPreference(MISC_CAT);

        mFingerprintManager = (FingerprintManager) getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
        mFingerprintVib = (SystemSettingSwitchPreference) findPreference(FINGERPRINT_VIB);
        if (!mFingerprintManager.isHardwareDetected()){
            miscCategory.removePreference(mFingerprintVib);
        }

        mQsColumns = (ListPreference) findPreference(QS_COLUMNS);
        int mQsColumnsValue = Settings.System.getInt(getActivity().getContentResolver(),
               Settings.System.QS_LAYOUT_COLUMNS, 3);
        mQsColumns.setValue(String.valueOf(mQsColumnsValue));
        mQsColumns.setSummary(mQsColumns.getEntry());
        mQsColumns.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mQsColumns) {
            int mQsColumnsValue = Integer.parseInt(((String) objValue).toString());
            mQsColumns.setSummary(mQsColumns.getEntries()[mQsColumnsValue - 3]);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.QS_LAYOUT_COLUMNS, mQsColumnsValue);
            return true;
        }
        return false;
    }
}
