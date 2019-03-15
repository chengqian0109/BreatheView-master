package com.jack.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * 呼吸控件
 *
 * @author chengqian
 * Created on 2019-03-11
 */
public class BreatheView extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 控件宽度
     */
    private int mViewWidth;
    /**
     * 控件高度
     */
    private int mViewHeight;

    /**
     * 最内层圆的最小半径
     */
    private float mFirstCircleMinRadius;

    /**
     * 最内层圆的最大半径
     */
    private float mFirstCircleMaxRadius;

    /**
     * 最内层圆颜色
     */
    private int mFirstCircleColor;

    /**
     * 由内向外第二层圆的半径
     */
    private float mSecondCircleRadius;

    /**
     * 由内向外第二层圆的颜色
     */
    private int mSecondCircleColor;

    /**
     * 由内向外第三层圆的半径
     */
    private float mThirdCircleRadius;

    /**
     * 由内向外第三层圆的颜色
     */
    private int mThirdCircleColor;

    /**
     * 呼吸圆最大半径
     */
    private float mBreatheCircleMaxRadius;

    /**
     * 呼吸圆颜色
     */
    private int mBreatheCircleColor;

    /**
     * 动画执行时长
     */
    private int mAnimationDuration;

    /**
     * 放大值动画
     */
    private ValueAnimator mEnlargeValueAnimator;

    /**
     * 缩小值动画
     */
    private ValueAnimator mNarrowAnimator;

    /**
     * 变化因子
     */
    private float mFraction;

    public BreatheView(Context context) {
        super(context);
        init();
    }

    public BreatheView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BreatheView);
        mFirstCircleMinRadius = typedArray.getDimension(R.styleable.BreatheView_first_circle_min_radius, dp2px(2));
        mFirstCircleMaxRadius = typedArray.getDimension(R.styleable.BreatheView_first_circle_max_radius, dp2px(4));
        mSecondCircleRadius = typedArray.getDimension(R.styleable.BreatheView_second_circle_radius, dp2px(6));
        mThirdCircleRadius = typedArray.getDimension(R.styleable.BreatheView_third_circle_radius, dp2px(9));
        mBreatheCircleMaxRadius = typedArray.getDimension(R.styleable.BreatheView_breathe_circle_max_radius, dp2px(30));
        mFirstCircleColor = typedArray.getColor(R.styleable.BreatheView_first_circle_color, Color.RED);
        mSecondCircleColor = typedArray.getColor(R.styleable.BreatheView_second_circle_color, Color.WHITE);
        mThirdCircleColor = typedArray.getColor(R.styleable.BreatheView_third_circle_color, Color.BLACK);
        mBreatheCircleColor = typedArray.getColor(R.styleable.BreatheView_breathe_circle_color,
                Color.parseColor("#80333333"));
        mAnimationDuration = typedArray.getInt(R.styleable.BreatheView_exhale_inhale_duration, 500);
        typedArray.recycle();
        init();
        start();
    }

    /**
     * 启动动画
     */
    public void start() {
        analyzeValue();
        mEnlargeValueAnimator.start();
    }

    /**
     * 初始化
     */
    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        // 放大动画
        mEnlargeValueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(mAnimationDuration);
        mEnlargeValueAnimator.addListener(this);
        mEnlargeValueAnimator.addUpdateListener(this);
        // 缩小动画
        mNarrowAnimator = ValueAnimator.ofFloat(1, 0).setDuration(mAnimationDuration);
        mNarrowAnimator.addListener(this);
        mNarrowAnimator.addUpdateListener(this);
        // 默认值配置
        if (mFirstCircleMinRadius == 0) {
            mFirstCircleMinRadius = dp2px(2);
        }
        if (mFirstCircleMaxRadius == 0) {
            mFirstCircleMinRadius = dp2px(4);
        }
        if (mSecondCircleRadius == 0) {
            mSecondCircleRadius = dp2px(6);
        }
        if (mThirdCircleRadius == 0) {
            mThirdCircleRadius = dp2px(9);
        }
        if (mBreatheCircleMaxRadius == 0) {
            mBreatheCircleMaxRadius = dp2px(30);
        }
        if (mFirstCircleColor == 0) {
            mFirstCircleColor = Color.RED;
        }
        if (mSecondCircleColor == 0) {
            mSecondCircleColor = Color.WHITE;
        }
        if (mThirdCircleColor == 0) {
            mThirdCircleColor = Color.BLACK;
        }
        if (mBreatheCircleColor == 0) {
            mBreatheCircleColor = Color.parseColor("#80333333");
        }
        if (mAnimationDuration == 0) {
            mAnimationDuration = 500;
        }
    }

    /**
     * 分析数值有效性
     */
    private void analyzeValue() {
        if (mFirstCircleMinRadius > mFirstCircleMaxRadius) {
            throw new IllegalArgumentException("最内侧圆最小半径比其最大半径大");
        }
        if (mFirstCircleMaxRadius >= mSecondCircleRadius ||
                mFirstCircleMaxRadius >= mThirdCircleRadius ||
                mFirstCircleMaxRadius >= mBreatheCircleMaxRadius) {
            throw new IllegalArgumentException("最内侧圆最大半径比外层圆半径大");
        }
        if (mSecondCircleRadius >= mThirdCircleRadius ||
                mSecondCircleRadius >= mBreatheCircleMaxRadius) {
            throw new IllegalArgumentException("最内侧第二圆半径比外层圆半径大");
        }
        if (mThirdCircleRadius >= mBreatheCircleMaxRadius) {
            throw new IllegalArgumentException("最内侧第三圆半径比呼吸圆半径大");
        }
        if (Color.alpha(mBreatheCircleColor) == 1.0f) {
            // 呼吸圆半径不是半透明效果
            throw new IllegalArgumentException("呼吸圆颜色必须是半透明的");
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mViewWidth / 2f, mViewHeight / 2f);

        // 由内向外第三层圆
        mPaint.setColor(mThirdCircleColor);
        canvas.drawCircle(0, 0, mThirdCircleRadius, mPaint);

        // 由内向外第二层圆
        mPaint.setColor(mSecondCircleColor);
        canvas.drawCircle(0, 0, mSecondCircleRadius, mPaint);

        // 呼吸圆
        mPaint.setColor(mBreatheCircleColor);
        canvas.drawCircle(0, 0, mBreatheCircleMaxRadius * mFraction, mPaint);

        // 最内层圆
        mPaint.setColor(mFirstCircleColor);
        canvas.drawCircle(0, 0, mFirstCircleMinRadius +
                (mFirstCircleMaxRadius - mFirstCircleMinRadius) * mFraction, mPaint);

        invalidate();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mFraction = (float) animation.getAnimatedValue();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            invalidate();
        }
    }

    @Override
    public void invalidate() {
        if (hasWindowFocus()) {
            super.invalidate();
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (animation == mEnlargeValueAnimator) {
            mNarrowAnimator.start();
        } else if (animation == mNarrowAnimator) {
            mEnlargeValueAnimator.start();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    /**
     * 设置呼或吸的动画时长
     *
     * @param milliSeconds 时长毫秒值
     */
    public BreatheView setAnimationDuration(int milliSeconds) {
        mAnimationDuration = milliSeconds;
        mEnlargeValueAnimator.setDuration(mAnimationDuration);
        mNarrowAnimator.setDuration(mAnimationDuration);
        return this;
    }

    /**
     * 设置最内层圆的半径
     *
     * @param minRadius 最内层圆的半径
     */
    public BreatheView setFirstCircleMinRadius(float minRadius) {
        mFirstCircleMinRadius = minRadius;
        return this;
    }

    /**
     * 设置最内层圆的半径
     *
     * @param minRadiusId 最内层圆半径资源id
     */
    public BreatheView setFirstCircleMinRadiusRes(@DimenRes int minRadiusId) {
        mFirstCircleMinRadius = getResources().getDimension(minRadiusId);
        return this;
    }

    /**
     * 设置最内侧圆最大半径
     *
     * @param maxRadius 最内侧圆最大半径值
     */
    public BreatheView setFirstCircleMaxRadius(float maxRadius) {
        mFirstCircleMaxRadius = maxRadius;
        return this;
    }

    /**
     * 设置最内侧圆最大半径
     *
     * @param maxRadiusId 最内侧圆最大半径值
     */
    public BreatheView setFirstCircleMaxRadiusRes(@DimenRes int maxRadiusId) {
        mFirstCircleMaxRadius = getResources().getDimension(maxRadiusId);
        return this;
    }

    /**
     * 设置最内层圆颜色
     *
     * @param color 最内层圆颜色值
     */
    public BreatheView setFirstCircleColor(@ColorInt int color) {
        mFirstCircleColor = color;
        return this;
    }

    /**
     * 设置最内层圆颜色
     *
     * @param colorId 最内层圆颜色值资源id
     */
    public BreatheView setFirstCircleColorRes(@ColorRes int colorId) {
        mFirstCircleColor = ContextCompat.getColor(getContext(), colorId);
        return this;
    }

    /**
     * 设置由内向外第二层圆的半径
     *
     * @param radius 由内向外第二层圆的半径
     */
    public BreatheView setSecondCircleRadius(float radius) {
        mSecondCircleRadius = radius;
        return this;
    }

    /**
     * 设置由内向外第二层圆的半径
     *
     * @param radiusId 由内向外第二层圆的半径资源id
     */
    public BreatheView setSecondCircleRadiusRes(@DimenRes int radiusId) {
        mSecondCircleRadius = getResources().getDimension(radiusId);
        return this;
    }

    /**
     * 设置由内向外第二层圆的颜色
     *
     * @param color 由内向外第二层圆的颜色值
     */
    public BreatheView setSecondCircleColor(@ColorInt int color) {
        mSecondCircleColor = color;
        return this;
    }

    /**
     * 设置由内向外第二层圆的颜色
     *
     * @param colorId 由内向外第二层圆的颜色值资源id
     */
    public BreatheView setSecondCircleColorRes(@ColorRes int colorId) {
        mSecondCircleColor = ContextCompat.getColor(getContext(), colorId);
        return this;
    }

    /**
     * 设置由内向外第三层圆的半径
     *
     * @param radius 由内向外第三层圆的半径
     */
    public BreatheView setThirdCircleRadius(float radius) {
        mThirdCircleRadius = radius;
        return this;
    }

    /**
     * 设置由内向外第三层圆的半径
     *
     * @param radiusId 由内向外第三层圆的半径资源id
     */
    public BreatheView setThirdCircleRadiusRes(@DimenRes int radiusId) {
        mThirdCircleRadius = getResources().getDimension(radiusId);
        return this;
    }

    /**
     * 设置由内向外第三层圆的颜色
     *
     * @param color 由内向外第三层圆的颜色值
     */
    public BreatheView setThirdCircleColor(@ColorInt int color) {
        mThirdCircleColor = color;
        return this;
    }

    /**
     * 设置由内向外第三层圆的颜色
     *
     * @param colorId 由内向外第三层圆的颜色值资源id
     */
    public BreatheView setThirdCircleColorRes(@ColorRes int colorId) {
        mThirdCircleColor = ContextCompat.getColor(getContext(), colorId);
        return this;
    }

    /**
     * 设置呼吸圆的最大半径
     *
     * @param maxRadius 呼吸圆的最大半径
     */
    public BreatheView setBreatheCircleMaxRadius(float maxRadius) {
        mBreatheCircleMaxRadius = maxRadius;
        return this;
    }

    /**
     * 设置呼吸圆的最大半径
     *
     * @param maxRadiusId 呼吸圆的最大半径资源id
     */
    public BreatheView setBreatheCircleMaxRadiusRes(@DimenRes int maxRadiusId) {
        mBreatheCircleMaxRadius = getResources().getDimension(maxRadiusId);
        return this;
    }

    /**
     * 设置呼吸圆的颜色
     *
     * @param color 呼吸圆的颜色值
     */
    public BreatheView setBreatheCircleColor(@ColorInt int color) {
        mBreatheCircleColor = color;
        return this;
    }

    /**
     * 设置呼吸圆的颜色
     *
     * @param colorId 呼吸圆的颜色资源id
     */
    public BreatheView setBreatheCircleColorRes(@ColorRes int colorId) {
        mBreatheCircleColor = ContextCompat.getColor(getContext(), colorId);
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int wSize;
        int hSize;
        int max;
        if (wMode == MeasureSpec.AT_MOST && hMode == MeasureSpec.AT_MOST) {
            // 控件宽高均为wrap_content
            wSize = (int) Math.ceil(2 * mBreatheCircleMaxRadius);
            hSize = (int) Math.ceil(2 * mBreatheCircleMaxRadius);
        } else {
            if (wMode != MeasureSpec.AT_MOST) {
                // 可以认为 wMode = MeasureSpec.EXACTLY
                if (hMode == MeasureSpec.AT_MOST) {
                    // 控件高度为wrap_content
                    // 那么设置控件宽高为可能的最大值
                    max = Math.max((int) Math.ceil(2 * mBreatheCircleMaxRadius), MeasureSpec.getSize(widthMeasureSpec));
                } else {
                    // 可以认为 hMode = MeasureSpec.EXACTLY
                    int partMax = Math.max((int) Math.ceil(2 * mBreatheCircleMaxRadius), MeasureSpec.getSize(widthMeasureSpec));
                    max = Math.max(partMax, MeasureSpec.getSize(heightMeasureSpec));
                }
            } else {
                // 控件宽度为wrap_content
                // 这种情况下可以认为hMode = MeasureSpec.EXACTLY
                max = Math.max((int) Math.ceil(2 * mBreatheCircleMaxRadius), MeasureSpec.getSize(heightMeasureSpec));
            }
            wSize = max;
            hSize = max;
        }

        wMode = MeasureSpec.EXACTLY;
        hMode = MeasureSpec.EXACTLY;
        super.setMeasuredDimension(MeasureSpec.makeMeasureSpec(wSize, wMode), MeasureSpec.makeMeasureSpec(hSize, hMode));
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dp2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
