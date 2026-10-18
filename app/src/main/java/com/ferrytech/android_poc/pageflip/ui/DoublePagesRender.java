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

import com.ferrytech.android_poc.pageflip.core.PageFlip;
import com.ferrytech.android_poc.pageflip.core.Page;
import com.ferrytech.android_poc.pageflip.core.PageFlipState;

public class DoublePagesRender extends PageRender {

    public DoublePagesRender(Context context, PageFlip pageFlip, Handler handler, int pageNo, RenderCallback renderCallback) {
        super(context, pageFlip, handler, pageNo, renderCallback);
    }

    @Override
    public void onDrawFrame() {
        // 1. delete unused textures to save memory
        mPageFlip.deleteUnusedTextures();

        // 2. there are two pages for representing the whole screen, we need to
        // draw them one by one
        final Page first = mPageFlip.getFirstPage();
        final Page second = mPageFlip.getSecondPage();

        // 3. check if the first texture is valid for first page, if not,
        // create it with relative content
        if (!first.isFirstTextureSet()) {
            drawPage(first.isLeftPage() ? mPageNo : mPageNo + 1);
            first.setFirstTexture(mBitmap);
        }

        // 4. check if the first texture is valid for second page
        if (!second.isFirstTextureSet()) {
            drawPage(second.isLeftPage() ? mPageNo : mPageNo + 1);
            second.setFirstTexture(mBitmap);
        }

        // 5. handle drawing command triggered from finger moving and animating
        if (mDrawCommand == DRAW_MOVING_FRAME ||
                mDrawCommand == DRAW_ANIMATING_FRAME) {
            // before drawing, check if back texture of first page is valid
            // Remember: the first page is always the fold page
            if (!first.isBackTextureSet()) {
                drawPage(first.isLeftPage() ? mPageNo - 1 : mPageNo + 2);
                first.setBackTexture(mBitmap);
            }

            // check the second texture of first page is valid.
            if (!first.isSecondTextureSet()) {
                drawPage(first.isLeftPage() ? mPageNo - 2 : mPageNo + 3);
                first.setSecondTexture(mBitmap);
            }

            // draw frame for page flip
            mPageFlip.drawFlipFrame();
        }
        // draw stationary page without flipping
        else if (mDrawCommand == DRAW_FULL_PAGE) {
            mPageFlip.drawPageFrame();
        }

        // 6. send message to main thread to notify drawing is ended so that
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
                // should handle forward flip to update page number and exchange
                // textures between first and second pages. Don't have to handle
                // backward flip since there is no such state happened in double
                // page mode
                if (mPageFlip.getFlipState() == PageFlipState.END_WITH_FORWARD) {
                    final Page first = mPageFlip.getFirstPage();
                    final Page second = mPageFlip.getSecondPage();
                    second.swapTexturesWithPage(first);

                    // update page number for left page
                    if (first.isLeftPage()) {
                        mPageNo -= 2;
                    } else {
                        mPageNo += 2;
                    }
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
        if (null != background) {
            Rect rect = new Rect(0, 0, width, height);
            mCanvas.drawBitmap(background, null, rect, p);
            recycleBitmap(background);
        }


        if (renderCallback.showPageNumber()) {
            int pageCount = renderCallback.getPageCount();
            // 2. draw page number
            int fontSize = (int) (80 * mContext.getResources().getDisplayMetrics()
                    .scaledDensity);
            p.setColor(Color.WHITE);
            p.setStrokeWidth(1);
            p.setAntiAlias(true);
            p.setShadowLayer(5.0f, 8.0f, 8.0f, Color.BLACK);
            p.setTextSize(fontSize);

            String text = String.valueOf(number);
            if (number < 1) {
                text = "Preface";
            } else if (number > pageCount) {
                text = "End";
            }
            float textWidth = p.measureText(text);
            float y = height - p.getTextSize() - 20;
            mCanvas.drawText(text, (width - textWidth) / 2, y, p);

            if (number == 1) {
                String firstPage = "The First Page";
                p.setTextSize(calcFontSize(16));
                float w = p.measureText(firstPage);
                float h = p.getTextSize();
                mCanvas.drawText(firstPage, (width - w) / 2, y + 5 + h, p);
            } else if (number == pageCount) {
                String lastPage = "The Last Page";
                p.setTextSize(calcFontSize(16));
                float w = p.measureText(lastPage);
                float h = p.getTextSize();
                mCanvas.drawText(lastPage, (width - w) / 2, y + 5 + h, p);
            }
        }
    }

    @Override
    public boolean canFlipForward() {
        final Page page = mPageFlip.getFirstPage();
        // current page is left page
        if (page.isLeftPage()) {
            return (mPageNo > 1);
        }

        // current page is right page
        return (mPageNo + 2 <= renderCallback.getPageCount());
    }

    @Override
    public boolean canFlipBackward() {
        return false;
    }
}
