package com.hbandroid.viewdemo.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.Random;

/**
 * Title:LevelProgressDemo
 * <p>
 * Description:
 * <p>
 * <p>
 * Author: baigege(baigegechen@gmail.com)
 * <p>
 * Date：2017-11-01
 */

public class MySearchView extends View {

    public static final int STATE_NORMAL = 0x00;
    public static final int STATE_SEARCH_ING = 0x01;
    public static final int STATE_SEARCH_END = 0x02;
    public static final int STATE_END = 0x03;
    private int mStatus = STATE_NORMAL;

    private final double DEFAULT_LINE_ANGLE = 45;

    private int mWidth;
    private int mHeight;

    private float angle = 0;

    private Path mCirclePath, mSearchPath;

    private Paint mCirclePaint, mSearchPaint;

    private int mRadius, mSmallRadius;

    private ObjectAnimator mAnimator;
    private ObjectAnimator mSearchAnimator;

    private int mCircleLength;
    private int mSweepAngle = 10;
    private int mSearchLength;
    private int mCurrSearchLength = 0;

    public MySearchView(Context context) {
        this(context, null);
    }

    public MySearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return angle;
    }

    public void setmCurrSearchLength(int mCurrSearchLength) {
        this.mCurrSearchLength = mCurrSearchLength;
    }

    private void init() {
        mCirclePath = new Path();
        mSearchPath = new Path();

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.RED);
        mCirclePaint.setStrokeWidth(6);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mSearchPaint = new Paint();
        mSearchPaint.setAntiAlias(true);
        mSearchPaint.setColor(Color.RED);
        mSearchPaint.setStrokeWidth(6);
        mSearchPaint.setStyle(Paint.Style.STROKE);

        mAnimator = ObjectAnimator.ofFloat(this, "angle", 0, 360);
        mAnimator.setDuration(500);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setRepeatCount(-1);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                int randomLen = new Random().nextInt(mCircleLength);
                mSweepAngle = 360 * randomLen / mCircleLength / 4;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        this.mRadius = (int) (Math.min(mWidth, mHeight) * 0.9f / 2);
        this.mSmallRadius = mRadius * 3 / 8;
    }

    public void setMode(int mode) {
        this.mStatus = mode;
        if (mode == STATE_SEARCH_ING)
            mAnimator.start();
        if (mode == STATE_SEARCH_END) {
            mAnimator = null;

            mSearchAnimator = ObjectAnimator.ofFloat(this, "mCurrSearchLength", 0, mSearchLength);
            mSearchAnimator.setDuration(2000);
            mSearchAnimator.setRepeatMode(ValueAnimator.RESTART);
            mSearchAnimator.setInterpolator(new LinearInterpolator());
            mSearchAnimator.setRepeatCount(1);
            mSearchAnimator.start();
        }
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mWidth / 2, mHeight / 2);
        mCirclePath.addCircle(0, 0, mRadius, Path.Direction.CW);
        float startLineX = (float) (mSmallRadius * 2 * Math.cos(Math.toRadians(DEFAULT_LINE_ANGLE)));
        float startLineY = (float) (mSmallRadius * 2 * Math.sin(Math.toRadians(DEFAULT_LINE_ANGLE)));
        mSearchPath.moveTo(startLineX, startLineY);
        mSearchPath.lineTo(0, 0);
        float circleCenterX = (float) (mSmallRadius * Math.cos(Math.toRadians(DEFAULT_LINE_ANGLE)));
        float circleCenterY = (float) (mSmallRadius * Math.sin(Math.toRadians(DEFAULT_LINE_ANGLE)));
        mSearchPath.addCircle(-circleCenterX, -mSmallRadius, circleCenterY, Path.Direction.CW);

        PathMeasure mPathMeasure = new PathMeasure(mSearchPath, true);
        mSearchLength = (int) mPathMeasure.getLength();
        switch (mStatus) {
            case STATE_NORMAL:
                canvas.drawPath(mCirclePath, mCirclePaint);
                canvas.drawPath(mSearchPath, mSearchPaint);
                break;

            case STATE_SEARCH_ING:
                PathMeasure measure = new PathMeasure(mCirclePath, true);
                mCircleLength = (int) (measure.getLength() / 4);
                canvas.drawArc(-mRadius, -mRadius, mRadius, mRadius, angle, mSweepAngle, false, mCirclePaint);
                invalidate();
                break;

            case STATE_SEARCH_END:
                Path newPath = new Path();
                mPathMeasure.getSegment(0, mCurrSearchLength, newPath, true);
                System.out.println("curr value:" + mCurrSearchLength);
                canvas.drawPath(newPath, mSearchPaint);
                if (mCurrSearchLength < mSearchLength)
                    invalidate();
                break;

            case STATE_END:

                break;
        }
    }
}
