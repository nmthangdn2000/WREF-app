package thang.com.wref.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private String idUser;

    public NewsAdapter(ArrayList<NewsModels> arrayList, Context context, String idUser, onClickRecyclerNews mListner) {
        this.arrayList = arrayList;
        this.context = context;
        this.mListner = mListner;
        this.idUser = idUser;
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
        if(arrayList.get(position).getMedia().length > 0)
            Glide.with(context).load(BASE_URL+"uploads/"+arrayList.get(position).getMedia()[0]).fitCenter().centerCrop().into(holder.imgNews);
//        Glide.with(context).load(BASE_URL+"uploads/"+arrayList.get(position).getIdUser().getAvata()).centerCrop().fitCenter().into(holder.imgUserNewsssss);
        holder.txtUserName.setText(arrayList.get(position).getIdUser().getUsername());
        holder.txtLocation.setText(arrayList.get(position).getIdLocation().getName());
        holder.txtContent.setText(arrayList.get(position).getContent());

        holder.evaluatePosts.setVisibility(View.GONE);
        holder.hrPosts.setVisibility(View.GONE);
        if(arrayList.get(position).getLike().length > 0 || arrayList.get(position).getComment() > 0){
            holder.evaluatePosts.setVisibility(View.VISIBLE);
            holder.hrPosts.setVisibility(View.VISIBLE);
            if(arrayList.get(position).getLike().length > 0){
                for (int i = 0; i < arrayList.get(position).getLike().length; i++){
                    if(arrayList.get(position).getLike()[i].getIduserLike().equals(idUser)){
                        Glide.with(context).load(R.drawable.ic_like_blur).into(holder.imgLike);
                        holder.btnLike.setTag("Like");
                        break;
                    }
                }
                holder.iconLike.setVisibility(View.VISIBLE);
                holder.txtNumberLike.setText(arrayList.get(position).getLike().length +"");
            }else {
                holder.iconLike.setVisibility(View.INVISIBLE);
                holder.txtNumberLike.setText("");
                holder.txtNumberComment.setText(arrayList.get(position).getComment() + " bình luận");
            }
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+arrayList.size());
        return arrayList.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CircleImageView imgUserNewsssss;
        private TextView txtUserName, txtTimePost, txtLocation, txtContent, txtNumberLike, txtNumberComment, txtNumberShare;
        private ImageView iconMore, iconLike, imgLike;
        private LinearLayout btnLike, btnComment, btnShare;
        private RoundedImageView imgNews;
        private RelativeLayout evaluatePosts;
        private View hrPosts;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            imgUserNewsssss = (CircleImageView) itemView.findViewById(R.id.imgUserNewsssss);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            iconMore = (ImageView) itemView.findViewById(R.id.iconMore);
            iconLike = (ImageView) itemView.findViewById(R.id.iconLike);
            imgLike = (ImageView) itemView.findViewById(R.id.imgLike);
            txtTimePost = (TextView) itemView.findViewById(R.id.txtTimePost);
            txtLocation = (TextView) itemView.findViewById(R.id.txtLocation);
            btnLike = (LinearLayout) itemView.findViewById(R.id.btnLike);
            btnComment = (LinearLayout) itemView.findViewById(R.id.btnComment);
            btnShare = (LinearLayout) itemView.findViewById(R.id.btnShare);
            imgNews = (RoundedImageView) itemView.findViewById(R.id.imgNews);
            txtContent = (TextView) itemView.findViewById(R.id.txtContent);
            evaluatePosts = (RelativeLayout) itemView.findViewById(R.id.evaluatePosts);
            txtNumberLike = (TextView) itemView.findViewById(R.id.txtNumberLike);
            txtNumberComment = (TextView) itemView.findViewById(R.id.txtNumberComment);
            txtNumberShare = (TextView) itemView.findViewById(R.id.txtNumberShare);
            hrPosts = (View) itemView.findViewById(R.id.hrPosts);

            btnComment.setOnClickListener(this);
            btnLike.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnComment:
                    Log.d(TAG, "onClick: "+getAdapterPosition());
                    mListner.onClickComment(getAdapterPosition(), btnComment);
                    break;
                case R.id.btnLike:
                    mListner.onClickLike(getAdapterPosition(), btnLike, evaluatePosts,
                            txtNumberLike, txtNumberComment, hrPosts, iconLike, imgLike);
                    break;
                default:
                    break;
            }
        }
    }
    public interface onClickRecyclerNews{
        void onClickComment(int position, LinearLayout btnComment);
        void onClickLike(int position, LinearLayout btnLike, RelativeLayout evaluatePosts,
                         TextView txtNumberLike, TextView txtNumberComment, View hrPosts, ImageView iconLike, ImageView imgLike);
    }
}
