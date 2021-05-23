package thang.com.wref.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import thang.com.wref.Models.PesticidesModel;
import thang.com.wref.R;

public class PesticidesAdapter extends RecyclerView.Adapter<PesticidesAdapter.ViewHodler>{
    private Context context;
    private ArrayList<PesticidesModel> pesticidesArr;

    public PesticidesAdapter() {

    }

    @NonNull
    @NotNull
    @Override
    public PesticidesAdapter.ViewHodler onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.pesticide_item, parent,false);
        return new PesticidesAdapter.ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PesticidesAdapter.ViewHodler holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHodler extends RecyclerView.ViewHolder{
        public ViewHodler(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }
}
