package com.ferrytech.android_poc.pageflip.utils;

public class FlipbookAnimUtils {
    public static void startPageAnim() {

//            ViewUtils.getViewSize(pageFlipView, new ViewUtils.ViewSizeCallback() {
//                @Override
//                public void size(final int width, final int height) {
//                    ViewUtils.runOnBgThread(new Runnable() {
//                        int x;
//                        int y;
//                        int count;
//                        int MAX_COUNT = 100;
//                        int DIFF_X = 5;
//                        int DIFF_Y = 5;
//                        boolean reverse;
//
//
//                        @Override
//                        public void run() {
//                            x = width;
//                            y = height;
//                            int upCount = MAX_COUNT / 2 + 5;
//                            ViewUtils.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    pageFlipView.onFingerDown(x, y);
//                                }
//                            });
//                            while (true) {
//                                count++;
//                                boolean decrement;
//                                if (count <= upCount) {
//                                    decrement = !reverse;
//                                } else {
//                                    decrement = reverse;
//                                }
//                                if (decrement) {
//                                    x -= DIFF_X;
//                                    y -= DIFF_Y;
//                                } else {
//                                    x += DIFF_X;
//                                    y += DIFF_Y;
//                                }
//
//                                if (x < width / 2) {
//                                    x = width / 2;
//                                    reverse = !reverse;
//                                }
//                                if (y < height / 2) {
//                                    y = height / 2;
//                                    reverse = !reverse;
//                                }
//
//                                if (x >= (width - 100)) {
//                                    x = width - 100;
//                                    reverse = !reverse;
//                                }
//                                if (y >= (height - 100)) {
//                                    y = height - 100;
//                                    reverse = !reverse;
//                                }
//
//                                if (count >= MAX_COUNT) {
//                                    count = 0;
//                                }
//
//                                try {
//                                    Thread.sleep(20);
//                                } catch (InterruptedException e) {
//                                }
//                                ViewUtils.runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (x < width && y < height) {
//                                            pageFlipView.onFingerMove(x, y);
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    });
//                }
//            });
    }

    public static void stopPageAnimation() {

    }
}
