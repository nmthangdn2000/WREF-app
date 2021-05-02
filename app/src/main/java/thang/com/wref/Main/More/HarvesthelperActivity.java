package thang.com.wref.Main.More;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.wref.Login.SharedPreferencesManagement;
import thang.com.wref.Models.CropsModel;
import thang.com.wref.Models.HarvesthelperModel;
import thang.com.wref.NetworkUtil;
import thang.com.wref.R;
import thang.com.wref.Retrofits.HarvesthelperRetrofit;

public class HarvesthelperActivity extends AppCompatActivity {
    private static final String TAG = "HarvesthelperActivity";

    private RecyclerView rcvHaverst;

    private NetworkUtil networkUtill;
    private Retrofit retrofit;
    private HarvesthelperRetrofit harvesthelperRetrofit;
    private ArrayList<CropsModel> cropsModelsArr;

    private SharedPreferencesManagement sharedPreferencesManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvesthelper);
        sharedPreferencesManagement = new SharedPreferencesManagement(this);

        networkUtill = new NetworkUtil();
        retrofit = networkUtill.getRetrofit();

        mappingView();
        getData();
    }
    private void mappingView(){
        rcvHaverst = (RecyclerView) findViewById(R.id.rcvHaverst);

        rcvHaverst.setHasFixedSize(true);
        LinearLayoutManager  linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvHaverst.setLayoutManager(linearLayoutManager);
    }

    private void getData(){
        cropsModelsArr = new ArrayList<>();
        harvesthelperRetrofit = retrofit.create(HarvesthelperRetrofit.class);
        Call<HarvesthelperModel> call = harvesthelperRetrofit.getByID(sharedPreferencesManagement.getTOKEN(), "606dd3e866d02b35584acb95");
        call.enqueue(new Callback<HarvesthelperModel>() {
            @Override
            public void onResponse(Call<HarvesthelperModel> call, Response<HarvesthelperModel> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(HarvesthelperActivity.this, "lỗi mạng", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    HarvesthelperModel harvesthelperModel = response.body();
                    cropsModelsArr.add(new CropsModel("Loại cây trồng", harvesthelperModel.getName()));
                    cropsModelsArr.add(new CropsModel("Sơ lược về cây trồng", harvesthelperModel.getDescription()));
                    cropsModelsArr.add(new CropsModel("name", harvesthelperModel.getName()));
                    cropsModelsArr.add(new CropsModel("name", harvesthelperModel.getName()));
                    cropsModelsArr.add(new CropsModel("name", harvesthelperModel.getName()));
                    cropsModelsArr.add(new CropsModel("name", harvesthelperModel.getName()));
                    cropsModelsArr.add(new CropsModel("name", harvesthelperModel.getName()));
                    cropsModelsArr.add(new CropsModel("name", harvesthelperModel.getName()));
                    cropsModelsArr.add(new CropsModel("name", harvesthelperModel.getName()));
                    cropsModelsArr.add(new CropsModel("name", harvesthelperModel.getName()));
                    cropsModelsArr.add(new CropsModel("name", harvesthelperModel.getName()));
                    cropsModelsArr.add(new CropsModel("name", harvesthelperModel.getName()));
                    cropsModelsArr.add(new CropsModel("name", harvesthelperModel.getName()));
                    cropsModelsArr.add(new CropsModel("name", harvesthelperModel.getName()));
                    cropsModelsArr.add(new CropsModel("name", harvesthelperModel.getName()));
                    cropsModelsArr.add(new CropsModel("name", harvesthelperModel.getName()));

                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<HarvesthelperModel> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                call.cancel();
            }
        });
    }
}