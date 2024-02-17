package helloandroid.ut3.mini_projet;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import helloandroid.ut3.mini_projet.models.Restaurant;
import helloandroid.ut3.mini_projet.services.RestaurantsService;

public class MapActivity extends Fragment {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    private MapView map = null;

    private Context ctx = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.map, container, false);
        ctx = getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = (MapView) view.findViewById(R.id.osmmap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permission = new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION};
            requestPermissionsIfNecessary(permission);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        GeoPoint startPoint = new GeoPoint(43.60, 1.43);
        if (location != null) startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        mapController.setZoom(18.00);
        mapController.setCenter(startPoint);
        generateMarker(map,startPoint);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this.getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(ctx, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this.getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    private void generateMarker(MapView map, GeoPoint startPoint) {
        //user
        Marker userMarker = new Marker(map);
        userMarker.setPosition(startPoint);
        userMarker.setIcon( ctx.getDrawable(R.drawable.baseline_gps_fixed_24));
        userMarker.setInfoWindow(new CustomInfoWindow(R.layout.custom_info_window, map,null));
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        map.getOverlays().add(userMarker);
        // restaurants
        RestaurantsService r = new RestaurantsService();

        CompletableFuture<ArrayList<Restaurant>> a = r.getAllRestaurants();
        a.thenAccept((res)->{
            res.forEach((restaurant)->{
                System.out.println(restaurant.getCoordinates().toString());
                Marker restaurantMarker = new Marker(map);
                restaurantMarker.setPosition(restaurant.getCoordinates());
                restaurantMarker.setIcon(ctx.getDrawable(R.drawable.restaurant_24));
                restaurantMarker.setInfoWindow(new CustomInfoWindow(R.layout.custom_info_window, map,restaurant));
                restaurantMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                map.getOverlays().add(restaurantMarker);
            });

        });

    }
}