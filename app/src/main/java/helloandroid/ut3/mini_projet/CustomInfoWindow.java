package helloandroid.ut3.mini_projet;

import android.widget.TextView;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class CustomInfoWindow extends MarkerInfoWindow {

    private TextView textView;

    public CustomInfoWindow(int layoutResId, MapView mapView,boolean isRestaurant) {
        super(layoutResId, mapView);
        textView = (TextView) mView.findViewById(R.id.info_window_text);
    }

    @Override
    public void onOpen(Object item) {
        // Mettez à jour le contenu de votre vue ici en fonction du marqueur cliqué
        if (item instanceof Marker) {
            Marker marker = (Marker) item;
            textView.setText("Vous êtes ici !\nLatitude: " + marker.getPosition().getLatitude() +
                    "\nLongitude: " + marker.getPosition().getLongitude());
        }

    }

    @Override
    public void onClose() {
    }
}
