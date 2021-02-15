

package com.example.phonefreedom;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private int counter;
    private int timeOut;
    private WindowManager windowManager;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        initSpinner();
        initButton();

        if (!Settings.canDrawOverlays(this)) {
            askPermission();
        }
    }

    @TargetApi(23)
    public void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 5469);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        this.timeOut = Integer.parseInt(parent.getItemAtPosition(pos).toString());
        setTimer(this.timeOut);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        System.out.println("Reached here");
    }

    private void initSpinner() {
        spinner = findViewById(R.id.time_picker);

        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.timer_hours, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    private void initButton() {
        Button button = findViewById(R.id.start_timer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlayApproach2();

            }

            public void overlayApproach1(View v) {
                windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

                imageView = new ImageView(getApplicationContext());
                imageView.setImageResource(R.drawable.ic_launcher_background);

                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);

                params.gravity = Gravity.TOP | Gravity.LEFT; // Orientation
                params.x = 100; // where you want to draw this, coordinates
                params.y = 100;

                windowManager.addView(v, params);
            }

            public void overlayApproach2() {
                WindowManager.LayoutParams windowManagerParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY ,
                        WindowManager.LayoutParams. FLAG_DIM_BEHIND, PixelFormat.TRANSLUCENT);

                WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View myView = inflater.inflate(R.layout.activity_timeout_screen, null);

                wm.addView(myView, windowManagerParams);
            }
        });
    }

    private void setTimer(int timeOut) {
        this.counter = 0;
        this.spinner.setEnabled(false);

        final TextView timerDisplay = findViewById(R.id.timer_display);
        CountDownTimer timer = new CountDownTimer((timeOut+1)*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerDisplay.setText(String.valueOf(counter));
                counter++;
            }

            @Override
            public void onFinish() {
                timerDisplay.setText("Finished");
                spinner.setEnabled(true);
            }
        };

        timer.start();
    }
}
