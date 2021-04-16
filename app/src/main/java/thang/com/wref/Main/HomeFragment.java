package thang.com.wref.Main;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

import thang.com.wref.Login.LoginActivity;
import thang.com.wref.Login.SharedPreferencesManagement;
import thang.com.wref.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private static final String TAG = "HomeFragment";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap map;
    private View view, gradientMap;
    private Task<Location> task;
    private FusedLocationProviderClient client;
    private SharedPreferencesManagement sharedPreferencesManagement;
    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        sharedPreferencesManagement = new SharedPreferencesManagement(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        client = LocationServices.getFusedLocationProviderClient(getContext().getApplicationContext());
        mappingView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        openGooogleMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        getLocationPermission();
    }
    private void openGooogleMap(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        fragmentTransaction.replace(R.id.map, mapFragment, "googlemap").commit();
        mapFragment.getMapAsync(this);
    }
    private void mappingView(){
        gradientMap = (View) view.findViewById(R.id.gradientMap);

        gradientMap.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gradientMap:
                clickMapView();
                break;
            default:
                break;
        }
    }
    private void clickMapView(){
        Intent intent = new Intent(getContext(), MapActivity.class);
        getActivity().startActivity(intent);
    }
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            task = client.getLastLocation();
            getCurrentLocation();
            return;
        }else{
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getCurrentLocation() {
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    float lati = (float) location.getLatitude();
                    float longti = (float) location.getLongitude();
                    List<Address> addressesList = null;
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        addressesList = geocoder.getFromLocation(lati, longti, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "onSuccess: " + addressesList.get(0).getAdminArea());
                    String addressLine = addressesList.get(0).getAddressLine(0);

                    sharedPreferencesManagement.setLocation(lati, longti, addressLine);
                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            }
        });
    }


}