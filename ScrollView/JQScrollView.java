package com.jinkun.globalguide.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;


/**
 * Created by coderwjq on 2017/3/21.
 */

public class JQScrollView extends ScrollView {

    private static final String TAG = JQScrollView.class.getName();

    private View mChildView;
    private float mStartY;

    private Rect mNormalRect = new Rect();
    private boolean mAnimationFinished = true;

    public JQScrollView(Context context) {
        super(context);
    }

    public JQScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JQScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 当inflate完成布局以后调用此方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();

        if (childCount > 0) {
            mChildView = getChildAt(0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mChildView != null) {
            handleTouchEvent(ev);
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 处理自定义ScrollView的touch事件
     *
     * @param ev
     */
    private void handleTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = ev.getY();
                int deltaY = (int) (currentY - mStartY);
                mStartY = currentY;

                if (isNeedScroll()) {
                    if (mNormalRect.isEmpty()) {
                        // 记录起始状态的位置
                        mNormalRect.set(mChildView.getLeft(), mChildView.getTop(), mChildView.getRight(), mChildView.getBottom());
                    }

                    mChildView.layout(mChildView.getLeft(), mChildView.getTop() + deltaY / 2, mChildView.getRight(), mChildView.getBottom() + deltaY / 2);
                }
                break;
            case MotionEvent.ACTION_UP:

                // 布局回滚到初始位置
                if (!mNormalRect.isEmpty()) {
                    animateToNormal();
                }
                break;
        }
    }

    /**
     * 动画回滚至初始状态
     */
    private void animateToNormal() {
        TranslateAnimation ta = new TranslateAnimation(0, 0, 0, mNormalRect.top - mChildView.getTop());
        ta.setDuration(200);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mAnimationFinished = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimationFinished = true;

                mChildView.layout(mNormalRect.left, mNormalRect.top, mNormalRect.right, mNormalRect.bottom);
                mChildView.clearAnimation();
                mNormalRect.setEmpty();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mChildView.setAnimation(ta);
    }

    private boolean isNeedScroll() {
        // 当ScrollView滚动至头部时，scrollY始终为 0
        // 当ScrollView滚动至底部时，scrollY始终为 mChildView.getMeasuredHeight() - getHeight()
        int scrollY = getScrollY();
        int offset = mChildView.getMeasuredHeight() - getHeight();

        if (scrollY == 0 || scrollY == offset) {
            return true;
        } else {
            return false;
        }
    }
}
