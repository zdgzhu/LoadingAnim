package com.loadinganim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.loadinganim.customView.WifiLoadingAnim;

public class MainActivity extends AppCompatActivity {

    private WifiLoadingAnim wifiLoadingAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        initAnim();

    }

    private void initAnim() {

        wifiLoadingAnim = (WifiLoadingAnim) findViewById(R.id.wifianim);
        wifiLoadingAnim.startAnim();


    }

}
