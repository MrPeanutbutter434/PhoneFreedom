

package com.example.phonefreedom;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private int counter;
    private WindowManager windowManager;
    private ImageView imageView;
    private Context context;
    private PopupWindow popupWindow;
    private AppPickerAdapter adapter;
    ListView listView;
    ArrayList dataModel;


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "You cannot leave", Toast.LENGTH_LONG);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            while (this.counter != 0) {
                // do nothing
                System.out.println("Hello");
            }
            return false;
        }

        if(keyCode == KeyEvent.KEYCODE_APP_SWITCH) {
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initSpinner();
        initButton();
        initAppPicker();
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

        int timeOut = Integer.parseInt(parent.getItemAtPosition(pos).toString());
        setTimer(timeOut);
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
                overlayApproach3();

            }

            // TODO revisit this logic
//            public void overlayApproach2() {
//                WindowManager.LayoutParams windowManagerParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY ,
//                        WindowManager.LayoutParams. FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSLUCENT);
//
//                WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//
//                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//                View myView = inflater.inflate(R.layout.activity_timeout_screen, null);
//
//                wm.addView(myView, windowManagerParams);
//            }

            public void  overlayApproach3() {
                Intent intent = new Intent(getApplicationContext(), TimeoutScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
                counter = 0;
            }
        };

        timer.start();
    }

    private void initAppPicker() {
        // Get the application context
        context = getApplicationContext();

        Button appPickerButton = findViewById(R.id.app_picker);

        appPickerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.app_picker, null);

                // Initialize a new instance of popup window
                popupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );


                popupWindow.setElevation(5.0f);


                // Get a reference for the custom view close button
                Button closeButton = customView.findViewById(R.id.app_picker_close);

                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        popupWindow.dismiss();
                    }
                });

                // Finally, show the popup window at the center location of root relative layout
                popupWindow.showAtLocation(findViewById(R.id.main_activity), Gravity.CENTER,0,0);

                listView = customView.findViewById(R.id.list_view);
                initListView();
            }

            public void initListView() {


                dataModel = new ArrayList<AppPickerDataModel>();

                dataModel.add(new AppPickerDataModel("Apple Pie", false));
                dataModel.add(new AppPickerDataModel("Banana Bread", false));

                adapter = new AppPickerAdapter(dataModel, getApplicationContext());

                listView.setAdapter(adapter);
            }
        });
    }
}
