package thang.com.wref.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Retrofit;
import thang.com.wref.Adapter.DetailLocationMapAdapter;
import thang.com.wref.NetworkUtil;
import thang.com.wref.R;

public class DetailLocationMap extends Fragment {
    private View view;
    private RecyclerView rcvInfTouchLocation;
    private DetailLocationMapAdapter detailLocationMapAdapter;
    private ArrayList<String> arrImg;
    private Context context;
    private Retrofit retrofit;
    private NetworkUtil networkUtil;
    private String nameLocation;

    public DetailLocationMap(Context context, String nameLocation) {
        this.context = context;
        this.nameLocation = nameLocation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrImg = new ArrayList<>();
        networkUtil = new NetworkUtil();
        retrofit = networkUtil.getRetrofit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_location_maps, container, false);
        mappingView();
        setUpRecyclerView();
        return view;
    }

    private void mappingView() {
        rcvInfTouchLocation = (RecyclerView) view.findViewById(R.id.rcvInfTouchLocation);

    }

    private void setUpRecyclerView() {
        rcvInfTouchLocation.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                context, RecyclerView.HORIZONTAL, false
        );
        rcvInfTouchLocation.setLayoutManager(linearLayoutManager);
        detailLocationMapAdapter = new DetailLocationMapAdapter(context, arrImg);
        rcvInfTouchLocation.setAdapter(detailLocationMapAdapter);
    }
}
