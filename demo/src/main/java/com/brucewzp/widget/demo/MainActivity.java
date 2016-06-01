package com.brucewzp.widget.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.brucewzp.widget.switcher.Switcher;


public class MainActivity extends AppCompatActivity {
    private Switcher switcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switcher = (Switcher) findViewById(R.id.switcher);
        switcher.setOnStateChangedListener(new Switcher.OnStateChangedListener() {
            @Override
            public void onStateTrue() {
                Log.d("tag", "changed to: " + switcher.getState());
            }

            @Override
            public void onStateFalse() {
                Log.d("tag", "changed to: " + switcher.getState());
            }
        });
    }
}
