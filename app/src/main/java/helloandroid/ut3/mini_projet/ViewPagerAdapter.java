package helloandroid.ut3.mini_projet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import helloandroid.ut3.mini_projet.activity.MapActivity;
import helloandroid.ut3.mini_projet.models.Restaurant;
import helloandroid.ut3.mini_projet.services.RestaurantsService;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private RestaurantsService rs;
    Restaurant selectedRestaurant;
    MapActivity mapActivity;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, RestaurantsService rs) {
                super(fragmentActivity);
                this.rs = rs;
    }

    public void setSelectedRestaurant(Restaurant selectedRestaurant) {
        this.selectedRestaurant = selectedRestaurant;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1 :
                mapActivity = new MapActivity();
                Bundle bundle = new Bundle();
                if (selectedRestaurant != null) {
                    bundle.putSerializable("Restaurant", selectedRestaurant);
                }
                mapActivity.setArguments(bundle);
                return mapActivity;
            default: return new ListeRestaurants();

        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
