package com.witlife.p2pinvest.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.witlife.p2pinvest.R;
import com.witlife.p2pinvest.util.UIUtils;

/**
 * Created by bruce on 23/08/2017.
 */

public class RoundProgress extends View {

    private int roundColor;
    private int roundProgressColor;
    private int textColor;

    private float roundWidth;
    private float textSize;

    private int max;
    private int progress;

    private int width;

    private Paint paint;

    public RoundProgress(Context context) {
        this(context, null);
    }

    public RoundProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint();
        paint.setAntiAlias(true);// remove

        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgress);
        roundColor = typeArray.getColor(R.styleable.RoundProgress_roundColor, Color.GRAY);
        roundProgressColor = typeArray.getColor(R.styleable.RoundProgress_roundProgressColor, Color.RED);
        textColor = typeArray.getColor(R.styleable.RoundProgress_textColor, Color.BLUE);
        roundWidth = typeArray.getDimension(R.styleable.RoundProgress_roundWidth, UIUtils.dp2px(10));
        textSize = typeArray.getDimension(R.styleable.RoundProgress_textSize, UIUtils.dp2px(20));
        max = typeArray.getInteger(R.styleable.RoundProgress_max, 100);
        progress = typeArray.getInteger(R.styleable.RoundProgress_progress, 30);

        typeArray.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = this.getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw a circle
        int cx = width /2;
        int cy = width / 2;
        float radius = width / 2 - roundWidth / 2;
        paint.setColor(roundColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(roundWidth);
        canvas.drawCircle(cx, cy, radius, paint);

        //draw arc
        RectF rectF = new RectF(roundWidth/2, roundWidth/2, width - roundWidth/2, width - roundWidth/2);
        paint.setColor(roundProgressColor);
        canvas.drawArc(rectF, 0, progress*360/max, false, paint);

        //draw text
        String text = progress * 100 / max + "%";

        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setStrokeWidth(0);

        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        int x = width / 2 - rect.width()/ 2;
        int y = width / 2 + rect.height() / 2;

        canvas.drawText(text, x, y, paint);
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
