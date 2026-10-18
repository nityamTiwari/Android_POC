package com.ferrytech.android_poc.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.core.ViewPort;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.ferrytech.android_poc.R;
import com.google.common.util.concurrent.ListenableFuture;

import org.jspecify.annotations.NonNull;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PIctureActivity extends AppCompatActivity
        implements GestureDetectorHelper.GestureListener {

    private static final String TAG = "PIctureActivity";

    private FrameLayout previewContainer;
    private PreviewView previewView;
    private Spinner spinner;
    private Button btnCapture;
    private TextView tvCountdown;
    private View reviewScreen;
    private ImageView ivCapturedPhoto;
    private Button btnRecapture;
    private Button btnSave;



    private ImageCapture imageCapture;
    private ProcessCameraProvider cameraProvider;
    private Size maxSize;
    private float aspectRatio = 4f / 3f;

    // gesture countdown
    private GestureDetectorHelper gestureHelper;
    private ExecutorService analysisExecutor;
    private CountDownTimer countDownTimer;
    private boolean isCountingDown = false;

    private File lastCapturedFile;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_picture);

        // Camera screen views
        previewContainer = findViewById(R.id.previewContainer);
        previewView = findViewById(R.id.previewView);
        spinner = findViewById(R.id.spinnerRatio);
        btnCapture = findViewById(R.id.btnCapture);
        tvCountdown = findViewById(R.id.tvCountdown);

        // Review screen views
        reviewScreen = findViewById(R.id.reviewScreen);
        ivCapturedPhoto = findViewById(R.id.ivCapturedPhoto);
        btnRecapture = findViewById(R.id.btnRecapture);
        btnSave = findViewById(R.id.btnSave);

        analysisExecutor = Executors.newSingleThreadExecutor();
        gestureHelper = new GestureDetectorHelper(this, this);

        setUpSpinner();
        btnCapture.setOnClickListener(v -> captureImage());
        btnRecapture.setOnClickListener(v -> startCameraScreen());
        btnSave.setOnClickListener(v -> saveAndExit());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelCountdown();
        if (gestureHelper != null) gestureHelper.close();
        if (analysisExecutor != null) analysisExecutor.shutdown();
    }

    private void startCameraScreen() {
        reviewScreen.setVisibility(View.GONE);
        previewContainer.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        btnCapture.setVisibility(View.VISIBLE);

        // Rebind camera after it was unbound during review
        previewView.post(() -> startCamera());
    }

    //Stop the camera, show the captured photo full-screen for review.
    //    Called right after a successful save.
    private void showReviewScreen(File photoFile) {
        // 1. Stop camera completely so preview freezes / releases
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
        }

        // 2. Decode and display captured photo
        Bitmap bm = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        ivCapturedPhoto.setImageBitmap(bm);

        // 3. Animate the transition: flash → reveal review
        flashScreen(() -> {
            previewContainer.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            btnCapture.setVisibility(View.GONE);
            reviewScreen.setVisibility(View.VISIBLE);
        });
    }

  //    * Brief white-flash animation that mimics a real shutter.

    private void flashScreen(Runnable afterFlash) {
        View flash = findViewById(R.id.flashOverlay);
        flash.setVisibility(View.VISIBLE);
        AlphaAnimation anim = new AlphaAnimation(1f, 0f);
        anim.setDuration(300);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation a) {
            }

            @Override
            public void onAnimationRepeat(Animation a) {
            }

            @Override
            public void onAnimationEnd(Animation a) {
                flash.setVisibility(View.GONE);
                afterFlash.run();
            }
        });
        flash.startAnimation(anim);
    }

    //  Save action from review screen

    private void saveAndExit() {
        if (lastCapturedFile != null) {
            Toast.makeText(this,
                    "Photo saved: " + lastCapturedFile.getName(),
                    Toast.LENGTH_LONG).show();
        }
        startCameraScreen();
    }

    //  GestureListener callbacks
    @Override
    public void onFistDetected() {
        runOnUiThread(() -> {
            if (!isCountingDown) startCountdown();
        });
    }

    @Override
    public void onFistReleased() {
    }


    private void startCountdown() {
        isCountingDown = true;
        tvCountdown.setVisibility(View.VISIBLE);
        tvCountdown.setText("3");

        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCountdown.setText(String.valueOf((millisUntilFinished / 1000) + 1));
            }

            @Override
            public void onFinish() {
                tvCountdown.setVisibility(View.GONE);
                isCountingDown = false;
                captureImage();
            }
        }.start();
    }

    private void cancelCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        isCountingDown = false;
        tvCountdown.setVisibility(View.GONE);
    }

    //  Spinner
    private void setUpSpinner() {
        String[] ratios = {"1:1", "4:3", "16:9", "9:16"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, ratios);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        aspectRatio = 1f / 1f;
                        break;
                    case 1:
                        aspectRatio = 4f / 3f;
                        break;
                    case 2:
                        aspectRatio = 16f / 9f;
                        break;
                    case 3:
                        aspectRatio = 9f / 16f;
                        break;
                }
                updatePreviewSize();
                previewView.post(() -> startCamera());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //  CameraX

    @SuppressLint("RestrictedApi")
    private void startCamera() {
        maxResolution();
        ListenableFuture<ProcessCameraProvider> future =
                ProcessCameraProvider.getInstance(this);

        future.addListener(() -> {
            try {
                cameraProvider = future.get();

                int ratio = (aspectRatio == 16f / 9f)
                        ? AspectRatio.RATIO_16_9 : AspectRatio.RATIO_4_3;

                Preview preview = new Preview.Builder()
                        .setTargetAspectRatio(ratio).build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder()
                        .setTargetResolution(maxSize)
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                        .build();

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setTargetAspectRatio(ratio)
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                        .build();

                imageAnalysis.setAnalyzer(analysisExecutor,
                        imageProxy -> gestureHelper.detectGesture(imageProxy));

                previewView.post(() ->
                        bindViewPort(cameraProvider, preview,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                imageCapture, imageAnalysis));

            } catch (Exception e) {
                Log.e(TAG, "startCamera failed: " + e.getMessage(), e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindViewPort(ProcessCameraProvider provider,
                              Preview preview, CameraSelector selector,
                              ImageCapture imageCapture, ImageAnalysis imageAnalysis) {
        int w = previewView.getWidth();
        int h = previewView.getHeight();

        if (w == 0 || h == 0) {
            previewView.postDelayed(() ->
                    bindViewPort(provider, preview, selector,
                            imageCapture, imageAnalysis), 200);
            return;
        }

        ViewPort viewPort = new ViewPort.Builder(
                new Rational(w, h),
                previewView.getDisplay().getRotation()
        ).build();

        UseCaseGroup group = new UseCaseGroup.Builder()
                .setViewPort(viewPort)
                .addUseCase(preview)
                .addUseCase(imageCapture)
                .addUseCase(imageAnalysis)
                .build();

        provider.unbindAll();
        provider.bindToLifecycle(this, selector, group);
    }

    //  Capture — saves file then switches to review screen
    private void captureImage() {
        if (imageCapture == null) return;

        File file = new File(
                getExternalFilesDir("picture"),
                "Img_" + System.currentTimeMillis() + ".jpg"
        );

        ImageCapture.OutputFileOptions options =
                new ImageCapture.OutputFileOptions.Builder(file).build();

        imageCapture.takePicture(options, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(
                            ImageCapture.@NonNull OutputFileResults results) {
                        lastCapturedFile = file;
                        showReviewScreen(file);   // ← key call
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException e) {
                        Log.e(TAG, "Capture error: " + e.getMessage(), e);
                        Toast.makeText(PIctureActivity.this,
                                "Capture failed: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //utilities
    private void maxResolution() {
        try {
            CameraManager manager = (CameraManager) getSystemService(CAMERA_SERVICE);
            for (String id : manager.getCameraIdList()) {
                CameraCharacteristics c = manager.getCameraCharacteristics(id);
                StreamConfigurationMap map = c.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) continue;
                for (Size s : map.getOutputSizes(android.graphics.ImageFormat.JPEG)) {
                    if (maxSize == null ||
                            s.getWidth() * s.getHeight() >
                                    maxSize.getWidth() * maxSize.getHeight()) {
                        maxSize = s;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "maxResolution error: " + e.getMessage(), e);
        }
    }

    private void updatePreviewSize() {
        previewContainer.post(() -> {
            int pw = previewContainer.getWidth();
            int ph = previewContainer.getHeight();
            float pr = (float) pw / ph;

            int nw = pw, nh = ph;
            if (pr > aspectRatio) nw = (int) (ph * aspectRatio);
            else nh = (int) (pw / aspectRatio);

            FrameLayout.LayoutParams p =
                    (FrameLayout.LayoutParams) previewView.getLayoutParams();
            p.width = nw;
            p.height = nh;
            p.gravity = Gravity.CENTER;
            previewView.setLayoutParams(p);
        });
    }
}