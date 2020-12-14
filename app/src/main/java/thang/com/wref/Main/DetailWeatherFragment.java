package thang.com.wref.Main;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.wref.Date.TimeCaculater;
import thang.com.wref.IconWeather;
import thang.com.wref.Login.SharedPreferencesManagement;
import thang.com.wref.Models.DetailWeatherModel;
import thang.com.wref.Models.Weather.dailyWeather;
import thang.com.wref.NetworkUtil;
import thang.com.wref.R;
import thang.com.wref.Retrofits.WeatherRetrofit;

public class DetailWeatherFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "DetailWeatherFragment";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private LineChart chart ;
    private ArrayList<Entry> entries;
    private ArrayList<String> sDay;

    private View view;
    private ImageView imgSunWeather;
    private AnimatedVectorDrawable animation;
    private RelativeLayout rltSunWeather, rltDuBao5Ngay, bacgroundImgWeather, bacgroundColorWeather;
    private TextView txtNhietdo, txtDesWeather, txtWeatherCurrent, txtWeatherNextOne, txtWeatherNextTwo,
            txtDataNhietdo1, txtDataNhietdo2, txtDataNhietdo3, txtDetailGio, txtDetailNhietdo, txtDetailUVo, txtDetailApsuat,
            txtNameLocation;

    private Path path, path2;
    private int night = 0;
    private int morning = 0;

    private NetworkUtil networkUtil;
    private Retrofit retrofit;
    private WeatherRetrofit weatherRetrofit;
    private SharedPreferencesManagement sharedPreferencesManagement;
    private DetailWeatherModel detailWeatherModel;
    private ArrayList<dailyWeather> dailyWeathers;
    private IconWeather iconWeather;
    private TimeCaculater timeUtil;
    private ImageView imhWeather1, imhWeather2, imhWeather3;

    private Calendar cal;
    private int oneHourBack;

    private LocationManager locationManager;

    public DetailWeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            Window window = getActivity().getWindow();
            window.setStatusBarColor(getContext().getResources().getColor(R.color.purple_700));
        }
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        sharedPreferencesManagement = new SharedPreferencesManagement(getContext());
        dailyWeathers = new ArrayList<>();
        iconWeather = new IconWeather();
        timeUtil = new TimeCaculater();
        weatherRetrofit = retrofit.create(WeatherRetrofit.class);

        cal = Calendar.getInstance();
