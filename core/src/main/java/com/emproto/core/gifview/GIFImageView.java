package com.emproto.core.gifview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.emproto.core.R;


/**
 * GIFImageView will allow only AnimatedImageDrawable file
 * If you want to show normal image use ImageView
 */
public class GIFImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Context context;

    private Drawable gifDrawable;
    private Movie movie;
    private int movieMillie;

    public GIFImageView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public GIFImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public GIFImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init() {
        movieMillie = 0;
    }

    /**
     * @param attributeSet -  all the details sent through XML
     */
    private void init(AttributeSet attributeSet) {
        movieMillie = 0;
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.GIFImageView, 0, 0);
        int bitmapId = attributes.getResourceId(R.styleable.GIFImageView_gifSrc, 0);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                gifDrawable = attributes.getDrawable(R.styleable.GIFImageView_gifSrc);
            }else{
                movie = Movie.decodeStream(getResources().openRawResource(bitmapId));
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                canvas.scale((float) getWidth() / gifDrawable.getIntrinsicWidth(), (float) getHeight() / gifDrawable.getIntrinsicHeight());
            } else {
                canvas.scale((float) getWidth() / movie.width(), (float) getHeight() / movie.height());
                movieMillie += 25;
            }
            startGif(canvas);
        } catch (Exception ignored) {
        }
        invalidate();
    }

    private void startGif(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            gifDrawable.draw(canvas);
            ((AnimatedImageDrawable) gifDrawable).start();
        } else {
            movie.setTime(movieMillie);
            if (movieMillie > movie.duration()) {
                movieMillie = 0;
            }
            movie.draw(canvas, 0, 0);
        }
    }

    public Drawable getGifDrawable() {
        return gifDrawable;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setGifDrawable(int gifDrawable) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                this.gifDrawable = getResources().getDrawable(gifDrawable, null);
            }else{
                movie = Movie.decodeStream(getResources().openRawResource(gifDrawable));
            }
        } catch (Exception ignored) {
        }
        postInvalidate();
    }

}
