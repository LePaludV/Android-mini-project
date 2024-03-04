package helloandroid.ut3.mini_projet;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

public class DisplayImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        imageView = findViewById(R.id.imageView);
        Uri imageUri = getIntent().getParcelableExtra("imageUri");
        GlideApp.with(this)
                .load(imageUri)
                .into(imageView);
    findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });
    findViewById(R.id.buttonValidate).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            saveImageToDevice(imageUri);
        }
    });


        // todo les fleche permettent modifier une valeur de l'image
    //-> shake to randomize value
        // + add stickeer
    }

    private void saveImageToDevice(Uri imageUri) {
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
       values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/InsTable");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                Toast.makeText(this, "Image enregistrée avec succès", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e(TAG, "Erreur lors de l'enregistrement de l'image", e);
                Toast.makeText(this, "Erreur lors de l'enregistrement de l'image", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e(TAG, "Erreur lors de l'ouverture du flux d'entrée", e);
            Toast.makeText(this, "Erreur lors de l'ouverture du flux d'entrée", Toast.LENGTH_SHORT).show();
        }
    }
}
