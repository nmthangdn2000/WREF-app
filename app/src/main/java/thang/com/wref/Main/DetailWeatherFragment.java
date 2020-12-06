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

import java.util.ArrayList;

import thang.com.wref.R;

public class DetailWeatherFragment extends Fragment{
    private static final String TAG = "DetailWeatherFragment";
    private final int count = 5;

    private CombinedChart chart ;

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
        chart  = (CombinedChart) view.findViewById(R.id.combinedChart);
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

        ArrayList<String> tack = new ArrayList<>();
        tack.add("Hôm nay");
        tack.add("Ngày mai");
        tack.add("Thứ 2");
        tack.add("Thứ 3");
        tack.add("Thứ 4");
        CombinedData data = new CombinedData();

        Description description = new Description();
        description.setText("Thứ");
        chart.setDescription(description);


        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(1f); // this replaces setStartAtZero(true)


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(tack));

        data.setData(generateLineData());
        leftAxis.setAxisMaximum(data.getYMax()+10f);
        data.setData(generateBarData());
        rightAxis.setAxisMaximum(data.getYMax()+100f);


        xAxis.setAxisMaximum(data.getXMax() + 0.5f);
//        rightAxis.setMaxWidth(data.get);

        chart.setData(data);
        chart.invalidate();

    }
    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();

        for (int index = 0; index < count; index++)
            entries.add(new Entry(index + 0.5f, getRandom(15, 5)));

        LineDataSet set = new LineDataSet(entries, "Nhiệt độ");
        set.setColor(Color.rgb(255, 0, 0));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(255, 0, 0));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(16f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }
    private BarData generateBarData() {
        ArrayList<BarEntry> entries1 = new ArrayList<>();

        for (int index = 0; index < count; index++) {
            entries1.add(new BarEntry(index+0.5f, getRandom(50, 50)));
        }

        BarDataSet set1 = new BarDataSet(entries1, "Lượng mưa");
        set1.setColor(Color.rgb(60, 220, 78));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(16f);
        set1.setAxisDependency(YAxis.AxisDependency.RIGHT);

        ArrayList<IBarDataSet> iBarDataSets = new ArrayList<>();
        iBarDataSets.add(set1);
        float barWidth = 0.8f;

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }
    private float getRandom(float range, float start){
        return (float) (Math.random() * range) + start;
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