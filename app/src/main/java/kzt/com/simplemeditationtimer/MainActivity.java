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
import android.view.Menu;
import android.view.View;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Locale;

import kzt.com.simplemeditationtimer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MainActivityHandlers, NumberPickerDialog.OnClickListener {

    private static final String PREF_SET_MINUTE = "set_time";

    private ActivityMainBinding binding;
    private CountDownTimer timer;
    private AnimatorSet animatorSet;
    private int tempTimeInSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setHandler(this);

        binding.progress.setStartingDegree(270);

        binding.musicSpinner.setItems(SoundManager.getSoundList());
        binding.musicSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Snackbar.make(view, "Clicked" + item, Snackbar.LENGTH_LONG).show();
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int minute = prefs.getInt(PREF_SET_MINUTE, 0);
        binding.timerText.setText(Utils.convertTime(minute));
        binding.progress.setProgress(minute * 60);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top, menu);
        return true;
    }

    private void startTimer(int timeSecond) {
        System.out.println("start:" + timeSecond);

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
        NumberPickerDialog dialog = new NumberPickerDialog();
        dialog.show(getSupportFragmentManager(), dialog.getClass().getCanonicalName());
    }

    private void playSound() {
        MediaPlayer mp = MediaPlayer.create(this, R.raw.piano);
        mp.start();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0, 400, 800, 400, 800, 2000}, -1);
    }

    @Override
    public void clickOk(int minute) {
        binding.timerText.setText(Utils.convertTime(minute));
        binding.progress.setProgress(minute * 60);
    }

    @Override
    public void clickCancel() {

    }
}
