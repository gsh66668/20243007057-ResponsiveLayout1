package com.example.responsivelayout1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Part 1: only inflates the layout. All of the work for this practical is in
 * res/layout/activity_main.xml.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
