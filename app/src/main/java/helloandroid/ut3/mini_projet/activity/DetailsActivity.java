package helloandroid.ut3.mini_projet.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import helloandroid.ut3.mini_projet.R;
import helloandroid.ut3.mini_projet.models.Restaurant;
import helloandroid.ut3.mini_projet.models.Review;
import helloandroid.ut3.mini_projet.services.PhotoService;
import helloandroid.ut3.mini_projet.services.ReviewService;

public class DetailsActivity extends AppCompatActivity {

    String restaurantId;
    Restaurant restaurant;
    PhotoService photoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        photoService = new PhotoService(getApplicationContext());
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Map<String, ArrayList<Long>> horaires = (Map<String, ArrayList<Long>>) extras.getSerializable("Horaires");

        ImageView image = findViewById(R.id.restaurantImage);
        TextView title = findViewById(R.id.restaurantName);
        TextView type = findViewById(R.id.restaurantType);
        TextView address = findViewById(R.id.restaurantAddress);
        TextView desc = findViewById(R.id.restaurantDescription);
        TextView nextHoraire = findViewById(R.id.restaurantHoraire);
        TextView note = findViewById(R.id.restaurantNote);

        restaurant= (Restaurant) intent.getExtras().getSerializable("Restaurant");
        restaurantId = restaurant.getId();
        String[] photos =restaurant.getPhotos();

        photoService.setPhoto(photos[0],image);
        title.setText(restaurant.getNom());
        type.setText(restaurant.getType());
        address.setText(restaurant.getAddress());
        desc.setText(restaurant.getDescription());
        nextHoraire.setText(intent.getStringExtra("HoraireDuJour"));
        note.setText(String.valueOf(restaurant.getNote()));

        Button showReviewsButton = findViewById(R.id.showReviewsButton);
        showReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadReviews();
            }
        });

        FloatingActionButton btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    findViewById(R.id.reviewBtn).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
            intent.putExtra("restaurantId", restaurantId);
            startActivity(intent);
        }
    });
        Button reservationButton = findViewById(R.id.reservationButton);
        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, ReservationActivity.class);

                Bundle bundle = new Bundle();
                System.out.println("in details"+ horaires);
                System.out.println(restaurant.getHoraires());
                bundle.putSerializable("Horaires", (Serializable) restaurant.getHoraires());
                intent.putExtras(bundle);
                System.out.println();

                startActivity(intent);
            }
        });
    findViewById(R.id.position).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
            intent.putExtra("selected_restaurant", restaurant);
            intent.putExtra("open_map_activity", true);
            startActivity(intent);
        }
    });

    }


    public void showHorairesPopup(View view) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.horaires_popup);
        dialog.setTitle("Horaires");

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Map<String, ArrayList<Long>> horaires =restaurant.getHoraires();

        List<String> horaireItems = new ArrayList<>();

        String[] joursDeLaSemaine = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};

        for (String jour : joursDeLaSemaine) {
            ArrayList<Long> value = horaires.get(jour);
            if (value != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(jour).append(": ");

                if (value.size() > 2) {
                    for (int i = 0; i < value.size(); i += 2) {
                        sb.append(String.format("%02d", value.get(i))).append("h - ").append(String.format("%02d", value.get(i + 1))).append("h");

                        if (i < value.size() - 2) {
                            sb.append(", ");
                        }
                    }
                } else {
                    sb.append(String.format("%02d", value.get(0))).append("h - ").append(String.format("%02d", value.get(1))).append("h");
                }

                horaireItems.add(sb.toString());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.horaire_item, R.id.horaireText, horaireItems) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView bullet = view.findViewById(R.id.bullet);
                bullet.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };

        ListView horairesListView = dialog.findViewById(R.id.horairesListView);
        horairesListView.setAdapter(adapter);

        dialog.show();
    }


    public void loadReviews(){
        ReviewService reviewService = new ReviewService();
        LinearLayout reviewsContainer = findViewById(R.id.reviewsContainer);
        reviewService.getAllReviewsByRestaurantId(restaurantId, new ReviewService.OnReviewsCallback() {

            @Override
            public void onSuccess(List<Review> reviews) {
                findViewById(R.id.showReviewsButton).setVisibility(View.INVISIBLE);
                if (!reviews.isEmpty()) {
                    for(Review review: reviews){
                        LayoutInflater inflater = getLayoutInflater();
                        View reviewView = inflater.inflate(R.layout.review_item, reviewsContainer, false);

                        TextView userNameTextView = reviewView.findViewById(R.id.userNameTextView);
                        TextView commentTextView = reviewView.findViewById(R.id.commentTextView);
                        ImageView userPicture = reviewView.findViewById(R.id.userImageView);
                        RatingBar note = reviewView.findViewById(R.id.ratingBar);
                        userNameTextView.setText(review.getUsername());
                        commentTextView.setText(review.getReview());
                        List<String> photos = review.getPhotos();
                        if (photos != null && !photos.isEmpty() && photos.get(0) != null && !photos.get(0).isEmpty()) {
                            photoService.setPhoto(photos.get(0), userPicture);
                        }

                        note.setRating(review.getRating());
                        FrameLayout frameLayout = reviewView.findViewById(R.id.frameLayout);
                        frameLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (photos != null && !photos.isEmpty() && photos.get(0) != null && !photos.get(0).isEmpty()) {
                                    showImageDialog(photos.get(0));
                                }
;
                            }
                        });
                        reviewsContainer.addView(reviewView);}
                } else {
                    LinearLayout reviewLayout = new LinearLayout(getApplicationContext());
                    reviewLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    reviewLayout.setOrientation(LinearLayout.VERTICAL);
                    TextView userNameTextView = new TextView(getApplicationContext());
                    userNameTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    userNameTextView.setText("Pas encore d'avis, soyez le premier !");
                    userNameTextView.setTextSize(18);
                    userNameTextView.setTypeface(null, Typeface.BOLD);
                    reviewLayout.addView(userNameTextView);
                    reviewsContainer.addView(reviewLayout);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                System.out.println("Error fetching reviews: " + errorMessage);
            }
        });
    }

    private void showImageDialog(String image) {
        Dialog dialog = new Dialog(DetailsActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_image);

        ImageView dialogImageView = dialog.findViewById(R.id.dialogImageView);
        photoService.setPhoto(image, dialogImageView);

        dialogImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}