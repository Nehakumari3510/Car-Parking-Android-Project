package com.example.carparkingapp.google_map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.carparkingapp.R;
import com.example.carparkingapp.register.RetrofitClient;
import com.example.carparkingapp.register.ApiInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private SearchView searchView;
    private LatLng currentLocation = null;
    private LatLng searchedLocation = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize Location Provider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize SearchView
        searchView = findViewById(R.id.mapSearch);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Check and request location permission
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getCurrentLocation();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE
        );
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        if (!checkPermissions()) return;

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(2000)
                .setNumUpdates(1); // Stop after first update

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null || locationResult.getLastLocation() == null) return;
                Location location = locationResult.getLastLocation();
                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                // Move camera to current location only if user has NOT searched for another location
                if (searchedLocation == null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                    fetchParkingLocations(currentLocation);
                }

                // Stop location updates (only need it once)
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private float getMarkerColor(double availabilityPercentage) {
        if (availabilityPercentage > 70) {
            return BitmapDescriptorFactory.HUE_GREEN;
        } else if (availabilityPercentage > 50) {
            return BitmapDescriptorFactory.HUE_YELLOW;
        } else if (availabilityPercentage > 10) {
            return BitmapDescriptorFactory.HUE_ORANGE;
        } else {
            return BitmapDescriptorFactory.HUE_RED;
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private void fetchParkingLocations(LatLng targetLocation) {
        if (targetLocation == null) return;

        Retrofit retrofit = RetrofitClient.getRetrofitInstance(this);
        ApiInterface apiService = retrofit.create(ApiInterface.class);

        apiService.getParkingLocations().enqueue(new Callback<List<ParkingLocation>>() {
            @Override
            public void onResponse(Call<List<ParkingLocation>> call, Response<List<ParkingLocation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ParkingLocation> allParkingList = response.body();
                    Log.d("Parking API", "Total parkings received: " + allParkingList.size());

                    for (ParkingLocation parking : allParkingList) {
                        Log.d("Parking API", "Name: " + parking.getParkingName() + ", Lat: " + parking.getLatitude() + ", Lng: " + parking.getLongitude());
                    }

                    List<ParkingLocation> nearbyParkingList = new ArrayList<>();
                    for (ParkingLocation parking : allParkingList) {
                        double distance = calculateDistance(targetLocation.latitude, targetLocation.longitude, parking.getLatitude(), parking.getLongitude());

                        if (distance <= 3) { // Check distance filter
                            nearbyParkingList.add(parking);
                        }
                    }

                    Log.d("Nearby Parking", "Total nearby parkings: " + nearbyParkingList.size());
                    updateMapWithParking(nearbyParkingList);
                }
            }

            @Override
            public void onFailure(Call<List<ParkingLocation>> call, Throwable t) {
                Log.e("DEBUG", "API call failed: " + t.getMessage());
            }
        });
    }

    private void updateMapWithParking(List<ParkingLocation> parkingList) {
        if (mMap == null) return;

        mMap.clear(); // Clear old markers

        for (ParkingLocation parking : parkingList) {
            LatLng parkingLatLng = new LatLng(parking.getLatitude(), parking.getLongitude());
            double availabilityPercentage = (double) parking.getAvailableSlots() / parking.getTotalSlots() * 100;
            float markerColor = getMarkerColor(availabilityPercentage);

            mMap.addMarker(new MarkerOptions()
                    .position(parkingLatLng)
                    .title(parking.getParkingName())
                    .snippet("Available: " + parking.getAvailableSlots() + "/" + parking.getTotalSlots())
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));
        }
    }

    private void searchLocation(String locationName) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                searchedLocation = new LatLng(address.getLatitude(), address.getLongitude());

                // ✅ Log searched location coordinates
                Log.d("Search Location", "Latitude: " + searchedLocation.latitude + ", Longitude: " + searchedLocation.longitude);

                // Move camera to searched location
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchedLocation, 16));

                // Fetch parkings near searched location
                fetchParkingLocations(searchedLocation);
            } else {
                Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_SHORT).show();
                Log.e("Search Error", "Location not found for query: " + locationName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Search Error", "Error fetching location: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Error fetching location", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (checkPermissions()) {
            mMap.setMyLocationEnabled(true);
            getCurrentLocation();
        } else {
            requestPermissions();
        }
    }
}
