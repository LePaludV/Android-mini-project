package helloandroid.ut3.mini_projet;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import helloandroid.ut3.mini_projet.services.RestaurantsService;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getIntent().getIntExtra("id", -1);

        RestaurantsService r = new RestaurantsService();

        setContentView(R.layout.activity_details);

        TextView title = findViewById(R.id.restaurantName);
        TextView type = findViewById(R.id.restaurantType);
        TextView desc = findViewById(R.id.restaurantDescription);
        TextView address = findViewById(R.id.restaurantAddress);
        TextView horaire= findViewById(R.id.restaurantHoraire);
        ImageView image = findViewById(R.id.restaurantImage);

        (r.getRestaurantById("FR2Tv4XMxkGK5rDCbAAx")).whenComplete((res, error) -> {
            if (error != null) {
                System.out.println("Exception occurred");
            } else {
                System.out.println(res);
                title.setText(res.getNom());
                type.setText(res.getType());
                desc.setText(res.getDescription());
                address.setText(res.getAddress());
                horaire.setText(res.getHoraire());
                StorageReference storageRef = (FirebaseStorage.getInstance()).getReference();

                StorageReference pathReference = storageRef.child(res.getPhotos()[0]);
                Glide.with(this.getApplicationContext())
                        .load(pathReference)
                        .into(image);
            }
        });
    }
}