// remove next line if you're always using the current time.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail_weather, container, false);
        night = ContextCompat.getColor(getContext(), R.color.nig_color);
        morning = ContextCompat.getColor(getContext(), R.color.mor_color);
        mapingView();
        return view;
    }
    private void mapingView(){
        rltSunWeather = (RelativeLayout) view.findViewById(R.id.rltSunWeather);
        rltDuBao5Ngay = (RelativeLayout) view.findViewById(R.id.rltDuBao5Ngay);
        chart  = (LineChart) view.findViewById(R.id.combinedChart);
        imgSunWeather = (ImageView) view.findViewById(R.id.imgSunWeather);
        txtNhietdo = (TextView) view.findViewById(R.id.txtNhietdo);
        txtDesWeather = (TextView) view.findViewById(R.id.txtDesWeather);
        bacgroundImgWeather = (RelativeLayout) view.findViewById(R.id.bacgroundImgWeather);
        bacgroundColorWeather = (RelativeLayout) view.findViewById(R.id.bacgroundColorWeather);
        txtWeatherCurrent = (TextView) view.findViewById(R.id.txtWeatherCurrent);
        txtWeatherNextOne = (TextView) view.findViewById(R.id.txtWeatherNextOne);
        txtWeatherNextTwo = (TextView) view.findViewById(R.id.txtWeatherNextTwo);
        txtDataNhietdo1 = (TextView) view.findViewById(R.id.txtDataNhietdo1);
        txtDataNhietdo2 = (TextView) view.findViewById(R.id.txtDataNhietdo2);
        txtDataNhietdo3 = (TextView) view.findViewById(R.id.txtDataNhietdo3);
        txtDetailGio = (TextView) view.findViewById(R.id.txtDetailGio);
        txtDetailNhietdo = (TextView) view.findViewById(R.id.txtDetailNhietdo);
        txtDetailUVo = (TextView) view.findViewById(R.id.txtDetailUVo);
        txtDetailApsuat = (TextView) view.findViewById(R.id.txtDetailApsuat);
        imhWeather1 = (ImageView) view.findViewById(R.id.imhWeather1);
        imhWeather2 = (ImageView) view.findViewById(R.id.imhWeather2);
        imhWeather3 = (ImageView) view.findViewById(R.id.imhWeather3);
        txtNameLocation = (TextView) view.findViewById(R.id.txtNameLocation);

        rltDuBao5Ngay.setOnClickListener(this);
        txtNhietdo.setOnClickListener(this);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        entries = new ArrayList<>();
        sDay = new ArrayList<>();
//        setUpData();


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtNhietdo:
                animationSundown();
                break;
            case R.id.rltDuBao5Ngay:
                animationSunrise();
                break;
            default:
                break;
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            getData();
            getData24h();
            txtNameLocation.setText(sharedPreferencesManagement.getADDRESS());
        }
    }

    private void setDataIn5Day(){
        txtWeatherCurrent.setText(timeUtil.dayStringFormat(detailWeatherModel.getCurrent().getDt())+"-"+iconWeather.typeWeather(detailWeatherModel.getCurrent().getWeather()[0].getDescription()));
        txtWeatherNextOne.setText(timeUtil.dayStringFormat(dailyWeathers.get(0).getDt())+"-"+iconWeather.typeWeather(dailyWeathers.get(0).getWeather()[0].getDescription()));
        txtWeatherNextTwo.setText(timeUtil.dayStringFormat(dailyWeathers.get(1).getDt())+"-"+iconWeather.typeWeather(dailyWeathers.get(1).getWeather()[0].getDescription()));

        txtDataNhietdo1.setText(iconWeather.Temp(detailWeatherModel.getCurrent().getTemp(), detailWeatherModel.getCurrent().getFeels_like()));
        txtDataNhietdo2.setText(iconWeather.Temp(dailyWeathers.get(0).getTemp().getMax(), dailyWeathers.get(0).getTemp().getMin()));
        txtDataNhietdo3.setText(iconWeather.Temp(dailyWeathers.get(1).getTemp().getMax(), dailyWeathers.get(1).getTemp().getMin()));

        txtDetailGio.setText(detailWeatherModel.getCurrent().getWind_speed()+" km/h");
        txtDetailNhietdo.setText((int) (detailWeatherModel.getCurrent().getTemp() - 273)+"°C");
        txtDetailUVo.setText(""+detailWeatherModel.getCurrent().getUvi());
        txtDetailApsuat.setText(detailWeatherModel.getCurrent().getPressure()+" mb");

        imhWeather1.setImageResource(iconWeather.IconWeather(detailWeatherModel.getCurrent().getWeather()[0].getMain()));
        imhWeather2.setImageResource(iconWeather.IconWeather(dailyWeathers.get(0).getWeather()[0].getMain()));
        imhWeather3.setImageResource(iconWeather.IconWeather(dailyWeathers.get(1).getWeather()[0].getMain()));
    }
    private void getData(){
        Call<DetailWeatherModel> detailWeatherModelCall = weatherRetrofit.getWeather(sharedPreferencesManagement.getTOKEN(), sharedPreferencesManagement.getLAT(), sharedPreferencesManagement.getLONG());
        detailWeatherModelCall.enqueue(new Callback<DetailWeatherModel>() {
            @Override
            public void onResponse(Call<DetailWeatherModel> call, Response<DetailWeatherModel> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
                }else{
                    detailWeatherModel = response.body();
                    Log.d(TAG, "onResponse: lozzzz"+detailWeatherModel.getCurrent().getWeather()[0].getDescription());
                    for (int i = 0; i < detailWeatherModel.getDaily().length; i++){
                        dailyWeathers.add(detailWeatherModel.getDaily()[i]);
                    }
                    setDataIn5Day();
                    showData();
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<DetailWeatherModel> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                call.cancel();
            }
        });

    }
    private void getData24h(){
        Call<DetailWeatherModel> detailWeatherModelCall = weatherRetrofit.getWeather24h(sharedPreferencesManagement.getTOKEN(), sharedPreferencesManagement.getLAT(), sharedPreferencesManagement.getLONG());
        detailWeatherModelCall.enqueue(new Callback<DetailWeatherModel>() {
            @Override
            public void onResponse(Call<DetailWeatherModel> call, Response<DetailWeatherModel> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getContext(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
                }else{
                    entries.clear();
                    sDay.clear();

                    detailWeatherModel = response.body();
                    entries.add(new Entry(0, detailWeatherModel.getCurrent().getFeels_like()-273, "°C"));
                    sDay.add("Bây giờ");

                    for (int i = 0; i < detailWeatherModel.getHourly().length-24; i++){
                        cal.setTime(new Date(detailWeatherModel.getHourly()[i].getDt()*1000));
                        cal.add(Calendar.HOUR, -1);
                        oneHourBack = cal.getTime().getHours();
                        entries.add(new Entry(i+1, detailWeatherModel.getHourly()[i].getTemp()-273, "°C"));
                        if(oneHourBack > 9 )
                            sDay.add(oneHourBack+":00");
                        else sDay.add("0"+oneHourBack+":00");
                    }
                    setupChar();
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<DetailWeatherModel> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                call.cancel();
            }
        });
    }
    private void showData(){
        float f = detailWeatherModel.getCurrent().getTemp();
        float c = f - 273;
        Log.d(TAG, "onClick: "+c);
        String val = "";
        if(detailWeatherModel.getCurrent().getWeather()[0].getDescription().equals("light rain"))
            val = "Mưa nhỏ";
        txtDesWeather.setText(val);
        txtNhietdo.setText((int) c+"°C");
    }
    private void setupChar(){
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setEnabled(false);

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(sDay));
        xAxis.setTextSize(14f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        chart.getDescription().setEnabled(false);
        chart.setScaleEnabled(false);
        chart.setExtraOffsets(5f,5f,5f,5f);

        LineDataSet lineDataSet = new LineDataSet(entries, "Data set 1");
        lineDataSet.setFillAlpha(35);
        lineDataSet.setValueTextSize(12f);
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setColor(Color.WHITE);
        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value)) + "°C";
            }
        });
        lineDataSet.notifyDataSetChanged();

        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(lineDataSet);

        LineData lineData = new LineData(iLineDataSets);
        yAxisLeft.setAxisMaximum(lineData.getYMax()+0.5f);
        yAxisLeft.setAxisMinimum(lineData.getYMin()-5f);
        xAxis.setAxisMaximum(lineData.getXMax()+0.5f);
        chart.setVisibleXRangeMaximum(5);
        chart.setData(lineData);
    }
