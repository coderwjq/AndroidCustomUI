package com.jinkun.globalguide.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.litesuits.android.log.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coderwjq on 2017/3/27.
 */

public class JQFlowLayout extends ViewGroup {
    private static final String TAG = JQFlowLayout.class.getSimpleName();

    private List<List<View>> mAllViews = new ArrayList<>();
    private List<Integer> mAllHeights = new ArrayList<>();

    public JQFlowLayout(Context context) {
        super(context);
    }

    public JQFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JQFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 根据子控件计算布局的宽高
     *
     * @param widthMeasureSpec  宽度的测量规格(包含测量模式和测量大小)
     * @param heightMeasureSpec 高度的测量规格(包含测量模式和测量大小)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Log.d(TAG, "widthMode:" + widthMode + " widthSize:" + widthSize + " heightMode:" + heightMode + " heightSize:" + heightSize);

        // 保存布局中最大行的宽度
        int maxWidth = 0;
        // 保存布局中所有行的高度
        int totalHeight = 0;

        // 保存当前行的最大宽度
        int lineWidth = 0;
        // 保存当前行的最大高度
        int lineHeight = 0;

        // 测量模式包含三种：EXACTLY/AT_MOST/UNSPECIFIED
        // 只需要在AT_MOST模式下才需要进行计算
        int childCount = getChildCount();

        Log.i(TAG, "JQFlowLayout onMeasure:" + childCount);

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 必须调用measureChild方法才可以获得子View的宽高值
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            // 获取子View的宽高值
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            // 获取子View的布局参数
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();

            Log.e(TAG, childWidth + " " + childHeight + " " + layoutParams.leftMargin + " " + layoutParams.topMargin + " " + layoutParams.rightMargin + " " + layoutParams.bottomMargin);

            if ((lineWidth + layoutParams.leftMargin + childWidth + layoutParams.rightMargin > widthSize) || (i == childCount - 1)) {
                // 需要换行，做换行处理
                maxWidth = Math.max(maxWidth, lineWidth);
                totalHeight += lineHeight;

                // 清除历史数据
                lineWidth = 0;
                lineHeight = 0;
            }

            // 不换行
            lineWidth += layoutParams.leftMargin + childWidth + layoutParams.rightMargin;
            lineHeight = Math.max(lineHeight, layoutParams.topMargin + childHeight + layoutParams.bottomMargin);
        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : maxWidth, heightMode == MeasureSpec.EXACTLY ? heightSize : totalHeight);
    }

    /**
     * 根据计算的宽高，对子空间进行布局
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        int childCount = getChildCount();

        int lineWidth = 0;
        int lineHeight = 0;

        Log.i(TAG, "JQFlowLayout onLayout:" + childCount);

        List<View> lineViews = new ArrayList<>();

        // 计算每一行所包含的View和每一行的高度
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            Log.d(TAG, "layout calculate for..." + childWidth + " " + childHeight);
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();

            if ((lineWidth + childWidth + layoutParams.leftMargin + layoutParams.rightMargin > width) || (i == childCount - 1)) {
                Log.d(TAG, "需要换行:" + i);
                // 需要换行，或做换行处理
                mAllViews.add(lineViews);
                mAllHeights.add(lineHeight);

                // 重置变量
                lineViews = new ArrayList<>();
                lineWidth = 0;
                lineHeight = 0;
            }

            Log.d(TAG, "不换行:" + i);
            // 不换行
            lineWidth += childWidth + layoutParams.leftMargin + layoutParams.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + layoutParams.topMargin + layoutParams.bottomMargin);

            lineViews.add(childView);

        }

        int currentLeft = 0;
        int currentTop = 0;

        // 根据计算结果和子View的left/top/right/bottom，摆放每一个子View的位置
        for (int i = 0; i < mAllViews.size(); i++) {
            Integer currentHeight = mAllHeights.get(i);

            Log.d(TAG, "第" + i + "行，有" + mAllViews.get(i).size() + "个子View");

            for (View view : mAllViews.get(i)) {
                int measuredHeight = view.getMeasuredHeight();
                int measuredWidth = view.getMeasuredWidth();

                Log.d(TAG, "layout for..." + measuredHeight + " " + measuredWidth);

                MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();

                int left = currentLeft + layoutParams.leftMargin;
                int top = currentTop + layoutParams.topMargin;
                int right = left + measuredWidth;
                int bottom = top + measuredHeight;

                Log.i(TAG, "JQFlowLayout onLayout:" + left + " " + top + " " + right + " " + bottom);

                view.layout(left, top, right, bottom);

                currentLeft = right + layoutParams.rightMargin;
            }

            currentLeft = 0;
            currentTop += currentHeight;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

}
