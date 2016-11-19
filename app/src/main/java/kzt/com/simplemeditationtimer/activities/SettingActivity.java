package kzt.com.simplemeditationtimer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;

import kzt.com.simplemeditationtimer.R;
import kzt.com.simplemeditationtimer.databinding.ActivitySettingBinding;

/**
 * Created by k-seito on 2016/11/10.
 */
public class SettingActivity extends AppCompatActivity {

    public static final String PREF_NO_SLEEP = "no_sleep";

    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        getSupportActionBar().setTitle("設定");

        binding.sleepSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                prefs.edit().putBoolean(PREF_NO_SLEEP, isChecked).apply();
            }
        });

        binding.aboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
            }
        });
    }
}
