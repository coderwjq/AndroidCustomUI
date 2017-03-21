package com.jinkun.globalguide.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jinkun.globalguide.R;

/**
 * Created by coderwjq on 2017/3/21.
 */

public class JQCircleProgressBar extends View {

    private Paint paint = new Paint();

    private int mForegroundColor;
    private int mBackgroundColor;
    private float mRoundWidth;
    private int mTextColor;
    private float mTextSize;
    private int mMaxProgress;
    private int mProgress;

    public JQCircleProgressBar(Context context) {
        this(context, null);
    }

    public JQCircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JQCircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttributes(context, attrs);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.JQCircleProgressBar);
        mForegroundColor = attrArray.getColor(R.styleable.JQCircleProgressBar_foregroundColor, Color.rgb(0, 153, 204));
        mBackgroundColor = attrArray.getColor(R.styleable.JQCircleProgressBar_backgroudnColor, Color.rgb(237, 237, 237));
        mRoundWidth = attrArray.getDimension(R.styleable.JQCircleProgressBar_roundWidth, 10);
        mTextColor = attrArray.getColor(R.styleable.JQCircleProgressBar_textColor, Color.rgb(255, 187, 51));
        mTextSize = attrArray.getDimension(R.styleable.JQCircleProgressBar_textSize, 12);
        mMaxProgress = attrArray.getInteger(R.styleable.JQCircleProgressBar_maxProgress, 100);
        mProgress = attrArray.getInteger(R.styleable.JQCircleProgressBar_progress, 50);
        attrArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制背景圆环
        paint.setColor(mBackgroundColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mRoundWidth);
        paint.setAntiAlias(true);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = (int) (centerX - mRoundWidth / 2);
        canvas.drawCircle(centerX, centerY, radius, paint);

        // 绘制进度文字
        String strProgress = mProgress + "%";
        float textWidth = paint.measureText(strProgress);
        float textHeight = mTextSize;
        paint.setColor(mTextColor);
        paint.setTextSize(mTextSize);
        paint.setStrokeWidth(0);
        // 绘制部分在TextView的左下角为起始位置
        canvas.drawText(strProgress, centerX - textWidth / 2, centerY + textHeight / 2, paint);

        // 绘制前景弧线
        RectF oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        paint.setColor(mForegroundColor);
        paint.setStrokeWidth(mRoundWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, 0, 360 * mProgress / mMaxProgress, false, paint);
    }

    public void setMaxProgress(int maxProgress) {
        if (maxProgress <= 0) {
            throw new RuntimeException("请设置一个合理范围的最大进度");
        } else {
            this.mMaxProgress = maxProgress;
        }
    }

    public void setProgress(int progress) {
        if (progress < 0) {
            throw new RuntimeException("请设置一个合理范围的进度值");
        } else {
            this.mProgress = progress;
            postInvalidate();
        }
    }
}
