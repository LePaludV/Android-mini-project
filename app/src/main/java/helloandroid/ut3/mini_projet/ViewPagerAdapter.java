package helloandroid.ut3.mini_projet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import helloandroid.ut3.mini_projet.activity.MapActivity;
import helloandroid.ut3.mini_projet.services.RestaurantsService;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private RestaurantsService rs;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, RestaurantsService rs) {
                super(fragmentActivity);
                this.rs = rs;
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1 : return new MapActivity();
            default: return new ListeRestaurants();

        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
