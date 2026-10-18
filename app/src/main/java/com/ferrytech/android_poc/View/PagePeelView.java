package com.ferrytech.android_poc.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PagePeelView extends View {

    private Bitmap currentPage;
    private Bitmap nextPage;

    private float touchX, touchY;
    private boolean isCurling = false;

    private final Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint flapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Path currentPagePath = new Path();
    private final Path curledBackPath = new Path();

    public PagePeelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Required to render shadow layers in Android
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        flapPaint.setColor(Color.WHITE);
        flapPaint.setShadowLayer(20, -5, 5, Color.argb(150, 0, 0, 0));
    }

    // Feed the PDF bitmaps into the view here
    public void setPages(Bitmap current, Bitmap next) {
        this.currentPage = current;
        this.nextPage = next;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isCurling = true;
                touchX = x;
                touchY = y;
                invalidate(); // Triggers onDraw
                return true;

            case MotionEvent.ACTION_UP:
                isCurling = false;
                invalidate(); // Snaps page back flat
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (currentPage == null) return;

        int width = getWidth();
        int height = getHeight();

        // If not touching the screen, just show the flat page
        if (!isCurling) {
            canvas.drawBitmap(currentPage, 0, 0, bitmapPaint);
            return;
        }

        // 1. Draw Next Page in the background
        if (nextPage != null) {
            canvas.drawBitmap(nextPage, 0, 0, bitmapPaint);
        }

        // To simplify 2D math, the fold intercepts the X and Y axes based on touch position
        float foldX = touchX;
        float foldY = touchY;

        // Prevent math bugs if dragged off-screen
        if (foldX < 0) foldX = 0;
        if (foldY < 0) foldY = 0;

        // 2. Define the polygon for the front page (Everything EXCEPT the folded bottom-right corner)
        currentPagePath.reset();
        currentPagePath.moveTo(0, 0);
        currentPagePath.lineTo(width, 0);
        currentPagePath.lineTo(width, foldY); // Right edge fold intercept
        currentPagePath.lineTo(foldX, height); // Bottom edge fold intercept
        currentPagePath.lineTo(0, height);
        currentPagePath.close();

        // 3. Draw the front page clipped to that polygon
        canvas.save();
        canvas.clipPath(currentPagePath);
        canvas.drawBitmap(currentPage, 0, 0, bitmapPaint);
        canvas.restore();

        // 4. Draw the folded-over flap (The back of the paper with a shadow)
        curledBackPath.reset();
        curledBackPath.moveTo(foldX, height); // Bottom intercept
        curledBackPath.lineTo(width, foldY);  // Right intercept
        curledBackPath.lineTo(foldX, foldY);  // The corner being dragged inward
        curledBackPath.close();

        canvas.drawPath(curledBackPath, flapPaint);
    }
}
