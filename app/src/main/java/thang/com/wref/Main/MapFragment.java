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
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.Layer;
import com.google.maps.android.data.kml.KmlGroundOverlay;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import thang.com.wref.R;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private final static String TAG = "MapFragment";
    private View view;
    private GoogleMap map;
    private MapView mapView;
    private KmlLayer layer;
    private Vector<LatLng> latLngs;
    private Polygon polygon;

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
        latLngs = new Vector<>();
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

                Log.d(TAG, "onFeatureClick: "+feature.getProperties());

                ArrayList o = (ArrayList) feature.getGeometry().getGeometryObject();
                ArrayList<LatLng> b = (ArrayList<LatLng>) o.get(0);
                PolylineMap(b);
//                Log.d(TAG, "onFeatureClickaa: "+feature.);
//                Log.d(TAG, "onFeatureClickaa: "+feature.getGe);
            }
        });
    }
    private void PolylineMap( ArrayList<LatLng> b){
        latLngs.clear();
        for (LatLng latLng : b){
            latLngs.add(new LatLng(latLng.latitude,  latLng.longitude));
        }
        if(polygon != null)
            polygon.remove();
        Log.d(TAG, "PolylineMap: "+latLngs);
        polygon = map.addPolygon(new PolygonOptions()
                .clickable(true)
                .addAll(latLngs));
        polygon.setTag("alpha");
        stylePolygon(polygon);
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
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
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