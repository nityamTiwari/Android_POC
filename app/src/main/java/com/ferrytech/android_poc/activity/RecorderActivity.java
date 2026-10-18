package com.ferrytech.android_poc.activity;

import android.Manifest;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ferrytech.android_poc.R;

import java.io.File;
import java.util.Arrays;

public class RecorderActivity extends AppCompatActivity {

    TextureView textureView;
    Spinner spinnerRatio;
    Button btnRecord, btnStop;

    CameraDevice cameraDevice;
    CameraCaptureSession captureSession;
    MediaRecorder mediaRecorder;

    String cameraId;
    Size selectedSize;
    String selectedRatio = "16:9";

    CameraCharacteristics characteristics;
    Rect cropRect;

    boolean isRecording = false;

    @RequiresPermission(allOf = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);

        textureView = findViewById(R.id.textureView);
        spinnerRatio = findViewById(R.id.spinnerRatio);
        btnRecord = findViewById(R.id.btnRecord);
        btnStop = findViewById(R.id.btnStop);

        setupSpinner();

        btnRecord.setOnClickListener(v -> startRecording());
        btnStop.setOnClickListener(v -> stopRecording());

        textureView.post(this::setupCamera);
    }

    private void setupSpinner() {
        String[] ratios = {"16:9", "9:16", "4:3", "3:4", "1:1"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                ratios
        );

        spinnerRatio.setAdapter(adapter);

        spinnerRatio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresPermission(Manifest.permission.CAMERA)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRatio = ratios[position];
                adjustPreviewSize();
                setupCamera();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    private void setupCamera() {
        try {
            CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

            for (String id : manager.getCameraIdList()) {
                CameraCharacteristics chars = manager.getCameraCharacteristics(id);
                Integer lens = chars.get(CameraCharacteristics.LENS_FACING);

                if (lens != null && lens == CameraCharacteristics.LENS_FACING_BACK) {

                    characteristics = chars;

                    StreamConfigurationMap map = chars.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                    Size[] sizes = map.getOutputSizes(MediaRecorder.class);
                    selectedSize = getBestSize(sizes);

                    cameraId = id;
                    break;
                }
            }

            manager.openCamera(cameraId, stateCallback, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Size getBestSize(Size[] sizes) {

        float targetRatio = getTargetRatio();

        Size best = sizes[0];
        float minDiff = Float.MAX_VALUE;

        for (Size size : sizes) {
            float ratio = (float) size.getWidth() / size.getHeight();
            float diff = Math.abs(ratio - targetRatio);

            if (diff < minDiff) {
                best = size;
                minDiff = diff;
            }
        }

        return best;
    }

    private float getTargetRatio() {
        switch (selectedRatio) {
            case "9:16":
                return 9f / 16f;
            case "4:3":
                return 4f / 3f;
            case "3:4":
                return 3f / 4f;
            case "1:1":
                return 1f;
            default:
                return 16f / 9f;
        }
    }

    private Rect getCropRect() {

        Rect sensorRect = characteristics.get(
                CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE
        );

        int sensorWidth = sensorRect.width();
        int sensorHeight = sensorRect.height();

        float targetRatio = getTargetRatio();
        float sensorRatio = (float) sensorWidth / sensorHeight;

        int cropWidth = sensorWidth;
        int cropHeight = sensorHeight;

        if (sensorRatio > targetRatio) {
            cropWidth = (int) (sensorHeight * targetRatio);
        } else {
            cropHeight = (int) (sensorWidth / targetRatio);
        }

        int left = (sensorWidth - cropWidth) / 2;
        int top = (sensorHeight - cropHeight) / 2;

        return new Rect(left, top, left + cropWidth, top + cropHeight);
    }

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            startPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
        }
    };

    private void startPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(
                    selectedSize.getWidth(),
                    selectedSize.getHeight()
            );

            Surface surface = new Surface(texture);

            CaptureRequest.Builder builder =
                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            cropRect = getCropRect();
            builder.set(CaptureRequest.SCALER_CROP_REGION, cropRect);

            builder.addTarget(surface);

            cameraDevice.createCaptureSession(
                    Arrays.asList(surface),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            captureSession = session;
                            try {
                                session.setRepeatingRequest(builder.build(), null, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                        }
                    },
                    null
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startRecording() {
        try {
            mediaRecorder = new MediaRecorder();

            String ratioTag = selectedRatio.replace(":", "x"); // 16:9 → 16x9

            String fileName = "VID_" + ratioTag + "_" + System.currentTimeMillis() + ".mp4";

            File file = new File(getExternalFilesDir(null), fileName);

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setOutputFile(file.getAbsolutePath());

            mediaRecorder.setVideoEncodingBitRate(1000000);
            mediaRecorder.setVideoFrameRate(30);
            mediaRecorder.setVideoSize(
                    selectedSize.getWidth(),
                    selectedSize.getHeight()
            );

            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            mediaRecorder.prepare();

            Surface recordSurface = mediaRecorder.getSurface();
            Surface previewSurface = new Surface(textureView.getSurfaceTexture());

            CaptureRequest.Builder builder =
                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);

            cropRect = getCropRect();
            builder.set(CaptureRequest.SCALER_CROP_REGION, cropRect);

            builder.addTarget(previewSurface);
            builder.addTarget(recordSurface);

            cameraDevice.createCaptureSession(
                    Arrays.asList(previewSurface, recordSurface),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            captureSession = session;
                            try {
                                session.setRepeatingRequest(builder.build(), null, null);
                                mediaRecorder.start();
                                isRecording = true;

                                Toast.makeText(RecorderActivity.this, "Recording Started", Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                        }
                    },
                    null
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;

            isRecording = false;

            Toast.makeText(this, "Recording Saved", Toast.LENGTH_SHORT).show();

            startPreview();
        }
    }

    private void adjustPreviewSize() {

        textureView.post(() -> {

            int parentWidth = ((View) textureView.getParent()).getWidth();
            int parentHeight = ((View) textureView.getParent()).getHeight();

            float ratio = getTargetRatio();

            int finalWidth, finalHeight;

            if ((float) parentWidth / parentHeight > ratio) {
                finalHeight = parentHeight;
                finalWidth = (int) (parentHeight * ratio);
            } else {
                finalWidth = parentWidth;
                finalHeight = (int) (parentWidth / ratio);
            }

            ViewGroup.LayoutParams params = textureView.getLayoutParams();
            params.width = finalWidth;
            params.height = finalHeight;
            textureView.setLayoutParams(params);
        });
    }
}
