package helloandroid.ut3.mini_projet;


import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class CameraActivity extends AppCompatActivity {
    public View cameraView;
    private ImageCapture imageCapture;
    private VideoCapture<Recorder> videoCapture;
    private Recording recording;
    private ExecutorService cameraExecutor;

    private Button imageCaptureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraView = findViewById(R.id.camera);
        imageCaptureButton = findViewById(R.id.image_capture_button);

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        // Set up the listeners for take photo and video capture buttons
        imageCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        /*cameraView.videoCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureVideo();
            }
        });*/

        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    public void takePhoto() {
        System.out.println("ON PREND LA HPOTO");
        ImageCapture imageCapture = this.imageCapture;
        if (imageCapture == null) {
            Log.e(TAG, "Image capture is null");
            return;
        }

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(ContextCompat.getMainExecutor(this), new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy image) {
                        super.onCaptureSuccess(image);
                        // Convertir l'objet ImageProxy en Bitmap

                        Bitmap bitmap = toBitmap(image);
                        // Créer un Intent pour démarrer DisplayImageActivity
                        Intent intent = new Intent(CameraActivity.this, DisplayImageActivity.class);
                        File tempFile = null;
                        try {
                            tempFile = createTempFile(bitmap);
                        } catch (IOException e) {
                            Log.e(TAG, "Failed to create temp file", e);
                        }
                        if (tempFile != null) {
                            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileprovider", tempFile);
                            intent.putExtra("imageUri", imageUri);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to save image", Toast.LENGTH_SHORT);
                        }
                        // Fermer l'objet ImageProxy
                        image.close();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        super.onError(exception);
                        Log.e(TAG, "Photo capture failed: " + exception.getMessage(), exception);
                    }
                });
    }




    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    getBaseContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    private static final String TAG = "CameraXApp";
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Failed to get camera process provider", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        imageCapture = new ImageCapture.Builder().build();

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        PreviewView viewFinder = findViewById(R.id.viewFinder);
        preview.setSurfaceProvider(viewFinder.getSurfaceProvider());
        androidx.camera.core.Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageCapture);

    }

    private Bitmap toBitmap(ImageProxy image) {
        System.out.println(image);
        if (image.getFormat() != ImageFormat.JPEG) {
            Log.e(TAG, "Image format is not JPEG");
            return null;
        }

        ImageProxy.PlaneProxy[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        byte[] imageBytes = new byte[buffer.remaining()];
        buffer.get(imageBytes);

        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private File createTempFile(Bitmap bitmap) throws IOException {
        File imageFolder = new File(getCacheDir(), "images");
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }

        File tempFile = File.createTempFile("temp_image_", ".jpg", imageFolder);
        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        }

        return tempFile;
    }






}
