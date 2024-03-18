package helloandroid.ut3.mini_projet.activity;

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
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

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

import helloandroid.ut3.mini_projet.CustomInfoWindow;
import helloandroid.ut3.mini_projet.R;
import helloandroid.ut3.mini_projet.models.Restaurant;
import helloandroid.ut3.mini_projet.services.RestaurantsService;

public class MapActivity extends Fragment {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private Context ctx = null;
    IMapController mapController;
    private RestaurantsService rs;
    private boolean centerOnRestaurant = false;
    Marker userMarker;
    public MapActivity(){
        this.rs = new RestaurantsService();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        var view = inflater.inflate(R.layout.map, container, false);
        map = view.findViewById(R.id.osmmap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        return view;
    }

    private boolean hasLocationPermissions() {
        return ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermissions() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
    }
    private void configureMap(GeoPoint restaurantCoordinates) {
        GeoPoint defaultPosition = new GeoPoint(43.60, 1.43);
        mapController = map.getController();
        mapController.setZoom(18.00);
        generateMarker(map);
        updateUserLocation();
        if (centerOnRestaurant && restaurantCoordinates != null) {
            mapController.setCenter(restaurantCoordinates);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configureMap(null);
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable location-related features)
            }
        }
    }
    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){

        map = (MapView) getView().findViewById(R.id.osmmap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        ctx = getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(ctx.getPackageName());
        Bundle arguments = getArguments();
        GeoPoint restaurantCoordinates=null;
        if (arguments != null) {
        Restaurant restaurant= (Restaurant) getArguments().getSerializable("Restaurant");
        if(restaurant!=null){
            centerOnRestaurant = true;
            restaurantCoordinates = restaurant.getCoordinates();
        }}
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        map.setMultiTouchControls(true);
        if (hasLocationPermissions()) {
            configureMap(restaurantCoordinates);
        } else {
            requestLocationPermissions();
        }

        Button updateButton = view.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserLocation();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }


    private void generateMarker(MapView map) {
        //user
        userMarker = new Marker(map);
        //userMarker.setPosition(startPoint);
        userMarker.setIcon( ctx.getDrawable(R.drawable.baseline_gps_fixed_24));
        userMarker.setInfoWindow(new CustomInfoWindow(R.layout.custom_info_window, map,null,rs));
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        map.getOverlays().add(userMarker);
        // restaurants
        RestaurantsService r = new RestaurantsService();

        CompletableFuture<ArrayList<Restaurant>> a = r.getAllRestaurants();
        a.thenAccept((res)->{
            res.forEach((restaurant)->{
                Marker restaurantMarker = new Marker(map);
                restaurantMarker.setPosition(restaurant.getCoordinates());
                restaurantMarker.setIcon(ctx.getDrawable(R.drawable.restaurant_24));
                restaurantMarker.setInfoWindow(new CustomInfoWindow(R.layout.custom_info_window, map,restaurant,rs));
                restaurantMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                map.getOverlays().add(restaurantMarker);
            });

        });

    }
    private void updateUserLocation() {
        if (hasLocationPermissions()) {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Task<Location> locationTask = fusedLocationClient.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    System.out.println(centerOnRestaurant);
                    GeoPoint position;
                    if (location != null) {
                        position=(new GeoPoint(location.getLatitude(), location.getLongitude()));
                    }
                    else{
                        position= new GeoPoint(43.60, 1.43);
                    }
                    if(!centerOnRestaurant){
                        mapController.setCenter(position);
                    }
                    centerOnRestaurant=false;
                    userMarker.setPosition(position);
                }
            });
        } else {
            requestLocationPermissions();
        }
    }


}