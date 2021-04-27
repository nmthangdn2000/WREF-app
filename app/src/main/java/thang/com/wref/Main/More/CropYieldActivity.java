package thang.com.wref.Main.More;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;

import thang.com.wref.Adapter.CropYieldAdapter;
import thang.com.wref.Adapter.PPAdapter;
import thang.com.wref.Models.CropYieldModel;
import thang.com.wref.Models.PPModel;
import thang.com.wref.R;

public class CropYieldActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CropYieldActivity";
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private String title = "";
    private RecyclerView rcvProcess, rcvProductivityPrediction;

    private ArrayList<CropYieldModel> cropYieldModelsArr;
    private CropYieldAdapter cropYieldAdapter;

    private ArrayList<PPModel> ppModelsArr;
    private PPAdapter ppAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_yield);
        setUpToolBar();
        mappingView();
        addDataProcess();
        addDataPP();
    }

    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "setUpToolBar: "+getIntent().getStringExtra("plant"));
        title = getIntent().getStringExtra("plant");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.CollapsingToolbarLayout);

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {
                    collapsingToolbarLayout.setExpandedTitleColor(Color.BLACK);
                    toolbar.getNavigationIcon().setTint(Color.BLACK);
                }
                else
                {
                    collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
                    toolbar.getNavigationIcon().setTint(Color.WHITE);
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void mappingView() {
        rcvProcess = (RecyclerView) findViewById(R.id.rcvProcess);
        rcvProductivityPrediction = (RecyclerView) findViewById(R.id.rcvProductivityPrediction);
    }

    @Override
    public void onClick(View v) {

    }
    private void addDataProcess() {
        rcvProcess.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvProcess.setLayoutManager(linearLayoutManager);

        cropYieldModelsArr = new ArrayList<>();
        cropYieldModelsArr.add(
                new CropYieldModel(
                        "Chọn giống - Gieo hạt", R.drawable.ic_process_1, "Các loại giống tối ưu", "12 loại giống", R.drawable.ic_baseline_check, "#22E079"
                ));
        cropYieldModelsArr.add(
                new CropYieldModel(
                        "Làm đất - Trồng cây", R.drawable.ic_process_2, "Chuẩn bị tốt cho cây trồng", "5 Kĩ thuật", R.drawable.ic_baseline_check, "#22E079"
                ));
        cropYieldModelsArr.add(
                new CropYieldModel(
                        "Bón phân - Phát triển", R.drawable.ic_process_3, "Tăng năng suất cây trồng", "6 Phân bón", R.drawable.ic_baseline_check, "#22E079"
                ));
        cropYieldModelsArr.add(
                new CropYieldModel(
                        "Phòng bệnh - Thuốc trừ sâu", R.drawable.ic_process_4, "Phòng các nguy cơ sâu bệnh", "8 Thuốc trừ sâu", R.drawable.ic_outline_circle, "#2285E0"
                ));
        cropYieldModelsArr.add(
                new CropYieldModel(
                        "Thu hoạch - Bảo quản", R.drawable.ic_process_5, "Phòng các nguy cơ sâu bệnh", "3 Kĩ thuật", R.drawable.ic_baseline_access_time, "#62AEFB"
                ));
        cropYieldAdapter = new CropYieldAdapter(cropYieldModelsArr, this);
        rcvProcess.setAdapter(cropYieldAdapter);
    }

    private void addDataPP(){
        rcvProductivityPrediction.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvProductivityPrediction.setLayoutManager(linearLayoutManager);

        ppModelsArr = new ArrayList<>();
        ppModelsArr.add(
                new PPModel(
                        "55.5 Tạ/ha", "Tháng 4 - 7", R.drawable.ic_mdi_arrow_top_right, "130%", "#22E079"
                ));
        ppModelsArr.add(
                new PPModel(
                        "40.5 Tạ/ha", "Tháng 8 - 10", R.drawable.ic_mdi_arrow_top_right, "105%", "#2285E0"
                ));
        ppModelsArr.add(
                new PPModel(
                        "40.5 Tạ/ha", "Tháng 11 - 2(năm sau)", R.drawable.ic_mdi_arrow_bottom_right, "85%", "#E04422"
                ));

        ppAdapter = new PPAdapter(ppModelsArr, this);
        rcvProductivityPrediction.setAdapter(ppAdapter);
    }
}