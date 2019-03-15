package com.jack.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jack.widget.BreatheView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout root = findViewById(R.id.ll_root);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(150),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        BreatheView breatheView = new BreatheView(this);
        root.addView(breatheView, params);
        breatheView.setAnimationDuration(1000)
                .setFirstCircleMinRadiusRes(R.dimen.dp_5)
                .setFirstCircleMaxRadiusRes(R.dimen.dp_10)
                .setFirstCircleColorRes(R.color.colorAccent)
                .setSecondCircleRadiusRes(R.dimen.dp_20)
                .setSecondCircleColorRes(R.color.colorPrimary)
                .setThirdCircleRadiusRes(R.dimen.dp_40)
                .setThirdCircleColorRes(R.color.cyan)
                .setBreatheCircleMaxRadiusRes(R.dimen.dp_60)
                .setBreatheCircleColorRes(R.color.breatheColor)
                .start();
        BreatheView anotherBreatheView = new BreatheView(this);
        root.addView(anotherBreatheView, params);
        anotherBreatheView.setAnimationDuration(1500)
                .setFirstCircleMinRadius(dp2px(7))
                .setFirstCircleMaxRadius(dp2px(14))
                .setFirstCircleColor(Color.parseColor("#9C27B0"))
                .setSecondCircleRadius(dp2px(30))
                .setSecondCircleColor(Color.parseColor("#FFC107"))
                .setThirdCircleRadius(dp2px(50))
                .setThirdCircleColor(Color.parseColor("#4CAF50"))
                .setBreatheCircleMaxRadius(dp2px(100))
                .setBreatheCircleColor(Color.parseColor("#662196F3"))
                .start();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dp2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
