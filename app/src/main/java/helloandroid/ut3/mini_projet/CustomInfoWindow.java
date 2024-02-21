package helloandroid.ut3.mini_projet;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import helloandroid.ut3.mini_projet.models.Restaurant;
import helloandroid.ut3.mini_projet.services.RestaurantsService;

public class CustomInfoWindow extends MarkerInfoWindow {

    private TextView title;
    private TextView description;
    private TextView address;
    private Button btn;

    RestaurantsService rs;
    Restaurant restaurant = null;
    public CustomInfoWindow(int layoutResId, MapView mapView, Restaurant restaurant, RestaurantsService rs) {
        super(layoutResId, mapView);
        this.restaurant=restaurant;
        title = (TextView) mView.findViewById(R.id.titre);
        description = (TextView) mView.findViewById(R.id.description);
        address = (TextView) mView.findViewById(R.id.address);
        btn = (Button) mView.findViewById(R.id.button2);
        this.rs=rs;

    }

    @Override
    public void onOpen(Object item) {
        // Mettez à jour le contenu de votre vue ici en fonction du marqueur cliqué
        if (item instanceof Marker) {
            Marker marker = (Marker) item;
            if(restaurant == null){
                title.setText("Vous êtes ici !\nLatitude: " + marker.getPosition().getLatitude() +
                        "\nLongitude: " + marker.getPosition().getLongitude());
            }
            else{
                title.setText(restaurant.getNom());
                description.setText(restaurant.getDescription()+"\n"+restaurant.getHoraire());
                address.setText(restaurant.getAddress());
                btn.setVisibility(View.VISIBLE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context= v.getContext();
                        System.out.println("ON CLICK");
                       context.startActivity(rs.goToDetails(context,restaurant));
                    }
                });
            }
            }

        }

    }

