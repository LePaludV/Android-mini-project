package helloandroid.ut3.mini_projet;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import helloandroid.ut3.mini_projet.models.Restaurant;
import helloandroid.ut3.mini_projet.services.RestaurantsService;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getIntent().getIntExtra("id", -1);

        RestaurantsService r = new RestaurantsService();

        setContentView(R.layout.activity_details);

        TextView title = findViewById(R.id.restaurantName);
        ImageView image = findViewById(R.id.restaurantImage);

        (r.getRestaurantById("FR2Tv4XMxkGK5rDCbAAx")).whenComplete((res, error) -> {
            if (error != null) {
                System.out.println("Exception occurred");
            } else {
                System.out.println(res);
                title.setText(res.getNom());
                ImageView img = findViewById(R.id.restaurantImage);
                StorageReference storageRef = (FirebaseStorage.getInstance()).getReference();

                StorageReference pathReference = storageRef.child(res.getPhotos()[0]);
                Glide.with(this.getApplicationContext())
                        .load(pathReference)
                        .into(img);
            }
        });
    }
}