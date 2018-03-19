package com.jackchen.view_day05_2;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progress_bar ;
    private ShapeView shape_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 进度条变化的控件
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        // 设置最大进度
        progress_bar.setMax(4000);
        // 采用属性动画让圆环跑起来
        ValueAnimator animator = ObjectAnimator.ofFloat(0, 4000);
        animator.setDuration(2000) ;
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //不断的获取当前进度，然后给ProgressBar 设置
                float progress = (float) animation.getAnimatedValue();
                progress_bar.setProgress((int) progress);
            }
        });


        // 切换形状的控件
        shape_view = (ShapeView) findViewById(R.id.shape_view);
    }


    /**
     * 点击 "切换形状" 让3个形状来回不断的切换
     * @param view
     */
    public void exchange(View view){

        // 开一个线程
        new Thread(new Runnable() {
            @Override
            public void run() {

                // 让其不断的循环 ，在这里更新UI，每循环一次，休息一秒钟
                while (true){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            shape_view.exchange();
                        }
                    });


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
