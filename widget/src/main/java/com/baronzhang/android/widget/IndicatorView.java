
package com.baronzhang.android.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author baronzhang (baron[dot]zhanglei[at]gmail[dot]com)
 *         16/12/11
 */
public class IndicatorView extends LinearLayout {

    private Context context;
    private Paint paint;
    private int markerId;
    private Bitmap marker = null;

    private int indicatorValue = 0;// 기본 AQI 값
    private int textSize = 6;// 기본 텍스트 크기
    private int intervalValue = 1;// TextView, unit dp 사이의 간격 크기
    private int textColorId = R.color.indicator_text_color;// 기본 텍스트 색상
    private int textColor;
    private int indicatorStringsResourceId = R.array.indicator_strings;
    private int indicatorColorsResourceId = R.array.indicator_colors;

    private int indicatorViewWidth;// IndicatorView 너비

    private int paddingTopInXML;

    private String[] indicatorStrings;
    int[] indicatorColorIds;

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 제어 초기화, 생성자 호출
     */
    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        this.setOrientation(LinearLayout.HORIZONTAL);
        //드로잉 캐시를 열어 드로잉 효율을 향상시킴
        this.setDrawingCacheEnabled(true);

        initPaint();
        initAttrs(attrs);
        fillViewToParent(context);

        this.setWillNotDraw(false);// onDraw()가 호출되었는지 확인

