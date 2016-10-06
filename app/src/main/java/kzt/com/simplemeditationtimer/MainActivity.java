package kzt.com.simplemeditationtimer;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;

import kzt.com.simplemeditationtimer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MainActivityHandlers {

    private static final String PREF_SET_MINUTE = "set_time";

    private TextView timerText;
    private ActivityMainBinding binding;
    private CountDownTimer timer;

    private int tempTimeInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setHandler(this);

        timerText = (TextView) findViewById(R.id.timer_text);

        binding.seekbar.setOnSeekBarChangeListener(new HoloCircleSeekBar.OnCircleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(HoloCircleSeekBar holoCircleSeekBar, int i, boolean b) {
                timerText.setText(CommonUtil.convertTime(i));
            }

            @Override
            public void onStartTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) {

            }
        });
    }

    private void startTimer(int timeMillis) {
        binding.seekbar.setMax(3600000);
        binding.seekbar.setValue(timeMillis);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putInt(PREF_SET_MINUTE, timeMillis / 60 / 1000).apply();

        binding.overlay.setVisibility(View.VISIBLE);

        timer = new CountDownTimer(timeMillis, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                long second = millisUntilFinished / 1000;
                binding.timerText.setText(String.format("%1$02d:%2$02d",
                        second / 60, second % 60));
                binding.seekbar.setValue(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "終了", Toast.LENGTH_SHORT).show();
            }
        };
        timer.start();
    }

    private void stopTimer() {
        binding.seekbar.setMax(60);
        binding.overlay.setVisibility(View.INVISIBLE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int minute = prefs.getInt(PREF_SET_MINUTE, 0);
        binding.timerText.setText(CommonUtil.convertTime(minute));
        timer.cancel();
    }

    private void pauseTimer() {
        if (tempTimeInMillis == 0) {
            //pause
            binding.pauseButton.setImageResource(R.mipmap.ic_start);
            tempTimeInMillis =  binding.seekbar.getValue();
            timer.cancel();

        } else {
            //reset pause
            binding.pauseButton.setImageResource(R.mipmap.ic_pause);
            startTimer(tempTimeInMillis);
            tempTimeInMillis = 0;
        }
    }

    @Override
    public void clickStartTimer(View view) {
        AnimationUtils.startMeditation(binding.startButton, binding.stopButton, binding.pauseButton);

        startTimer(binding.seekbar.getValue() * 60 * 1000);
    }

    @Override
    public void clickStopTimer(View view) {
        AnimationUtils.stopMeditation(binding.startButton, binding.stopButton, binding.pauseButton);

        stopTimer();
    }

    @Override
    public void clickPauseTimer(View view) {
        pauseTimer();
    }
}
