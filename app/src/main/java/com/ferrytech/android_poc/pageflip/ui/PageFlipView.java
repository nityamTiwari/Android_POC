package com.ferrytech.android_poc.pageflip.ui;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ferrytech.android_poc.pageflip.core.PageFlip;
import com.ferrytech.android_poc.pageflip.core.PageFlipException;

import java.util.concurrent.locks.ReentrantLock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;



public class PageFlipView extends GLSurfaceView implements Renderer {

    private static final String TAG = "PageFlipView";

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_ANIM_DURATION = 1000;
    private static final int DEFAULT_PIXELS_OF_MESH = 10;

    private int mDuration;

    private Context mContext;
    private Handler mHandler;
    private PageFlip mPageFlip;
    private PageRender mPageRender;
    private ReentrantLock mDrawLock;
    private PageRender.RenderCallback renderCallback;

    public PageFlipView(Context context) {
        super(context);
        init(context);
    }

    public PageFlipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        // create handler to tackle message
        initHandler();

        mDuration = DEFAULT_ANIM_DURATION;
        // create PageFlip
        mPageFlip = new PageFlip(context);

        mPageFlip.setSemiPerimeterRatio(0.8f)
                .setShadowWidthOfFoldEdges(5, 60, 0.3f)
                .setShadowWidthOfFoldBase(5, 80, 0.4f)
                .setPixelsOfMesh(DEFAULT_PIXELS_OF_MESH)
                .enableAutoPage(true);

        setEGLContextClientVersion(2);
        // init others
        mDrawLock = new ReentrantLock();
        // configure render
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private void initHandler() {
        mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PageRender.MSG_ENDED_DRAWING_FRAME:
                        try {
                            mDrawLock.lock();
                            // notify page render to handle ended drawing
                            // message
                            if (mPageRender != null && mPageRender.onEndedDrawing(msg.arg1)) {
                                requestRender();
                            }
                        } finally {
                            mDrawLock.unlock();
                        }
                        break;

                    default:
                        break;
                }
            }
        };
    }

    public void setAnimDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public void setPageRender(PageRender.RenderCallback renderCallback) {
        setPageRender(DEFAULT_PAGE_NUM, false, renderCallback);
    }

    public void setPageRender(int mPageNo, boolean isAuto, PageRender.RenderCallback renderCallback) {
        this.renderCallback = renderCallback;
        if (isAuto) {
            this.mPageRender = getDoublePageRender(mPageNo);
        } else {
            this.mPageRender = getSinglePageRender(mPageNo);
        }
    }

    @NonNull
    private SinglePageRender getSinglePageRender(int pageNo) {
        if (null == renderCallback) {
            return null;
        }
        return new SinglePageRender(mContext,
                mPageFlip,
                mHandler,
                pageNo,
                renderCallback);
    }

    @NonNull
    private DoublePagesRender getDoublePageRender(int pageNo) {
        if (null == renderCallback) {
            return null;
        }
        return new DoublePagesRender(mContext,
                mPageFlip,
                mHandler,
                pageNo,
                renderCallback);
    }

    public void onFingerDown(float x, float y) {
        Log.d("PDF_DEBUG", "onFingerDown");

        // if the animation is going, we should ignore this event to avoid
        // mess drawing on screen
        if (!mPageFlip.isAnimating() && mPageFlip.getFirstPage() != null) {
            mPageFlip.onFingerDown(x, y);
        }
    }

    public void onFingerMove(float x, float y) {
        Log.d("PDF_DEBUG", "onFingerMove");

        if (mPageFlip.isAnimating()) {
            // nothing to do during animating
        } else if (mPageFlip.canAnimate(x, y)) {
            // if the point is out of current page, try to start animating
            onFingerUp(x, y);
        }
        // move page by finger
        else if (mPageFlip.onFingerMove(x, y)) {
            try {
                mDrawLock.lock();
                if (mPageRender != null && mPageRender.onFingerMove(x, y)) {
                    requestRender();
                }
            } finally {
                mDrawLock.unlock();
            }
        }
    }

    public void onFingerUp(float x, float y) {
        Log.d("FLIP_TEST", "onFingerUp");
        if (!mPageFlip.isAnimating()) {
            mPageFlip.onFingerUp(x, y, mDuration);
            try {
                mDrawLock.lock();
                if (mPageRender != null && mPageRender.onFingerUp(x, y)) {
                    requestRender();
                }
            } finally {
                mDrawLock.unlock();
            }
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d("PDF_DEBUG", "onDrawFrame entered");
        try {
            mDrawLock.lock();
            if (mPageRender != null) {
                mPageRender.onDrawFrame();
            }
        } finally {
            mDrawLock.unlock();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        try {
            mPageFlip.onSurfaceChanged(width, height);

            // if there is the second page, create double page render when need
            int pageNo = mPageRender.getPageNo();
            if (mPageFlip.getSecondPage() != null && width > height) {
                if (!(mPageRender instanceof DoublePagesRender)) {
                    mPageRender.release();
                    mPageRender = getDoublePageRender(pageNo);
                }
            }
            // if there is only one page, create single page render when need
            else if (!(mPageRender instanceof SinglePageRender)) {
                mPageRender.release();
                mPageRender = getSinglePageRender(pageNo);
            }

            // let page render handle surface change
            mPageRender.onSurfaceChanged(width, height);
        } catch (PageFlipException e) {
            Log.e(TAG, "Failed to run PageFlipFlipRender:onSurfaceChanged");
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
            mPageFlip.onSurfaceCreated();
        } catch (PageFlipException e) {
            Log.e(TAG, "Failed to run PageFlipFlipRender:onSurfaceCreated");
        }
    }
}
