package com.besaba.anvarov.ra_silenttimer;

import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button mClose, mKill;
    RadioGroup mMode;
    TextView mCurrent;
    static AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioManager = (AudioManager) getBaseContext().getSystemService(AUDIO_SERVICE);
        mClose = (Button) findViewById(R.id.btCloseView);
        mClose.setOnClickListener(this);
        mKill = (Button) findViewById(R.id.btKillApp);
        mKill.setOnClickListener(this);
        mCurrent = (TextView) findViewById(R.id.tvCurrentMode);
        mCurrent.setText(checkMode());
        mMode = (RadioGroup) findViewById(R.id.rbMode);
        mMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbSilent:
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        break;
                    case R.id.rbVibrate:
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        break;
                    default:
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
                mCurrent.setText(checkMode());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btCloseView:
                finish();
                break;
            case R.id.btKillApp:
                //stopService(new Intent(this, MyService.class));
                finish();
                break;
        }
    }

    private String checkMode() {
        if (audioManager.getRingerMode() == 2)
            return "Normal mode";
        else
            return audioManager.getRingerMode() > 0 ? "Vibrate mode" : "Silent mode";

    }
    // http://stackoverflow.com/questions/11699603/is-it-possible-to-turn-off-the-silent-mode-programmatically-in-android
    // http://developer.android.com/intl/ru/reference/android/media/AudioManager.html
}
