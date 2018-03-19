package com.jackchen.view_day05_2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/3/18 18:14
 * Version 1.0
 * Params:
 * Description:   进度条持续变化 从 0% 变到 100%
*/
public class ProgressBar extends View {

    // 内部的背景
    private int mInnerBackground = Color.RED ;
    // 外部的背景
    private int mOuterBackground = Color.RED ;
    // 圆弧的宽度
    private int mRoundWidth = 10 ;  // 10px
    // 进度文字的大小
    private int mProgressTextSize = 15 ;
    // 进度文字的颜色
    private int mProgressTextColor = Color.RED ;


    private Paint mInnerPaint, mOuterPaint, mTextPaint;

    private int mMax = 100 ;
    private int mProgress = 0 ;

    public ProgressBar(Context context) {
        this(context , null);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);
        // 内部的背景
        mInnerBackground = array.getColor(R.styleable.ProgressBar_innerBackground , mInnerBackground) ;
        // 外部的背景
        mOuterBackground = array.getColor(R.styleable.ProgressBar_outerBackground , mOuterBackground) ;
        // 圆弧的宽度
        mRoundWidth = (int)array.getDimension(R.styleable.ProgressBar_roundWidth , dip2px(10)) ;
        // 字体大小
        mProgressTextSize = array.getDimensionPixelSize(R.styleable.ProgressBar_progressTextSize , sp2px(mProgressTextSize)) ;
        // 字体颜色
        mProgressTextColor = array.getColor(R.styleable.ProgressBar_progressTextColor , mProgressTextColor) ;

        array.recycle();

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerBackground);
        mInnerPaint.setStrokeWidth(mRoundWidth);
        mInnerPaint.setStyle(Paint.Style.STROKE);

        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setColor(mOuterBackground);
        mOuterPaint.setStrokeWidth(mRoundWidth);
        mOuterPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mProgressTextColor);
        mTextPaint.setTextSize(mProgressTextSize);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec) ;
        int height = MeasureSpec.getSize(heightMeasureSpec) ;

        // 获取宽高最小值  保证它是正方形
        setMeasuredDimension(Math.min(width , height) , Math.min(width , height));
    }


    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        // 1. 先画内圆
        int center = getWidth() / 2;
        canvas.drawCircle(center, center, center - mRoundWidth / 2, mInnerPaint);

        // 2. 画外圆
        RectF rect = new RectF(0 + mRoundWidth / 2, 0 + mRoundWidth / 2,
                getWidth() - mRoundWidth / 2, getHeight() - mRoundWidth / 2);

        if (mProgress ==0){
            return;
        }
        float percent = (float) mProgress / mMax;
        canvas.drawArc(rect, 0, percent * 360, false, mOuterPaint);


        // 画进度的文字
        String text = ((int)(percent*100)) + "%" ;
        Rect textBounds = new Rect() ;
        mTextPaint.getTextBounds(text , 0, text.length() , textBounds);

        int x = getWidth()/2 - textBounds.width()/2 ;
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();

        int dy = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom ;
        int baseLine = getHeight()/2 + dy ;
        canvas.drawText(text , x , baseLine , mTextPaint);


    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private float dip2px(int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }


    /**
     * 给几个方法，让它动起来
     * 下边设置 synchronized 目的就是为了防止多个 线程同时操作一个资源，可能会出问题，而添加synchronized后，可以避免这个问题
     * 比如说：一次有两个线程，分别是线程1和线程2
     *        如果不加synchronized：那么线程1和线程2可能会同时执行该方法，可能会出问题
     *        如果加了synchronized：可以避免两个线程同时操作该方法，因为加了synchronized后，如果线程1先执行，它会把这个方法锁住，不让线程2进来，
     *                             当线程1执行完后，线程2才可以执行这个方法
     */

    // 设置最大进度
    public synchronized void setMax(int max){
        if (max < 0){

        }
        this.mMax = max ;
    }


    // 设置当前进度
    public synchronized void setProgress(int progress){
        if (progress < 0){

        }
        this.mProgress = progress ;
        // 因为效果是一直在变，所以这里需要不断重绘
        invalidate();
    }

}
