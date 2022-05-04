package com.emproto.core.dottedline;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.emproto.core.R;

public class Dottedline extends View {

    private Paint mPaint;

    public Dottedline(Context context) {
        super(context);
        init();
    }

    public Dottedline(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Dottedline(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Dottedline(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        Resources res = getResources();
        mPaint = new Paint();

        mPaint.setColor(res.getColor(R.color.app_color));
        int size = res.getDimensionPixelSize(R.dimen.margin_size_2);
        int gap = res.getDimensionPixelSize(R.dimen.margin_size_10);
        mPaint.setStyle(Paint.Style.STROKE);

        // To get actually round dots, we define a circle...
        Path path = new Path();
        path.addCircle(0, 10, size, Path.Direction.CCW);
        // ...and use the path with the circle as our path effect
        mPaint.setPathEffect(new PathDashPathEffect(path, gap, 0, PathDashPathEffect.Style.MORPH));

        // If we don't render in software mode, the dotted line becomes a solid line.
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), mPaint);
    }
}
