package thang.com.wref.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import thang.com.wref.Models.StoriesModels;
import thang.com.wref.R;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.ViewHolder>{
    private ArrayList<StoriesModels> arrayList;
    private Context context;

    public StoriesAdapter(ArrayList<StoriesModels> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_stories, parent,false);
        return new StoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 12;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView imgUserStories;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUserStories = (CircleImageView) itemView.findViewById(R.id.imgUserStories);
        }
    }
}