//    private void setUpData(){
//        entries.clear();
//        entries.add(new Entry(0, 22f, "°C"));
//        entries.add(new Entry(1, 20f, "°C"));
//        entries.add(new Entry(2, 20f, "°C"));
//        entries.add(new Entry(3, 20f, "°C"));
//        entries.add(new Entry(4, 20f, "°C"));
//        entries.add(new Entry(5, 18f, "°C"));
//        entries.add(new Entry(6, 18f, "°C"));
//        entries.add(new Entry(7, 17f, "°C"));
//        entries.add(new Entry(8, 17f, "°C"));
//        entries.add(new Entry(9, 20f, "°C"));
//        entries.add(new Entry(10, 23f, "°C"));
//        entries.add(new Entry(11, 23f, "°C"));
//        entries.add(new Entry(12, 23f, "°C"));
//        entries.add(new Entry(13, 24f, "°C"));
//        entries.add(new Entry(14, 24f, "°C"));
//        entries.add(new Entry(15, 24f, "°C"));
//        entries.add(new Entry(16, 20f, "°C"));
//        entries.add(new Entry(17, 20f, "°C"));
//
//        sDay.clear();
//        sDay.add("Bây giờ");
//        sDay.add("01:00");
//        sDay.add("02:00");
//        sDay.add("03:00");
//        sDay.add("04:00");
//        sDay.add("05:00");
//        sDay.add("06:00");
//        sDay.add("07:00");
//        sDay.add("08:00");
//        sDay.add("09:00");
//        sDay.add("10:00");
//        sDay.add("11:00");
//        sDay.add("12:00");
//        sDay.add("13:00");
//        sDay.add("14:00");
//        sDay.add("15:00");
//        sDay.add("16:00");
//        sDay.add("17:00");
//        sDay.add("18:00");
//    }
    private void animationSunrise(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "animationWeather: "+ imgSunWeather.getX() +" "+imgSunWeather.getY());
            path = new Path();
            path.arcTo(0f-dpToPx(170), 0f, rltSunWeather.getRight(), rltSunWeather.getBottom()+dpToPx(300), 180f, 110f, true);

            startAnimationWeather(path, night, morning);
            if(!bacgroundImgWeather.getTag().equals("morning")){
                changeBackgroundImg(R.drawable.background_morning);
                bacgroundImgWeather.setTag("night");
            }else{
                return;
            }
        } else {
            // Create animator without using curved path
        }
    }
    private void animationSundown(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            path2 = new Path();
            path2.arcTo(0f + imgSunWeather.getLeft() + dpToPx(168), 0f + dpToPx(30), 1000f + dpToPx(50), 1000f, 270f, 90f, true);

            startAnimationWeather(path2, morning, night);
            if(!bacgroundImgWeather.getTag().equals("night")){
                changeBackgroundImg(R.drawable.background_night);
            }else{
                return;
            }

        }else{

        }
    }
    // bắt đầu animation
    private void startAnimationWeather(Path path, int fromColor, int toColor){
        ObjectAnimator animator = ObjectAnimator.ofFloat(imgSunWeather, View.X, View.Y, path);
        animator.setDuration(2000);




        ValueAnimator valueAnimator = new ValueAnimator();

        valueAnimator.setIntValues(fromColor, toColor);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bacgroundColorWeather.setBackgroundColor((int) valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(2000);
        valueAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator, valueAnimator);
        animatorSet.start();
    }
    private void changeBackgroundImg(int background){
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        bacgroundImgWeather.startAnimation(fadeOut);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bacgroundImgWeather.setBackgroundResource(background);
                Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                bacgroundImgWeather.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    // chuyển đỗi dp sang px
    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}