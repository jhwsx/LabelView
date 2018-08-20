package com.wzc.labelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class LabelView extends View {

    int mTextColor;
    float mTextSize;
    float mTextHeight;
    float mTextWidth;
    int mTextStyle;
    float mTextBottomPadding;
    float mTextTopPadding;
    float mTextLeftPadding;
    float mTextRightPadding;
    Paint mLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    int mBackGroundColor;
    float mDegrees;
    String mText;
    int width;
    int height;
    private Path mPath = new Path();
    private RectF mRect = new RectF();
    Rect rectText = new Rect();
    private Paint.FontMetrics mFontMetrics;
    private static final String EMPTY_TEXT = " ";
    public static final int DEGREES_RIGHT = 45;
    public static final int DEGREES_LEFT = -45;
    public static final int TEXT_STYLE_NORMAL = 0;
    public static final int TEXT_STYLE_BOLD = 1;

    public LabelView(Context context) {
        this(context, null);
    }

    public LabelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LabelView);

        mTextLeftPadding = ta.getDimension(R.styleable.LabelView_label_text_left_padding, getResources().getDimension(R.dimen.default_padding));
        mTextTopPadding = ta.getDimension(R.styleable.LabelView_label_text_top_padding, getResources().getDimension(R.dimen.default_padding));
        mTextRightPadding = ta.getDimension(R.styleable.LabelView_label_text_right_padding, getResources().getDimension(R.dimen.default_padding));
        mTextBottomPadding = ta.getDimension(R.styleable.LabelView_label_text_bottom_padding, getResources().getDimension(R.dimen.default_padding));
        mBackGroundColor = ta.getColor(R.styleable.LabelView_label_background_color, getResources().getColor(R.color.default_background_color));
        mTextColor = ta.getColor(R.styleable.LabelView_label_text_color, getResources().getColor(R.color.default_text_color));
        mTextSize = ta.getDimension(R.styleable.LabelView_label_text_size, getResources().getDimension(R.dimen.default_text_size));
        mText = ta.getString(R.styleable.LabelView_label_text);
        mTextStyle = ta.getInt(R.styleable.LabelView_label_text_style, TEXT_STYLE_BOLD);
        mDegrees = ta.getInt(R.styleable.LabelView_label_direction, DEGREES_LEFT);

        ta.recycle();

        initTextPaint();
        initLabelPaint();
        resetTextStatus();
    }

    private void initTextPaint() {

        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
        if (mTextStyle == TEXT_STYLE_BOLD) {
            mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            mTextPaint.setTypeface(Typeface.DEFAULT);
        }
    }

    private void initLabelPaint() {
        mLabelPaint.setColor(mBackGroundColor);
    }

    private void resetTextStatus() {

        if (TextUtils.isEmpty(mText)) {
            mText = EMPTY_TEXT;
        }
        mTextPaint.getTextBounds(mText, 0, mText.length(), rectText);
        mTextWidth = rectText.width();
        mTextHeight = rectText.height();
        mFontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = mFontMetrics.bottom - mFontMetrics.top + mTextBottomPadding + mTextTopPadding;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = (int) (mTextWidth + mTextLeftPadding + mTextRightPadding + mTextHeight * 2);

        height = width / 2;

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
        mRect.left = mTextHeight;
        mRect.top = height - mTextHeight;
        mRect.right = mRect.left + mTextWidth + mTextLeftPadding + mTextRightPadding;
        mRect.bottom = height;

        mPath.moveTo(0, height);
        mPath.lineTo(mRect.left, mRect.top);
        mPath.lineTo(mRect.right, mRect.top);
        mPath.lineTo(width, height);
        mPath.close();
        canvas.drawPath(mPath, mLabelPaint);

        float baseline = mRect.centerY() - (mFontMetrics.top + mFontMetrics.bottom) / 2;
        canvas.drawText(mText, mRect.centerX(), baseline, mTextPaint);

    }

    public void setText(String text) {
        mText = text;
        resetTextStatus();
        invalidate();
    }

    public void setText(int resId) {
        mText = getResources().getString(resId);
        setText(mText);
    }

    public void setLabelBackgroundColor(int color) {
        mBackGroundColor = color;
        initLabelPaint();
        invalidate();
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        initTextPaint();
        invalidate();
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
        initTextPaint();
        invalidate();
    }

    public void setTextStyle(int textStyle) {
        mTextStyle = textStyle;
        initTextPaint();
        invalidate();
    }

    public void setTextBottomPadding(float textBottomPadding) {
        mTextBottomPadding = textBottomPadding;
        resetTextStatus();
        invalidate();
    }

    public void setTextTopPadding(float textTopPadding) {
        mTextTopPadding = textTopPadding;
        resetTextStatus();
        invalidate();
    }

    public void setTextLeftPadding(float textLeftPadding) {
        mTextLeftPadding = textLeftPadding;
        resetTextStatus();
        invalidate();
    }

    public void setTextRightPadding(float textRightPadding) {
        mTextRightPadding = textRightPadding;
        resetTextStatus();
        invalidate();
    }

    public void setDegrees(float degrees) {
        mDegrees = degrees;
        invalidate();
    }

}