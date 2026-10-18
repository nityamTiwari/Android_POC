package com.ferrytech.android_poc.pageflip.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;

import com.ferrytech.android_poc.pageflip.core.OnPageFlipListener;
import com.ferrytech.android_poc.pageflip.core.PageFlip;
import com.ferrytech.android_poc.pageflip.core.Page;

public abstract class PageRender implements OnPageFlipListener {

    public final static int MSG_ENDED_DRAWING_FRAME = 1;

    final static int DRAW_MOVING_FRAME = 0;
    final static int DRAW_ANIMATING_FRAME = 1
            ;
    final static int DRAW_FULL_PAGE = 2;

    protected Context mContext;
    protected PageFlip mPageFlip;
    protected Handler mHandler;
    protected int mPageNo;
    protected RenderCallback renderCallback;

    protected int mDrawCommand;
    protected Canvas mCanvas;
    protected Bitmap mBackgroundBitmap;
    protected Bitmap mBitmap;

    public PageRender(Context context, PageFlip pageFlip, Handler handler, int pageNo, RenderCallback renderCallback) {
        this.mContext = context;
        this.mPageFlip = pageFlip;
        this.mHandler = handler;
        this.mPageNo = pageNo;
        this.renderCallback = renderCallback;
        mDrawCommand = DRAW_FULL_PAGE;
        mCanvas = new Canvas();
        mPageFlip.setListener(this);
    }

    public int getPageNo() {
        return mPageNo;
    }

    public void release() {
        recycleBitmap(mBitmap);
        recycleBitmap(mBackgroundBitmap);

        mPageFlip.setListener(null);
        mCanvas = null;
    }

    protected void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        bitmap = null;
    }

    public boolean onFingerMove(float x, float y) {
        mDrawCommand = DRAW_MOVING_FRAME;
        return true;
    }

    public boolean onFingerUp(float x, float y) {
        if (mPageFlip.animating()) {
            mDrawCommand = DRAW_ANIMATING_FRAME;
            return true;
        }
        return false;
    }

    protected int calcFontSize(int size) {
        return (int) (size * mContext.getResources().getDisplayMetrics()
                .scaledDensity);
    }

    abstract void onDrawFrame();

    protected void onSurfaceChanged(int width, int height) {
        // recycle bitmap resources if need

        recycleBitmap(mBackgroundBitmap);
        recycleBitmap(mBitmap);

        // create bitmap and canvas for page
        //mBackgroundBitmap = background;
        Page page = mPageFlip.getFirstPage();
        int pageW = (int) page.width();
        int pageH = (int) page.height();
        mBitmap = Bitmap.createBitmap(pageW, pageH, Bitmap.Config.ARGB_8888);
        Log.d("PDF_DEBUG", "pageW = " + pageW);
        Log.d("PDF_DEBUG", "pageH = " + pageH);
        Log.d("PDF_DEBUG", "mCanvas = " + mCanvas);
        mCanvas.setBitmap(mBitmap);
    }

    abstract boolean onEndedDrawing(int what);

    protected void drawPageNumber(int number, int width, int height, Paint p) {
        // 2. draw page number
        int fontSize = calcFontSize(80);
        p.setColor(Color.WHITE);
        p.setStrokeWidth(1);
        p.setAntiAlias(true);
        p.setShadowLayer(5.0f, 8.0f, 8.0f, Color.BLACK);
        p.setTextSize(fontSize);
        String text = String.valueOf(number);
        float textWidth = p.measureText(text);
        float posY = height - p.getTextSize() - 20;
        mCanvas.drawText(text, (width - textWidth) / 2, posY, p);

        if (number <= 1) {
            String firstPage = "The First Page";
            p.setTextSize(calcFontSize(16));
            float w = p.measureText(firstPage);
            float h = p.getTextSize();
            mCanvas.drawText(firstPage, (width - w) / 2, posY + 5 + h, p);
        } else if (number >= renderCallback.getPageCount()) {
            String lastPage = "The Last Page";
            p.setTextSize(calcFontSize(16));
            float w = p.measureText(lastPage);
            float h = p.getTextSize();
            mCanvas.drawText(lastPage, (width - w) / 2, posY + 5 + h, p);
        }
    }

    public interface RenderCallback {

        Bitmap getBitmap(int pageNum);

        int getPageCount();

        boolean showPageNumber();
    }
}
