package kzt.com.simplemeditationtimer.activities;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import java.util.Locale;

import kzt.com.simplemeditationtimer.MainActivityHandlers;
import kzt.com.simplemeditationtimer.NumberPickerDialog;
import kzt.com.simplemeditationtimer.R;
import kzt.com.simplemeditationtimer.SoundManager;
import kzt.com.simplemeditationtimer.databinding.ActivityMainBinding;
import kzt.com.simplemeditationtimer.utils.AnimationUtils;
import kzt.com.simplemeditationtimer.utils.Utils;

public class MainActivity extends AppCompatActivity implements MainActivityHandlers, NumberPickerDialog.OnClickListener {

    private static final String PREF_SET_MINUTE = "set_time";

    private ActivityMainBinding binding;
    private CountDownTimer timer;
    private AnimatorSet animatorSet;
    private int tempTimeInSecond;
    private boolean isMeditating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setHandler(this);

        binding.progress.setStartingDegree(270);

        binding.musicSpinner.setItems(SoundManager.getSoundList());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int minute = prefs.getInt(PREF_SET_MINUTE, 0);
        binding.timerText.setText(Utils.convertTime(minute));
        binding.progress.setProgress(minute * 60);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setSleepMode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.top_menu_setting:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startTimer(int timeSecond) {
        System.out.println("start:" + timeSecond);
        isMeditating = true;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putInt(PREF_SET_MINUTE, timeSecond / 60).apply();

        timer = new CountDownTimer(timeSecond * 1000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                long second = millisUntilFinished / 1000;
                binding.timerText.setText(String.format(Locale.JAPAN, "%1$02d:%2$02d", second / 60, second % 60));
                binding.progress.setProgress(second);
                System.out.println("per second:" + second);
            }

            @Override
            public void onFinish() {
                playSound();
                vibrate();
                finishTimer();
            }
        };
        timer.start();
    }

    private void stopTimer() {
        AnimationUtils.stopMeditation(binding.startButton, binding.stopButton, binding.pauseButton);

        isMeditating = false;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int minute = prefs.getInt(PREF_SET_MINUTE, 0);
        binding.timerText.setText(Utils.convertTime(minute));
        binding.progress.setProgress(minute * 60);
        timer.cancel();

        //pause -> stop
        if (tempTimeInSecond != 0) {
            animatorSet.removeAllListeners();
            binding.pauseButton.setImageResource(R.mipmap.ic_pause);
            tempTimeInSecond = 0;
        }
    }

    private void pauseTimer() {
        if (tempTimeInSecond == 0) {
            //pause
            animatorSet = AnimationUtils.flashing(binding.timerText);
            binding.pauseButton.setImageResource(R.mipmap.ic_start);
            tempTimeInSecond = Utils.convertTextToTime(binding.timerText.getText().toString()) + 1;
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

    private void finishTimer() {
        binding.pauseButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void clickStartTimer(View view) {
        AnimationUtils.startMeditation(binding.startButton, binding.stopButton, binding.pauseButton);
        startTimer((int) binding.progress.getProgress());
    }

    @Override
    public void clickStopTimer(View view) {
        stopTimer();
    }

    @Override
    public void clickPauseTimer(View view) {
        pauseTimer();
    }

    @Override
    public void clickChangeTimer(View view) {

        if (isMeditating) {
            return;
        }

        NumberPickerDialog dialog = new NumberPickerDialog();
        dialog.show(getSupportFragmentManager(), dialog.getClass().getCanonicalName());
    }

    @Override
    public void clickSoundListen(View view) {
        playSound();
    }

    private void playSound() {
        int musicResource = SoundManager.findSoundById(binding.musicSpinner.getSelectedIndex());
        MediaPlayer mp = MediaPlayer.create(this, musicResource);
        mp.start();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0, 400, 800, 400, 800, 800}, -1);
    }

    @Override
    public void clickOk(int minute) {
        binding.timerText.setText(Utils.convertTime(minute));
        binding.progress.setProgress(minute * 60);
    }

    @Override
    public void clickCancel() {

    }

    private void init() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean(Utils.PREF_FIRST_BOOT, true)) {
            prefs.edit()
                    .putBoolean(Utils.PREF_FIRST_BOOT, true)
                    .putBoolean(Utils.PREF_NO_SLEEP, true)
                    .apply();
        }
    }

    private void setSleepMode() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean(Utils.PREF_NO_SLEEP, false)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            System.out.println("no sleep");
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            System.out.println("sleep");
        }
    }
}
