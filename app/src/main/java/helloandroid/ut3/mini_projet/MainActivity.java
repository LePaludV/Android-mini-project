package helloandroid.ut3.mini_projet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import helloandroid.ut3.mini_projet.services.RestaurantsService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RestaurantsService r= new RestaurantsService();
        r.getAllRestaurants().toString();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}