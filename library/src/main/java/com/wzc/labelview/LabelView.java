package com.wzc.labelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class LabelView extends View {

    Paint mTextPaint;
    int mTextColor;
    float mTextSize;
    float mTextHeight;
    float mTextWidth;
    int mTextStyle;
    float mTopMargin;
    float mBottomPadding;
    float mTopPadding;
    Paint mTrapezoidPaint;
    int mBackGroundColor;
    float mDegrees;
    String mText;
    int width;
    int height;

    public static final int DEGREES_LEFT = -45;
    public static final int DEGREES_RIGHT = 45;
    private Path mPath;


    public LabelView(Context context) {
        this(context, null);
    }

    public LabelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelView);

        mTopMargin = ta.getDimension(R.styleable.LabelView_label_top_margin, dp2px(7));

        mTopPadding = ta.getDimension(R.styleable.LabelView_label_top_padding, dp2px(3));

        mBottomPadding = ta.getDimension(R.styleable.LabelView_label_bottom_padding, dp2px(3));

        mBackGroundColor = ta.getColor(R.styleable.LabelView_label_background_color, Color.parseColor("#66000000"));

        mTextColor = ta.getColor(R.styleable.LabelView_label_text_color, Color.WHITE);

        mTextSize = ta.getDimension(R.styleable.LabelView_label_text_size, sp2px(11));

        mText = ta.getString(R.styleable.LabelView_label_text);

        mTextStyle = ta.getInt(R.styleable.LabelView_label_text_style, TEXT_STYLE_BOLD);

        mDegrees = ta.getInt(R.styleable.LabelView_label_direction, 45);

        ta.recycle();

        initTextPaint();
        initTrapezoidPaint();
        mPath = new Path();
        resetTextStatus();

    }


    public void setText(String text) {
        mText = text;
        resetTextStatus();
        invalidate();
    }


    public void setBackGroundColor(int color) {
        mTrapezoidPaint.setColor(color);
        invalidate();
    }

    private static final int TEXT_STYLE_NORMAL = 0;
    private static final int TEXT_STYLE_ITALIC = 1;
    private static final int TEXT_STYLE_BOLD = 2;
    private void initTextPaint() {

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
        if (mTextStyle == TEXT_STYLE_ITALIC) {
            mTextPaint.setTypeface(Typeface.SANS_SERIF);
        } else if (mTextStyle == TEXT_STYLE_BOLD) {
            mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    private void initTrapezoidPaint() {

        mTrapezoidPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrapezoidPaint.setColor(mBackGroundColor);
    }

    private void resetTextStatus() {

        Rect rectText = new Rect();
        if (TextUtils.isEmpty(mText)) {
            throw new IllegalArgumentException("mText cannot be empty");
        }
        mTextPaint.getTextBounds(mText, 0, mText.length(), rectText);
        mTextWidth = rectText.width();
        mTextHeight = rectText.height();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = (int) (mTopMargin + mTopPadding + mBottomPadding + mTextHeight);
        width = 2 * height;


        int realHeight = (int) (height * Math.sqrt(2));
        setMeasuredDimension(width, realHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.translate(0, (float) ((height * Math.sqrt(2)) - height));
        if (mDegrees == DEGREES_LEFT) {
            canvas.rotate(mDegrees, 0, height);
        } else if (mDegrees == DEGREES_RIGHT) {
            canvas.rotate(mDegrees, width, height);
        }


        mPath.moveTo(0, height);
        mPath.lineTo(width / 2 - mTopMargin, mTopMargin);
        mPath.lineTo(width / 2 + mTopMargin, mTopMargin);
        mPath.lineTo(width, height);
        mPath.close();

        canvas.drawPath(mPath, mTrapezoidPaint);

        canvas.drawText(mText, width / 2, mTopMargin + mTextHeight + mTopPadding, mTextPaint);
    }



    public int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public float sp2px(float spValue) {
        final float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return spValue * scale;
    }
}