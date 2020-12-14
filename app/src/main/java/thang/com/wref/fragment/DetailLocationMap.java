package thang.com.wref.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.wref.Adapter.DetailLocationMapAdapter;
import thang.com.wref.IconWeather;
import thang.com.wref.Login.SharedPreferencesManagement;
import thang.com.wref.Models.Weather.currentWeather;
import thang.com.wref.Models.WeatherLocationModel;
import thang.com.wref.NetworkUtil;
import thang.com.wref.R;
import thang.com.wref.Retrofits.LocationRetrofit;

public class DetailLocationMap extends Fragment {
    private static final String TAG = "DetailLocationMap";

    private View view;
    private RecyclerView rcvInfTouchLocation;
    private DetailLocationMapAdapter detailLocationMapAdapter;
    private TextView txtNameLocation, txtDesLocation;
    private ArrayList<String> arrImg;
    private Context context;
    private Retrofit retrofit;
    private NetworkUtil networkUtil;
    private String nameLocation="", address ="", addressLine="";

    private SharedPreferencesManagement sharedPreferencesManagement;
    private LocationRetrofit locationRetrofit;
    private float lati=0, longti =0;
    private currentWeather weatherCurrent;
    private IconWeather iconWeather;
    private ImageView imgWeather;
    private LinearLayout notImg;
    private LottieAnimationView lottieLoadingData;
    private RelativeLayout rltData;


    public DetailLocationMap(Context context, String nameLocation, String address, float lati, float longti) {
        this.context = context;
        this.nameLocation = nameLocation;
        this.address = address;
        this.lati = lati;
        this.longti = longti;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrImg = new ArrayList<>();
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        iconWeather = new IconWeather();
        sharedPreferencesManagement = new SharedPreferencesManagement(context);
        String[] strings = address.split(", ");
        addressLine = strings[0];

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_location_maps, container, false);
        mappingView();
        setUpRecyclerView();
        getData();
        return view;
    }

    private void mappingView() {
        rcvInfTouchLocation = (RecyclerView) view.findViewById(R.id.rcvInfTouchLocation);
        txtNameLocation = (TextView) view.findViewById(R.id.txtNameLocation);
        txtDesLocation = (TextView) view.findViewById(R.id.txtDesLocation);
        imgWeather = (ImageView) view.findViewById(R.id.imgWeather);
        notImg = (LinearLayout) view.findViewById(R.id.notImg);
        lottieLoadingData = (LottieAnimationView) view.findViewById(R.id.lottieLoadingData);
        rltData = (RelativeLayout) view.findViewById(R.id.rltData);

        txtNameLocation.setText(address);

    }

    private void setUpRecyclerView() {
        rcvInfTouchLocation.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                context, RecyclerView.HORIZONTAL, false
        );
        rcvInfTouchLocation.setLayoutManager(linearLayoutManager);

    }
    private void getData(){
        locationRetrofit = retrofit.create(LocationRetrofit.class);
        Call<WeatherLocationModel> weatherLocationModelCall = locationRetrofit.getWeatherLocation(sharedPreferencesManagement.getTOKEN(), addressLine, lati, longti);
        weatherLocationModelCall.enqueue(new Callback<WeatherLocationModel>() {
            @Override
            public void onResponse(Call<WeatherLocationModel> call, Response<WeatherLocationModel> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    WeatherLocationModel weatherLocationModel = response.body();

                    Log.d(TAG, "onResponse: "+weatherLocationModel.getMedia());
                    for (int i = 0; i < weatherLocationModel.getMedia().length; i++){
                        arrImg.add(weatherLocationModel.getMedia()[i]);
                    }
                    if(arrImg.size() == 0){
                        notImg.setVisibility(View.VISIBLE);
                        rcvInfTouchLocation.setVisibility(View.INVISIBLE);
                    }else{
                        notImg.setVisibility(View.GONE);
                        rcvInfTouchLocation.setVisibility(View.VISIBLE);
                    }
                    detailLocationMapAdapter.notifyDataSetChanged();
                    txtDesLocation.setText(""+iconWeather.typeWeather(weatherLocationModel.getWeather().getCurrent()
                            .getWeather()[0].getDescription())+" "+(int) (weatherLocationModel.getWeather().getCurrent().getTemp()-273)+"/"+
                            (int) (weatherLocationModel.getWeather().getCurrent().getFeels_like() - 273)+"°C");
                    imgWeather.setImageResource(iconWeather.IconWeather(weatherLocationModel.getWeather().getCurrent()
                            .getWeather()[0].getMain()));
                }
                call.cancel();
                lottieLoadingData.pauseAnimation();
                lottieLoadingData.setVisibility(View.GONE);
                rltData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<WeatherLocationModel> call, Throwable t) {
                call.cancel();
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        detailLocationMapAdapter = new DetailLocationMapAdapter(context, arrImg);
        rcvInfTouchLocation.setAdapter(detailLocationMapAdapter);
    }
}
