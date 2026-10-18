/*
 * Copyright (C) 2016 eschao <esc.chao@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ferrytech.android_poc.pageflip.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ferrytech.android_poc.pageflip.core.PageFlip;
import com.ferrytech.android_poc.pageflip.core.Page;
import com.ferrytech.android_poc.pageflip.core.PageFlipState;


public class SinglePageRender extends PageRender {

    public SinglePageRender(Context mContext, PageFlip mPageFlip, Handler mHandler, int pageNo, RenderCallback renderCallback) {
        super(mContext, mPageFlip, mHandler, pageNo, renderCallback);
    }

    @Override
    public void onDrawFrame() {

        Log.d("PDF_DEBUG",
                "SinglePageRender.onDrawFrame");

        Log.d("PDF_DEBUG",
                "mDrawCommand = " + mDrawCommand);
        // 1. delete unused textures
        mPageFlip.deleteUnusedTextures();
        Page page = mPageFlip.getFirstPage();

        // 2. handle drawing command triggered from finger moving and animating
        if (mDrawCommand == DRAW_MOVING_FRAME ||
                mDrawCommand == DRAW_ANIMATING_FRAME) {
            // is forward flip
            if (mPageFlip.getFlipState() == PageFlipState.FORWARD_FLIP) {
                // check if second texture of first page is valid, if not,
                // create new one
                if (!page.isSecondTextureSet()) {
                    drawPage(mPageNo + 1);
                    page.setSecondTexture(mBitmap);
                }
            }
            // in backward flip, check first texture of first page is valid
            else if (!page.isFirstTextureSet()) {
                drawPage(--mPageNo);
                Log.d("PDF_DEBUG",
                        "setFirstTexture called");
                page.setFirstTexture(mBitmap);
            }

            Log.d("PDF_DEBUG",
                    "drawPageFrame called");
            // draw frame for page flip
            mPageFlip.drawFlipFrame();
        }
        // draw stationary page without flipping
        else if (mDrawCommand == DRAW_FULL_PAGE) {

            Log.d("PDF_DEBUG",
                    "ENTER FULL PAGE");

            Log.d("PDF_DEBUG",
                    "isFirstTextureSet = "
                            + page.isFirstTextureSet());

            if (!page.isFirstTextureSet()) {

                Log.d("PDF_DEBUG",
                        "calling drawPage");

                drawPage(mPageNo);

                Log.d("PDF_DEBUG",
                        "calling setFirstTexture");

                page.setFirstTexture(mBitmap);

                Log.d("PDF_DEBUG",
                        "setFirstTexture finished");
            }

            Log.d("PDF_DEBUG",
                    "calling drawPageFrame");

            mPageFlip.drawPageFrame();

            Log.d("PDF_DEBUG",
                    "drawPageFrame finished");
        }

        // 3. send message to main thread to notify drawing is ended so that
        // we can continue to calculate next animation frame if need.
        // Remember: the drawing operation is always in GL thread instead of
        // main thread
        Message msg = Message.obtain();
        msg.what = MSG_ENDED_DRAWING_FRAME;
        msg.arg1 = mDrawCommand;
        mHandler.sendMessage(msg);
    }

    @Override
    public boolean onEndedDrawing(int what) {
        if (what == DRAW_ANIMATING_FRAME) {
            boolean isAnimating = mPageFlip.animating();
            // continue animating
            if (isAnimating) {
                mDrawCommand = DRAW_ANIMATING_FRAME;
                return true;
            }
            // animation is finished
            else {
                final PageFlipState state = mPageFlip.getFlipState();
                // update page number for backward flip
                if (state == PageFlipState.END_WITH_BACKWARD) {
                    // don't do anything on page number since mPageNo is always
                    // represents the FIRST_TEXTURE no;
                }
                // update page number and switch textures for forward flip
                else if (state == PageFlipState.END_WITH_FORWARD) {
                    mPageFlip.getFirstPage().setFirstTextureWithSecond();
                    mPageNo++;
                }

                mDrawCommand = DRAW_FULL_PAGE;
                return true;
            }
        }
        return false;
    }

    private void drawPage(int number) {
        final int width = mCanvas.getWidth();
        final int height = mCanvas.getHeight();
        Paint p = new Paint();
        p.setFilterBitmap(true);

        // 1. draw background bitmap
        Bitmap background = renderCallback.getBitmap(number);
        Log.d("PDF_DEBUG", "Bitmap width = " + background.getWidth());

        Log.d("PDF_DEBUG", "Bitmap height = " + background.getHeight());
        if (null != background) {
            Rect rect = new Rect(0, 0, width, height);
            mCanvas.drawBitmap(background, null, rect, p);
            Log.d("PDF_DEBUG", "Bitmap drawn to canvas");
            recycleBitmap(background);
        }

        if (renderCallback.showPageNumber()) {
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
    }

    @Override
    public boolean canFlipForward() {
        return (mPageNo < renderCallback.getPageCount());
    }

    @Override
    public boolean canFlipBackward() {
        if (mPageNo > 1) {
            mPageFlip.getFirstPage().setSecondTextureWithFirst();
            return true;
        } else {
            return false;
        }
    }
}
