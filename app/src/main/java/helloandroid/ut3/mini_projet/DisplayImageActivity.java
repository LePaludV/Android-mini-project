package helloandroid.ut3.mini_projet;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DisplayImageActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        imageView = findViewById(R.id.imageView);

        // Récupérer l'URI de l'image à partir de l'Intent
        Uri imageUri = getIntent().getParcelableExtra("imageUri");

        // Utiliser Glide pour charger l'image à partir de l'URI
        GlideApp.with(this)
                .load(imageUri)
                .into(imageView);
    }
}
