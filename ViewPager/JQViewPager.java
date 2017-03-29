package com.jinkun.globalguide.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinkun.globalguide.R;
import com.jinkun.globalguide.domain.JQViewPagerBean;

import java.util.List;

/**
 * Created by coderwjq on 2017/3/29.
 */

public class JQViewPager extends RelativeLayout {

    private ViewPager mViewPager;
    private LinearLayout mPointContainer;
    private TextView mTvTitle;
    private List<JQViewPagerBean> mDatas;
    private ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {
        /**
         * 当ViewPager滚动时的回调方法
         *
         * @param position 当前选中的位置
         * @param positionOffset 滑动的百分比
         * @param positionOffsetPixels 偏移的距离，滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当ViewPager的某个页面被选中时候的回调方法
         *
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            position = position % mDatas.size();

            int childCount = mPointContainer.getChildCount();

            for (int i = 0; i < childCount; i++) {
                View point = mPointContainer.getChildAt(i);

                point.setBackgroundResource(i == position ? R.drawable.point_selected : R.drawable.point_normal);
            }

            mTvTitle.setText(mDatas.get(position).getTitle());
        }

        /**
         * 当ViewPager的滑动状态发生改变时的回调方法
         * SCROLL_STATE_IDLE : 闲置状态
         * SCROLL_STATE_DRAGGING :拖动状态
         * SCROLL_STATE_SETTLING: 固定状态
         *
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            System.out.println(state);
        }
    };

    // 默认ViewPager只加载要显示View左右两边的View，如果不存在则不加载
    // 所以ViewPager默认最多加载3个View
    // 可以通过设置setOffscreenPageLimit，修改要加载View的个数
    private PagerAdapter mAdapter = new PagerAdapter() {

        /**
         * @return 页面的数量
         */
        @Override
        public int getCount() {
            return mDatas == null ? 0 : Integer.MAX_VALUE;
        }

        /**
         * 判断缓存的标记是否是将要显示的View
         * @param view
         * @param object
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 初始化item
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mDatas.size();

            ImageView iv = mDatas.get(position).getImage();
            mViewPager.addView(iv);
            // 记录缓存标记
            return iv;
        }

        /**
         * 销毁item
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            position = position % mDatas.size();

            ImageView iv = mDatas.get(position).getImage();
            mViewPager.removeView(iv);
        }
    };
    private int mStartItem;

    public JQViewPager(Context context) {
        this(context, null);
    }

    public JQViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 将xml和class进行绑定
        initView(context);
        initData();
        initEvent();
    }

    private void initEvent() {
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(mListener);
    }

    public void setDatas(List<JQViewPagerBean> datas) {
        this.mDatas = datas;
        mAdapter.notifyDataSetChanged();

        int middle = Integer.MAX_VALUE / 2;
        mStartItem = middle - middle % mDatas.size();
        mViewPager.setCurrentItem(mStartItem);

        // 根据adapter中的数据设置title和圆点
        for (int i = 0; i < mDatas.size(); i++) {
            View point = new View(getContext());
            point.setBackgroundResource(R.drawable.point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);

            if (i != 0) {
                params.leftMargin = 20;
            } else {
                point.setBackgroundResource(R.drawable.point_selected);

                mTvTitle.setText(mDatas.get(i).getTitle());
            }

            mPointContainer.addView(point, params);
        }

    }

    private void initData() {
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.custom_view_pager, this);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mPointContainer = (LinearLayout) findViewById(R.id.ll_point_container);
    }
}
