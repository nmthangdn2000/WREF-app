package thang.com.wref.Main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import thang.com.wref.Login.LoginActivity;
import thang.com.wref.Login.SharedPreferencesManagement;
import thang.com.wref.Main.More.ThemeAgriActivity;
import thang.com.wref.Main.More.ProductAgriActivity;
import thang.com.wref.Main.More.SearchActivity;
import thang.com.wref.R;

public class CameraPredictFragment extends Fragment implements View.OnClickListener{
    private View view;
    private LinearLayout btnLogout, lnlInforAgri, lnlSearch, lnlProduct, lnlCodeScan;
    private SharedPreferencesManagement sharedPreferencesManagement;
    public CameraPredictFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            Window window = getActivity().getWindow();
            window.setStatusBarColor(getContext().getResources().getColor(R.color.colorStatusBar));
        }
        sharedPreferencesManagement = new SharedPreferencesManagement(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_camera_predict, container, false);
        btnLogout = (LinearLayout) view.findViewById(R.id.btnLogout);
        lnlInforAgri = (LinearLayout) view.findViewById(R.id.lnlInforAgri);
        lnlSearch = (LinearLayout) view.findViewById(R.id.lnlSearch);
        lnlProduct = (LinearLayout) view.findViewById(R.id.lnlProduct);
        lnlCodeScan = (LinearLayout) view.findViewById(R.id.lnlCodeScan);

        lnlInforAgri.setOnClickListener(this);
        lnlSearch.setOnClickListener(this);
        lnlProduct.setOnClickListener(this);
        lnlCodeScan.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lnlInforAgri:
                inforAgri();
                break;
            case R.id.lnlSearch:
                search();
                break;
            case R.id.lnlProduct:
                product();
                break;
            case R.id.lnlCodeScan:
                codeScan();
                break;
            case R.id.btnLogout:
                logOut();
                break;
            default:
                break;
        }
    }
    private void inforAgri(){
        Intent intent = new Intent(getContext(), ThemeAgriActivity.class);
        startActivity(intent);
    }
    private void search(){
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
    }
    private void product(){
        Intent intent = new Intent(getContext(), ProductAgriActivity.class);
        startActivity(intent);
    }
    private void codeScan(){

    }

    private void logOut(){
        sharedPreferencesManagement.clearData();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}