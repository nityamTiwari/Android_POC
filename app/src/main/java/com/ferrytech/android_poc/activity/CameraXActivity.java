package com.ferrytech.android_poc.activity;

import android.Manifest;
import android.os.Bundle;
import android.util.Rational;
import android.view.Surface;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.core.ViewPort;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.FileOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.ferrytech.android_poc.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class CameraXActivity extends AppCompatActivity {

    PreviewView previewView;
    Button btnRecord, btnStop;
    Spinner spinnerRatio;

    VideoCapture<Recorder> videoCapture;
    Recording recording;

    String selectedRatio = "16:9";

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_xactivity);

        previewView = findViewById(R.id.previewView);
        btnRecord = findViewById(R.id.btnRecord);
        btnStop = findViewById(R.id.btnStop);
        spinnerRatio = findViewById(R.id.spinnerRatio);

        setupSpinner();

        btnRecord.setOnClickListener(v -> startRecording());
        btnStop.setOnClickListener(v -> stopRecording());

        startCamera();
    }

    private void setupSpinner() {
        String[] ratios = {"16:9", "9:16", "1:1", "4:3"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                ratios
        );

        spinnerRatio.setAdapter(adapter);

        spinnerRatio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedRatio = ratios[position];
                startCamera(); // reload camera with new ratio
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void startCamera() {

        ListenableFuture<ProcessCameraProvider> future =
                ProcessCameraProvider.getInstance(this);

        future.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = future.get();

                cameraProvider.unbindAll();
                // preview
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                //  VideoCapture
                Recorder recorder = new Recorder.Builder()
                        .setQualitySelector(QualitySelector.from(Quality.FHD))
                        .build();

                videoCapture = VideoCapture.withOutput(recorder);

                //  ViewPort
                Rational rational = getRationalFromRatio();

                ViewPort viewPort = new ViewPort.Builder(
                        rational,
                        Surface.ROTATION_0
                ).build();

                // UseCaseGroup
                UseCaseGroup group = new UseCaseGroup.Builder()
                        .addUseCase(preview)
                        .addUseCase(videoCapture)
                        .setViewPort(viewPort)
                        .build();

                cameraProvider.bindToLifecycle(
                        this,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        group
                );

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private Rational getRationalFromRatio() {
        switch (selectedRatio) {
            case "9:16":
                return new Rational(9, 16);
            case "1:1":
                return new Rational(1, 1);
            case "4:3":
                return new Rational(4, 3);
            default:
                return new Rational(16, 9);
        }
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private void startRecording() {

        if (videoCapture == null) return;

        String ratioTag = selectedRatio.replace(":", "x");

        File file = new File(
                getExternalFilesDir(null),
                "VID_" + ratioTag + "_" + System.currentTimeMillis() + ".mp4"
        );

        FileOutputOptions options =
                new FileOutputOptions.Builder(file).build();

        recording = videoCapture.getOutput()
                .prepareRecording(this, options)
                .withAudioEnabled()
                .start(ContextCompat.getMainExecutor(this), event -> {

                    if (event instanceof VideoRecordEvent.Start) {
                        Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show();
                    }

                    if (event instanceof VideoRecordEvent.Finalize) {
                        Toast.makeText(this, "Saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void stopRecording() {
        if (recording != null) {
            recording.stop();
            recording = null;
        }
    }
}