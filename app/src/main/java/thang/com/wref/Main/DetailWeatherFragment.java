package thang.com.wref.Main;

import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

import thang.com.wref.R;

public class DetailWeatherFragment extends Fragment{
    private static final String TAG = "DetailWeatherFragment";
    private final int count = 5;

    private CombinedChart chart ;

    private View view;
    private ImageView img;
    private AnimatedVectorDrawable animation;
    private RelativeLayout dasdasd;

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
        mapingView();
        return view;
    }
    private void mapingView(){
        dasdasd = (RelativeLayout) view.findViewById(R.id.dasdasd);
        chart  = (CombinedChart) view.findViewById(R.id.combinedChart);
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
}