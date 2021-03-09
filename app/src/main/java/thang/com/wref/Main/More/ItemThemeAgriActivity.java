package thang.com.wref.Main.More;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import thang.com.wref.Adapter.AgriAdapter;
import thang.com.wref.Adapter.ItemThemeAgriAdapter;
import thang.com.wref.Login.SharedPreferencesManagement;
import thang.com.wref.Models.AgriModel;
import thang.com.wref.Models.InforAgriModel;
import thang.com.wref.Models.ThemeAgriModel;
import thang.com.wref.NetworkUtil;
import thang.com.wref.R;
import thang.com.wref.Retrofits.InforAgriRetrofit;

public class ItemThemeAgriActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "ItemThemeAgriActivity";
    private Toolbar toolbar;
    private String themeAgriId ="";
    private NetworkUtil networkUtil;
    private Retrofit retrofit;
    private InforAgriRetrofit inforAgriRetrofit;
    private ArrayList<InforAgriModel> inforAgriModelsArr;
    private String iduser = "", token ="";
    private SharedPreferencesManagement sharedPreferencesManagement;
    private ItemThemeAgriAdapter itemThemeAgriAdapter;

    private RecyclerView rcvItemThemeAgri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_theme_agri);

        toolbar = (Toolbar) findViewById(R.id.toolbarInfor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("Title"));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        themeAgriId = getIntent().getStringExtra("idTheme");
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
        sharedPreferencesManagement = new SharedPreferencesManagement(this);
        iduser= sharedPreferencesManagement.getID();
        token = sharedPreferencesManagement.getTOKEN();

        mapingView();
        getData();

    }
    private void mapingView() {
        rcvItemThemeAgri = (RecyclerView) findViewById(R.id.rcvItemThemeAgri);
        rcvItemThemeAgri.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvItemThemeAgri.setLayoutManager(linearLayoutManager);
    }
    @Override
    public void onClick(View v) {

    }
    private void getData() {
        inforAgriModelsArr = new ArrayList<>();
        inforAgriRetrofit = retrofit.create(InforAgriRetrofit.class);
        Call<List<InforAgriModel>> listCall = inforAgriRetrofit.getInforAgri(token, themeAgriId);
        listCall.enqueue(new Callback<List<InforAgriModel>>() {
            @Override
            public void onResponse(Call<List<InforAgriModel>> call, Response<List<InforAgriModel>> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(ItemThemeAgriActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    List<InforAgriModel> themeAgriModels = response.body();
                    for(InforAgriModel inforAgriModel : themeAgriModels){
                        inforAgriModelsArr.add(inforAgriModel);
                    }
                    itemThemeAgriAdapter.notifyDataSetChanged();
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<List<InforAgriModel>> call, Throwable t) {
                call.cancel();
                Log.d(TAG, "onFailure: "+ t.getMessage());
            }
        });
        itemThemeAgriAdapter = new ItemThemeAgriAdapter(inforAgriModelsArr, this);
        rcvItemThemeAgri.setAdapter(itemThemeAgriAdapter);
    }



}