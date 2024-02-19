package helloandroid.ut3.mini_projet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
                super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new ListeRestaurants();
            case 1 : return new MapActivity();
            default: return new ListeRestaurants();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
