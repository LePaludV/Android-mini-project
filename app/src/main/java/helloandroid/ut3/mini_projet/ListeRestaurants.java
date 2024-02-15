package helloandroid.ut3.mini_projet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import helloandroid.ut3.mini_projet.models.Restaurant;
import helloandroid.ut3.mini_projet.services.RestaurantsService;


public class ListeRestaurants extends Fragment {

    ListView restaurantListView;
    List<Restaurant> restaurants;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liste_restaurants, container, false);
        restaurantListView = view.findViewById(R.id.listview);
        restaurants = new ArrayList<>();

        RestaurantsService r = new RestaurantsService();
        CompletableFuture<ArrayList<Restaurant>> a = r.getAllRestaurants();
        a.thenAccept((res)->{
            restaurants.addAll(res);
            // Créez un adaptateur ArrayAdapter pour lier la liste à la ListView
            RestaurantAdapter adapter = new RestaurantAdapter(requireContext(), R.layout.item_layout, restaurants);
            restaurantListView.setAdapter(adapter);
        });

        return view;
    }

    private class RestaurantAdapter extends ArrayAdapter<Restaurant> {
        private Context context;
        private int resource;

        public RestaurantAdapter(Context context, int resource, List<Restaurant> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(resource, parent, false);

            Restaurant restaurant = getItem(position);

            TextView textViewRestaurantName = view.findViewById(R.id.textViewRestaurantName);

            textViewRestaurantName.setText(restaurant.getNom());

            view.setTag(position);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    Restaurant selectedRestaurant = getItem(position);

                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("Titre", selectedRestaurant.getNom());
                    intent.putExtra("Type", selectedRestaurant.getType());
                    intent.putExtra("Description", selectedRestaurant.getDescription());
                    intent.putExtra("Adresse", selectedRestaurant.getAddress());
                    intent.putExtra("Horaire", selectedRestaurant.getHoraire());
                    //intent.putExtra("Image", restaurant.getImage());

                    context.startActivity(intent);
                }
            });

            return view;
        }
    }
}

