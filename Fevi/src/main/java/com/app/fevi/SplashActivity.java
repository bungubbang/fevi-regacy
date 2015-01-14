package com.app.fevi;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by bungubbang on 3/22/14.
 */
public class SplashActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash); //splash이미지 레이아웃

        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
            }
        }, 2000);

    }
    public void onBackPressed(){} //splash 이미지 띄우는 과정에 백 버튼을 누를 수도 있다. 백버튼 막기
}
