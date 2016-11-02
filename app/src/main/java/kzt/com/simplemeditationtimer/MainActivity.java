package kzt.com.simplemeditationtimer;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;

import kzt.com.simplemeditationtimer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MainActivityHandlers {

    private static final String PREF_SET_MINUTE = "set_time";

    private TextView timerText;
    private ActivityMainBinding binding;
    private CountDownTimer timer;
    private AnimatorSet animatorSet;

    private int tempTimeInSecond;

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

        binding.musicSpinner.setItems(SoundManager.getSoundList());
        binding.musicSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Snackbar.make(view, "Clicked" + item, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void startTimer(int timeSecond) {
        binding.seekbar.setMax(3600);
        binding.seekbar.setValue(timeSecond);
        System.out.println("start:" + timeSecond);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putInt(PREF_SET_MINUTE, timeSecond / 60).apply();

        binding.overlay.setVisibility(View.VISIBLE);

        timer = new CountDownTimer(timeSecond * 1000, 200) {
            @Override
            public void onTick(long millisUntilFinished) {
                long second = millisUntilFinished / 1000;
                binding.timerText.setText(String.format("%1$02d:%2$02d",
                        second / 60, second % 60));
                binding.seekbar.setValue(second);
                System.out.println("per second:" + second);
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
        if (tempTimeInSecond == 0) {
            //pause
            animatorSet = AnimationUtils.flashing(binding.timerText);
            binding.pauseButton.setImageResource(R.mipmap.ic_start);
            tempTimeInSecond = CommonUtil.convertTextToTime(binding.timerText.getText().toString()) + 1;
            timer.cancel();
            System.out.println("pause save:" + tempTimeInSecond);

        } else {
            //reset pause
            animatorSet.removeAllListeners();
            binding.pauseButton.setImageResource(R.mipmap.ic_pause);
            startTimer(tempTimeInSecond);
            tempTimeInSecond = 0;
            System.out.println("pause reset:" + tempTimeInSecond);
        }
    }

    @Override
    public void clickStartTimer(View view) {
        AnimationUtils.startMeditation(binding.startButton, binding.stopButton, binding.pauseButton);

        startTimer(binding.seekbar.getValue() * 60);
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

    private void playSound() {
        MediaPlayer mp = MediaPlayer.create(this, R.raw.piano);
        mp.start();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0, 400, 800, 400, 800, 2000}, -1);
    }
}
