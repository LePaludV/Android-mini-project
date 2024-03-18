package helloandroid.ut3.mini_projet.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import helloandroid.ut3.mini_projet.R;
import helloandroid.ut3.mini_projet.ViewPagerAdapter;
import helloandroid.ut3.mini_projet.models.Restaurant;
import helloandroid.ut3.mini_projet.services.RestaurantsService;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager2);
        viewPagerAdapter = new ViewPagerAdapter(this,new RestaurantsService());
        viewPager2.setOffscreenPageLimit(2);
        viewPager2.setUserInputEnabled(false);

        viewPager2.setAdapter(viewPagerAdapter);
        if (getIntent().getBooleanExtra("open_map_activity", false)) {
            Restaurant selectedRestaurant = (Restaurant) getIntent().getSerializableExtra("selected_restaurant");
            if (selectedRestaurant != null) {
                viewPagerAdapter.setSelectedRestaurant(selectedRestaurant);
                viewPager2.setCurrentItem(1);
                tabLayout.getTabAt(1).select();
            }
        }

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
}