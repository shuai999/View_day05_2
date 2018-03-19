package com.jackchen.view_day05_2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/3/19 8:55
 * Version 1.0
 * Params:
 * Description:   3个形状来回切换（圆形、正方形、三角）
 *
 *
 *    思路就是：
 *          1>：首先设置刚进入app时，默认的形状，比如为圆
 *          2>：在onDraw()方法中 画圆、画正方形、画三角；
 *          3>：然后对外提供一个 exchange()方法，作用是根据当前形状判断：
 *              如果当前形状是圆，就把它改变为正方形；
 *              如果当前形状是正方形，就把它设置为三角；
 *              如果当前形状是三角，就把它设置为圆；
 *
*/

public class ShapeView extends View {

    // 当前形状默认是圆
    private Shape mCurrentShape = Shape.Circle;
    // 画笔
    private Paint mPaint ;
    // 画三角的路径
    private Path mPath ;

    public ShapeView(Context context) {
        this(context,null);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化画笔
        initPaint() ;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec) ;
        int height = MeasureSpec.getSize(heightMeasureSpec) ;

        // 获取最小值 为了保证是正方形
        setMeasuredDimension(Math.min(width , height) , Math.min(width , height));
    }


    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        switch (mCurrentShape){
            case Circle:
                 // 画圆
                 int center = getWidth()/2 ;
                 mPaint.setColor(Color.YELLOW);
                 canvas.drawCircle(center , center , center , mPaint);
                 break;
            case Square:
                 // 画正方形
                 mPaint.setColor(Color.BLUE);
                 canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);
                 break;
            case Triangle:
                 // 画等边三角形 Path 画路线
                 mPaint.setColor(Color.RED);
                 if (mPath == null){
                     // 画路径
                     mPath = new Path() ;
                     mPath.moveTo(getWidth()/2 , 0);
                     mPath.lineTo(0 , (float) ((getWidth()/2)*Math.sqrt(3)));
                     mPath.lineTo(getWidth() , (float) ((getWidth()/2)*Math.sqrt(3)));
//                     mPath.lineTo(getWidth()/2,0);
                     mPath.close();  // 把路径闭合，或者用上边lineTo即可
                 }
                 canvas.drawPath(mPath,mPaint);
                 break;
        }
    }


    public void exchange(){
        switch (mCurrentShape){
            case Circle:
                 // 如果是圆，就把当前形状置为正方形
                 mCurrentShape = Shape.Square;
                 break;
            case Square:
                 // 如果是正方形，就把当前形状置为三角
                 mCurrentShape = Shape.Triangle;
                 break;
            case Triangle:
                 // 如果是三角，就把当前形状置为圆
                 mCurrentShape = Shape.Circle ;
                 break;
        }
        // 因为形状不断的变化，所以需要不断的重新绘制
        invalidate();
    }

    private void initPaint() {
        mPaint = new Paint() ;
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }


    public enum Shape{
        Circle , Square , Triangle
    }
}
