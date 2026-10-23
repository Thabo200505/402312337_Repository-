package com.example.a402312337_mncedane_mobileappdev700_assignmentdocx;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private SettingsManager settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        settings = new SettingsManager(this);

        SwitchMaterial switchExpiry = findViewById(R.id.switchExpiry);
        RadioGroup rgUnits = findViewById(R.id.rgUnits);
        RadioButton rbMetric = findViewById(R.id.rbMetric);
        RadioButton rbImperial = findViewById(R.id.rbImperial);

        // 1. show the saved values
        switchExpiry.setChecked(settings.isExpiryAlertsEnabled());
        if (SettingsManager.IMPERIAL.equals(settings.getUnitSystem())) {
            rbImperial.setChecked(true);
        } else {
            rbMetric.setChecked(true);
        }

        // 2. save when the user changes something
        switchExpiry.setOnCheckedChangeListener((button, isChecked) -> {
            settings.setExpiryAlertsEnabled(isChecked);
            Toast.makeText(this,
                    isChecked ? "Expiry alerts on" : "Expiry alerts off",
                    Toast.LENGTH_SHORT).show();
        });

        rgUnits.setOnCheckedChangeListener((group, checkedId) -> {
            String system = (checkedId == R.id.rbImperial)
                    ? SettingsManager.IMPERIAL : SettingsManager.METRIC;
            settings.setUnitSystem(system);
            Toast.makeText(this, "Default unit: " + settings.getDefaultUnit(),
                    Toast.LENGTH_SHORT).show();
        });

        // 3. bottom navigation
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        NavigationHelper.setup(this, nav, R.id.nav_settings);
    }
}