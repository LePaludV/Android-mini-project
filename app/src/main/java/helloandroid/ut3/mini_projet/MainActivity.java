package helloandroid.ut3.mini_projet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import helloandroid.ut3.mini_projet.models.Restaurant;
import helloandroid.ut3.mini_projet.services.RestaurantsService;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;

    private ListView restaurantListView;
    private ArrayList<String> restaurantNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RestaurantsService r= new RestaurantsService();
        CompletableFuture<ArrayList<Restaurant>> a = r.getAllRestaurants();
        a.thenAccept((res)->{
            System.out.println(res);
        });

        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager2);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

    }

    public void goToMap(View view){
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }


    // Méthode appelée lors du clic sur un item
    public void onItemClick(View view) {
        // Lancez une nouvelle activité ou effectuez toute autre action que vous souhaitez
        //Intent intent = new Intent(this, test.class);
        //startActivity(intent);
    }

}