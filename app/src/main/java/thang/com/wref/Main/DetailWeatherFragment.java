package thang.com.wref.Main;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import thang.com.wref.R;

public class DetailWeatherFragment extends Fragment{
    private static final String TAG = "DetailWeatherFragment";

    private LineChart chart ;
    private ArrayList<Entry> entries;
    private ArrayList<String> sDay;

    private View view;
    private ImageView imgSunWeather;
    private AnimatedVectorDrawable animation;
    private RelativeLayout rltSunWeather, rltDuBao5Ngay, bacgroundImgWeather, bacgroundColorWeather;
    private TextView txtNhietdo;

    private Path path, path2;
    private int night = 0;
    private int morning = 0;

    public DetailWeatherFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
        bacgroundImgWeather = (RelativeLayout) view.findViewById(R.id.bacgroundImgWeather);
        bacgroundColorWeather = (RelativeLayout) view.findViewById(R.id.bacgroundColorWeather);

        rltDuBao5Ngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationSunrise();
            }
        });
        txtNhietdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "animationWeather: "+ imgSunWeather.getX() +" "+imgSunWeather.getY());
                animationSundown();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        entries = new ArrayList<>();
        sDay = new ArrayList<>();
        setUpData();
        setupChar();
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
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

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

        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(lineDataSet);

        LineData lineData = new LineData(iLineDataSets);
        yAxisLeft.setAxisMaximum(lineData.getYMax()+0.5f);
        yAxisLeft.setAxisMinimum(lineData.getYMin()-5f);
        xAxis.setAxisMaximum(lineData.getXMax()+0.5f);
        chart.setVisibleXRangeMaximum(5);
        chart.setData(lineData);
    }
    private void setUpData(){
        entries.clear();
        entries.add(new Entry(0, 22f, "°C"));
        entries.add(new Entry(1, 20f, "°C"));
        entries.add(new Entry(2, 20f, "°C"));
        entries.add(new Entry(3, 20f, "°C"));
        entries.add(new Entry(4, 20f, "°C"));
        entries.add(new Entry(5, 18f, "°C"));
        entries.add(new Entry(6, 18f, "°C"));
        entries.add(new Entry(7, 17f, "°C"));
        entries.add(new Entry(8, 17f, "°C"));
        entries.add(new Entry(9, 20f, "°C"));
        entries.add(new Entry(10, 23f, "°C"));
        entries.add(new Entry(11, 23f, "°C"));
        entries.add(new Entry(12, 23f, "°C"));
        entries.add(new Entry(13, 24f, "°C"));
        entries.add(new Entry(14, 24f, "°C"));
        entries.add(new Entry(15, 24f, "°C"));
        entries.add(new Entry(16, 20f, "°C"));
        entries.add(new Entry(17, 20f, "°C"));

        sDay.clear();
        sDay.add("Bây giờ");
        sDay.add("01:00");
        sDay.add("02:00");
        sDay.add("03:00");
        sDay.add("04:00");
        sDay.add("05:00");
        sDay.add("06:00");
        sDay.add("07:00");
        sDay.add("08:00");
        sDay.add("09:00");
        sDay.add("10:00");
        sDay.add("11:00");
        sDay.add("12:00");
        sDay.add("13:00");
        sDay.add("14:00");
        sDay.add("15:00");
        sDay.add("16:00");
        sDay.add("17:00");
        sDay.add("18:00");
    }
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