package helloandroid.ut3.mini_projet.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import helloandroid.ut3.mini_projet.R;
import helloandroid.ut3.mini_projet.models.Review;
import helloandroid.ut3.mini_projet.services.ReviewService;

public class ReviewActivity extends AppCompatActivity {

    String restaurantId;
    EditText usernameInput;
    EditText reviewInput;
    RatingBar ratingBar;

    ReviewService reviewService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        reviewService = new ReviewService();

        Intent intent = getIntent();
        restaurantId = intent.getStringExtra("restaurantId");

        usernameInput = findViewById(R.id.usernameInput);
        reviewInput = findViewById(R.id.reviewInput);
        ratingBar = findViewById(R.id.ratingBar);

        findViewById(R.id.addReviewImageBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewActivity.this,CameraActivity.class);
                startActivity(intent);
            }
        });
    }

    public void OnClickReviewSendBtn(View v) {
        String username = usernameInput.getText().toString();
        String review = reviewInput.getText().toString();
        float rating = ratingBar.getRating();
        Date date = new Date();
        List<String> photos = new ArrayList<String>(); // TODO : PHOTOS

        Review reviewObject = new Review(date,photos,rating,restaurantId,review,username);

        reviewService.addReview(reviewObject);
        finish();
    }

}