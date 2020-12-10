package thang.com.wref.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

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
        if(position == 0)
            Glide.with(context).load(getImage("anh1")).fitCenter().centerCrop().into(holder.imgStories);
        if(position == 1)
            Glide.with(context).load(getImage("anh2")).fitCenter().centerCrop().into(holder.imgStories);
        if(position == 2)
            Glide.with(context).load(getImage("anh3")).fitCenter().centerCrop().into(holder.imgStories);
        if(position == 3)
            Glide.with(context).load(getImage("anh4")).fitCenter().centerCrop().into(holder.imgStories);
        if(position == 4)
            Glide.with(context).load(getImage("anh5")).fitCenter().centerCrop().into(holder.imgStories);
        if(position == 5)
            Glide.with(context).load(getImage("anh6")).fitCenter().centerCrop().into(holder.imgStories);
        holder.txtUserName.setText("Anh Huy");
    }
    public int getImage(String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        return drawableResourceId;
    }
    @Override
    public int getItemCount() {
        return 6;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView imgUserStories;
        private RoundedImageView imgStories;
        private TextView txtUserName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUserStories = (CircleImageView) itemView.findViewById(R.id.imgUserStories);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            imgStories = (RoundedImageView) itemView.findViewById(R.id.imgStories);
        }
    }
}