        this.paddingTopInXML = this.getPaddingTop();
        this.setPadding(this.getPaddingLeft() + this.marker.getWidth() / 2,
                this.getPaddingTop() + this.marker.getHeight(),
                this.getPaddingRight() + this.marker.getWidth() / 2,
                this.getPaddingBottom());
    }

    /**
     * paint 초기화
     */
    private void initPaint() {
        this.paint = new Paint();
        // 앤티 앨리어싱 기능을 사용할지 설정, 많은 리소스 소모, 그래픽 느려짐。
        this.paint.setAntiAlias(true);
        // 이미지 지터 처리를 사용할지 설정하면 그림을 부드럽고 꽉 차게 만들고 더 선명해짐
        this.paint.setDither(true);
    }

    /**
     * 맞춤attrs 가져오기
     */
    private void initAttrs(AttributeSet attrs) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        this.textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, dm);
        this.intervalValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, intervalValue, dm);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        this.markerId = typedArray.getResourceId(R.styleable.IndicatorView_marker, R.drawable.ic_vector_indicator_down);
        this.marker = drawableToBitmap(createVectorDrawable(markerId, R.color.indicator_color_1));
        this.indicatorValue = typedArray.getInt(R.styleable.IndicatorView_indicatorValue, indicatorValue);
        this.textSize = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_textSize, textSize);
        this.intervalValue = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_intervalSize, intervalValue);
        this.textColor = typedArray.getColor(R.styleable.IndicatorView_textColor, getResources().getColor(textColorId));
        this.indicatorStringsResourceId = typedArray.getInt(R.styleable.IndicatorView_indicatorStrings, indicatorStringsResourceId);
        this.indicatorColorsResourceId = typedArray.getInt(R.styleable.IndicatorView_indicatorColors, indicatorColorsResourceId);
        typedArray.recycle();
    }

    /**
     * 부모 컨테이너에 뷰 채우기
     */
    private void fillViewToParent(Context context) {
        indicatorStrings = context.getResources().getStringArray(indicatorStringsResourceId);
        indicatorColorIds = context.getResources().getIntArray(indicatorColorsResourceId);
        if (indicatorStrings.length != indicatorColorIds.length) {
            throw new IllegalArgumentException("qualities,aqiColors이 배열의 길이와 다름！");
        }
        for (int i = 0; i < indicatorStrings.length; i++) {
            addTextView(context, indicatorStrings[i], indicatorColorIds[i]);
            if (i != (indicatorStrings.length - 1)) {
                addBlankView(context);
            }
        }
    }

    /**
     * 상위 컨테이너에 TextView 추가
     *
     * @param text  TextView는 텍스트를 표시
     * @param color TextView 배경색 : "#FADBCC"
     */
    private void addTextView(Context context, String text, int color) {
        TextView textView = new TextView(context);
        textView.setBackgroundColor(color);
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        textView.setSingleLine();
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0F));
        this.addView(textView);
    }

    /**
     * 상위 컨테이너에 빈보기 추가
     */
    private void addBlankView(Context context) {
        View transparentView = new View(context);
        transparentView.setBackgroundColor(Color.TRANSPARENT);
        transparentView.setLayoutParams(new LayoutParams(intervalValue, LayoutParams.WRAP_CONTENT));
        this.addView(transparentView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        this.indicatorViewWidth = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int indicatorViewHeight = MeasureSpec.getSize(heightMeasureSpec);

        int desiredWidth = indicatorViewWidth + getPaddingLeft() + getPaddingRight();
        int desiredHeight = this.getChildAt(0).getMeasuredHeight() + getPaddingTop() + getPaddingBottom();

        //측정 폭
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                indicatorViewWidth = Math.min(desiredWidth, indicatorViewWidth);
                break;
            case MeasureSpec.UNSPECIFIED:
                indicatorViewWidth = desiredWidth;
                break;
        }

        //높이 측정
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
                indicatorViewHeight = Math.min(desiredHeight, indicatorViewHeight);
                break;
            case MeasureSpec.UNSPECIFIED:
                indicatorViewHeight = desiredHeight;
                break;
        }
        setMeasuredDimension(indicatorViewWidth, indicatorViewHeight);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawMarkView(canvas);
    }

    /**
     * 아이콘 그리는데 사용
     */
    private void drawMarkView(Canvas canvas) {

        int width = this.indicatorViewWidth - this.getPaddingLeft() - this.getPaddingRight() - intervalValue * 5;

        int left = this.getPaddingLeft();
        if (indicatorValue <= 50) {
            left += indicatorValue * (width * 4 / 6 / 200);
        } else if (indicatorValue > 50 && indicatorValue <= 100) {
            left += indicatorValue * (width * 4 / 6 / 200) + intervalValue;
        } else if (indicatorValue > 100 && indicatorValue <= 150) {
            left += indicatorValue * (width * 4 / 6 / 200) + intervalValue * 2;
        } else if (indicatorValue > 150 && indicatorValue <= 200) {
            left += indicatorValue * (width * 4 / 6 / 200) + intervalValue * 3;
        } else if (indicatorValue > 200 && indicatorValue <= 300) {
            left += (width * 4 / 6) + (indicatorValue - 200) * width / 6 / 100 + intervalValue * 4;
        } else {
            left += (width * 5 / 6) + (indicatorValue - 300) * width / 6 / 200 + intervalValue * 5;
        }
        canvas.drawBitmap(marker, left - marker.getWidth() / 2 - 2, this.paddingTopInXML, paint);
    }

    private IndicatorValueChangeListener indicatorValueChangeListener;

    public void setIndicatorValueChangeListener(IndicatorValueChangeListener indicatorValueChangeListener) {
        this.indicatorValueChangeListener = indicatorValueChangeListener;
    }

    public void setIndicatorValue(int indicatorValue) {

        if (indicatorValue < 0)
            throw new IllegalStateException("매개변수indicatorValue는 0보다 커야합니다.");

        this.indicatorValue = indicatorValue;
        if (indicatorValueChangeListener != null) {
            String stateDescription;
            int indicatorTextColor;
            if (indicatorValue <= 50) {
                stateDescription = indicatorStrings[0];
                indicatorTextColor = indicatorColorIds[0];
            } else if (indicatorValue > 50 && indicatorValue <= 100) {
                stateDescription = indicatorStrings[1];
                indicatorTextColor = indicatorColorIds[1];
            } else if (indicatorValue > 100 && indicatorValue <= 150) {
                stateDescription = indicatorStrings[2];
                indicatorTextColor = indicatorColorIds[2];
            } else if (indicatorValue > 150 && indicatorValue <= 200) {
                stateDescription = indicatorStrings[3];
                indicatorTextColor = indicatorColorIds[3];
            } else if (indicatorValue > 200 && indicatorValue <= 300) {
                stateDescription = indicatorStrings[4];
                indicatorTextColor = indicatorColorIds[4];
            } else {
                stateDescription = indicatorStrings[5];
                indicatorTextColor = indicatorColorIds[5];
            }
            marker.recycle();
            marker = drawableToBitmap(createVectorDrawable(markerId, indicatorTextColor));
            indicatorValueChangeListener.onChange(this.indicatorValue, stateDescription, indicatorTextColor);
        }
        invalidate();
    }

    private Drawable createVectorDrawable(int drawableId, int color) {

        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(getResources(), drawableId, context.getTheme());
        assert vectorDrawableCompat != null;
        DrawableCompat.setTint(vectorDrawableCompat, color);
        DrawableCompat.setTintMode(vectorDrawableCompat, PorterDuff.Mode.SRC_IN);

        return vectorDrawableCompat;
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}