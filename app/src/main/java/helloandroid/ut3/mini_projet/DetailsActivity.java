package helloandroid.ut3.mini_projet;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import helloandroid.ut3.mini_projet.models.Review;
import helloandroid.ut3.mini_projet.services.PhotoService;
import helloandroid.ut3.mini_projet.services.RestaurantsService;
import helloandroid.ut3.mini_projet.services.ReviewService;

public class DetailsActivity extends AppCompatActivity {

    String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        PhotoService photoService = new PhotoService(getApplicationContext());
        Intent intent = getIntent();
        TextView title = findViewById(R.id.restaurantName);
        TextView type = findViewById(R.id.restaurantType);
        TextView desc = findViewById(R.id.restaurantDescription);
        TextView address = findViewById(R.id.restaurantAddress);
        TextView horaire= findViewById(R.id.restaurantHoraire);
        ImageView image = findViewById(R.id.restaurantImage);

        restaurantId = intent.getStringExtra("Id");
        title.setText(intent.getStringExtra("Titre"));
        type.setText(intent.getStringExtra("Type"));
        desc.setText(intent.getStringExtra("Description"));
        address.setText(intent.getStringExtra("Adresse"));
        horaire.setText(intent.getStringExtra("Horaire"));
        String[] photos = intent.getStringArrayExtra("Photos");
        photoService.setPhoto(photos[0],image);
        FloatingActionButton btnBack = findViewById(R.id.backToMain);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ReviewService reviewService = new ReviewService();
        reviewService.getAllReviewsByRestaurantId(restaurantId, new ReviewService.OnReviewsCallback() {
            @Override
            public void onSuccess(List<Review> reviews) {
                if (!reviews.isEmpty()) {
                    // TODO : Affichage des reviews
                    for(Review r: reviews){
                        System.out.println(r.toString());
                    }
                } else {
                    // TODO : Affichage s'il n'y a pas de review
                    System.out.println("No reviews found for this restaurant.");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                System.out.println("Error fetching reviews: " + errorMessage);
            }
        });
    }

    public void onClickReviewBtn(View v) {
        Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
        intent.putExtra("restaurantId", restaurantId);
        startActivity(intent);
    }
}