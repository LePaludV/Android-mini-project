package helloandroid.ut3.mini_projet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import helloandroid.ut3.mini_projet.models.Restaurant;
import helloandroid.ut3.mini_projet.services.RestaurantsService;


public class ListeRestaurants extends Fragment {

    ListView restaurantListView;
    List<String> restaurantNames;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_liste_restaurants, container, false);
        restaurantListView = view.findViewById(R.id.listview);
        restaurantNames = new ArrayList<>();
        RestaurantsService r = new RestaurantsService();
        CompletableFuture<ArrayList<Restaurant>> a = r.getAllRestaurants();
        a.thenAccept((res)->{
            res.forEach((restaurant)->{
                System.out.println(restaurant.getCoordinates().toString());
                restaurantNames.add(restaurant.getNom());
            });
            // Créez un adaptateur ArrayAdapter pour lier la liste à la ListView
            ArrayAdapter<String> adapter = new CustomAdapter(requireContext(), R.layout.item_layout, restaurantNames);
            // Liez l'adaptateur à la ListView
            restaurantListView.setAdapter(adapter);
        });




        // Inflate the layout for this fragment
        return view;
    }

    private class CustomAdapter extends ArrayAdapter<String> {
        private Context context;
        private int resource;

        public CustomAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);

            // Utilisez le layout personnalisé pour chaque élément de la liste
            View view = inflater.inflate(resource, parent, false);

            // Récupérez les vues du layout personnalisé
            TextView textViewRestaurantName = view.findViewById(R.id.textViewRestaurantName);

            // Récupérez le nom du restaurant à partir de la liste
            String restaurantName = getItem(position);

            // Configurez les vues avec les données appropriées
            textViewRestaurantName.setText(restaurantName);

            // Ajoutez le code pour le bouton (si nécessaire)

            return view;
        }
    }

}