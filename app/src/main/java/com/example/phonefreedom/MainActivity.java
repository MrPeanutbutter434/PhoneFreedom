

package com.example.phonefreedom;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private int counter;
    private int timeOut;
    private WindowManager windowManager;
    private ImageView imageView;
    private Context mContext;
    private RelativeLayout mRelativeLayout;
    private Button mButton;
    private PopupWindow mPopupWindow;
    private Activity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        initSpinner();
        initButton();
        initAppPicker();

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

    private void initAppPicker() {
        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = MainActivity.this;

        // Get the widgets reference from XML layout
        mRelativeLayout = findViewById(R.id.app_picker_layout);
        mButton = findViewById(R.id.app_picker);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.app_picker, (ViewGroup) findViewById(R.id.main_activity), false);

                /*
                    public PopupWindow (View contentView, int width, int height)
                        Create a new non focusable popup window which can display the contentView.
                        The dimension of the window must be passed to this constructor.

                        The popup does not provide any background. This should be handled by
                        the content view.

                    Parameters
                        contentView : the popup's content
                        width : the popup's width
                        height : the popup's height
                */
                // Initialize a new instance of popup window
                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                // Get a reference for the custom view close button
                Button closeButton = customView.findViewById(R.id.app_picker_close);

                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });

                /*
                    public void showAtLocation (View parent, int gravity, int x, int y)
                        Display the content view in a popup window at the specified location. If the
                        popup window cannot fit on screen, it will be clipped.
                        Learn WindowManager.LayoutParams for more information on how gravity and the x
                        and y parameters are related. Specifying a gravity of NO_GRAVITY is similar
                        to specifying Gravity.LEFT | Gravity.TOP.

                    Parameters
                        parent : a parent view to get the getWindowToken() token from
                        gravity : the gravity which controls the placement of the popup window
                        x : the popup's x location offset
                        y : the popup's y location offset
                */
                // Finally, show the popup window at the center location of root relative layout
                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);
            }
        });
    }
}
