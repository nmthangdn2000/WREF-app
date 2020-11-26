package thang.com.wref.Main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.Layer;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import thang.com.wref.R;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private final static String TAG = "MapFragment";
    private View view;
    private GoogleMap map;
    private MapView mapView;
    private KmlLayer layer;

    public MapFragment() {
        // Required empty public constructor
    }

//    public static MapFragment newInstance() {
//        MapFragment fragment = new MapFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);
        mappingView();

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
    private void mappingView(){
        mapView = (MapView) view.findViewById(R.id.map);

    }
    private void setupMap(){

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(16.051841, 108.168782);
        map = googleMap;

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        map.addMarker(new MarkerOptions()
            .position(latLng)
            .title("Nguyễn Minh Thắng")
            .snippet("Đẹp trai"));


//        InputStream path = getResources().openRawResource(R.raw.danang);
//        KmlLayer layer = createLayerFromKmz(path);
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
    private void setOnclickFeature(KmlLayer layer){
        layer.setOnFeatureClickListener(new Layer.OnFeatureClickListener() {
            @Override
            public void onFeatureClick(Feature feature) {
                Log.d(TAG, "onFeatureClick: "+feature.getPropertyKeys());
                Log.d(TAG, "onFeatureClick: "+feature.getId());
//                Log.d(TAG, "onFeatureClick: "+feature.getGeometry());
                Log.d(TAG, "onFeatureClick: "+feature.getProperties());

            }
        });
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

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}