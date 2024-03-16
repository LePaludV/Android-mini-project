package helloandroid.ut3.mini_projet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import helloandroid.ut3.mini_projet.models.Restaurant;
import helloandroid.ut3.mini_projet.services.RestaurantsService;


public class ListeRestaurants extends Fragment {

    RestaurantsService rs;
    ListView restaurantListView;
    List<Restaurant> restaurants;

    public ListeRestaurants(){
        this.rs=new RestaurantsService();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liste_restaurants, container, false);
        restaurantListView = view.findViewById(R.id.listview);
        restaurants = new ArrayList<>();

        CompletableFuture<ArrayList<Restaurant>> a = this.rs.getAllRestaurants();
        a.thenAccept((res)->{
            restaurants.addAll(res);
            RestaurantAdapter adapter = new RestaurantAdapter(requireContext(), R.layout.restaurant_item_layout, restaurants, this.rs);
            restaurantListView.setAdapter(adapter);
        });

        return view;
    }

    private class RestaurantAdapter extends ArrayAdapter<Restaurant> {
        private Context context;
        RestaurantsService rs;
        private int resource;

        public RestaurantAdapter(Context context, int resource, List<Restaurant> objects, RestaurantsService rs) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            this.rs=rs;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(resource, parent, false);
            Restaurant restaurant = getItem(position);

            TextView textViewRestaurantName = view.findViewById(R.id.textViewRestaurantName);
            textViewRestaurantName.setText(restaurant.getNom());

            TextView textViewRestaurantStatus = view.findViewById(R.id.textViewRestaurantStatus);

            String hoursString = restaurant.getHoursString();

            RatingBar ratingBar = view.findViewById(R.id.ratingBar);
            ratingBar.setRating(restaurant.getNote());

            Calendar calendar = Calendar.getInstance();
            if (restaurant.isOpen(calendar)) {
                textViewRestaurantStatus.setText("Ouvert actuellement : " + hoursString);
                textViewRestaurantStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            } else {
                textViewRestaurantStatus.setText("Fermé actuellement : " + hoursString);
                textViewRestaurantStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            }

            view.setTag(position);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    Restaurant selectedRestaurant = getItem(position);
                    context.startActivity(rs.goToDetails(context,selectedRestaurant));
                }
            });

            return view;
        }
    }
}

