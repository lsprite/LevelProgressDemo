package com.hbandroid.viewdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.hbandroid.viewdemo.R;

/**
 * Title:ViewDemo
 * <p/>
 * Description:
 * <p/>
 * <p/>
 * Author: baigege(baigegechen@gmail.com)
 * <p/>
 * Date：2017-10-19
 */

public class HBLevelView extends View {

    //设置进度显示文字的所有编剧
    private static final int TEXT_LEFT_RIGHT_PADDING = 6;

    //绘制进度条和进度文字的画笔
    private Paint mProgressPaint;
    private Paint mTextPaint;

    private Context mContext;

    //进度条的底色和完成进度的颜色
    private int mProgressBackColor;
    private int mProgressForeColor;

    //进度条上方现实的文字
    private String mProgressText;
    //进度文字的颜色
    private int mTextColor;
    //进度文字的字体大小
    private int mTextSize;

    //进度条的起始值，当前值和结束值
    private float currentProgress;
    private float startProgress;
    private float endProgress;

    //进度条的高度
    private int mProgressBarHeight;

    //view的上下内边距
    private int mPaddingTop;
    private int mPaddingBottom;

    //用于测量文字显示区域的宽度和高度
    private Paint.FontMetricsInt mTextFontMetrics;
    private Rect mTextBound;

    //用于绘制三角形的箭头
    private Path mPath;

    //进度条和进度文字显示框的间距
    private int mLine2TextDividerHeight;

    //三角形箭头的高度
    private int mTriangleHeight;

    //绘制进度条圆角矩形的圆角
    private int mRectCorn;

    public HBLevelView(Context context) {
        this(context, null);
    }

    public HBLevelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HBLevelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        if (attrs != null) {
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs, R.styleable.HBLevelView, defStyleAttr, 0);
            int n = a.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = a.getIndex(i);
                switch (attr) {
                    case R.styleable.HBLevelView_backColor:
                        mProgressBackColor = a.getColor(attr, Color.GRAY);
                        break;
                    case R.styleable.HBLevelView_foreColor:
                        mProgressForeColor = a.getColor(attr, Color.GRAY);
                        break;
                    case R.styleable.HBLevelView_textColor:
                        mTextColor = a.getColor(attr, Color.GRAY);
                        break;
                    case R.styleable.HBLevelView_startProgress:
                        startProgress = a.getFloat(attr, 0);
                        break;
                    case R.styleable.HBLevelView_endProgress:
                        endProgress = a.getFloat(attr, 0);
                        break;
                    case R.styleable.HBLevelView_scurrProgress:
                        currentProgress = a.getFloat(attr, 0);
                        mProgressText = String.valueOf((int) currentProgress);
                        break;
                    case R.styleable.HBLevelView_progressTextSize:
                        mTextSize = a.getDimensionPixelSize(attr, 24);
                        break;
                    case R.styleable.HBLevelView_rectCorn:
                        mRectCorn = a.getDimensionPixelSize(attr, 4);
                        break;
                }
            }
            a.recycle();
        }
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getPaddingTop() + mTextFontMetrics.bottom - mTextFontMetrics.top + mTriangleHeight + mLine2TextDividerHeight + mProgressBarHeight + mPaddingBottom);
    }

    private void init() {
        mTriangleHeight = 6;
        mProgressBarHeight = 20;
        mLine2TextDividerHeight = 5;
        mRectCorn = mProgressBarHeight / 2;

        mTextBound = new Rect();

        mProgressPaint = new Paint();
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setStrokeWidth(mProgressBarHeight);

        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        reCaculateTextSize();

        mPaddingTop = getPaddingTop();
        mPaddingBottom = getPaddingBottom();

        mPath = new Path();
    }

    private void reCaculateTextSize() {
        mTextPaint.setTextSize(mTextSize);
        mTextFontMetrics = mTextPaint.getFontMetricsInt();
        mTextPaint.getTextBounds(mProgressText, 0, mProgressText.length(), mTextBound);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制前清理上次绘制的痕迹
        mPath.reset();
        mProgressPaint.setColor(mProgressBackColor);

        //计算开始绘制进度条的y坐标
        int startLineLocationY = mPaddingTop - mTextFontMetrics.top + mTextFontMetrics.bottom + mTriangleHeight + mLine2TextDividerHeight + mProgressBarHeight / 2;

        //绘制进度条底部背景
//        canvas.drawLine(0, startLineLocationY, getScreenWidth(), startLineLocationY, mProgressPaint);
        canvas.drawRoundRect(0, startLineLocationY, getScreenWidth(), startLineLocationY + mProgressBarHeight, mRectCorn, mRectCorn, mProgressPaint);

        //绘制已经完成了的进度条
        mProgressPaint.setColor(mProgressForeColor);
        double progress = (currentProgress - startProgress) / (endProgress - startProgress);
        int currProgress = (int) (getScreenWidth() * progress);
//        canvas.drawLine(0, startLineLocationY, currProgress, startLineLocationY, mProgressPaint);
        canvas.drawRoundRect(0, startLineLocationY, currProgress, startLineLocationY + mProgressBarHeight, mRectCorn, mRectCorn, mProgressPaint);
        /*
        绘制显示文字三角形框
         */
        //计算文字显示区域的宽度和高度
        int textWidth = mTextBound.right - mTextBound.left;
        int textHeight = mTextFontMetrics.bottom - mTextFontMetrics.top;

        //计算三角形定点开始时的y坐标
        int startTriangleY = startLineLocationY - mProgressBarHeight / 2 - mLine2TextDividerHeight;

        mPath.moveTo(currProgress, startTriangleY);
        mPath.lineTo(currProgress + 10, startTriangleY - mTriangleHeight);
        mPath.lineTo(currProgress + textWidth / 2 + TEXT_LEFT_RIGHT_PADDING, startTriangleY - mTriangleHeight);
        mPath.lineTo(currProgress + textWidth / 2 + TEXT_LEFT_RIGHT_PADDING, startTriangleY - mTriangleHeight - textHeight);
        mPath.lineTo(currProgress - textWidth / 2 - TEXT_LEFT_RIGHT_PADDING, startTriangleY - mTriangleHeight - textHeight);
        mPath.lineTo(currProgress - textWidth / 2 - TEXT_LEFT_RIGHT_PADDING, startTriangleY - mTriangleHeight);
        mPath.lineTo(currProgress - 10, startTriangleY - mTriangleHeight);
        mPath.close();
        canvas.drawPath(mPath, mProgressPaint);

        //绘制文字
        canvas.drawText(mProgressText, (float) (currProgress - textWidth / 2), mPaddingTop - mTextFontMetrics.top, mTextPaint);

    }

    public void resetLevelProgress(float start, float end, float current) {
        this.startProgress = start;
        this.endProgress = end;
        this.currentProgress = current;
        this.mProgressText = String.valueOf((int) currentProgress);
        reCaculateTextSize();
        invalidate();
    }

    public int getScreenWidth() {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
