package helloandroid.ut3.mini_projet.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import helloandroid.ut3.mini_projet.R;
import helloandroid.ut3.mini_projet.Shaker;

public class DisplayImageActivity extends AppCompatActivity {

    private ImageView imageView;

    private static float[][]FILTERS ={
            {1,1,1},
            {1,0.5f,1},
            {1,1,0.5f,},
            {0.5f,1,1},
            {0.67f, 0.28f, 0.94f},
            {0.55f, 0.71f, 0.42f},
            {0.09f, 0.55f, 0.77f},
            {0.32f, 0.65f, 0.16f},
            {0.84f, 0.55f, 0.19f},
            {0.55f, 0.01f, 0.76f},
            {0.68f, 0.87f, 0.36f},
            {0.52f, 0.49f, 0.99f},
            {0.08f, 0.67f, 0.47f},
            {0.55f, 0.34f, 0.21f},
            {0.74f, 0.51f, 0.39f},
            {0.21f, 0.21f, 0.93f},
            {0.68f, 0.65f, 0.26f},
            {0.74f, 0.21f, 0.76f},
            {0.55f, 0.65f, 0.57f},
            {0.23f, 0.87f, 0.62f},
            {0.36f, 0.07f, 0.15f},
            {0.97f, 0.54f, 0.79f},
            {0.21f, 0.46f, 0.88f},
            {0.03f, 0.64f, 0.11f},
            {0.32f, 0.26f, 0.05f},
            {0.58f, 0.67f, 0.61f},
            {0.71f, 0.77f, 0.91f},
            {0.42f, 0.55f, 0.82f},
            {0.61f, 0.93f, 0.45f},
            {0.34f, 0.13f, 0.22f},
            {0.56f, 0.21f, 0.55f},
            {0.18f, 0.24f, 0.39f},
            {0.86f, 0.61f, 0.35f},
            {0.77f, 0.02f, 0.53f}};


    private int indice = 0;

    TextView filterInfo;

    Shaker s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        imageView = findViewById(R.id.imageView);
        filterInfo = findViewById(R.id.filterInfo);
        s = new Shaker(this);
        s.start();
        Uri imageUri = getIntent().getParcelableExtra("imageUri");
        Glide.with(this)
                .load(imageUri)
                .into(imageView);
    findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            s.stop();
            finish();
        }
    });
    findViewById(R.id.buttonValidate).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            saveImageToDevice(imageUri);
        }
    });
    findViewById(R.id.buttonNext).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            indice=(indice+1)%FILTERS.length;
            changeFilter();
        }

    });
    findViewById(R.id.buttonPrevious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indice=(indice-1+FILTERS.length)%FILTERS.length;
                changeFilter();
            }

        });
    //-> shake to randomize value
        // + add stickers
    }

    private void saveImageToDevice(Uri imageUri) {
        String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
       values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/InsTable");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
            Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);
            Paint paint = new Paint();
            paint.setColorFilter(applyFilter());
            Bitmap filteredImageBitmap = Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), imageBitmap.getConfig());
            Canvas canvas = new Canvas(filteredImageBitmap);
            canvas.drawBitmap(imageBitmap, 0, 0, paint);
            try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                filteredImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
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
    public ColorFilter applyFilter() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        float red = FILTERS[indice][0];
        float green =  FILTERS[indice][1];
        float blue = FILTERS[indice][2];
        matrix.set(new float[] {
                red, 0, 0, 0, 0, // red
                0, green, 0, 0, 0, // green
                0, 0, blue, 0, 0, // blue
                0, 0, 0, 1, 0 // alpha
        });

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        return filter;
    }

    public void onShakeDetected() {
        indice = getRandomNumber(0,FILTERS.length);
        changeFilter();
    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void changeFilter(){
        //filterInfo

        Paint paint = new Paint();
        paint.setColorFilter(applyFilter());
        imageView.setColorFilter(paint.getColorFilter());
        filterInfo.setText(indice==0 ? "No filter selected" : "Filter "+indice+"/"+FILTERS.length);
    }
}
