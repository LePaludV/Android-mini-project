package helloandroid.ut3.mini_projet.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import helloandroid.ut3.mini_projet.R;
import helloandroid.ut3.mini_projet.models.Review;
import helloandroid.ut3.mini_projet.services.PhotoService;
import helloandroid.ut3.mini_projet.services.ReviewService;

public class ReviewActivity extends AppCompatActivity {

    String restaurantId;
    EditText usernameInput;
    EditText reviewInput;
    RatingBar ratingBar;
    String imageName;
    ReviewService reviewService;
    ImageView previewPhoto;
    PhotoService photoService;
    private ActivityResultLauncher<Intent> cameraActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        reviewService = new ReviewService();
        photoService = new PhotoService(getApplicationContext());
        previewPhoto = findViewById(R.id.imageView2);
        Intent intent = getIntent();
        restaurantId = intent.getStringExtra("restaurantId");

        usernameInput = findViewById(R.id.usernameInput);
        reviewInput = findViewById(R.id.reviewInput);
        ratingBar = findViewById(R.id.ratingBar);
        FloatingActionButton btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.addReviewImageBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewActivity.this,CameraActivity.class);
                cameraActivity.launch(intent);
            }


        });
        cameraActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode()== Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null){
                            imageName = data.getStringExtra("imageName");
                            previewPhoto.setVisibility(View.VISIBLE);
                            previewPhoto.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            photoService.setPhotoFromStorage(imageName,previewPhoto);
                        }
                    }
                }
        );
    }

    public void OnClickReviewSendBtn(View v) {
        String username = usernameInput.getText().toString();
        String review = reviewInput.getText().toString();
        float rating = ratingBar.getRating();
        Date date = new Date();
        List<String> photos = new ArrayList<String>();
        photos.add(imageName);
        Review reviewObject = new Review(date,photos,rating,restaurantId,review,username);

        reviewService.addReview(reviewObject);
        finish();
    }

}