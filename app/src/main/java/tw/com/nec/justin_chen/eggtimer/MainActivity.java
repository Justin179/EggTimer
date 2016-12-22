package tw.com.nec.justin_chen.eggtimer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    SeekBar timerSeekBar;
    TextView timerTextView;
    // 一開始時，timer還沒有開始跑
    Boolean counterIsActive = false;
    Button controllerButton;
    CountDownTimer countDownTimer;

    public void updateTimer(int progress){

        int minutes =  (int) progress/60; // 分，取整數部分，秒消失

        int seconds = progress - (minutes*60); // 秒 (總秒數-分的部分的秒數)

        // 秒的部分，永遠是兩位，不然看起來很怪
        String secondString = String.valueOf(seconds);
        if("0".equals(secondString)){
            secondString = "00";
        } else if (secondString.length()==1){
            secondString = "0"+secondString;
        }


        timerTextView.setText(String.valueOf(minutes)+":"+secondString);
    }

    private void reset(){
        timerTextView.setText("0:30");
        timerSeekBar.setProgress(30);
        timerSeekBar.setEnabled(true);
        countDownTimer.cancel();
        controllerButton.setText("Go!");
        counterIsActive = false;
    }

    public void controlTimer(View view){

        Log.i("Button Pressed: ", String.valueOf(timerSeekBar.getProgress()));

        if(counterIsActive == false){
            // 點下Go!時…

            // 正在倒數計時
            counterIsActive = true;
            // 不可以去拉時間bar
            timerSeekBar.setEnabled(false);
            // Go! -> Stop
            controllerButton.setText("Stop");


            // 加100(0.1秒)是解決delay的問題
            countDownTimer = new CountDownTimer(timerSeekBar.getProgress()*1000+100,1000){

                @Override
                public void onTick(long millisUntilFinished) {

                    updateTimer((int) millisUntilFinished/1000);

                }

                @Override
                public void onFinish() {
                    // 倒數結束
                    // play sound
                    MediaPlayer mplayer = MediaPlayer.create(getApplicationContext(),R.raw.airhorn);
                    mplayer.start();

                    reset();
                }
            }.start();

        } else {
            // 正在倒數的時侯點下Stop時… reset the timer
            // reset
            reset();

        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerSeekBar = (SeekBar) findViewById(R.id.timerSeekBar);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        controllerButton = (Button) findViewById(R.id.controllerButton);

        timerSeekBar.setMax(600); // 600秒=10分鐘
        timerSeekBar.setProgress(30);

        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                updateTimer(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }
}
