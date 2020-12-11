package thang.com.wref.Main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.Layer;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


import thang.com.wref.R;
import thang.com.wref.fragment.DetailLocationMap;

public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private final static String TAG = "MapFragment";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private View view;
    private GoogleMap map;
    private MapView mapView;
    private KmlLayer layer;
    private Vector<LatLng> latLngs;
    private Polygon polygon;
    private SearchView searchView;
    private FusedLocationProviderClient client;
    private LinearLayout location, typeMap, showTypeMap, btnChangeMapGis, btnChangeGoogleMap, btnMapTemp, btnMapCloud, btnMapRain;
    private Task<Location> task;
    private Marker marker;
    private MeowBottomNavigation meowBottomNavigation;
    private FrameLayout frameInforTouchLocation;
    private ImageView iconsearch;
    private DetailLocationMap detailLocationMap;
    private Bundle savedInstanceStates;
    // gis
    private ArcGISMap mapGis;
    private ServiceFeatureTable serviceFeatureTable;
    private FeatureLayer layerGis;
    private String freatureLayerUrl = "https://services3.arcgis.com/U26uBjSD32d7xvm2/arcgis/rest/services/MyMapService_DaNang/FeatureServer/1";
    private FragmentTransaction fragmentTransaction;
    private SupportMapFragment mapFragment;
    private TileOverlay tileOverlay;

    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;
    private static final int COLOR_BLACK_ARGB = 0xff000000;

    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);

    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA =
            Arrays.asList(DOT, GAP, DASH, GAP);

    public MapFragment(MeowBottomNavigation meowBottomNavigation) {
        this.meowBottomNavigation = meowBottomNavigation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        latLngs = new Vector<>();
        if(Build.VERSION.SDK_INT >= 21){
            Window window = getActivity().getWindow();
            window.setStatusBarColor(getContext().getResources().getColor(R.color.purple_700));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);
        mappingView();
        savedInstanceStates = savedInstanceState;
        return view;
    }
    private void openGooogleMap(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        mapFragment = SupportMapFragment.newInstance();
        fragmentTransaction.replace(R.id.map, mapFragment, "googlemap").commit();
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        openGooogleMap();
        searchLocation();
    }

    private void mappingView() {
        mapView = (MapView) view.findViewById(R.id.map);
        searchView = (SearchView) view.findViewById(R.id.searchView);
        client = LocationServices.getFusedLocationProviderClient(getContext().getApplicationContext());
        location = (LinearLayout) view.findViewById(R.id.location);
        frameInforTouchLocation = (FrameLayout) view.findViewById(R.id.frameInforTouchLocation);
        iconsearch = (ImageView) view.findViewById(R.id.iconsearch);
        typeMap = (LinearLayout) view.findViewById(R.id.typeMap);
        showTypeMap = (LinearLayout) view.findViewById(R.id.showTypeMap);
        btnChangeMapGis = (LinearLayout) view.findViewById(R.id.btnChangeMapGis);
        btnChangeGoogleMap = (LinearLayout) view.findViewById(R.id.btnChangeGoogleMap);
        btnMapTemp = (LinearLayout) view.findViewById(R.id.btnMapTemp);
        btnMapCloud = (LinearLayout) view.findViewById(R.id.btnMapCloud);
        btnMapRain = (LinearLayout) view.findViewById(R.id.btnMapRain);

        btnMapTemp.setOnClickListener(this);
        btnMapCloud.setOnClickListener(this);
        btnMapRain.setOnClickListener(this);
        iconsearch.setOnClickListener(this);
        typeMap.setOnClickListener(this);
        location.setOnClickListener(this);
        btnChangeMapGis.setOnClickListener(this);
        btnChangeGoogleMap.setOnClickListener(this);
    }

    private void searchLocation() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                searchLocation(newText);
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(16.051841, 108.168782);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8));
        setupGoogleMap();
    }

    private void changeTypeMap(String mapName) {
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {

                String s = String.format("https://tile.openweathermap.org/map/%s/%d/%d/%d.png?appid=33e61186254ecc50e7d4298f5fb97f4d", mapName, zoom, x, y);
                if (!checkTileExists(x, y, zoom)) {
                    return null;
                }
                try {
                    return new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
            }
            private boolean checkTileExists(int x, int y, int zoom) {
                int minZoom = 1;
                int maxZoom = 20;

                return (zoom >= minZoom && zoom <= maxZoom);
            }
        };
        if (tileOverlay != null)
            tileOverlay.remove();
        tileOverlay = map.addTileOverlay(new TileOverlayOptions()
                .tileProvider(tileProvider));
    }

    private void setOnclickFeature(KmlLayer layer) {
        layer.setOnFeatureClickListener(new Layer.OnFeatureClickListener() {
            @Override
            public void onFeatureClick(Feature feature) {
                String addressName = feature.getProperties().toString();
                String[] parts = addressName.split("=");
                String part2 = parts[1]; // 034556
                searchLocation(method(part2));
                Log.d(TAG, "onFeatureClick: "+method(part2));
                ArrayList o = (ArrayList) feature.getGeometry().getGeometryObject();
                ArrayList<LatLng> b = (ArrayList<LatLng>) o.get(0);
                PolylineMap(b);

            }
        });
    }

    private void PolylineMap(ArrayList<LatLng> b) {
        latLngs.clear();
        for (LatLng latLng : b) {
            latLngs.add(new LatLng(latLng.latitude, latLng.longitude));
        }
        if (polygon != null)
            polygon.remove();
        polygon = map.addPolygon(new PolygonOptions()
                .clickable(true)
                .addAll(latLngs));
        polygon.setTag("alpha");
        stylePolygon(polygon);
    }

    private void searchLocation(String query) {
        List<Address> addressesList = null;
        if (query != null || !query.equals("")) {
            Geocoder geocoder = new Geocoder(getContext());
            try {
                addressesList = geocoder.getFromLocationName(query, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressesList != null && addressesList.size() > 0) {
                Address address = addressesList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                Log.d(TAG, "searchLocation: "+latLng);
                if(marker != null)
                    marker.remove();
                marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(query)
                        .snippet("Đẹp trai"));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                showDetailLocationMap(query);
            }
        }
    }
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
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
                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            }
        });
    }
    @Override
    public void onPause(){
        mapView.pause();
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        mapView.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.dispose();
    }
    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ']') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
    private void showDetailLocationMap(String query) {
        if(getFragmentManager().findFragmentByTag("detailLocationMap") != null)
            getFragmentManager().beginTransaction().remove(detailLocationMap).commit();
        detailLocationMap = new DetailLocationMap(getContext().getApplicationContext(), query);
        getFragmentManager().beginTransaction().add(R.id.frameInforTouchLocation, detailLocationMap, "detailLocationMap").commit();
        if(iconsearch.getTag().equals("location")){
            iconsearch.setTag("backMap");
            iconsearch.setImageResource(R.drawable.ic_baseline_arrow_back_24);
            Animation animationUp = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
            frameInforTouchLocation.startAnimation(animationUp);
            Animation animationDown = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_down);
            meowBottomNavigation.startAnimation(animationDown);
            meowBottomNavigation.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.location:
                if(showTypeMap.getVisibility() == View.VISIBLE)
                    showTypeMap.setVisibility(View.INVISIBLE);
                getLocationPermission();
                break;
            case R.id.iconsearch:
                if (polygon != null)
                    polygon.remove();
                if(showTypeMap.getVisibility() == View.VISIBLE)
                    showTypeMap.setVisibility(View.INVISIBLE);
                if(iconsearch.getTag().equals("backMap")){
                    iconsearch.setTag("location");
                    iconsearch.setImageResource(R.drawable.ic_baseline_location_on_24);
                    getFragmentManager().beginTransaction().remove(detailLocationMap).commit();
                    Animation animationDown = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_down);
                    frameInforTouchLocation.startAnimation(animationDown);
                    Animation animationUp = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
                    meowBottomNavigation.startAnimation(animationUp);
                    meowBottomNavigation.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.typeMap:
                if(showTypeMap.getVisibility() == View.VISIBLE)
                    showTypeMap.setVisibility(View.INVISIBLE);
                else showTypeMap.setVisibility(View.VISIBLE);
                break;
            case R.id.btnChangeGoogleMap:
                mapView.setMap(null);
                openGooogleMap();
                setupGoogleMap();
                showTypeMap.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnChangeMapGis:
                setupMapGis();
                showTypeMap.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnMapTemp:
                checkMap();
                changeTypeMap("temp_new" );
                showTypeMap.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnMapRain:
                checkMap();
                changeTypeMap("precipitation_new" );
                showTypeMap.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnMapCloud:
                checkMap();
                changeTypeMap("clouds_new" );
                showTypeMap.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }
    private void checkMap(){
        if(getFragmentManager().findFragmentByTag("googlemap") == null){
            mapView.setMap(null);
            openGooogleMap();
            setupGoogleMap();
        }
    }
    private void setupGoogleMap(){
        try {
            layer = new KmlLayer(map, R.raw.dananga, getContext().getApplicationContext());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        layer.addLayerToMap();

        setOnclickFeature(layer);
    }
    private void setupMapGis() {
        if(getFragmentManager().findFragmentByTag("googlemap") != null)
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
        if (mapView != null) {
            Basemap.Type basemapType = Basemap.Type.OPEN_STREET_MAP;
            double latitude = 16.048856; //16.048856, 108.201523
            double longitude = 108.201523;
            int levelOfDetail = 11;
            mapGis = new ArcGISMap(basemapType, latitude, longitude, levelOfDetail);
            serviceFeatureTable = new ServiceFeatureTable(freatureLayerUrl);
            layerGis = new FeatureLayer(serviceFeatureTable);

            mapGis.getOperationalLayers().add(layerGis);
            mapView.setMap(mapGis);
        }
    }
    private void stylePolygon(Polygon polygon) {
        String type = "";
        // Get the data object stored with the polygon.
        if (polygon.getTag() != null) {
            type = polygon.getTag().toString();
        }

        List<PatternItem> pattern = null;
        int strokeColor = COLOR_BLACK_ARGB;
        int fillColor = COLOR_WHITE_ARGB;

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "alpha":
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = COLOR_GREEN_ARGB;
                fillColor = COLOR_PURPLE_ARGB;
                break;
            case "beta":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_BETA;
                strokeColor = COLOR_ORANGE_ARGB;
                fillColor = COLOR_BLUE_ARGB;
                break;
        }

        polygon.setStrokePattern(pattern);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(strokeColor);
        polygon.setFillColor(fillColor);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if( requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }
    //    private KmlLayer createLayerFromKmz(InputStream kmzFileName) {
//        KmlLayer kml = null;
//
//        InputStream inputStream;
//        ZipInputStream zipInputStream;
//
//        try {
//            inputStream = kmzFileName;
//            zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream));
//            ZipEntry zipEntry;
//
//            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
//                if (!zipEntry.isDirectory()) {
//                    String fileName = zipEntry.getName();
//                    if (fileName.endsWith(".kml")) {
//                        kml = new KmlLayer(map, zipInputStream, getContext().getApplicationContext());
//                    }
//                }
//
//                zipInputStream.closeEntry();
//            }
//
//            zipInputStream.close();
//        }
//        catch(IOException e)
//        {
//            e.printStackTrace();
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        }
//
//        return kml;
//    }
}