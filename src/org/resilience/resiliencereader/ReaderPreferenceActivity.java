package org.resilience.resiliencereader;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

public class ReaderPreferenceActivity extends SherlockPreferenceActivity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
