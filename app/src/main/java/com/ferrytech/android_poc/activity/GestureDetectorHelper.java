package com.ferrytech.android_poc.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import androidx.camera.core.ImageProxy;

import com.google.mediapipe.framework.image.BitmapImageBuilder;
import com.google.mediapipe.framework.image.MPImage;
import com.google.mediapipe.tasks.core.BaseOptions;
import com.google.mediapipe.tasks.vision.core.RunningMode;
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker;
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult;

import java.util.Arrays;
import java.util.List;

public class GestureDetectorHelper {

    private static final String TAG = "GestureHelper";

    private static final List<Integer> FINGER_TIP_IDS = Arrays.asList(8, 12, 16, 20);
    private static final List<Integer> FINGER_MCP_IDS = Arrays.asList(5,  9, 13, 17);

    public interface GestureListener {
        void onFistDetected();
        void onFistReleased();
    }

    private final GestureListener listener;
    private HandLandmarker handLandmarker;

    public GestureDetectorHelper(Context context, GestureListener listener) {
        this.listener = listener;
        setupHandLandmarker(context);
    }

    private void setupHandLandmarker(Context context) {
        try {
            Log.d(TAG, "Setting up HandLandmarker...");

            BaseOptions baseOptions = BaseOptions.builder()
                    .setModelAssetPath("hand_landmarker.task")
                    .build();

            HandLandmarker.HandLandmarkerOptions options =
                    HandLandmarker.HandLandmarkerOptions.builder()
                            .setBaseOptions(baseOptions)
                            .setNumHands(1)
                            .setMinHandDetectionConfidence(0.5f)
                            .setMinHandPresenceConfidence(0.5f)
                            .setMinTrackingConfidence(0.5f)
                            .setRunningMode(RunningMode.IMAGE)
                            .build();

            handLandmarker = HandLandmarker.createFromOptions(context, options);
            Log.d(TAG, "HandLandmarker created successfully ✓");

        } catch (Exception e) {
            Log.e(TAG, "FAILED to create HandLandmarker: " + e.getMessage(), e);
            handLandmarker = null;
        }
    }

    public void detectGesture(ImageProxy imageProxy) {
        if (handLandmarker == null) {
            Log.w(TAG, "detectGesture called but handLandmarker is NULL — skipping");
            imageProxy.close();
            return;
        }

        try {
            // 1. Copy pixels out of the ImageProxy buffer
            Bitmap bitmapBuffer = Bitmap.createBitmap(
                    imageProxy.getWidth(),
                    imageProxy.getHeight(),
                    Bitmap.Config.ARGB_8888
            );
            bitmapBuffer.copyPixelsFromBuffer(imageProxy.getPlanes()[0].getBuffer());
            imageProxy.close();

            // 2. Rotate to upright orientation
            int rotation = imageProxy.getImageInfo().getRotationDegrees();
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap rotatedBitmap = Bitmap.createBitmap(
                    bitmapBuffer, 0, 0,
                    bitmapBuffer.getWidth(), bitmapBuffer.getHeight(),
                    matrix, true
            );

            // 3. Run inference
            MPImage mpImage = new BitmapImageBuilder(rotatedBitmap).build();
            HandLandmarkerResult result = handLandmarker.detect(mpImage);

            // 4. Evaluate result
            if (result == null || result.landmarks().isEmpty()) {
                Log.d(TAG, "No hand detected");
                listener.onFistReleased();
                return;
            }

            Log.d(TAG, "Hand detected! Landmarks count: " + result.landmarks().get(0).size());

            boolean fist = isFistGesture(result);
            Log.d(TAG, "Is fist: " + fist);

            if (fist) {
                listener.onFistDetected();
            } else {
                listener.onFistReleased();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error in detectGesture: " + e.getMessage(), e);
            try { imageProxy.close(); } catch (Exception ignored) {}
        }
    }

    private boolean isFistGesture(HandLandmarkerResult result) {
        List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>
                landmarks = result.landmarks().get(0);

        int closedCount = 0;
        for (int i = 0; i < FINGER_TIP_IDS.size(); i++) {
            float tipY = landmarks.get(FINGER_TIP_IDS.get(i)).y();
            float mcpY = landmarks.get(FINGER_MCP_IDS.get(i)).y();
            if (tipY > mcpY) closedCount++;
        }

        Log.d(TAG, "Closed fingers: " + closedCount + "/4");
        return closedCount >= 4;
    }

    public void close() {
        if (handLandmarker != null) {
            handLandmarker.close();
            handLandmarker = null;
            Log.d(TAG, "HandLandmarker closed");
        }
    }
}