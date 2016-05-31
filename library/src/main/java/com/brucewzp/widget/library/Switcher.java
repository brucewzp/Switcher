package com.brucewzp.widget.library;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;

/**
 * Created on:4/11/16
 * Email:brucewzp@gmail.com
 **/
public class Switcher extends CompoundButton {
    private ColorStateList mBgColor;
    private ColorStateList mFloatColor;
    private ObjectAnimator mAnimator;
    private Paint mPaint;
    private Paint mRectPaint;
    private float mProcess;
    private RectF offset;
    private float w;
    private boolean mState = false;
    private OnStateChangedListener mStateChangedListener;

    public Switcher(Context context) {
        super(context);
        init(null);
    }

    public Switcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Switcher(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.Switcher);
        if (ta != null) {
            mBgColor = ta.getColorStateList(R.styleable.Switcher_bgColor);
            mFloatColor = ta.getColorStateList(R.styleable.Switcher_floatColor);
            ta.recycle();
        }
        if (mBgColor == null) {
            mBgColor = new ColorStateList(
                    new int[][]{new int[]{0}},
                    new int[]{Color.argb(255, 207, 206, 214)});
        }
        if (mFloatColor == null) {
            mFloatColor = new ColorStateList(
                    new int[][]{new int[]{0}},
                    new int[]{Color.argb(255, 73, 67, 109)});
        }
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mState) {
                    mState = false;
                } else {
                    mState = true;
                }
                doAnimation();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectPaint.setColor(mBgColor.getDefaultColor());
        canvas.drawRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), 12, 12, mRectPaint);
        if (mState) {
            mPaint.setColor(mFloatColor.getDefaultColor());
        } else {
            mPaint.setARGB(255, 225, 228, 236);
        }
        canvas.drawRoundRect(offset, 12, 12, mPaint);
    }

    private void doAnimation() {
        if (mState) {
            mAnimator = ObjectAnimator.ofFloat(this, "process", 0, w / 3 * 2 - 10);
        } else {
            mAnimator = ObjectAnimator.ofFloat(this, "process", w / 3 * 2 - 10, 0);
        }
        mAnimator.setDuration(250);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Switcher.this.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Switcher.this.setEnabled(true);
                if (mState) {
                    mStateChangedListener.onStateTrue();
                } else {
                    mStateChangedListener.onStateFalse();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Switcher.this.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.start();
    }

    public final void setProcess(float p) {
        mProcess = p;
        offset = new RectF(10 + mProcess, getMeasuredHeight() / 6, getMeasuredWidth() / 3 + mProcess, getMeasuredHeight() / 6 * 5);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        w = getMeasuredWidth();
        offset = new RectF(10, getMeasuredHeight() / 6, getMeasuredWidth() / 3, getMeasuredHeight() / 6 * 5);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public boolean getState() {
        return mState;
    }

    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener){
        mStateChangedListener = onStateChangedListener;
    }

    public interface OnStateChangedListener{
        void onStateTrue();
        void onStateFalse();
    }
}
