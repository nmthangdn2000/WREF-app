package thang.com.wref.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import thang.com.wref.Models.NewsModels;
import thang.com.wref.R;

import static thang.com.wref.util.Constants.BASE_URL;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHodler> {
    private static final String TAG = "NewsAdapter";
    private ArrayList<NewsModels> arrayList;
    private Context context;
    private onClickRecyclerNews mListner;

    public NewsAdapter(ArrayList<NewsModels> arrayList, Context context, onClickRecyclerNews mListner) {
        this.arrayList = arrayList;
        this.context = context;
        this.mListner = mListner;
    }
    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_news, parent,false);
        return new NewsAdapter.ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
        Log.d(TAG, "onBindViewHolder: "+arrayList.get(position).getMedia()[0]);
        Glide.with(context).load(BASE_URL+"uploads/"+arrayList.get(position).getMedia()[0]).fitCenter().centerCrop().into(holder.imgNews);
        Glide.with(context).load(BASE_URL+"uploads/"+arrayList.get(position).getIdUser().getAvata()).centerCrop().fitCenter().into(holder.imgUserNewsssss);
        holder.txtUserName.setText(arrayList.get(position).getIdUser().getUsername());
        holder.txtLocation.setText(arrayList.get(position).getIdLocation().getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CircleImageView imgUserNewsssss;
        private TextView txtUserName, txtTimePost, txtLocation;
        private ImageView iconMore;
        private LinearLayout btnLike, btnComment, btnShare;
        private RoundedImageView imgNews;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            imgUserNewsssss = (CircleImageView) itemView.findViewById(R.id.imgUserNewsssss);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            iconMore = (ImageView) itemView.findViewById(R.id.iconMore);
            txtTimePost = (TextView) itemView.findViewById(R.id.txtTimePost);
            txtLocation = (TextView) itemView.findViewById(R.id.txtLocation);
            btnLike = (LinearLayout) itemView.findViewById(R.id.btnLike);
            btnComment = (LinearLayout) itemView.findViewById(R.id.btnComment);
            btnShare = (LinearLayout) itemView.findViewById(R.id.btnShare);
            imgNews = (RoundedImageView) itemView.findViewById(R.id.imgNews);

            btnComment.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnComment:
                    Log.d(TAG, "onClick: "+getAdapterPosition());
                    mListner.onClickComment(getAdapterPosition(), btnComment);
                    break;
                default:
                    break;
            }
        }
    }
    public interface onClickRecyclerNews{
        void onClickComment(int position, LinearLayout btnComment);
    }
}
