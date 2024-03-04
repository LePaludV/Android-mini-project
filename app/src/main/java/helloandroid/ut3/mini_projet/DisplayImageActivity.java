package helloandroid.ut3.mini_projet;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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
    }
}
