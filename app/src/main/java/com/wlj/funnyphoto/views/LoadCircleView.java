package com.wlj.funnyphoto.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.wlj.funnyphoto.util.DimenUtils;


/**
 * created by taobangping
 */
public class LoadCircleView extends View {

    private Context mContext;
    private Paint mPaint;
    private int mWidth = 0;
    private int currentLineIndex = 0;

    public LoadCircleView(Context context) {
        this(context, null);
    }

    public LoadCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode != MeasureSpec.AT_MOST && heightSpecMode != MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize >= heightSpecSize ? widthSpecSize : heightSpecSize;
        } else if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode != MeasureSpec.AT_MOST) {
            mWidth = heightSpecSize;
        } else if (widthSpecMode != MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = DimenUtils.dip2px(mContext, 50);
        }
        setMeasuredDimension(mWidth, mWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 圆心坐标
        int center = mWidth >> 1;
        int radius = (mWidth >> 1) - 8;

        if (currentLineIndex >= 12) {
            currentLineIndex = 0;
        }

        // 画线
        for (int i = 0; i < 12; i++) {
            if (i < currentLineIndex + 4 && i >= currentLineIndex) {
                mPaint.setColor(Color.GRAY);
            } else if (currentLineIndex > 8 && i < currentLineIndex + 4 - 12) {
                mPaint.setColor(Color.GRAY);
            } else {
                mPaint.setColor(Color.WHITE);
            }

            canvas.drawLine(center, (float) (center + 1.0 / 2 * radius), center, 2 * radius, mPaint);
            canvas.rotate(30, center, center);
        }
        currentLineIndex++;
        postInvalidateDelayed(50);
    }
}
