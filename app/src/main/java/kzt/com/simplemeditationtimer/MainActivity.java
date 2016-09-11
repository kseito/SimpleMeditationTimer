package kzt.com.simplemeditationtimer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;

import kzt.com.simplemeditationtimer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MainActivityHandlers {

    private TextView timerText;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setHandler(this);

        timerText = (TextView) findViewById(R.id.timer_text);

        HoloCircleSeekBar seekbar = (HoloCircleSeekBar) findViewById(R.id.seekbar);
        seekbar.setOnSeekBarChangeListener(new HoloCircleSeekBar.OnCircleSeekBarChangeListener() {
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

    private void changeButton(boolean flag) {
        if (flag) {

        }
    }

    @Override
    public void clickStartTimer(View view) {
        Toast.makeText(this, "くりく", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clickStopTimer(View view) {

    }

    @Override
    public void clickPauseTimer(View view) {

    }
}
