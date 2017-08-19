package com.loadinganim.customView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 2017/8/19.
 */

public class WifiLoadingAnim extends View{

    private float mWidth = 0f;
    private Paint mPaint;
    //WiFi图像有几条圆弧
    private int signalSize = 5;


    private ValueAnimator valueAnimator;
    private float mAnimatedValue = 0.9f;

    public WifiLoadingAnim(Context context) {
        this(context,null);
    }

    public WifiLoadingAnim(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WifiLoadingAnim(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);

    }


    /**自定义view的绘制过程，
     *Measure
      View会先做一次测量，算出自己需要占用多大的面积。View的Measure过程给我们暴露了一个接口onMeasure，方法的定义是这样的，
     *
     *Layout
     Layout过程对于View类非常简单，同样View给我们暴露了onLayout方法

     Draw
     Draw过程，就是在canvas上画出我们需要的View样式。同样View给我们暴露了onDraw方法
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() > getHeight()) {
            mWidth = getMeasuredHeight();
        } else {
            mWidth = getMeasuredWidth();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();//锁画布(为了保存之前的画布状态)
        //平移，将画布的坐标原点向左右方向移动x，向上下方向移动y.canvas的默认位置是在（0,0）
        canvas.translate(0, mWidth / signalSize);
        mPaint.setStrokeWidth(mWidth / signalSize / 2 / 2 / 2);
        int scale = (int) ((mAnimatedValue * signalSize - (int) (mAnimatedValue * signalSize)) * signalSize) + 1;
        RectF rect = null;
        float signalRadius = mWidth / 2 / signalSize;
        for (int i=0;i<signalSize;i++) {

            if (i > signalSize - scale) {
                float radius = signalRadius * i;
                rect = new RectF(radius, radius, mWidth - radius, mWidth - radius);
                if (i < signalSize - 1) {
                    mPaint.setStyle(Paint.Style.STROKE);
                    canvas.drawArc(rect, -135, 90, false, mPaint);
                } else {
                    mPaint.setStyle(Paint.Style.FILL);
                    canvas.drawArc(rect,-135,90,true,mPaint);
                }
            }

        }
        //把当前画布返回（调整）到上一个save()状态之前
        canvas.restore();
    }

    public void startAnim() {
        stopAnim();
        startViewAnim(0f,1f,6000);



    }

    public void stopAnim() {
        if (valueAnimator != null) {
            /**
             * 也就是说，如果我们调用了setFillAfter(true)，动画结束的时候就不会调用clearAnimation()，mCurrentAnimation就不会被置空了。在我们实现的动画里，确实调用了setFillAfter(true)。。。
             总结：做动画时，如果调用了setFillAfter(true)，动画结束后如果要设置该view的可见性，需要先调用一次clearAnimation()。
             */
            clearAnimation();
            valueAnimator.setRepeatCount(0);
            valueAnimator.cancel();
            valueAnimator.end();
            mAnimatedValue = 0.9f;
            /**
             * 在这么多线程当中，把主要是负责控制UI界面的显示、更新和控件交互的线程称为UI线程，由于onCreate()方法是由UI线程执行的，所以也可以把UI线程理解为主线程。其余的线程可以理解为工作者线程。
             invalidate()得在UI线程中被调动，在工作者线程中可以通过Handler来通知UI线程进行界面更新。
             而postInvalidate()在工作者线程中被调用
             */
            postInvalidate();
        }
    }


    private ValueAnimator startViewAnim(float startF, final float endF, long time) {
        valueAnimator = ValueAnimator.ofFloat(startF, endF);
        valueAnimator.setDuration(time);//动画执行的时间
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        //设置重复模式：逆向重复  设置动画重复的模式，包含REVERSE（逆向重复）和RESTART（重新开始）
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

//        valueAnimator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
        return valueAnimator;
    }





















}
